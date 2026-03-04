package in.co.rays.proj4.test;

import java.sql.Timestamp;
import java.util.Date;

import in.co.rays.proj4.bean.HospitalBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.HospitalModel;

public class TestHospitalModel {
	
	public static HospitalModel model = new HospitalModel();
	
	public static void main(String[] args) {
		
		//testAdd();
		testUpdate();
		
	}
public static void testAdd() {
		
		HospitalBean bean = new HospitalBean();
		
		try {
			bean.setName("Rashri Hospital");
			bean.setAddress("Vijay Nagar");
			bean.setCity("Indore");
			bean.setEmail("2762avb@gmail.com");
			bean.setPhone("9673890712");
			bean.setCreatedBy("admin");
			bean.setModifiedBy("admin");
			bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
			bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
			long pk = model.add(bean);
			System.out.println("Test add Successfull");

		} catch (ApplicationException | DuplicateRecordException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

public static void testUpdate() {
 
	
	try {
		HospitalBean bean = model.findByPk(3L);
		
		bean.setName("Rajshri Hospital");
		bean.setAddress("Vijay Nagar");
		bean.setCity("Indore");
		bean.setEmail("2762avb@gmail.com");
		bean.setPhone("9673890712");
		bean.setCreatedBy("admin");
		bean.setModifiedBy("admin");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));
		model.update(bean);
		System.out.println("Test Update Succesfull");

	} catch (ApplicationException | DuplicateRecordException e) {
		e.printStackTrace();
	} catch (Exception e) {
		e.printStackTrace();
	}
}
public static void testDelete() {
	
	try {
		HospitalBean bean = new HospitalBean();
		
		long pk = 1L;
		bean.setId(pk);
		model.delete(bean);
		HospitalBean deletedbean = model.findByPk(pk);
		if (deletedbean != null) {
			System.out.println("Test Delete fail");
		}
	} catch (ApplicationException e) {
		e.printStackTrace();
	}
}
}
