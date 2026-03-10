<%@page import="in.co.rays.proj4.controller.HospitalCtl"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Add Hospital</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
	<form action="<%=ORSView.HOSPITAL_CTL%>" method="post">

		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.proj4.bean.HospitalBean"
			scope="request"></jsp:useBean>
		<div align="center">
			<h1 align="center" style="margin-bottom:-15; color: navy">
				<%
					if (bean != null && bean.getId() > 0) {
				%>Update<%
					} else {
				%>Add<%
					}
				%>
				Hospital
			</h1>

			<div style="height: 15px; margin-bottom: 12px">
				<H3 align="center">
					<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
					</font>
				</H3>

				<H3 align="center">
					<font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
					</font>
				</H3>
			</div>

			<input type="hidden" name="id" value="<%=bean.getId()%>"> <input
				type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
			<input type="hidden" name="modifiedBy"
				value="<%=bean.getModifiedBy()%>"> <input type="hidden"
				name="createdDatetime"
				value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
			<input type="hidden" name="modifiedDatetime"
				value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">

			<table>

				<tr>
					<th align="right">Hospital Name<span style="color: red">*</span></th>
					<td><input type="text" name="name"
						placeholder="Enter Hospital Name"
						value="<%=DataUtility.getStringData(bean.getName())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("name", request)%>
					</font></td>
				</tr>

				<tr>
					<th align="right">Address<span style="color: red">*</span></th>
					<td><input type="text" name="address"
						placeholder="Enter Address"
						value="<%=DataUtility.getStringData(bean.getAddress())%>">
					</td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("address", request)%>
					</font></td>
				</tr>

				<tr>
					<th align="right">City<span style="color: red">*</span></th>
					<td><input type="text" name="city" placeholder="Enter City"
						value="<%=DataUtility.getStringData(bean.getCity())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("city", request)%>
					</font></td>
				</tr>

				<tr>
					<th align="right">Phone<span style="color: red">*</span></th>
					<td><input type="text" name="phone" maxlength="10"
						placeholder="Enter Phone Number"
						value="<%=DataUtility.getStringData(bean.getPhone())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("phone", request)%>
					</font></td>
				</tr>

				<tr>
					<th align="right">Email<span style="color: red">*</span></th>
					<td><input type="text" name="email" placeholder="Enter Email"
						value="<%=DataUtility.getStringData(bean.getEmail())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("email", request)%>
					</font></td>
				</tr>
				</table>
			<tr>
				<th></th>
				<%
					if (bean != null && bean.getId() > 0) {
				%>
				<td align="left" colspan="2">
				<input type="submit" name="operation" value="<%=HospitalCtl.OP_UPDATE%>"> 
				<input type="submit" name="operation" value="<%=HospitalCtl.OP_CANCEL%>">
					<%
						} else {
					%>
				<td align="left" colspan="2">
				<input type="submit" name="operation" value="<%=HospitalCtl.OP_SAVE%>"> 
				<input type="submit" name="operation" value="<%=HospitalCtl.OP_RESET%>">
					<%
						}
					%>
			</tr>
			</table>
		</div>
	</form>
</body>
</html>