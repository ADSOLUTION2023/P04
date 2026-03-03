package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.PatientBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DataBaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

/**
 * @author AMIT
 *
 */
public class PatientModel {

	/**
	 * @return
	 * @throws DataBaseException
	 */
	public Integer nextPk() throws DataBaseException {

		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from patient");
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
	 * Adds a new patient to the database.
	 *
	 * @param bean the PatientBean containing patient details
	 * @return primary key of the newly added patient
	 * @throws ApplicationException 
	 * @throws DuplicateRecordException 
	 * @throws Exception 
	 */
	public long add(PatientBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;
		int pk = 0;

		PatientBean existbean = findMobileNo(bean.getMobileNo());

		if (existbean != null) {
			throw new DuplicateRecordException("Patient already exists");
		}

		try {
			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("INSERT INTO patient VALUES(?,?,?,?,?,?,?,?)");

			ps.setLong(1, nextPk());
			ps.setString(2, bean.getFirstName());
			ps.setString(3, bean.getLastName());
			ps.setDate(4, new java.sql.Date(bean.getDob().getTime()));
			ps.setString(5, bean.getGender());
			ps.setString(6, bean.getMobileNo());
			ps.setString(7, bean.getEmail());
			ps.setString(8, bean.getAddress());
			ps.executeUpdate();

			conn.commit();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add patient");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	private PatientBean findMobileNo(String mobileNo) throws DuplicateRecordException  {

		PatientBean bean = null;
		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from patient where mobile_no= ?");

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ps.setString(1, mobileNo);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				bean = new PatientBean();
				bean.setId(rs.getLong("id"));
				bean.setFirstName(rs.getString("first_name"));
				bean.setLastName(rs.getString("last_name"));
				bean.setDob(rs.getDate("dob"));
				bean.setGender(rs.getString("gender"));
				bean.setMobileNo(rs.getString("mobile_no"));
				bean.setEmail(rs.getString("email"));
				bean.setAddress(rs.getString("address"));
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DuplicateRecordException("Exception : Exception in getting patient by mobileNo");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;

	}

	/**
	 * Updates an existing patient in the database.
	 *
	 * @param bean the PatientBean containing updated patient details
	 * @throws DuplicateRecordException if the Patient with Id already exists for
	 *                                  another patient
	 * @throws ApplicationException if an application-level exception occurs
	 */
	public void update(PatientBean bean) throws DuplicateRecordException, ApplicationException {

		Connection conn = null;

		PatientBean beanExist = findByPk(bean.getId());

		if (beanExist != null && (beanExist.getId()!= bean.getId())) {
			throw new DuplicateRecordException("Patient with Id is already exist");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE patient SET first_name=?,last_name=?,dob=?,gender=?,mobile_no=?,email=?,address=? WHERE id=?");

			ps.setString(1, bean.getFirstName());
			ps.setString(2, bean.getLastName());
			ps.setDate(3, new java.sql.Date(bean.getDob().getTime()));
			ps.setString(4, bean.getGender());
			ps.setString(5, bean.getMobileNo());
			ps.setString(6, bean.getEmail());
			ps.setString(7, bean.getAddress());
			ps.setLong(8, bean.getId());
			ps.executeUpdate();
			conn.commit();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception in updating patient ");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Deletes a patient from the database.
	 *
	 * @param bean the PatientBean containing patient ID to be deleted
	 * @throws ApplicationException if an application-level exception occurs
	 */
	public void delete(PatientBean bean) throws ApplicationException {

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("delete from patient where id = ?");
			ps.setLong(1, bean.getId());
			ps.executeUpdate();
			conn.commit();
			ps.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete patient");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Finds a patient by primary key.
	 *
	 * @param pk the primary key of the patient
	 * @return PatientBean if found, otherwise null
	 * @throws ApplicationException if an application-level exception occurs
	 */
	public PatientBean findByPk(long pk) throws ApplicationException {

		PatientBean bean = null;
		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from patient where id = ?");

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new PatientBean();
				bean.setId(rs.getLong("id"));
				bean.setFirstName(rs.getString("first_name"));
				bean.setLastName(rs.getString("last_name"));
				bean.setDob(rs.getDate("dob"));
				bean.setGender(rs.getString("gender"));
				bean.setMobileNo(rs.getString("mobile_no"));
				bean.setEmail(rs.getString("email"));
				bean.setAddress(rs.getString("address"));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting patient by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	/**
	 * Finds a patient by login ID.
	 *
	 * @param login the login ID of the patient
	 * @return PatientBean if found, otherwise null
	 * @throws Exception 
	 */
	
	public List<PatientBean> search(PatientBean bean, int pageNo, int pageSize)
	        throws ApplicationException {

	    StringBuffer sql = new StringBuffer("select * from patient where 1=1");

	    if (bean != null) {

	        if (bean.getId() > 0) {
	            sql.append(" and id = " + bean.getId());
	        }

	        if (bean.getFirstName()!= null && bean.getFirstName().length() > 0) {
	            sql.append(" and first_name like '%" + bean.getFirstName() + "%'");
	        }

	        if (bean.getLastName()!= null && bean.getLastName().length() > 0) {
	            sql.append(" and last_name like '%" + bean.getLastName() + "%'");
	        }

	        if (bean.getEmail()!= null && bean.getEmail().length() > 0) {
	            sql.append(" and email like '%" + bean.getEmail() + "%'");
	        }

	        if (bean.getMobileNo()!= null && bean.getMobileNo().length() > 0) {
	            sql.append(" and mobile_no like '%" + bean.getMobileNo() + "%'");
	        }

	        if (bean.getGender()!= null && bean.getGender().length() > 0) {
	            sql.append(" and gender like '%" + bean.getGender() + "%'");
	        }
	    }

	    // ⭐ Pagination (same ORS logic)
	    if (pageSize > 0) {
	        pageNo = (pageNo - 1) * pageSize;
	        sql.append(" limit " + pageNo + ", " + pageSize);
	    }

	    Connection conn = null;
	    ArrayList<PatientBean> list = new ArrayList<PatientBean>();

	    try {
	        conn = JDBCDataSource.getConnection();
	        PreparedStatement pstmt = conn.prepareStatement(sql.toString());
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {

	            PatientBean b = new PatientBean();

	            b.setId(rs.getLong(1));
	            b.setFirstName(rs.getString(2));
	            b.setLastName(rs.getString(3));
	            b.setDob(rs.getDate(4));
	            b.setGender(rs.getString(5));
	            b.setMobileNo(rs.getString(6));
	            b.setEmail(rs.getString(7));
	            b.setAddress(rs.getString(8));
	            list.add(b);
	        }

	        rs.close();
	        pstmt.close();

	    } catch (Exception e) {
	    	e.printStackTrace();
	        throw new ApplicationException("Exception : Exception in search Patient");
	    } finally {
	        JDBCDataSource.closeConnection(conn);
	    }

	    return list;
	}

}