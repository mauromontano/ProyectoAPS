package Vistas;

import Modelos.Alumno;

public class VistaAlumno extends VistaHome {
	
	private static final long serialVersionUID = 1L;
	private Alumno modeloAlumno;
	private static VistaAlumno instancia = null;
	
	
	public static VistaAlumno obtenerVistaAlumno () {
		if (instancia == null) {
			instancia = new VistaAlumno();
		}
		return instancia;
	}
	
	
	private VistaAlumno () {
		super();
		this.setBounds(0,0,1200,728);
	}
	
	
	public void generarVistaAlumno (Alumno mod) {
		modeloAlumno = mod;
		lblIdentidad.setText(modeloAlumno.obtenerNombre() + " " + modeloAlumno.obtenerApellido() + " - LU: " + modeloAlumno.obtenerLU());
	}

}
