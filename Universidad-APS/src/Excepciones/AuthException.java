package Excepciones;

public class AuthException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public AuthException (String msg) {
		super(msg);
	}

}
