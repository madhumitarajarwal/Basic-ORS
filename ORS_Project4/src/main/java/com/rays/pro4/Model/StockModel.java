
package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

//import org.apache.log4j.Logger;

import com.rays.pro4.Bean.StockBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

/**
 * JDBC Implementation of StockModel.
 * 
 * @author Madhumita Rajarwal
 *
 */

public class StockModel {
	private static Logger log = Logger.getLogger(StockModel.class);

	/**
	 * Find next PK of Stock
	 *
	 * @throws DatabaseException
	 */

	public int nextPK() throws DatabaseException {

		log.debug("Model nextPK Started");

		String sql = "select max(id) from st_stock";
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
		} catch (Exception e) {

			throw new DatabaseException("Exception : Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model nextPK Started");
		return pk + 1;

	}

	/**
	 * Stock Add
	 *
	 * @param bean
	 * @throws DatabaseException
	 *
	 */

	/**
	 * @param bean
	 * @return
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public long add(StockBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");

		String sql = "insert into st_stock values(?,?,?,?,?)";

		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();

			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, pk);
			pstmt.setInt(2, bean.getQuantity());
			pstmt.setDouble(3, bean.getPurchasePrice());
			pstmt.setDate(4, new java.sql.Date(bean.getPurchaseDate().getTime()));
			pstmt.setInt(5, bean.getOrderType());

			int i = pstmt.executeUpdate();
			System.out.println(i);
			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			log.error("Database Exception ...", e);
			try {
				e.printStackTrace();
				conn.rollback();

			} catch (Exception e2) {
				e2.printStackTrace();
				// application exception
				throw new ApplicationException("Exception : add rollback exceptionn" + e2.getMessage());
			}
		}

		finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Add End");
		return pk;

	}

	/**
	 * Delete a Stock
	 *
	 * @param bean
	 * @throws DatabaseException
	 */
	public void delete(StockBean bean) throws ApplicationException {
		log.debug("Model delete start");
		String sql = "delete from st_stock where id=?";
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			log.error("DataBase Exception", e);
			try {
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("Exception: Delete rollback Exception" + e2.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Delete End");
	}

	/**
	 * Find Stock by PK
	 *
	 * @param pk : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */

	public StockBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findBy PK start");
		String sql = "select * from st_stock where id=?";
		StockBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new StockBean();
				bean.setId(rs.getLong(1));
				bean.setQuantity(rs.getInt(2));
				bean.setPurchasePrice(rs.getDouble(3));
				bean.setPurchaseDate(rs.getDate(4));
				bean.setOrderType(rs.getInt(5));

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DataBase Exception ", e);
			throw new ApplicationException("Exception : Exception in getting Stock by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Method Find By PK end");
		return bean;
	}

	/**
	 * Update a stock
	 *
	 * @param bean
	 * @throws DatabaseException
	 */

	public void update(StockBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model Update Start");
		String sql = "update st_stock set QUANTITY=?, PURCHASE_PRICE=?, PURCHASE_DATE=?, ORDER_TYPE=? where ID=? ";
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, bean.getQuantity());
			pstmt.setDouble(2, bean.getPurchasePrice());
			pstmt.setDate(3, new java.sql.Date(bean.getPurchaseDate().getTime()));
            pstmt.setInt(4, bean.getOrderType());
			pstmt.setLong(5, bean.getId());

			int i = pstmt.executeUpdate();
			System.out.println("update stock>> " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DataBase Exception", e);
			try {
				conn.rollback();
			} catch (Exception e2) {
				e2.printStackTrace();
				throw new ApplicationException("Exception : Update Rollback Exception " + e2.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Update End ");
	}

	/**
	 * Search Stock
	 *
	 * @param bean : Search Parameters
	 * @throws DatabaseException
	 */

	public List search(StockBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * Search Stock with pagination
	 *
	 * @return list : List of Stocks
	 * @param bean     : Search Parameters
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 *
	 * @throws DatabaseException
	 */

	public List search(StockBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model Search Start");
		StringBuffer sql = new StringBuffer("select * from st_stock where 1=1");
		if (bean != null) {

			

			if (bean.getQuantity() > 0) {
				sql.append(" AND QUANTITY = " + bean.getQuantity());
			}

			if (bean.getPurchaseDate() != null && bean.getPurchaseDate().getTime() > 0) {
				Date d = new Date(bean.getPurchaseDate().getTime());
				sql.append(" AND PURCHASE_DATE = " + d);
			}
			
			if (bean.getId() > 0) {
				sql.append(" AND ID = " + bean.getId());
			}
			
			if (bean.getPurchasePrice() > 0) {
				sql.append(" AND PURCHASE_PRICE = " + bean.getPurchasePrice());
			}
			
			if (bean.getOrderType() > 0) {
				sql.append(" AND ORDER_TYPE = " + bean.getOrderType());
			}
			
			

		}
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}
		System.out.println("sql query search >>= " + sql.toString());
		List list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new StockBean();
				bean.setId(rs.getLong(1));
				bean.setQuantity(rs.getInt(2));
				bean.setPurchasePrice(rs.getDouble(3));
				bean.setPurchaseDate(rs.getDate(4));
				bean.setOrderType(rs.getInt(5));


				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception", e);
			throw new ApplicationException("Exception: Exception in Search Stock");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Search end");
		return list;

	}

	/**
	 * Get List of Stock
	 *
	 * @return list : List of Stock
	 * @throws DatabaseException
	 */

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * Get List of Stock with pagination
	 *
	 * @return list : List of stocks
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @throws DatabaseException
	 */

	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from st_stock");

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				StockBean bean = new StockBean();

				bean.setId(rs.getLong(1));
				bean.setQuantity(rs.getInt(2));
				bean.setPurchasePrice(rs.getDouble(3));
				bean.setPurchaseDate(rs.getDate(4));
				bean.setOrderType(rs.getInt(5));


				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception...", e);
			throw new ApplicationException("Exception : Exception in getting list of stocks");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model list End");
		return list;
	}

}
