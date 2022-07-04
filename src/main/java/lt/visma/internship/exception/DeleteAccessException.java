package lt.visma.internship.exception;

public class DeleteAccessException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public DeleteAccessException() {
		
	}

	public DeleteAccessException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

}
