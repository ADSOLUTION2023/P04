package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import in.co.rays.proj4.bean.DoctorBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DataBaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class DoctorModel {

	public Integer nextPk() throws DataBaseException {

		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from doctor");
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
	 * Adds a new Doctor to the database.
	 *
	 * @param bean the DoctorBean containing Doctor details
	 * @return primary key of the newly added Doctor
	 * @throws ApplicationException     if an application-level exception occurs
	 * @throws DuplicateRecordException if the login ID already exists
	 */
	public long add(DoctorBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;
		int pk = 0;

		DoctorBean existbean = findByName(bean.getFirstName());

		if (existbean != null) {
			throw new DuplicateRecordException("Doctor already exists");
		}

		try {
			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn
					.prepareStatement("insert into doctor values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getFirstName());
			pstmt.setString(3, bean.getLastName());
			pstmt.setLong(4, bean.getExperience());
			pstmt.setString(5, bean.getMobileNo());
			pstmt.setString(6, bean.getEmail());
			pstmt.setLong(7, bean.getConsultationFee());
			pstmt.setLong(8, bean.getSpcId());
			pstmt.setString(9, bean.getCreatedBy());
			pstmt.setString(10, bean.getModifiedBy());
			pstmt.setTimestamp(11, bean.getCreatedDatetime());
			pstmt.setTimestamp(12, bean.getModifiedDatetime());
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
			throw new ApplicationException("Exception : Exception in add Doctor");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	/**
	 * Updates an existing Doctor in the database.
	 *
	 * @param bean the DoctorBean containing updated Doctor details
	 * @throws DuplicateRecordException if the login ID already exists for another
	 *                                  Doctor
	 * @throws ApplicationException     if an application-level exception occurs
	 */
	public void update(DoctorBean bean) throws DuplicateRecordException, ApplicationException {

		Connection conn = null;

		DoctorBean beanExist = findByName(bean.getFirstName());

		if (beanExist != null && (beanExist.getId() != bean.getId())) {
			throw new DuplicateRecordException("Login Id is already exist");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update doctor set first_name = ?, last_name = ?, experience = ?, mobile_no = ?, email = ?, consultationFee = ?,spcId = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
			pstmt.setString(1, bean.getFirstName());
			pstmt.setString(2, bean.getLastName());
			pstmt.setLong(3, bean.getExperience());
			pstmt.setString(4, bean.getMobileNo());
			pstmt.setString(5, bean.getEmail());
			pstmt.setLong(6, bean.getConsultationFee());
			pstmt.setLong(7, bean.getSpcId());
			pstmt.setString(8, bean.getCreatedBy());
			pstmt.setString(9, bean.getModifiedBy());
			pstmt.setTimestamp(10, bean.getCreatedDatetime());
			pstmt.setTimestamp(11, bean.getModifiedDatetime());
			pstmt.setLong(12, bean.getId());
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
			throw new ApplicationException("Exception in updating Doctor ");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Deletes a Doctor from the database.
	 *
	 * @param bean the DoctorBean containing Doctor ID to be deleted
	 * @throws ApplicationException if an application-level exception occurs
	 */
	public void delete(DoctorBean bean) throws ApplicationException {

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from doctor where id = ?");
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
			throw new ApplicationException("Exception : Exception in delete Doctor");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Finds a Doctor by primary key.
	 *
	 * @param pk the primary key of the Doctor
	 * @return DoctorBean if found, otherwise null
	 * @throws ApplicationException if an application-level exception occurs
	 */
	public DoctorBean findByPk(long pk) throws ApplicationException {

		DoctorBean bean = null;
		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from doctor where id = ?");

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new DoctorBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setExperience(rs.getLong(4));
				bean.setMobileNo(rs.getString(5));
				bean.setEmail(rs.getString(6));
				bean.setConsultationFee(rs.getLong(7));
				bean.setSpcId(rs.getLong(8));
				bean.setCreatedBy(rs.getString(9));
				bean.setModifiedBy(rs.getString(10));
				bean.setCreatedDatetime(rs.getTimestamp(11));
				bean.setModifiedDatetime(rs.getTimestamp(12));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting Doctor by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	/**
	 * Finds a Doctor by login ID.
	 *
	 * @param login the login ID of the Doctor
	 * @return DoctorBean if found, otherwise null
	 * @throws ApplicationException if an application-level exception occurs
	 */
	public DoctorBean findByName(String firstName) throws ApplicationException {

		StringBuffer sql = new StringBuffer("select * from doctor where firstName = ?");

		DoctorBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, firstName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new DoctorBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setExperience(rs.getLong(4));
				bean.setMobileNo(rs.getString(5));
				bean.setEmail(rs.getString(6));
				bean.setConsultationFee(rs.getLong(7));
				bean.setSpcId(rs.getLong(8));
				bean.setCreatedBy(rs.getString(9));
				bean.setModifiedBy(rs.getString(10));
				bean.setCreatedDatetime(rs.getTimestamp(11));
				bean.setModifiedDatetime(rs.getTimestamp(12));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting Doctor by login");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	/**
	 * Authenticates a Doctor using login and password.
	 *
	 * @param login    the login ID
	 * @param password the password
	 * @return DoctorBean if credentials are correct, otherwise null
	 * @throws ApplicationException if an application-level exception occurs
	 */
	public DoctorBean authenticate(String login, String password) throws ApplicationException {

		DoctorBean bean = null;
		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from doctor where login = ? and password = ?");

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, login);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new DoctorBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setExperience(rs.getLong(4));
				bean.setMobileNo(rs.getString(5));
				bean.setEmail(rs.getString(6));
				bean.setConsultationFee(rs.getLong(7));
				bean.setSpcId(rs.getLong(8));
				bean.setCreatedBy(rs.getString(9));
				bean.setModifiedBy(rs.getString(10));
				bean.setCreatedDatetime(rs.getTimestamp(11));
				bean.setModifiedDatetime(rs.getTimestamp(12));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in get roles");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	/**
	 * Searches Doctors based on criteria and pagination.
	 *
	 * @param bean     the DoctorBean containing search criteria
	 * @param pageNo   the page number
	 * @param pageSize the number of records per page
	 * @return list of matching Doctors
	 * @throws ApplicationException if an application-level exception occurs
	 */
	public List<DoctorBean> search(DoctorBean bean, int pageNo, int pageSize) throws ApplicationException {

		Connection conn = null;
		ArrayList<DoctorBean> list = new ArrayList<DoctorBean>();

		StringBuffer sql = new StringBuffer("select * from doctor where 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" and id = " + bean.getId());
			}
			if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
				sql.append(" and first_name like '" + bean.getFirstName() + "%'");
			}
			if (bean.getLastName() != null && bean.getLastName().length() > 0) {
				sql.append(" and last_name like '" + bean.getLastName() + "%'");
			}
			if (bean.getExperience() > 0) {
			    sql.append(" and experience = " + bean.getExperience());
			}
			if (bean.getMobileNo() != null && bean.getMobileNo().length() > 0) {
			    sql.append(" and mobile_no like '" + bean.getMobileNo() + "%'");
			}
			if (bean.getEmail() != null && bean.getEmail().length() > 0) {
			    sql.append(" and email like '" + bean.getEmail() + "%'");
			}
			if (bean.getConsultationFee() > 0) {
			    sql.append(" and consultation_fee = " + bean.getConsultationFee());
			}
			if (bean.getSpcId() > 0) {
			    sql.append(" and spc_id = " + bean.getSpcId());
			}
		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}
		System.out.println("sql ===== > " + sql.toString());
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new DoctorBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setExperience(rs.getLong(4));
				bean.setMobileNo(rs.getString(5));
				bean.setEmail(rs.getString(6));
				bean.setConsultationFee(rs.getLong(7));
				bean.setSpcId(rs.getLong(8));
				bean.setCreatedBy(rs.getString(9));
				bean.setModifiedBy(rs.getString(10));
				bean.setCreatedDatetime(rs.getTimestamp(11));
				bean.setModifiedDatetime(rs.getTimestamp(12));
				list.add(bean);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in search Doctor");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}
}
