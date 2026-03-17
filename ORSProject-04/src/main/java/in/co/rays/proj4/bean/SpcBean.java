package in.co.rays.proj4.bean;

public class SpcBean extends BaseBean {
	
	public static final int CARDIOLOGIST = 1;
	public static final int ONCHOLOGIST = 2;
	public static final int NUEROLOGIST = 3;
	public static final int ORTHOPEDIC = 4;
	public static final int PHYSICIAN = 5;
	
	private String name;
	private String specialization;
	private String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
