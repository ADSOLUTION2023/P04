<%@page import="in.co.rays.proj4.bean.PatientBean"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="in.co.rays.proj4.controller.PatientListCtl"%>
<%@page import="java.util.Iterator"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Patient List</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>

<body>
	<%@include file="Header.jsp"%>

	<jsp:useBean id="bean" class="in.co.rays.proj4.bean.PatientBean"
		scope="request"></jsp:useBean>

	<div align="center">
		<h1 align="center" style="margin-bottom: -15; color: navy;">Patient
			List</h1>

		<div style="height: 15px; margin-bottom: 12px">
			<h3>
				<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
			</h3>
			<h3>
				<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
			</h3>
		</div>

		<form action="<%=ORSView.PATIENT_LIST_CTL%>" method="post">

			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;

				int nextListSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

				List<PatientBean> list = (List<PatientBean>) ServletUtility.getList(request);
				Iterator<PatientBean> it = list.iterator();


				if (list.size() != 0) {
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">

			<table style="width: 100%">
				<tr>
					<td align="center"><label><b>First Name :</b></label> <input
						type="text" name="firstName" placeholder="Enter First Name"
						value="<%=ServletUtility.getParameter("firstName", request)%>">&emsp;

						<label><b>Email :</b></label> <input type="text" name="email"
						placeholder="Enter Email"
						value="<%=ServletUtility.getParameter("email", request)%>">&emsp;

						<input type="submit" name="operation"
						value="<%=PatientListCtl.OP_SEARCH%>"> <input
						type="submit" name="operation"
						value="<%=PatientListCtl.OP_RESET%>"></td>
				</tr>
			</table>

			<br>

			<table border="1" style="width: 100%; border: groove;">
				<tr style="background-color: #e1e6f1e3;">
					<th width="5%"><input type="checkbox" id="selectall" /></th>
					<th width="5%">S.No</th>
					<th width="15%">First Name</th>
					<th width="15%">Last Name</th>
					<th width="15%">Email</th>
					<th width="10%">Mobile</th>
					<th width="10%">Gender</th>
					<th width="10%">DOB</th>
					<th width="10%">Edit</th>
				</tr>

				<%
					while (it.hasNext()) {
							bean = it.next();

							SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
							String date = sdf.format(bean.getDob());
				%>

				<tr>
					<td align="center"><input type="checkbox" class="case"
						name="ids" value="<%=bean.getId()%>"></td>

					<td align="center"><%=index++%></td>
					<td align="center"><%=bean.getFirstName()%></td>
					<td align="center"><%=bean.getLastName()%></td>
					<td align="center"><%=bean.getEmail()%></td>
					<td align="center"><%=bean.getMobileNo()%></td>
					<td align="center"><%=bean.getGender()%></td>
					<td align="center"><%=date%></td>

					<td align="center"><a href="PatientCtl?id=<%=bean.getId()%>">Edit</a>
					</td>
				</tr>

				<%
					}
				%>
			</table>

			<table style="width: 100%">
				<tr>
					<td style="width: 25%"><input type="submit" name="operation"
						value="<%=PatientListCtl.OP_PREVIOUS%>"
						<%=pageNo > 1 ? "" : "disabled"%>></td>

					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=PatientListCtl.OP_NEW%>"></td>

					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=PatientListCtl.OP_DELETE%>"></td>

					<td style="width: 25%" align="right"><input type="submit"
						name="operation" value="<%=PatientListCtl.OP_NEXT%>"
						<%=nextListSize != 0 ? "" : "disabled"%>></td>
				</tr>
			</table>

			<%
				} else {
			%>

			<table>
				<tr>
					<td align="right"><input type="submit" name="operation"
						value="<%=PatientListCtl.OP_BACK%>"></td>
				</tr>
			</table>

			<%
				}
			%>

		</form>
	</div>
</body>
</html>