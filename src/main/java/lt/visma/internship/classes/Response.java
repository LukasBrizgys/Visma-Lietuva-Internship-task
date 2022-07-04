package lt.visma.internship.classes;

import java.io.Serializable;

public class Response implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public Response(String name) {
		this.message = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String name) {
		this.message = name;
	}
}
