package Vistas;

import Modelos.Alumno;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VistaAlumno extends VistaHome {
	
	private static final long serialVersionUID = 1L;
	private Alumno modeloAlumno;
	private static VistaAlumno instancia = null;
	
	
	public static VistaAlumno vista () {
		if (instancia == null) {
			instancia = new VistaAlumno();
		}
		return instancia;
	}
	
	
	private VistaAlumno () {
		super();
		lblIdentidad.setText("");
		this.setBounds(0,0,1200,728);
		
		JButton btnInsCarrera = new JButton("Inscribirse a carrera");
		btnInsCarrera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaInsripcionCarrera(modeloAlumno.obtenerLU());
			}
		});
		btnInsCarrera.setBounds(341, 83, 160, 31);
		add(btnInsCarrera);
		
		JButton btnInsMateria = new JButton("Inscribirse a materia");
		btnInsMateria.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaInscripcionMateria(modeloAlumno.obtenerLU());
			}
		});
		btnInsMateria.setBounds(586, 83, 160, 31);
		add(btnInsMateria);
	}
	
	
	public void generarVistaAlumno (Alumno mod) {
		modeloAlumno = mod;
		lblIdentidad.setText(modeloAlumno.obtenerNombre() + " " + modeloAlumno.obtenerApellido() + " - LU: " + modeloAlumno.obtenerLU());
	}

}
