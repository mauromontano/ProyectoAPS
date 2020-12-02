package Excepciones;

public class DBUpdateException extends Exception {
	
	private static final long serialVersionUID = 1L;

	/**
	 * ExcepcionLexica: constructor.
	 * @param msg: mensaje de reporte para la excepción.
	 */
	public DBUpdateException (String msg) {
		super(msg);
	}	

}
