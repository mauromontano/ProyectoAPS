package Excepciones;

public class ExcepcionEliminacion extends Exception {
	
	private static final long serialVersionUID = 1L;

	/**
	 * ExcepcionLexica: constructor.
	 * @param msg: mensaje de reporte para la excepci�n.
	 */
	public ExcepcionEliminacion (String msg) {
		super(msg);
	}	

}
