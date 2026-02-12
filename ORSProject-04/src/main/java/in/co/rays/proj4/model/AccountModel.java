package in.co.rays.proj4.model;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import in.co.rays.proj4.bean.AccountBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DataBaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

/**
 * @author AMIT
 *
 */
public class AccountModel {

	/**
	 * @return
	 * @throws DataBaseException
	 */
	public Integer nextPk() throws DataBaseException {

		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from account ");
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
	 * @param bean
	 * @return
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public long add(AccountBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;
		int pk = 0;

		AccountBean existbean = findByAccNo(bean.getAccountNo());

		if (existbean != null) {
			throw new DuplicateRecordException("Account Number already exists");
		}

		try {
			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn
					.prepareStatement("insert into account values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getBankName());
			pstmt.setString(3, bean.getAccountNo());
			pstmt.setString(4,bean.getName());
			pstmt.setDouble(5, bean.getBalance());
			pstmt.setString(6, bean.getAccountType());
			pstmt.setDate(7, new java.sql.Date(bean.getDoo().getTime()));
			pstmt.setString(8,bean.getCreatedBy());
			pstmt.setString(9, bean.getModifiedBy());
			pstmt.setTimestamp(10, bean.getCreatedDatetime());
			pstmt.setTimestamp(11, bean.getModifiedDatetime());
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
			throw new ApplicationException("Exception : Exception in add account");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	/**
	 * @param bean
	 * @throws DuplicateRecordException
	 * @throws ApplicationException
	 */
	public void update(AccountBean bean) throws DuplicateRecordException, ApplicationException {

		Connection conn = null;

		AccountBean beanExist = findByAccNo(bean.getAccountNo());

		if (beanExist != null && (beanExist.getId() != bean.getId())) {
			throw new DuplicateRecordException("Account already exist");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update account set bankName = ?, accountNo = ?,name = ?, balance = ?, accountType = ?, doo = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
			pstmt.setString(1, bean.getBankName());
			pstmt.setString(2, bean.getAccountNo());
			pstmt.setString(3, bean.getName());
			pstmt.setDouble(4, bean.getBalance());
			pstmt.setString(5, bean.getAccountType());
			pstmt.setDate(6, new java.sql.Date(bean.getDoo().getTime()));
			pstmt.setString(7, bean.getCreatedBy());
			pstmt.setString(8, bean.getModifiedBy());
			pstmt.setTimestamp(9, bean.getCreatedDatetime());
			pstmt.setTimestamp(10, bean.getModifiedDatetime());
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
			throw new ApplicationException("Exception in updating User ");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * @param bean
	 * @throws ApplicationException
	 */
	public void delete(AccountBean bean) throws ApplicationException {

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from account where id = ?");
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
			throw new ApplicationException("Exception : Exception in delete account");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}
	/**
	 * @param pk
	 * @return
	 * @throws ApplicationException
	 */
	public AccountBean findByPk(long pk) throws ApplicationException {

		AccountBean bean = null;
		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from account where id = ?");

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new AccountBean();

				bean.setId(rs.getLong(1));
				bean.setBankName(rs.getString(2));
				bean.setAccountNo(rs.getString(3));
				bean.setName(rs.getString(4));
				bean.setBalance(rs.getDouble(5));
				bean.setAccountType(rs.getString(6));
				bean.setDoo(rs.getDate(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting User by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	/**
	 * @param login
	 * @return
	 * @throws ApplicationException
	 */
	private AccountBean findByAccNo(String login) throws ApplicationException {

		AccountBean bean = null;
		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from account where accountNo = ?");

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			bean.setAccountNo(bean.getAccountNo());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new AccountBean();

				bean.setId(rs.getLong(1));
				bean.setBankName(rs.getString(2));
				bean.setAccountNo(rs.getString(3));
				bean.setName(rs.getString(4));
				bean.setBalance(rs.getDouble(5));
				bean.setAccountType(rs.getString(6));
				bean.setDoo(rs.getDate(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting account by accountNo");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}
	/**
	 * @return
	 * @throws ApplicationException
	 */
	public List<AccountBean> list() throws ApplicationException {
        return search(null, 0, 0);
    }

	/**
	 * @param bean
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws ApplicationException
	 */
	public List<AccountBean> search(AccountBean bean, int pageNo, int pageSize) throws ApplicationException {

		Connection conn = null;
		ArrayList<AccountBean> list = new ArrayList<AccountBean>();

		StringBuffer sql = new StringBuffer("select * from account where 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" and id = " + bean.getId());
			}
			if (bean.getBankName() != null && bean.getBankName().length() > 0) {
				sql.append(" and mobile_no = " + bean.getBankName());
			}
			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" and name like '" + bean.getName() + "%'");
			}
			if (bean.getAccountNo() != null && bean.getAccountNo().length() > 0) {
				sql.append(" and accountNo like '" + bean.getAccountNo() + "%'");
			}
			if (bean.getBalance() >= 0) {
				sql.append(" and balance like '" + bean.getBalance() + "%'");
			}
			if (bean.getAccountType() != null && bean.getAccountType().length() > 0) {
				sql.append(" and accType like '" + bean.getAccountType() + "%'");
			}
			if (bean.getDoo() != null && bean.getDoo().getTime() > 0) {
				sql.append(" and doo like '" + new java.sql.Date(bean.getDoo().getTime()) + "%'");
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
				bean = new AccountBean();
				bean.setId(rs.getLong(1));
				bean.setBankName(rs.getString(2));
				bean.setAccountNo(rs.getString(3));
				bean.setName(rs.getString(4));
				bean.setBalance(rs.getDouble(5));
				bean.setAccountType(rs.getString(6));
				bean.setDoo(rs.getDate(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));
				list.add(bean);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in search account");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}

}
