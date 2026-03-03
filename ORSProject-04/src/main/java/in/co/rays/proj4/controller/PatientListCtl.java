package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.PatientBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.PatientModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;
@WebServlet(name = "PatientListCtl", urlPatterns = { "/ctl/PatientListCtl" })
public class PatientListCtl extends BaseCtl{
	
	private static Logger log = Logger.getLogger(PatientListCtl.class);

	 /**
     * Populates a PatientBean with request parameters for search filters.
     *
     * @param request the HttpServletRequest
     * @return populated PatientBean
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
    	
    	log.debug("PatientListCtl Method populate started");

        PatientBean bean = new PatientBean();

	    bean.setId(DataUtility.getLong(request.getParameter("id")));
	    bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
	    bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
	    bean.setDob(DataUtility.getDate(request.getParameter("dob")));
	    bean.setGender(DataUtility.getString(request.getParameter("gender")));
	    bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
	    bean.setEmail(DataUtility.getString(request.getParameter("email")));
	    bean.setAddress(DataUtility.getString(request.getParameter("address")));
	    
	    log.debug("PatientListCtl Method populate ended");
	    return bean;
    }

    /**
     * Handles GET request to display the initial Patient list with pagination.
     *
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	log.debug("PatientListCtl Method doGet started");

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        PatientBean bean = (PatientBean) populateBean(request);
        PatientModel model = new PatientModel();

        try {
            List<PatientBean> list = model.search(bean, pageNo, pageSize);
            List<PatientBean> next = model.search(bean, pageNo + 1, pageSize);

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
            return;
        }
        log.debug("PatientListCtl Method doGet ended");
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
    	
    	log.debug("PatientListCtl Method doPost started");

        List list = null;
        List next = null;

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        PatientBean bean = (PatientBean) populateBean(request);
        PatientModel model = new PatientModel();

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
                ServletUtility.redirect(ORSView.PATIENT_CTL, request, response);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                if (ids != null && ids.length > 0) {
                    PatientBean deletebean = new PatientBean();
                    for (String id : ids) {
                        deletebean.setId(DataUtility.getInt(id));
                        model.delete(deletebean);
                        ServletUtility.setSuccessMessage("Patient deleted successfully", request);
                    }
                } else {
                    ServletUtility.setErrorMessage("Select at least one record", request);
                }

            } else if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.PATIENT_LIST_CTL, request, response);
                return;

            } else if (OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.PATIENT_LIST_CTL, request, response);
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
            return;
        }
        
        log.debug("PatientListCtl Method doPost ended");
    }

    /**
     * Returns the view for Patient List screen.
     *
     * @return Patient_LIST_VIEW constant
     */
    @Override
    protected String getView() {
        return ORSView.PATIENT_LIST_VIEW;
    }

}
