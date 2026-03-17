package in.co.rays.proj4.bean;

public class DoctorBean extends BaseBean{
	
	
	private long id;
    private String firstName;
    private String lastName;
    private long experience;
    private String mobileNo;
    private String email;
    private long consultationFee;
    private long spcId;
    

	public long getSpcId() {
		return spcId;
	}

	public void setSpcId(long spcId) {
		this.spcId = spcId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getExperience() {
		return experience;
	}

	public void setExperience(long experience) {
		this.experience = experience;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getConsultationFee() {
		return consultationFee;
	}

	public void setConsultationFee(long consultationFee) {
		this.consultationFee = consultationFee;
	}

	@Override
	public String getKey() {
	 
		return id + "";
	}

	@Override
	public String getValue() {
		 
		return firstName+ "" + lastName;
	}

}
