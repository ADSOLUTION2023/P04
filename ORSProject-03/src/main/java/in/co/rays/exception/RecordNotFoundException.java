package in.co.rays.exception;

/**
 * @author Anshul Prajapati
 */
public class RecordNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public RecordNotFoundException(String msg){
		super(msg);
	}
}
