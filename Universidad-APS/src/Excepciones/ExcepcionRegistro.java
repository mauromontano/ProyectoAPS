package Excepciones;

public class ExcepcionRegistro extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * ExcepcionLexica: constructor.
	 * @param msg: mensaje de reporte para la excepci�n.
	 */
	public ExcepcionRegistro (String msg) {
		super(msg);
	}	

}
