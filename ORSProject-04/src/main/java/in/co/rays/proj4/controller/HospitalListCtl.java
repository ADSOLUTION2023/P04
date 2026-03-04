package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.HospitalBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.RoleModel;
import in.co.rays.proj4.model.HospitalModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "HospitalListCtl", urlPatterns = { "/ctl/HospitalListCtl" })

public class HospitalListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(HospitalListCtl.class);

	
	/**
	 * Populates a HospitalBean with request parameters for search filters.
	 *
	 * @param request the HttpServletRequest
	 * @return populated HospitalBean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.debug("HospitalListCtl Method populate started");

		HospitalBean bean = new HospitalBean();

		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setAddress(DataUtility.getString(request.getParameter("address")));
		bean.setCity(DataUtility.getString(request.getParameter("city")));
		bean.setPhone(DataUtility.getString(request.getParameter("phone")));
		bean.setEmail(DataUtility.getString(request.getParameter("email")));
		
		log.debug("HospitalListCtl Method populate ended");
		return bean;
	}

	/**
	 * Handles GET request to display the initial Hospital list with pagination.
	 *
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("HospitalListCtl Method doGet started");

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		HospitalBean bean = (HospitalBean) populateBean(request);
		HospitalModel model = new HospitalModel();

		try {
			List<HospitalBean> list = model.search(bean, pageNo, pageSize);
			List<HospitalBean> next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.isEmpty()) {
				ServletUtility.setErrorMessage("No Record Found", request);
			}else

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.setBean(bean, request);
			request.setAttribute("nextListSize", next.size());

			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			e.printStackTrace();
			ServletUtility.handleExceptionDB(getView(), request, response);
			return;

		}

		log.debug("HospitalListCtl Method doGet ended");
	}

	/**
	 * Handles POST request for operations: Search, Next, Previous, New, Delete,
	 * Reset, Back.
	 *
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("HospitalListCtl Method doPost started");

		List list = null;
		List next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		HospitalBean bean = (HospitalBean) populateBean(request);
		HospitalModel model = new HospitalModel();

		String op = DataUtility.getString(request.getParameter("operation"));
		String[] ids = request.getParameterValues("ids");

		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || "Next".equalsIgnoreCase(op) || "Previous".equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}

			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.HOSPITAL_CTL, request, response);
				return;

			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					HospitalBean deletebean = new HospitalBean();
					for (String id : ids) {
						deletebean.setId(DataUtility.getInt(id));
						model.delete(deletebean);
						ServletUtility.setSuccessMessage("Hospital deleted successfully", request);
					}
				} else {
					ServletUtility.setErrorMessage("Select at least one record", request);
				}

			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.HOSPITAL_LIST_CTL, request, response);
				return;

			} else if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.HOSPITAL_LIST_CTL, request, response);
				return;
			}

			list = model.search(bean, pageNo, pageSize);
			next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.setBean(bean, request);
			request.setAttribute("nextListSize", next.size());

			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			e.printStackTrace();
			ServletUtility.handleExceptionDB(getView(), request, response);
			return;

		}
		log.debug("HospitalListCtl Method doPost ended");
	}

	@Override
	protected String getView() {

		return ORSView.HOSPITAL_LIST_VIEW;
	}

}
