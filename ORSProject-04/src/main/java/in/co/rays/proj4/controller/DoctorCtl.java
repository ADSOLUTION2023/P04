package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.DoctorBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;

import in.co.rays.proj4.model.SpcModel;
import in.co.rays.proj4.model.DoctorModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "DoctorCtl", urlPatterns = { "/ctl/DoctorCtl" })
public class DoctorCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(DoctorCtl.class);

	/**
	 * Preloads list of roles to populate role dropdown in the form.
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void preload(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("DoctorCtl Method preload started");
		SpcModel spcModel = new SpcModel();

		try {
			List spcList = spcModel.list();
			//System.out.println("SPC LIST SIZE = " + spcList.size());
			request.setAttribute("spcList", spcList);
		} catch (ApplicationException e) {
			request.setAttribute("spcList", new ArrayList<>());
//			ServletUtility.handleExceptionDB(getView(), request, response);
		}
		log.debug("DoctorCtl Method preload ended");
	}

	/**
	 * Validates the Doctor form input.
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {

		log.debug("DoctorCtl Method validate started");

		// ⭐ ORS GOLDEN RULE – validation only on SAVE / UPDATE
		String op = DataUtility.getString(request.getParameter("operation"));

		if (!OP_SAVE.equalsIgnoreCase(op) && !OP_UPDATE.equalsIgnoreCase(op)) {
			return true;
		}

		boolean pass = true;

		// First Name
		if (DataValidator.isNull(request.getParameter("firstName"))) {
			request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("firstName"))) {
			request.setAttribute("firstName", "Invalid First Name");
			pass = false;
		}

		// Last Name
		if (DataValidator.isNull(request.getParameter("lastName"))) {
			request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("lastName"))) {
			request.setAttribute("lastName", "Invalid Last Name");
			pass = false;
		}

		// Experience
		if (DataValidator.isNull(request.getParameter("experience"))) {
			request.setAttribute("experience", PropertyReader.getValue("error.require", "Experience"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("experience"))) {
			request.setAttribute("experience", PropertyReader.getValue("error.integer", "Experience"));
			pass = false;
		}

		// Mobile No
		if (DataValidator.isNull(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "Mobile No"));
			pass = false;
		} else if (!DataValidator.isMobileNo(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNo", PropertyReader.getValue("error.mobileNo", "Mobile No"));
			pass = false;
		}

		// Email
		if (DataValidator.isNull(request.getParameter("email"))) {
			request.setAttribute("email", PropertyReader.getValue("error.require", "Email"));
			pass = false;
		} else if (!DataValidator.isEmail(request.getParameter("email"))) {
			request.setAttribute("email", PropertyReader.getValue("error.email", "Email"));
			pass = false;
		}

		// Consultation Fee
		if (DataValidator.isNull(request.getParameter("consultationFee"))) {
			request.setAttribute("consultationFee", PropertyReader.getValue("error.require", "Consultation Fee"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("consultationFee"))) {
			request.setAttribute("consultationFee", PropertyReader.getValue("error.integer", "Consultation Fee"));
			pass = false;
		}
		// Specialization Id
		if (DataValidator.isNull(request.getParameter("spcId"))) {
			request.setAttribute("spcId", PropertyReader.getValue("error.require", "Specialization"));
			pass = false;
		}

		log.debug("DoctorCtl Method validate ended");
		return pass;
	}

	/**
	 * Populates a DoctorBean from request parameters.
	 */
	@Override
	protected DoctorBean populateBean(HttpServletRequest request) {

		log.debug("DoctorCtl Method populate started");

		DoctorBean bean = new DoctorBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
		bean.setSpcId(DataUtility.getLong(request.getParameter("spcId")));
		bean.setExperience(DataUtility.getLong(request.getParameter("experience")));
		bean.setEmail(DataUtility.getString(request.getParameter("email")));
		bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
		bean.setConsultationFee(DataUtility.getLong(request.getParameter("consultationFee")));

		populateDTO(bean, request);
		log.debug("DoctorCtl Method populate ended");
		return bean;
	}

	/**
	 * Handles GET request to display Doctor data if id is present.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("DoctorCtl Method doGet started");

		long id = DataUtility.getLong(request.getParameter("id"));
		DoctorModel model = new DoctorModel();

		if (id > 0) {
			try {
				DoctorBean bean = model.findByPk(id);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				e.printStackTrace();
				ServletUtility.handleExceptionDB(getView(), request, response);
				return;

			}
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("DoctorCtl Method doGet ended");
	}

	/**
	 * Handles POST request to add or update Doctor data.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("DoctorCtl Method doPost started");

		String op = DataUtility.getString(request.getParameter("operation"));
		DoctorModel model = new DoctorModel();
		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)) {
			DoctorBean bean = (DoctorBean) populateBean(request);
			try {
				long pk = model.add(bean);
				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage("Doctor added successfully", request);
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Doctor already exists", request);
			} catch (ApplicationException e) {
				e.printStackTrace();
				ServletUtility.handleExceptionDB(getView(), request, response);
				return;

			}

		} else if (OP_UPDATE.equalsIgnoreCase(op)) {
			DoctorBean bean = (DoctorBean) populateBean(request);
			try {
				if (id > 0) {
					model.update(bean);
				}
				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage("Doctor updated successfully", request);
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Doctor already exists", request);
			} catch (ApplicationException e) {
				e.printStackTrace();
				//ServletUtility.setErrorMessage("Database Server Down...", request);
				ServletUtility.handleExceptionDB(getView(), request, response);
				return;
			}
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.DOCTOR_LIST_CTL, request, response);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.DOCTOR_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("DoctorCtl Method doPost ended");
	}

	/**
	 * Returns the view for Doctor form.
	 */

	@Override
	protected String getView() {

		return ORSView.DOCTOR_VIEW;
	}

}
