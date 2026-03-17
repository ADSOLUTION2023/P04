package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.SpcBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DataBaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class SpcModel {
	
	public Integer nextPk() throws DataBaseException {
		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from spc");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			throw new DataBaseException("Exception : Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;
	}

	/**
	 * Adds a new Spc record into database.
	 *
	 * @param bean SpcBean containing Spc data to add
	 * @return primary key of newly inserted Spc
	 * @throws DatabaseException         if a database access error occurs while getting pk
	 * @throws ApplicationException      if any SQL exception occurs while adding Spc
	 * @throws DuplicateRecordException  if a Spc with same name already exists
	 */
	public long add(SpcBean bean) throws DataBaseException, ApplicationException, DuplicateRecordException {

	    Connection conn = null;
	    int pk = 0;

	    SpcBean existBSpc = findByName(bean.getName());
	    if (existBSpc != null) {
	        throw new DuplicateRecordException("Spc already exists");
	    }

	    try {

	        pk = nextPk();
	        conn = JDBCDataSource.getConnection();
	        conn.setAutoCommit(false);

	        PreparedStatement pstmt = conn.prepareStatement(
	                "insert into spc values (?, ?, ?, ?, ?, ?, ?, ?)");

	        pstmt.setInt(1, pk);
	        pstmt.setString(2, bean.getName());
	        pstmt.setString(3, bean.getDescription());
	        pstmt.setString(4, bean.getSpecialization());
	        pstmt.setString(5, bean.getCreatedBy());
	        pstmt.setString(6, bean.getModifiedBy());
	        pstmt.setTimestamp(7, bean.getCreatedDatetime());
	        pstmt.setTimestamp(8, bean.getModifiedDatetime());

	        pstmt.executeUpdate();
	        conn.commit();
	        pstmt.close();

	    } catch (SQLException e) {

	        try {
	            conn.rollback();
	        } catch (SQLException ex) {
	            throw new ApplicationException("Exception: add rollback exception " + ex.getMessage());
	        }

	        throw new ApplicationException("Exception: Exception in add Spc");

	    } finally {
	        JDBCDataSource.closeConnection(conn);
	    }

	    return pk;
	}

	/**
	 * Deletes a Spc record from database.
	 *
	 * @param bean SpcBean containing id of Spc to delete
	 * @throws ApplicationException if a SQL error occurs during delete
	 */
	public void delete(SpcBean bean) throws ApplicationException {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from spc where id = ?");
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete Spc");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Updates an existing Spc record.
	 *
	 * @param bean SpcBean containing updated values (must include id)
	 * @throws ApplicationException     if a SQL error occurs while updating
	 * @throws DuplicateRecordException if another Spc with same name exists
	 */
	public void update(SpcBean bean) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;
		SpcBean existSpc = findByName(bean.getName());

		if (existSpc != null && existSpc.getId() != bean.getId()) {
			throw new DuplicateRecordException("Spc already exists");
		}

		
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update spc set name = ?, description = ?,specialisation = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getDescription());
			pstmt.setString(3, bean.getSpecialization());
			pstmt.setString(4, bean.getCreatedBy());
			pstmt.setString(5, bean.getModifiedBy());
			pstmt.setTimestamp(6, bean.getCreatedDatetime());
			pstmt.setTimestamp(7, bean.getModifiedDatetime());
			pstmt.setLong(8, bean.getId());
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
			throw new ApplicationException("Exception in updating Spc");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Finds a Spc by primary key.
	 *
	 * @param pk primary key of Spc
	 * @return SpcBean if found, otherwise null
	 * @throws ApplicationException if a SQL error occurs while fetching data
	 */
	public SpcBean findByPk(long pk) throws ApplicationException {
		Connection conn = null;
		SpcBean bean = null;
		StringBuffer sql = new StringBuffer("select * from spc where id = ?");
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new SpcBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setSpecialization(rs.getString(4));
				bean.setCreatedBy(rs.getString(5));
				bean.setModifiedBy(rs.getString(6));
				bean.setCreatedDatetime(rs.getTimestamp(7));
				bean.setModifiedDatetime(rs.getTimestamp(8));
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in getting User by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}
	
	/**
	 * Finds a Spc by name.
	 *
	 * @param name Spc name to find
	 * @return SpcBean if found, otherwise null
	 * @throws ApplicationException if a SQL error occurs while fetching data
	 */
	public SpcBean findByName(String name) throws ApplicationException {
		Connection conn = null;
		SpcBean bean = null;
		StringBuffer sql = new StringBuffer("select * from spc where name = ?");
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new SpcBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setSpecialization(rs.getString(4));
				bean.setCreatedBy(rs.getString(5));
				bean.setModifiedBy(rs.getString(6));
				bean.setCreatedDatetime(rs.getTimestamp(7));
				bean.setModifiedDatetime(rs.getTimestamp(8));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in getting Doctor by name");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}
	
	/**
	 * Returns all Spcs.
	 *
	 * @return List of SpcBean
	 * @throws ApplicationException if a SQL error occurs during retrieval
	 */
	public List list() throws ApplicationException {
		return search(null, 0, 0);
	}
	
	/**
	 * Searches Spcs based on provided filter bean and supports pagination.
	 *
	 * @param bean     SpcBean filter (null means no filter)
	 * @param pageNo   page number (1-based). If pageSize &gt; 0, pageNo is used to compute offset.
	 * @param pageSize number of records per page. If 0, returns all matching rows.
	 * @return List of SpcBean matching criteria
	 * @throws ApplicationException if a SQL error occurs during search
	 */
	public List<SpcBean> search(SpcBean bean, int pageNo, int pageSize) throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from spc where 1=1");
		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" and id = " + bean.getId());
			}
			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" and name like '%" + bean.getName() + "%'");
			}
			if (bean.getDescription() != null && bean.getDescription().length() > 0) {
				sql.append(" and description like '%" + bean.getDescription() + "%'");
			}
			if (bean.getSpecialization() != null && bean.getSpecialization().length() > 0) {
			    sql.append(" and specialization like '%" + bean.getSpecialization() + "%'");
			}
		}
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}
		Connection conn = null;
		ArrayList<SpcBean> list = new ArrayList<SpcBean>();
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new SpcBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setSpecialization(rs.getString(4));
				bean.setCreatedBy(rs.getString(5));
				bean.setModifiedBy(rs.getString(6));
				bean.setCreatedDatetime(rs.getTimestamp(7));
				bean.setModifiedDatetime(rs.getTimestamp(8));
				list.add(bean);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in search Spc");
		}finally {
			JDBCDataSource.closeConnection(conn);
		}
		
		return list;
	}
}

