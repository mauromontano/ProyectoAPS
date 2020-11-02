package Vistas;

import Modelos.Profesor;

public class VistaProfesor extends VistaHome {
	
	private static final long serialVersionUID = 1L;
	private Profesor modeloProfesor;
	private static VistaProfesor instancia = null;
	
	
	public static VistaProfesor obtenerVistaProfesor () {
		if (instancia == null) {
			instancia = new VistaProfesor();
		}
		return instancia;
	}
	
	
	private VistaProfesor () {
		super();
		this.setBounds(0,0,1200,728);
	}
	
	
	public void generarVistaProfesor (Profesor mod) {
		modeloProfesor = mod;
		lblIdentidad.setText(
				modeloProfesor.obtenerNombre() + " " + modeloProfesor.obtenerApellido() + " - Matrícula: " + modeloProfesor.obtenerMatricula());
	}

}
