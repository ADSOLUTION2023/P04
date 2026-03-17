package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.SpcBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DataBaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.SpcModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;
@WebServlet(name = "SpcCtl", urlPatterns = { "/ctl/SpcCtl" })
public class SpcCtl extends BaseCtl{	
	
	@Override
	protected void preload(HttpServletRequest request, HttpServletResponse response) {

	    SpcModel model = new SpcModel();

	    try {

	        List spclist = model.list();

	        System.out.println("SPC LIST SIZE = " + spclist.size());

	        request.setAttribute("spcList", spclist);

	    } catch (ApplicationException e) {
	        e.printStackTrace();
	    }
	}
	    /**
	     * Validates the Spc form inputs from the request.
	     * 
	     * @param request HttpServletRequest
	     * @return true if all inputs are valid; false otherwise
	     */
	    @Override
	    protected boolean validate(HttpServletRequest request) {

	        boolean pass = true;

	        if (DataValidator.isNull(request.getParameter("name"))) {
	            request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
	            pass = false;
	        } else if (!DataValidator.isName(request.getParameter("name"))) {
	            request.setAttribute("name", "Invalid Name");
	            pass = false;
	        }

	        if (DataValidator.isNull(request.getParameter("description"))) {
	            request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
	            pass = false;
	        }
	        return pass;
	    }

	    /**
	     * Populates SpcBean from HttpServletRequest parameters.
	     * 
	     * @param request HttpServletRequest
	     * @return populated BaseBean (SpcBean)
	     */
	    @Override
	    protected BaseBean populateBean(HttpServletRequest request) {

	        SpcBean bean = new SpcBean();
	        bean.setName(DataUtility.getString(request.getParameter("name")));
	        bean.setDescription(DataUtility.getString(request.getParameter("description")));

	        populateDTO(bean, request);

	        return bean;
	    }

	    /**
	     * Handles GET request.
	     * Loads Spc data if ID is present and forwards to view.
	     */
	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        long id = DataUtility.getLong(request.getParameter("id"));
	        SpcModel model = new SpcModel();

	        if (id > 0) {
	            try {
	                SpcBean bean = model.findByPk(id);
	                ServletUtility.setBean(bean, request);
	            } catch (ApplicationException e) {
					e.printStackTrace();
					 ServletUtility.handleException(e, request, response,getView());
					return;
	            }
	        }
	        ServletUtility.forward(getView(), request, response);
	    }

	    /**
	     * Handles POST request.
	     * Supports save, update, cancel, and reset operations.
	     */
	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        String op = DataUtility.getString(request.getParameter("operation"));
	        SpcModel model = new SpcModel();
	        long id = DataUtility.getLong(request.getParameter("id"));

	        if (OP_SAVE.equalsIgnoreCase(op)) {

	            SpcBean bean = (SpcBean) populateBean(request);
	            try {
	                long pk = model.add(bean);
	                ServletUtility.setBean(bean, request);
	                ServletUtility.setSuccessMessage("Spc added successfully", request);
	            } catch (DuplicateRecordException e) {
	                ServletUtility.setBean(bean, request);
	                ServletUtility.setErrorMessage("Spc Id already exists", request);
	            } catch (ApplicationException e) {
					e.printStackTrace();
					ServletUtility.handleExceptionDB(getView(), request, response);
					return;
	            } catch (DataBaseException e) {
					e.printStackTrace();
				}
	        } else if (OP_UPDATE.equalsIgnoreCase(op)) {

	            SpcBean bean = (SpcBean) populateBean(request);

	            try {
	                if (id > 0) {
	                    model.update(bean);
	                }
	                ServletUtility.setBean(bean, request);
	                ServletUtility.setSuccessMessage("Data is successfully updated", request);
	            } catch (DuplicateRecordException e) {
	                ServletUtility.setBean(bean, request);
	                ServletUtility.setErrorMessage("Spc already exists", request);
	            } catch (ApplicationException e) {
					e.printStackTrace();
					ServletUtility.handleExceptionDB(getView(), request, response);
					return;
	            }

	        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
	            ServletUtility.redirect(ORSView.SPC_LIST_CTL, request, response);
	            return;
	        } else if (OP_RESET.equalsIgnoreCase(op)) {
	            ServletUtility.redirect(ORSView.SPC_CTL, request, response);
	            return;
	        }
	        ServletUtility.forward(getView(), request, response);
	    }

	    /**
	     * Returns the view page for SpcCtl.
	     * 
	     * @return String representing Spc view path
	     */
	    @Override
	    protected String getView() {
	        return ORSView.SPC_VIEW;
	    }

}
