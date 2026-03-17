package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.DoctorBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.RoleModel;
import in.co.rays.proj4.model.SpcModel;
import in.co.rays.proj4.model.DoctorModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "DoctorListCtl", urlPatterns = { "/ctl/DoctorListCtl" })
public class DoctorListCtl extends BaseCtl  {
		
		private static Logger log = Logger.getLogger(DoctorListCtl.class);

	    /**
	     * Loads preload lists such as Role list to populate dropdown filters.
	     *
	     * @param request the HttpServletRequest
	     */
	    @Override
	    protected void preload(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	    	
	    	log.debug("DoctorListCtl Method preload started");
	    	
	        SpcModel spcmodel = new SpcModel();

	        try {
	            List spcList = spcmodel.list();
	            request.setAttribute("spcList", spcList);
	        } catch (ApplicationException e) {
	           e.printStackTrace();
	        	ServletUtility.handleExceptionDB(getView(),request, response);
	        }

	        log.debug("DoctorListCtl Method preload ended");
	    }

	    /**
	     * Populates a DoctorBean with request parameters for search filters.
	     *
	     * @param request the HttpServletRequest
	     * @return populated DoctorBean
	     */
	    @Override
	    protected BaseBean populateBean(HttpServletRequest request) {
	    	
	    	log.debug("DoctorListCtl Method populate started");

	        DoctorBean bean = new DoctorBean();

	        bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
	        bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
	        bean.setExperience(DataUtility.getLong(request.getParameter("experience")));
	        bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
	        bean.setEmail(DataUtility.getString(request.getParameter("email")));
	        bean.setSpcId(DataUtility.getLong(request.getParameter("spcId")));
	        bean.setConsultationFee(DataUtility.getLong(request.getParameter("consultationFee")));

	        log.debug("DoctorListCtl Method populate ended");
	        return bean;
	    }

	    /**
	     * Handles GET request to display the initial Doctor list with pagination.
	     *
	     * @param request the HttpServletRequest
	     * @param response the HttpServletResponse
	     */
	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	    	
	    	log.debug("DoctorListCtl Method doGet started");

	        int pageNo = 1;
	        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

	        DoctorBean bean = (DoctorBean) populateBean(request);
	        DoctorModel model = new DoctorModel();

	        try {
	            List<DoctorBean> list = model.search(bean, pageNo, pageSize);
	            List<DoctorBean> next = model.search(bean, pageNo + 1, pageSize);

	            if (list == null || list.isEmpty()) {
	                ServletUtility.setErrorMessage("no record found", request);
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
		
	        log.debug("DoctorListCtl Method doGet ended");
	    }

	    /**
	     * Handles POST request for operations:
	     * Search, Next, Previous, New, Delete, Reset, Back.
	     *
	     * @param request the HttpServletRequest
	     * @param response the HttpServletResponse
	     */
	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	    	
	    	log.debug("DoctorListCtl Method doPost started");

	        List list = null;
	        List next = null;

	        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
	        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

	        pageNo = (pageNo == 0) ? 1 : pageNo;
	        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

	        DoctorBean bean = (DoctorBean) populateBean(request);
	        DoctorModel model = new DoctorModel();

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
	                ServletUtility.redirect(ORSView.DOCTOR_CTL, request, response);
	                return;

	            } else if (OP_DELETE.equalsIgnoreCase(op)) {
	                pageNo = 1;
	                if (ids != null && ids.length > 0) {
	                    DoctorBean deletebean = new DoctorBean();
	                    for (String id : ids) {
	                        deletebean.setId(DataUtility.getInt(id));
	                        model.delete(deletebean);
	                        ServletUtility.setSuccessMessage("Doctor deleted successfully", request);
	                    }
	                } else {
	                    ServletUtility.setErrorMessage("Select at least one record", request);
	                }

	            } else if (OP_RESET.equalsIgnoreCase(op)) {
	                ServletUtility.redirect(ORSView.DOCTOR_LIST_CTL, request, response);
	                return;

	            } else if (OP_BACK.equalsIgnoreCase(op)) {
	                ServletUtility.redirect(ORSView.DOCTOR_LIST_CTL, request, response);
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
	        log.debug("DoctorListCtl Method doPost ended");
	    }

	    /**
	     * Returns the view for Doctor List screen.
	     *
	     * @return Doctor_LIST_VIEW constant
	     */
	    @Override
	    protected String getView() {
	        return ORSView.DOCTOR_LIST_VIEW;
	    }

}
