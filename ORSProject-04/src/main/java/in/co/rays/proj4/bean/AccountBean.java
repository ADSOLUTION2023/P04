package in.co.rays.proj4.bean;

import java.util.Date;

public class AccountBean extends BaseBean{
	  
		private String bankName;
	    private String accountNo;
	    private String name;
	    private double balance;
	    private String accountType;
	    private Date doo;
	    //public static final int SAVING = 4;
		//public static final int CURRENT = 5;
	    
		public String getBankName() {
			return bankName;
		}
		public void setBankName(String bankName) {
			this.bankName = bankName;
		}
		public String getAccountNo() {
			return accountNo;
		}
		public void setAccountNo(String accountNo) {
			this.accountNo = accountNo;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getBalance() {
			return balance;
		}
		public void setBalance(double balance) {
			this.balance = balance;
		}
		public String getAccountType() {
			return accountType;
		}
		public void setAccountType(String accountType) {
			this.accountType = accountType;
		}
		public Date getDoo() {
			return doo;
		}
		public void setDoo(Date doo) {
			this.doo = doo;
		}
		@Override
		public String getKey() {
			return id + "";
		}

		@Override
		public String getValue() {
			return name;
		}
}
	   
		