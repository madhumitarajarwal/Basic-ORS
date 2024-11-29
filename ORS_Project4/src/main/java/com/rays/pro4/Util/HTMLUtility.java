package com.rays.pro4.Util;

import java.util.Collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.rays.pro4.Bean.DropdownListBean;
import com.rays.pro4.Model.BaseModel;

/**
 * HTML Utility class to produce HTML contents like Dropdown List.
 * 
 * @author Madhumita Rajarwal
 *
 */
public class HTMLUtility {

	public static String getList(String name, String selectedVal, HashMap<String, String> map) {

		StringBuffer sb = new StringBuffer(
				"<select style='width: 219px;  height: 23px;' class='form-control' name='" + name + "'>");

		Set<String> keys = map.keySet();
		String val = null;

		boolean select = true;
		if (select) {
			sb.append(
					"<option style='width: 219px;  height: 30px;' selected value=''>-------------------Select-----------------</option>");
		}

		for (String key : keys) {
			val = map.get(key);
			if (key.trim().equals(selectedVal)) {
				sb.append("<option selected value='" + key + "'>" + val + "</option>");
			} else {
				sb.append("<option value='" + key + "'>" + val + "</option>");
			}
		}
		sb.append("</select>");
		return sb.toString();
	}
	  public static String getList2(String name, String selectedVal, Map<Integer, String> map) {


			StringBuffer sb = new StringBuffer(
					"<select style='width: 219px;  height: 23px;' class='form-control' name='" + name + "'>");

			Set<Integer> keys = map.keySet();
			String val = null;
			boolean select = true;
			if (select) {

				// Add placeholder option
				sb.append(
						"<option style='width: 219px;  height: 30px;' selected value=''>-------------------Select------------------</option>");
			}

			for (Integer key : keys) {
				val = map.get(key);
				// Convert key to String for comparison and value attribute
				String keyString = key.toString();
				//System.out.println(keyString + "12345678900000000000----===");

				if (keyString.trim().equals(selectedVal)) {
					//System.out.println(selectedVal + "12345677778899900--====");
					// Mark the option as selected if it matches the selectedVal
					sb.append("<option selected value='" + key + "'>" + val + "</option>");
				} else {
					sb.append("<option value='" + key + "'>" + val + "</option>");
				}
			}

			sb.append("</select>");
			System.out.println("get list 2=========" + sb.toString());

			return sb.toString();
			}

  /**
   * Creates submit button if user has access permission.
   *

	/**
	 * Create HTML SELECT List from List parameter
	 *
	 * @param name
	 * @param selectedVal
	 * @param list
	 * @return
	 */
	public static String getList(String name, String selectedVal, List list) {

		Collections.sort(list);
		StringBuffer sb = new StringBuffer(
				"<select style='width: 219px;  height: 22px;' class='form-control' name='" + name + "'>");

		boolean select = true;
		if (select) {
			sb.append(
					"<option style='width: 219px;  height: 22px;' selected value=''>-------------------Select-----------------</option>");
		}

		List<DropdownListBean> dd = (List<DropdownListBean>) list;

		// StringBuffer sb = new StringBuffer( "<select style='width: 163px; height:
		// 23px;' class='form-control' name='" + name + "'>");

		String key = null;
		String val = null;

		for (DropdownListBean obj : dd) {
			key = obj.getkey();
			val = obj.getValue();

			if (key.trim().equals(selectedVal)) {
				sb.append("<option selected value='" + key + "'>" + val + "</option>");
			} else {
				sb.append("<option value='" + key.trim() + "'>" + val + "</option>");
			}
		}
		sb.append("</select>");
		return sb.toString();
	}

	/**
	 * Returns Error Message with HTML tag and CSS
	 *
	 * @param request
	 * @return
	 */
	public static String getErrorMessage(HttpServletRequest request) {
		String msg = ServletUtility.getErrorMessage(request);
		if (!DataValidator.isNull(msg)) {
			msg = "<p class='st-error-header'>" + msg + "</p>";
		}
		return msg;
	}

	/**
	 * Returns Success Message with HTML tag and CSS
	 *
	 * @param request
	 * @return
	 */

	public static String getSuccessMessage(HttpServletRequest request) {
		String msg = ServletUtility.getSuccessMessage(request);
		if (!DataValidator.isNull(msg)) {
			msg = "<p class='st-success-header'>" + msg + "</p>";
		}
		return msg;
	}

	/**
	 * Creates submit button if user has access permission.
	 *
	 * @param label
	 * @param access
	 * @param request
	 * @return
	 */
	public static String getSubmitButton(String label, boolean access, HttpServletRequest request) {

		String button = "";

		if (access) {
			button = "<input type='submit' name='operation'    value='" + label + "' >";
		}
		return button;
	}

	public static String getCommonFields(HttpServletRequest request) {

		BaseModel model = ServletUtility.getModel(request);

		StringBuffer sb = new StringBuffer();

		sb.append("<input type='hidden' name='id' value=" + model.getId() + ">");
		/*
		 * sb.append("<input type='hidden' name='createdBy' value=" +
		 * DataUtility.getString(model.getCreatedBy()) + ">");
		 * sb.append("<input type='hidden' name='modifiedBy' value=" +
		 * DataUtility.getString(model.getModifiedBy()) + ">");
		 * sb.append("<input type='hidden' name='createdDatetime' value=" +
		 * DataUtility.getTimestamp(model.getCreatedDatetime()) + ">");
		 * sb.append("<input type='hidden' name='modifiedDatetime' value=" +
		 * DataUtility.getTimestamp(model.getModifiedDatetime()) + ">");
		 */
		return sb.toString();
	}

}
