package jarse;

public class JarseException extends Exception {

	private String message;
	
	public JarseException() {
		message = "";
	}
	
	public JarseException(String msg) {
		this.message = msg;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
