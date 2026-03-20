package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.ShoppingBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DataBaseException;
import in.co.rays.proj4.util.JDBCDataSource;

public class ShoppingModel {

    // 🔥 NEXT PK
    public Integer nextPk() throws DataBaseException {

        Connection conn = null;
        int pk = 0;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("select max(id) from st_shopping");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pk = rs.getInt(1);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            throw new DataBaseException("Exception in getting PK");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return pk + 1;
    }

    // 🔥 ADD
    public long add(ShoppingBean bean) throws ApplicationException {

        Connection conn = null;
        int pk = 0;

        try {
            pk = nextPk();

            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement("insert into st_shopping values (?, ?, ?, ?, ?)"
            );

            ps.setInt(1, pk);
            ps.setString(2, bean.getProductName());
            ps.setString(3, bean.getShopName());
            ps.setString(4, bean.getProductPrice());
            ps.setDate(5, new java.sql.Date(bean.getPurchaseDate().getTime()));
           

            ps.executeUpdate();

            conn.commit();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (Exception ex) {
            	e.printStackTrace();
                throw new ApplicationException("Rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in add Shopping");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return pk;
    }

    // 🔥 UPDATE
    public void update(ShoppingBean bean) throws ApplicationException {

        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(
                "update st_shopping set product_name=?, shop_name=?, product_price=?, purchase_date=?,where id=?"
            );

            ps.setString(1, bean.getProductName());
            ps.setString(2, bean.getShopName());
            ps.setString(3, bean.getProductPrice());
            ps.setDate(4, new java.sql.Date(bean.getPurchaseDate().getTime()));
            ps.setLong(7, bean.getId());

            ps.executeUpdate();
            conn.commit();
            ps.close();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Rollback error " + ex.getMessage());
            }
            throw new ApplicationException("Exception in update Shopping");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    // 🔥 DELETE
    public void delete(ShoppingBean bean) throws ApplicationException {

        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement("delete from st_shopping where id=?");

            ps.setLong(1, bean.getId());
            ps.executeUpdate();

            conn.commit();
            ps.close();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Rollback error " + ex.getMessage());
            }
            throw new ApplicationException("Exception in delete Shopping");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    // 🔥 FIND BY PK
    public ShoppingBean findByPk(long pk) throws ApplicationException {

        ShoppingBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                "select * from st_shopping where id=?"
            );

            ps.setLong(1, pk);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                bean = new ShoppingBean();
                bean.setId(rs.getLong(1));
                bean.setProductName(rs.getString(2));
                bean.setShopName(rs.getString(3));
                bean.setProductPrice(rs.getString(4));
                bean.setPurchaseDate(rs.getDate(5));
               
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in findByPk Shopping");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return bean;
    }

    // 🔥 SEARCH
    public List<ShoppingBean> search(ShoppingBean bean, int pageNo, int pageSize)
            throws ApplicationException {

        List<ShoppingBean> list = new ArrayList<>();
        Connection conn = null;

        StringBuffer sql = new StringBuffer("select * from st_shopping where 1=1");

        if (bean != null) {

            if (bean.getProductName() != null && bean.getProductName().length() > 0) {
                sql.append(" and product_name like '" + bean.getProductName() + "%'");
            }

            if (bean.getShopName() != null && bean.getShopName().length() > 0) {
                sql.append(" and shop_name like '" + bean.getShopName() + "%'");
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + ", " + pageSize);
        }

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                bean = new ShoppingBean();
                bean.setId(rs.getLong(1));
                bean.setProductName(rs.getString(2));
                bean.setShopName(rs.getString(3));
                bean.setProductPrice(rs.getString(4));
                bean.setPurchaseDate(rs.getDate(5));

                list.add(bean);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in search Shopping");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return list;
    }
}