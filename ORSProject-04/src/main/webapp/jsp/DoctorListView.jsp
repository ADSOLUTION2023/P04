<%@page import="in.co.rays.proj4.model.SpcModel"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="java.util.Iterator"%>
<%@page import="in.co.rays.proj4.bean.DoctorBean"%>
<%@page import="in.co.rays.proj4.bean.SpcBean"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="in.co.rays.proj4.controller.DoctorListCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Doctor List</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
	<%@include file="Header.jsp"%>

	<jsp:useBean id="bean" class="in.co.rays.proj4.bean.DoctorBean"
		scope="request"></jsp:useBean>

	<div align="center">
		<h1 align="center" style="margin-bottom: -15; color: navy;">Doctor
			List</h1>

		<div style="height: 15px; margin-bottom: 12px">
			<h3>
				<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
			</h3>
			<h3>
				<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
			</h3>
		</div>

		<form action="<%=ORSView.DOCTOR_LIST_CTL%>" method="post">
			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;
				int nextListSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

				List<SpcBean> spcList = (List<SpcBean>) request.getAttribute("spcList");
				List<DoctorBean> list = (List<DoctorBean>) ServletUtility.getList(request);
				Iterator<DoctorBean> it = list.iterator();

				if (list.size() != 0) {
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input    s
				type="hidden" name="pageSize" value="<%=pageSize%>">

			<table style="width: 100%">
				<tr>
					<td align="center"><label><b>First Name :</b></label> <input
						type="text" name="firstName" placeholder="Enter First Name"
						value="<%=ServletUtility.getParameter("firstName", request)%>">&emsp;

						<label><b>Last Name:</b></label> <input type="text" name="login"
						placeholder="Enter Email ID"
						value="<%=ServletUtility.getParameter("login", request)%>">&emsp;

						<label><b>Specialization: </b></label> <%=HTMLUtility.getList("spcId", String.valueOf(bean.getSpcId()), spcList)%>&emsp;

						<input type="submit" name="operation"
						value="<%=DoctorListCtl.OP_SEARCH%>"> &nbsp; <input
						type="submit" name="operation" value="<%=DoctorListCtl.OP_RESET%>">
					</td>
				</tr>
			</table>
			<br>

			<table border="1" style="width: 100%; border: groove;">
				<tr style="background-color: #e1e6f1e3;">
					<th width="5%"><input type="checkbox" id="selectall" /></th>
					<th width="5%">S.No</th>
					<th width="13%">First Name</th>
					<th width="13%">Last Name</th>
					<th width="23%">Experience</th>
					<th width="10%">Mobile No</th>
					<th width="8%">Email</th>
					<th width="10%">Consultation Fee</th>
					<th width="8%">Specialization</th>
					<th width="5%">Edit</th>
				</tr>

				<%
					while (it.hasNext()) {
							bean = (DoctorBean) it.next();
							SpcModel model = new SpcModel();
							SpcBean spcBean = model.findByPk(bean.getSpcId());
				%>

				<tr>
					<td style="text-align: center;"><input type="checkbox"
						class="case" name="ids" value="<%=bean.getId()%>"
						<%=(user.getId() == bean.getId() || bean.getSpcId() == SpcBean.PHYSICIAN) ? "disabled" : ""%>>
					</td>
					<td style="text-align: center;"><%=index++%></td>
					<td style="text-align: center; text-transform: capitalize;"><%=bean.getFirstName()%></td>
					<td style="text-align: center; text-transform: capitalize;"><%=bean.getLastName()%></td>
					<td style="text-align: center; text-transform: lowercase;"><%=bean.getExperience()%></td>
					<td style="text-align: center;"><%=bean.getMobileNo()%></td>
					<td style="text-align: center; text-transform: capitalize;"><%=bean.getEmail()%></td>
					<td style="text-align: center;">text-transform: capitalize;"><%=bean.getConsultationFee()%></td>
					<td style="text-align: center; text-transform: capitalize;"><%=spcBean.getName()%></td>
					<td style="text-align: center;"><a
						href="SpcCtl?id=<%=bean.getId()%>"
						<%=(user.getId() == bean.getId() || "PHYSICIAN".equalsIgnoreCase(spcBean.getSpecialization()))
							? "onclick='return false;'"
							: ""%>>
							Edit </a></td>
				</tr>

				<%
					}
				%>
			</table>

			<table style="width: 100%">
				<tr>
					<td style="width: 25%"><input type="submit" name="operation"
						value="<%=DoctorListCtl.OP_PREVIOUS%>"
						<%=pageNo > 1 ? "" : "disabled"%>></td>
					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=DoctorListCtl.OP_NEW%>"></td>
					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=DoctorListCtl.OP_DELETE%>"></td>
					<td style="width: 25%" align="right"><input type="submit"
						name="operation" value="<%=DoctorListCtl.OP_NEXT%>"
						<%=nextListSize != 0 ? "" : "disabled"%>></td>
				</tr>
			</table>

			<%
				} else {
			%>

			<table>
				<tr>
					<td align="right"><input type="submit" name="operation"
						value="<%=DoctorListCtl.OP_BACK%>"></td>
				</tr>
			</table>

			<%
				}
			%>
		</form>
	</div>
</body>
</html>