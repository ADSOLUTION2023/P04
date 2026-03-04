package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.HospitalBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.RoleModel;
import in.co.rays.proj4.model.HospitalModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "HospitalCtl", urlPatterns = { "/ctl/HospitalCtl" })
public class HospitalCtl extends BaseCtl {

		private static Logger log = Logger.getLogger(HospitalCtl.class);

		/**
		 * Validates the Hospital form input.
		 */
		protected boolean validate(HttpServletRequest request) {

			    log.debug("HospitalCtl Method validate Started");

			    boolean pass = true;

			    // Hospital Name
			    if (DataValidator.isNull(request.getParameter("name"))) {
			        request.setAttribute("name",
			                PropertyReader.getValue("error.require", "Hospital Name"));
			        pass = false;

			    } else if (!DataValidator.isName(request.getParameter("name"))) {
			        request.setAttribute("name", "Invalid Hospital Name");
			        pass = false;
			    }

			    // Address
			    if (DataValidator.isNull(request.getParameter("address"))) {
			        request.setAttribute("address",
			                PropertyReader.getValue("error.require", "Address"));
			        pass = false;
			    }

			    // City
			    if (DataValidator.isNull(request.getParameter("city"))) {
			        request.setAttribute("city",
			                PropertyReader.getValue("error.require", "City"));
			        pass = false;

			    } else if (!DataValidator.isName(request.getParameter("city"))) {
			        request.setAttribute("city", "Invalid City Name");
			        pass = false;
			    }

			    // Phone Number
			    if (DataValidator.isNull(request.getParameter("phone"))) {
			        request.setAttribute("phone",
			                PropertyReader.getValue("error.require", "Phone Number"));
			        pass = false;

			    } else if (!DataValidator.isPhoneLength(request.getParameter("phone"))) {
			        request.setAttribute("phone", "Phone number must be 10 digits");
			        pass = false;

			    } else if (!DataValidator.isPhoneNo(request.getParameter("phone"))) {
			        request.setAttribute("phone", "Invalid Phone Number");
			        pass = false;
			    }

			    // Email
			    if (DataValidator.isNull(request.getParameter("email"))) {
			        request.setAttribute("email",
			                PropertyReader.getValue("error.require", "Email"));
			        pass = false;

			    } else if (!DataValidator.isEmail(request.getParameter("email"))) {
			        request.setAttribute("email", "Invalid Email Address");
			        pass = false;
			    }

			    log.debug("HospitalCtl Method validate Ended");

			    return pass;
			}

		/**
		 * Populates a HospitalBean from request parameters.
		 */
		@Override
		protected BaseBean populateBean(HttpServletRequest request) {

			log.debug("HospitalCtl Method populate started");

			HospitalBean bean = new HospitalBean();

			    bean.setId(DataUtility.getLong(request.getParameter("id")));
			    bean.setName(DataUtility.getString(request.getParameter("name")));
			    bean.setAddress(DataUtility.getString(request.getParameter("address")));
			    bean.setCity(DataUtility.getString(request.getParameter("city")));
			    bean.setPhone(DataUtility.getString(request.getParameter("phone")));
			    bean.setEmail(DataUtility.getString(request.getParameter("email")));

			    populateDTO(bean, request); // ORS audit fields
			    log.debug("HospitalCtl Method populate ended");
			    return bean;
		
			
		}

		/**
		 * Handles GET request to display Hospital data if id is present.
		 */
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

			log.debug("HospitalCtl Method doGet started");

			long id = DataUtility.getLong(request.getParameter("id"));
			HospitalModel model = new HospitalModel();

			if (id > 0) {
				try {
					HospitalBean bean = model.findByPk(id);
					ServletUtility.setBean(bean, request);
				} catch (ApplicationException e) {
					e.printStackTrace();
					ServletUtility.handleExceptionDB(getView(), request, response);
					return;

				}
			}
			ServletUtility.forward(getView(), request, response);
			log.debug("HospitalCtl Method doGet ended");
		}

		/**
		 * Handles POST request to add or update Hospital data.
		 */
		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

			log.debug("HospitalCtl Method doPost started");

			String op = DataUtility.getString(request.getParameter("operation"));
			HospitalModel model = new HospitalModel();
			long id = DataUtility.getLong(request.getParameter("id"));

			if (OP_SAVE.equalsIgnoreCase(op)) {
				HospitalBean bean = (HospitalBean) populateBean(request);
				try {
					System.out.println("TRY BLOCK STARTED");
					long pk = model.add(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Hospital added successfully", request);
					 System.out.println("DATA FETCHED");
				} catch (DuplicateRecordException e) {
					e.printStackTrace();
					System.out.println("CATCH1 BLOCK EXECUTED");
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage("Hospital already exists", request);
				} catch (ApplicationException e) {
					System.out.println("CATCH2 BLOCK EXECUTED");
					e.printStackTrace();
					ServletUtility.handleExceptionDB(getView(), request, response);
					return;

				}

			} else if (OP_UPDATE.equalsIgnoreCase(op)) {
				HospitalBean bean = (HospitalBean) populateBean(request);
				try {
					if (id > 0) {
						model.update(bean);
					}
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Hospital updated successfully", request);
				} catch (DuplicateRecordException e) {
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage("Hospital already exists", request);
				} catch (ApplicationException e) {
					e.printStackTrace();
					ServletUtility.setErrorMessage("Database Server Down...", request);
					ServletUtility.handleException(e, request, response, getView());
					return;
				}
			} else if (OP_CANCEL.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.HOSPITAL_LIST_CTL, request, response);
				return;

			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.HOSPITAL_CTL, request, response);
				return;
			}

			ServletUtility.forward(getView(), request, response);
			log.debug("HospitalCtl Method doPost ended");
		}

		/**
		 * Returns the view for Hospital form.
		 */
	
		@Override
		protected String getView() {
			 
			return ORSView.HOSPITAL_VIEW;
		}
}