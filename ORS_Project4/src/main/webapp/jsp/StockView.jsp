
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.StockCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>Stock Page</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Utilities.js"></script>
<script>
	$(function() {
		$("#udatee").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '2024:2026',
		});
	});
</script>

</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.StockBean"
		scope="request"></jsp:useBean>
	<%@ include file="Header.jsp"%>


	<div align="center">

		<form action="<%=ORSView.STOCK_CTL%>" method="post">




			<h1>

				<%
					if (bean != null && bean.getId() > 0) {
				%>
				<tr>
					<th><font size="5px"> Update Stock Purchase </font></th>
				</tr>
				<%
					} else {
				%>
				<tr>
					<th><font size="5px"> Add Stock Purchase </font></th>
				</tr>
				<%
					}
				%>
			</h1>

			<h3>
				<font color="red"> <%=ServletUtility.getErrorMessage(request)%></font>
				<font color="limegreen"> <%=ServletUtility.getSuccessMessage(request)%></font>
			</h3>

			<%
				Map map = (Map) request.getAttribute("stock");
			%>

			<table>
				<tr>
					<input type="hidden" name="id" value="<%=bean.getId()%>">
					<th align="left">Quantity<span style="color: red">*</span>:
					</th>
					<td><input type="text" name="quantity"
						placeholder="Enter Quantity" size="26"
						oninput=" handleIntegerInput(this, 'quantityError', 8)"
						onblur=" validateIntegerInput(this, 'quantityError', 8)"
						value="<%=(DataUtility.getStringData(bean.getQuantity()).equals("0") ? "" : bean.getQuantity())%>"></td>

					<td style="position: fixed"><font color="red"
						id="quantityError"><%=ServletUtility.getErrorMessage("quantity", request)%></font></td>

				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">Purchase Price<span style="color: red">*</span>
						:
					</th>
					<td><input type="text" name="purchasePrice"
						placeholder="Enter Purchase Price" size="26"
						oninput=" handleDoubleInput(this, 'purchasePriceError', 8)"
						value="<%=(DataUtility.getStringData(bean.getPurchasePrice()).equals("0.0") ? "" : bean.getPurchasePrice())%>"></td>
					<td style="position: fixed"><font color="red"
						id="purchasePriceError"><%=ServletUtility.getErrorMessage("purchasePrice", request)%></font></td>

				</tr>








				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">Purchase Date <span style="color: red">*</span>
						:
					</th>
					<td><input type="text" name="purchaseDate"
						placeholder="Enter Purchase Date" size="26" readonly="readonly"
						id="udatee"
						value="<%=DataUtility.getDateString(bean.getPurchaseDate())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("purchaseDate", request)%></font></td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">Order Type<span style="color: red">*</span> :
					</th>
					<td>
						<%
							String hlist = HTMLUtility.getList2("orderType", DataUtility.getStringData(bean.getOrderType()), map);
						%> <%=hlist%>
					</td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("orderType", request)%></font></td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>





				<tr>
					<th></th>
					<%
						if (bean.getId() > 0) {
					%>
					<td colspan="2">&nbsp; &emsp; <input type="submit"
						name="operation" value="<%=StockCtl.OP_UPDATE%>"> &nbsp;
						&nbsp; <input type="submit" name="operation"
						value="<%=StockCtl.OP_CANCEL%>"></td>

					<%
						} else {
					%>

					<td colspan="2">&nbsp; &emsp; <input type="submit"
						name="operation" value="<%=StockCtl.OP_SAVE%>"> &nbsp;
						&nbsp; <input type="submit" name="operation"
						value="<%=StockCtl.OP_RESET%>"></td>

					<%
						}
					%>
				</tr>
			</table>
		</form>
	</div>

	<%@ include file="Footer.jsp"%>

</body>
</html>
