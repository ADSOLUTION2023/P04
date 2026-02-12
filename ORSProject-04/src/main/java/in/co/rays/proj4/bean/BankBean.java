package in.co.rays.proj4.bean;

import java.util.Date;

public class BankBean extends BaseBean {
	private String name;
	private String accountNo;
	private String Type;
	private Date Dob;

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public Date getDob() {
		return Dob;
	}

	public void setDob(Date dob) {
		Dob = dob;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
