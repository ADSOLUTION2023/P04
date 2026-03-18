<%@page import="in.co.rays.proj4.bean.DoctorBean"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.proj4.bean.SpcBean"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.model.SpcModel"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.controller.DoctorCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Add Doctor</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
	<form action="<%=ORSView.DOCTOR_CTL%>" method="post">

		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.proj4.bean.DoctorBean"
			scope="request"></jsp:useBean>

		<%
			List<SpcBean> spcList = (List<SpcBean>) request.getAttribute("spcList");
		%>

		<div align="center">
			<h1 align="center" style="margin-bottom: -15; color: navy">
				<%
					if (bean != null && bean.getId() > 0) {
				%>Update<%
					} else {
				%>Add<%
					}
				%>
				Doctor
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
					<th align="right">First Name<span style="color: red">*</span></th>
					<td><input type="text" name="firstName"
						placeholder="Enter First Name"
						value="<%=DataUtility.getStringData(bean.getFirstName())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("firstName", request)%></font></td>
				</tr>
				<tr>
					<th align="right">Last Name<span style="color: red">*</span></th>
					<td><input type="text" name="lastName"
						placeholder="Enter Last Name"
						value="<%=DataUtility.getStringData(bean.getLastName())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("lastName", request)%></font></td>
				</tr>
				<tr>
					<th align="right">Experience<span style="color: red">*</span></th>
					<td><input type="text" name="experience"
						placeholder="Enter Experience"
						value="<%=DataUtility.getStringData(bean.getExperience())%>">
					</td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("experience", request)%>
					</font></td>
				</tr>
				<tr>
					<th align="right">Mobile No<span style="color: red">*</span></th>
					<td><input type="text" name="mobileNo"
						placeholder="Enter Mobile No"
						value="<%=DataUtility.getStringData(bean.getMobileNo())%>">
					</td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("mobileNo", request)%>
					</font></td>
				</tr>
				<tr>
					<th align="right">Email<span style="color: red">*</span></th>
					<td><input type="text" name="email" placeholder="Enter Email"
						value="<%=DataUtility.getStringData(bean.getEmail())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("email", request)%>
					</font></td>
				</tr>
				<tr>
					<th align="right">Specialization<span style="color: red">*</span></th>
					<td><%=HTMLUtility.getList("spcId", String.valueOf(bean.getSpcId()), spcList)%>
					</td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("spcId", request)%>
					</font></td>
				</tr>
				<th align="right">Consultation Fee<span style="color: red">*</span></th>
				<td><input type="text" name="consultationFee"
					placeholder="Enter Consultation Fee"
					value="<%=DataUtility.getStringData(bean.getConsultationFee())%>">
				</td>
				<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("consultationFee", request)%>
				</font></td>
				</tr>


				<tr>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th></th>
					<%
						if (bean != null && bean.getId() > 0) {
					%>
					<td align="center" colspan="2"><input type="submit"
						name="operation" value="<%=DoctorCtl.OP_UPDATE%>"> <input
						type="submit" name="operation" value="<%=DoctorCtl.OP_CANCEL%>">
						<%
							} else {
						%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=DoctorCtl.OP_SAVE%>"> <input
						type="submit" name="operation" value="<%=DoctorCtl.OP_RESET%>">
						<%
							}
						%>
				</tr>
			</table>
		</div>
	</form>
</body>

</html>