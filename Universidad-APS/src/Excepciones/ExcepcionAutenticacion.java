package Excepciones;

public class ExcepcionAutenticacion extends Exception {
	
	private static final long serialVersionUID = 1L;

	/**
	 * ExcepcionLexica: constructor.
	 * @param msg: mensaje de reporte para la excepci�n.
	 */
	public ExcepcionAutenticacion (String msg) {
		super(msg);
	}
	

}
