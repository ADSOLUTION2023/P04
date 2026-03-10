package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.HospitalBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DataBaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class HospitalModel {

	/**
	 * Returns the next primary key for Hospital table.
	 *
	 * @return next primary key
	 * @throws DatabaseException if any database error occurs
	 */
	public Integer nextPk() throws DataBaseException {

		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from hospital");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			throw new DataBaseException("Exception : Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;
	}

	/**
	 * Adds a new Hospital to the database.
	 *
	 * @param bean the HospitalBean containing Hospital details
	 * @return primary key of the newly added Hospital
	 * @throws ApplicationException     if an application-level exception occurs
	 * @throws DuplicateRecordException if the login ID already exists
	 */
	public long add(HospitalBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;
		int pk = 0;

		HospitalBean existbean = findByName(bean.getName());

		if (existbean != null) {
			throw new DuplicateRecordException("Hospital already exists");
		}

		try {
			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO HOSPITAL VALUES(?,?,?,?,?,?)");

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getAddress());
			pstmt.setString(4, bean.getCity());
			pstmt.setString(5, bean.getPhone());
			pstmt.setString(6, bean.getEmail());
			
			pstmt.executeUpdate();

			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add Hospital");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	/**
	 * Updates an existing Hospital in the database.
	 *
	 * @param bean the HospitalBean containing updated Hospital details
	 * @throws DuplicateRecordException if the login ID already exists for another
	 *                                  Hospital
	 * @throws ApplicationException     if an application-level exception occurs
	 */
	public void update(HospitalBean bean) throws DuplicateRecordException, ApplicationException {

		Connection conn = null;

		HospitalBean beanExist = findByName(bean.getName());

		if (beanExist != null && (beanExist.getId() != bean.getId())) {
			throw new DuplicateRecordException("Hospital is already exist");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"UPDATE HOSPITAL SET NAME=?, ADDRESS=?, CITY=?, PHONE=?, EMAIL=?,WHERE ID=?");

			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getAddress());
			pstmt.setString(3, bean.getCity());
			pstmt.setString(4, bean.getPhone());
			pstmt.setString(5, bean.getEmail());
			pstmt.setLong(10, bean.getId());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception in updating Hospital ");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Deletes a Hospital from the database.
	 *
	 * @param bean the HospitalBean containing Hospital ID to be deleted
	 * @throws ApplicationException if an application-level exception occurs
	 */
	public void delete(HospitalBean bean) throws ApplicationException {

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from hospital where id = ?");
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete Hospital");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Finds a Hospital by primary key.
	 *
	 * @param pk the primary key of the Hospital
	 * @return HospitalBean if found, otherwise null
	 * @throws ApplicationException if an application-level exception occurs
	 */
	public HospitalBean findByPk(long pk) throws ApplicationException {

		HospitalBean bean = null;
		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from hospital where id = ?");

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new HospitalBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setCity(rs.getString(4));
				bean.setPhone(rs.getString(5));
				bean.setEmail(rs.getString(6));
				
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting Hospital by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	/**
	 * Finds a Hospital by name.
	 *
	 * @param name of the Hospital
	 * @return HospitalBean if found, otherwise null
	 * @throws ApplicationException if an application-level exception occurs
	 */
	public HospitalBean findByName(String string) throws ApplicationException {

		HospitalBean bean = null;
		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from hospital where name = ?");

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, string);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new HospitalBean();
				
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setCity(rs.getString(4));
				bean.setPhone(rs.getString(5));
				bean.setEmail(rs.getString(6));
				
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting Hospital by name");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}
	/**
	 * Searches Hospitals based on criteria and pagination.
	 *
	 * @param bean     the HospitalBean containing search criteria
	 * @param pageNo   the page number
	 * @param pageSize the number of records per page
	 * @return list of matching Hospitals
	 * @throws ApplicationException if an application-level exception occurs
	 */
	public List<HospitalBean> search(HospitalBean bean, int pageNo, int pageSize)
	        throws ApplicationException {

	    Connection conn = null;
	    ArrayList<HospitalBean> list = new ArrayList<>();

	    StringBuffer sql = new StringBuffer("SELECT * FROM hospital WHERE 1=1");

	    if (bean != null) {

	        if (bean.getId() > 0) {
	            sql.append(" AND ID = " + bean.getId());
	        }

	        if (bean.getName() != null && bean.getName().length() > 0) {
	            sql.append(" AND NAME LIKE '" + bean.getName() + "%'");
	        }

	        if (bean.getCity() != null && bean.getCity().length() > 0) {
	            sql.append(" AND CITY LIKE '" + bean.getCity() + "%'");
	        }

	        if (bean.getPhone() != null && bean.getPhone().length() > 0) {
	            sql.append(" AND PHONE LIKE '" + bean.getPhone() + "%'");
	        }

	        if (bean.getEmail() != null && bean.getEmail().length() > 0) {
	            sql.append(" AND EMAIL LIKE '" + bean.getEmail() + "%'");
	        }
	    }

	    // Pagination
	    if (pageSize > 0) {
	        pageNo = (pageNo - 1) * pageSize;
	        sql.append(" LIMIT " + pageNo + "," + pageSize);
	    }

	    System.out.println("SQL => " + sql);

	    try {
	        conn = JDBCDataSource.getConnection();
	        PreparedStatement pstmt = conn.prepareStatement(sql.toString());

	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {

	            HospitalBean hbean = new HospitalBean();

	            hbean.setId(rs.getLong("ID"));
	            hbean.setName(rs.getString("NAME"));
	            hbean.setAddress(rs.getString("ADDRESS"));
	            hbean.setCity(rs.getString("CITY"));
	            hbean.setPhone(rs.getString("PHONE"));
	            hbean.setEmail(rs.getString("EMAIL"));

	            list.add(hbean);
	        }

	        rs.close();
	        pstmt.close();

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new ApplicationException("Exception in search Hospital");
	    } finally {
	        JDBCDataSource.closeConnection(conn);
	    }

	    return list;
	}
}
