package in.co.rays.model;

import java.util.List;

import in.co.rays.dto.CollegeDTO;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;


/**
 * Interface of College model
 * @author AMIT
 *
 */
public interface CollegeModelInt {
public long add(CollegeDTO dto)throws ApplicationException,DuplicateRecordException;
public void delete(CollegeDTO dto)throws ApplicationException;
public void update(CollegeDTO dto)throws ApplicationException,DuplicateRecordException;
public List list()throws ApplicationException, Exception;
public List list(int pageNo,int pageSize)throws ApplicationException, Exception;
public List search(CollegeDTO dto)throws ApplicationException;
public List search(CollegeDTO dto,int pageNo,int pageSize)throws ApplicationException;
public CollegeDTO findByPK(long pk)throws ApplicationException;
public CollegeDTO fingByName(String name)throws ApplicationException;
}
