package Excepciones;

public class DBUpdateException extends Exception {
	
	private static final long serialVersionUID = 1L;

	/**
	 * ExcepcionLexica: constructor.
	 * @param msg: mensaje de reporte para la excepci�n.
	 */
	public DBUpdateException (String msg) {
		super(msg);
	}	

}
