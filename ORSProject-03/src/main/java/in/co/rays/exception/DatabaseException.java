package in.co.rays.exception;

/**
 * @author Anshul Prajapati
 */
public class DatabaseException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public DatabaseException(String msg){
		super(msg);
	}
}
