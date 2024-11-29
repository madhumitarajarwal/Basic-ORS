
package com.rays.pro4.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.StockBean;
import com.rays.pro4.Bean.StockBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;

import com.rays.pro4.Model.StockModel;
import com.rays.pro4.Model.StockModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

//TODO: Auto-generated Javadoc
/**
 * The Class StockCtl.
 * 
 * @author Madhumita Rajarwal
 * 
 */
@WebServlet(name = "StockCtl", urlPatterns = { "/ctl/StockCtl" })
public class StockCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	/** The log. */
	private static Logger log = Logger.getLogger(StockCtl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#validate(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("uctl Validate");
		log.debug("StockCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("quantity"))) {
			request.setAttribute("quantity", PropertyReader.getValue("error.require", "Quantity"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("quantity"))) {
			request.setAttribute("quantity", PropertyReader.getValue("error.integer", "Quantity"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("orderType"))) {
			request.setAttribute("orderType", PropertyReader.getValue("error.require", "OrderType"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("purchaseDate"))) {
			request.setAttribute("purchaseDate", PropertyReader.getValue("error.require", "Purchase Date"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("purchaseDate"))) {
			request.setAttribute("purchaseDate", PropertyReader.getValue("error.date", "Purchase Date"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("purchasePrice"))) {
			request.setAttribute("purchasePrice", PropertyReader.getValue("error.require", "Purchase Price"));
			pass = false;
		} else if (!DataValidator.isFloat(request.getParameter("purchasePrice"))) {
			request.setAttribute("purchasePrice", "Purchase Price contains numerical vlaue only");
			pass = false;
		}

		log.debug("StockCtl Method validate Ended");

		return pass;
	}

	@Override
	protected void preload(HttpServletRequest request) {

		Map<Integer, String> map = new HashMap();

		map.put(1, "Market Order");
		map.put(2, "Limit Order");
		map.put(3, "Stop loss Order");
		map.put(4, "Stop Order");

		request.setAttribute("stock", map);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#populateBean(javax.servlet.http.
	 * HttpServletRequest)
	 */

	/**
	 * This is Populate Bean
	 */
	protected BaseBean populateBean(HttpServletRequest request) {
		System.out.println(" uctl Base bean P bean");
		log.debug("StockCtl Method populatebean Started");

		StockBean bean = new StockBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setQuantity(DataUtility.getInt(request.getParameter("quantity")));

		bean.setOrderType(DataUtility.getInt(request.getParameter("orderType")));

		bean.setPurchaseDate(DataUtility.getDate(request.getParameter("purchaseDate")));

		bean.setPurchasePrice(DataUtility.getDouble(request.getParameter("purchasePrice")));

		log.debug("StockCtl Method populatebean Ended");

		return bean;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("StockCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		StockModel model = new StockModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		System.out.println("Stock Edit Id >= " + id);
		if (id != 0 && id > 0) {
			System.out.println("in id > 0  condition");
			StockBean bean;
			try {
				bean = model.findByPK(id);
				System.out.println(bean);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("StockCtl Method doGet Ended");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("uctl Do Post");

		log.debug("StockCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		StockModel model = new StockModel();
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			StockBean bean = (StockBean) populateBean(request);
			System.out.println(" U ctl DoPost 11111111");

			try {
				if (id > 0) {

					// System.out.println("hi i am in dopost update");

					model.update(bean);
					ServletUtility.setBean(bean, request);
					System.out.println(" U ctl DoPost 222222");
					ServletUtility.setSuccessMessage("Stock is successfully Updated", request);

				} else {
					System.out.println(" U ctl DoPost 33333");
					long pk = model.add(bean);

					bean = model.findByPK(pk);

					ServletUtility.setBean(bean, request);

					ServletUtility.setSuccessMessage("Stock is successfully Added", request);
					/*
					 * ServletUtility.forward(getView(), request, response);
					 */ }
				/*
				 * ServletUtility.setBean(bean, request);
				 * ServletUtility.setSuccessMessage("Stock is successfully saved", request);
				 */

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				System.out.println(" U ctl D post 4444444");
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {
			System.out.println(" U ctl D p 5555555");

			StockBean bean = (StockBean) populateBean(request);
			try {
				model.delete(bean);
				System.out.println(" u ctl D Post  6666666");
				ServletUtility.redirect(ORSView.STOCK_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			System.out.println(" U  ctl Do post 77777");

			ServletUtility.redirect(ORSView.STOCK_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("StockCtl Method doPostEnded");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#getView()
	 */
	@Override
	protected String getView() {
		return ORSView.STOCK_VIEW;
	}

}
