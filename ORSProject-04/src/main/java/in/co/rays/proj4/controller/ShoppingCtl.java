package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.ShoppingBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.ShoppingModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "ShoppingCtl", urlPatterns = { "/ctl/ShoppingCtl" })
public class ShoppingCtl extends BaseCtl {

    private static Logger log = Logger.getLogger(ShoppingCtl.class);

    @Override
    protected void preload(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // No dropdown needed
    }

    @Override
    protected boolean validate(HttpServletRequest request) {

        log.debug("ShoppingCtl Method validate started");

        String op = DataUtility.getString(request.getParameter("operation"));

        if (!OP_SAVE.equalsIgnoreCase(op) && !OP_UPDATE.equalsIgnoreCase(op)) {
            return true;
        }

        boolean pass = true;

        // Product Name
        if (DataValidator.isNull(request.getParameter("productName"))) {
            request.setAttribute("productName",
                    PropertyReader.getValue("error.require", "Product Name"));
            pass = false;
        }

        // Shop Name
        if (DataValidator.isNull(request.getParameter("shopName"))) {
            request.setAttribute("shopName",
                    PropertyReader.getValue("error.require", "Shop Name"));
            pass = false;
        }

        // Product Price
        if (DataValidator.isNull(request.getParameter("productPrice"))) {
            request.setAttribute("productPrice",
                    PropertyReader.getValue("error.require", "Product Price"));
            pass = false;
        }

        // Purchase Date
        if (DataValidator.isNull(request.getParameter("purchaseDate"))) {
            request.setAttribute("purchaseDate",
                    PropertyReader.getValue("error.require", "Purchase Date"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("purchaseDate"))) {
            request.setAttribute("purchaseDate",
                    PropertyReader.getValue("error.date", "Purchase Date"));
            pass = false;
        }

        log.debug("ShoppingCtl Method validate ended");
        return pass;
    }

    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.debug("ShoppingCtl Method populateBean started");

        ShoppingBean bean = new ShoppingBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setProductName(DataUtility.getString(request.getParameter("productName")));
        bean.setShopName(DataUtility.getString(request.getParameter("shopName")));
        bean.setProductPrice(DataUtility.getString(request.getParameter("productPrice")));
        bean.setPurchaseDate(DataUtility.getDate(request.getParameter("purchaseDate")));

        populateDTO(bean, request);

        log.debug("ShoppingCtl Method populateBean ended");
        return bean;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("ShoppingCtl Method doGet started");

        long id = DataUtility.getLong(request.getParameter("id"));
        ShoppingModel model = new ShoppingModel();

        if (id > 0) {
            try {
                ShoppingBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, request);
            } catch (ApplicationException e) {
                e.printStackTrace();
                //ServletUtility.handleExceptionDB(getView(), request, response);
                return;
            }
        }

        ServletUtility.forward(getView(), request, response);
        log.debug("ShoppingCtl Method doGet ended");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("ShoppingCtl Method doPost started");

        String op = DataUtility.getString(request.getParameter("operation"));
        ShoppingModel model = new ShoppingModel();

        long id = DataUtility.getLong(request.getParameter("id"));

        if (OP_SAVE.equalsIgnoreCase(op)) {

            ShoppingBean bean = (ShoppingBean) populateBean(request);

            try {
                model.add(bean);
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Product Added Successfully", request);

            } catch (ApplicationException e) {
                e.printStackTrace();
               // ServletUtility.handleExceptionDB(getView(), request, response);
                return;
            }

        } else if (OP_UPDATE.equalsIgnoreCase(op)) {

            ShoppingBean bean = (ShoppingBean) populateBean(request);

            try {
                if (id > 0) {
                    model.update(bean);
                }
                ServletUtility.setBean(bean, request);
                ServletUtility.setSuccessMessage("Product Updated Successfully", request);

            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, request, response, getView());
                return;
            }

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.SHOPPING_CTL, request, response);
            return;

        } else if (OP_RESET.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.SHOPPING_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
        log.debug("ShoppingCtl Method doPost ended");
    }

    @Override
    protected String getView() {
        return ORSView.SHOPPING_VIEW;
    }
}