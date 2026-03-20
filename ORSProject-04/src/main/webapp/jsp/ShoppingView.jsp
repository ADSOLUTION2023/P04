<%@page import="in.co.rays.proj4.bean.ShoppingBean"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.controller.ShoppingCtl"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Add Shopping</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>

<body>

	<form action="<%=ORSView.SHOPPING_CTL%>" method="post">

		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.proj4.bean.ShoppingBean"
			scope="request"></jsp:useBean>

		<div align="center">

			<h1 style="color: navy">
				<%
					if (bean != null && bean.getId() > 0) {
				%>
				Update
				<%
					} else {
				%>
				Add
				<%
					}
				%>
				Shopping
			</h1>

			<div style="height: 15px; margin-bottom: 12px">
				<h3>
					<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
				</h3>
				<h3>
					<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>
			</div>

			<input type="hidden" name="id" value="<%=bean.getId()%>">

			<table>

				<tr>
					<th align="right">Product Name<span style="color: red">*</span></th>
					<td><input type="text" name="productName"
						value="<%=DataUtility.getStringData(bean.getProductName())%>"
						placeholder="Enter Product Name"></td>
					<td><font color="red"><%=ServletUtility.getErrorMessage("productName", request)%></font></td>
				</tr>

				<tr>
					<th align="right">Shop Name<span style="color: red">*</span></th>
					<td><input type="text" name="shopName"
						value="<%=DataUtility.getStringData(bean.getShopName())%>"
						placeholder="Enter Shop Name"></td>
					<td><font color="red"><%=ServletUtility.getErrorMessage("shopName", request)%></font></td>
				</tr>

				<tr>
					<th align="right">Product Price<span style="color: red">*</span></th>
					<td><input type="text" name="productPrice"
						value="<%=DataUtility.getStringData(bean.getProductPrice())%>"
						placeholder="Enter Price"></td>
					<td><font color="red"><%=ServletUtility.getErrorMessage("productPrice", request)%></font></td>
				</tr>

				<tr>
					<th align="right">Purchase Date<span style="color: red">*</span></th>
					<td><input type="date" name="purchaseDate"
						value="<%=DataUtility.getDateString(bean.getPurchaseDate())%>"></td>
					<td><font color="red"><%=ServletUtility.getErrorMessage("purchaseDate", request)%></font></td>
				</tr>

				<tr>
					<th></th>
					<td></td>
				</tr>

				<tr>
					<%
						if (bean != null && bean.getId() > 0) {
					%>
					<td align="center" colspan="2"><input type="submit"
						name="operation" value="<%=ShoppingCtl.OP_UPDATE%>"> <input
						type="submit" name="operation" value="<%=ShoppingCtl.OP_CANCEL%>">
					</td>
					<%
						} else {
					%>
					<td align="center" colspan="2"><input type="submit"
						name="operation" value="<%=ShoppingCtl.OP_SAVE%>"> <input
						type="submit" name="operation" value="<%=ShoppingCtl.OP_RESET%>">
					</td>
					<%
						}
					%>
				</tr>

			</table>

		</div>

	</form>

</body>
</html>