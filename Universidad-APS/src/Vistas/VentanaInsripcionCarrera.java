package Vistas;

import javax.swing.JFrame;

import Controladores.ControladorCarrera;
import Controladores.ControladorInscripcion;
import Modelos.Carrera;
import javax.swing.JComboBox;
import java.util.List;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaInsripcionCarrera extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JComboBox<Carrera> comboCarreras;
	
	/**
	 * CONSTRUCTOR: ventana para la inscripción a una carrera por parte de un
	   alumno particular	
	 * @param lu: número de libreta universitaria asociado al alumno involucrado
	 */
	public VentanaInsripcionCarrera(int lu) {
		setSize(315,232);
		getContentPane().setLayout(null);
		setVisible(true);
		
		comboCarreras = new JComboBox<Carrera>();
		comboCarreras.setBounds(68, 43, 165, 20);
		getContentPane().add(comboCarreras);
		
		cargarCarreras();
		
		JButton btnGuardarInscripcin = new JButton("Guardar inscripci\u00F3n");
		btnGuardarInscripcin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Carrera carreraElegida = (Carrera) comboCarreras.getSelectedItem();
				ControladorInscripcion.controlador().registrarInsCarrera(lu, carreraElegida.obtenerId());
				dispose();
			}
		});
		btnGuardarInscripcin.setBounds(91, 108, 123, 23);
		getContentPane().add(btnGuardarInscripcin);
	}
	
	
	/**
	 * cargarCarreras: lleva a cabo la carga de cada uno de los modelos de carreras
	   en el combobox asociado a las carreras en las que el alumno con LU dado está
	   inscripto
	 * @param lu: número de libreta universitaria asociado al alumno
	 */
	private void cargarCarreras () {
		List<Carrera> listaCarreras;
		listaCarreras = ControladorCarrera.controlador().carreras();
		for (Carrera c : listaCarreras) {
			comboCarreras.addItem(c);
		}
	}
}
