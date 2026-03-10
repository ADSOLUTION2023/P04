<%@page import="in.co.rays.proj4.bean.HospitalBean"%>
<%@page import="in.co.rays.proj4.controller.HospitalListCtl"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
<title>Hospital List</title>

<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />

</head>

<body>

	<%@include file="Header.jsp"%>

	<jsp:useBean id="bean" class="in.co.rays.proj4.bean.HospitalBean"
		scope="request"></jsp:useBean>

	<div align="center">

		<h1 align="center" style="margin-bottom: -15; color: navy;">
			Hospital List</h1>

		<div style="height: 15px; margin-bottom: 12px">

			<h3>
				<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
				</font>
			</h3>

			<h3>
				<font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
				</font>
			</h3>

		</div>

		<form action="<%=ORSView.HOSPITAL_LIST_CTL%>" method="post">

			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;

				int nextListSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

				List<HospitalBean> list = (List<HospitalBean>) ServletUtility.getList(request);
				Iterator<HospitalBean> it = list.iterator();

				if (list.size() != 0) {
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">

			<table style="width: 50%">

				<tr>

					<td align="center"><label><b>Hospital Name :</b></label> <input
						type="text" name="city" placeholder="Enter Hospital Name"
						value="<%=ServletUtility.getParameter("city", request)%>">
						
						<td align="center"><label><b>City Name :</b></label> <input
						type="text" name="name" placeholder="Enter City Nmae"
						value="<%=ServletUtility.getParameter("name", request)%>">

						<input type="submit" name="operation"
						value="<%=HospitalListCtl.OP_SEARCH%>"> <input
						type="submit" name="operation"
						value="<%=HospitalListCtl.OP_RESET%>"></td>

				</tr>

			</table>

			<br>

			<table border="1" style="width: 100%; border: groove;">

				<tr style="background-color: #e1e6f1e3;">

					<th width="5%"><input type="checkbox" id="selectall">
					</th>

					<th width="5%">S.No</th>

					<th width="15%">Hospital Name</th>

					<th width="20%">Address</th>

					<th width="15%">City</th>

					<th width="10%">Phone</th>

					<th width="20%">Email</th>

					<th width="10%">Edit</th>

				</tr>

				<%
					while (it.hasNext()) {

							bean = it.next();
				%>

				<tr>

					<td style="text-align: center;"><input type="checkbox"
						class="case" name="ids" value="<%=bean.getId()%>"></td>

					<td style="text-align: center;"><%=index++%></td>

					<td style="text-align: center; text-transform: capitalize;"><%=bean.getName()%>
					</td>

					<td style="text-align: center;"><%=bean.getAddress()%></td>

					<td style="text-align: center;"><%=bean.getCity()%></td>

					<td style="text-align: center;"><%=bean.getPhone()%></td>

					<td style="text-align: center;"><%=bean.getEmail()%></td>

					<td style="text-align: center;"><a
						href="HospitalCtl?id=<%=bean.getId()%>"> Edit </a></td>

				</tr>

				<%
					}
				%>

			</table>

			<table style="width: 100%">

				<tr>

					<td style="width: 25%"><input type="submit" name="operation"
						value="<%=HospitalListCtl.OP_PREVIOUS%>"
						<%=pageNo > 1 ? "" : "disabled"%>></td>

					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=HospitalListCtl.OP_NEW%>"></td>

					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=HospitalListCtl.OP_DELETE%>"></td>

					<td style="width: 25%" align="right"><input type="submit"
						name="operation" value="<%=HospitalListCtl.OP_NEXT%>"
						<%=nextListSize != 0 ? "" : "disabled"%>></td>

				</tr>

			</table>

			<%
				} else {
			%>

			<table>

				<tr>

					<td align="right"><input type="submit" name="operation"
						value="<%=HospitalListCtl.OP_BACK%>"></td>

				</tr>

			</table>

			<%
				}
			%>

		</form>

	</div>

</body>

</html>