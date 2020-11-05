package Vistas;

import java.util.List;
import Controladores.ControladorInscripcion;
import Controladores.ControladorMateria;
import Excepciones.ExcepcionRegistro;
import Modelos.Carrera;
import Modelos.Materia;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class VentanaInscripcionMateria extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JComboBox<Carrera> comboCarreras;
	private JComboBox<Materia> comboMaterias;
	
	/**
	 * CONSTRUCTOR: vista para la inscripción de un alumno a una materia
	 */
	public VentanaInscripcionMateria (int lu) {
		setTitle("Inscripci\u00F3n a una materia");
		setSize(331,236);
		getContentPane().setLayout(null);
		setResizable(false);		
		setLocationRelativeTo(null);
		setVisible(true);
		
		comboCarreras = new JComboBox<Carrera>();
		comboCarreras.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					cargarMaterias(lu);
				}				
			}
		});
		comboCarreras.setBounds(40, 44, 237, 20);
		getContentPane().add(comboCarreras);
		
		comboMaterias = new JComboBox<Materia>();
		comboMaterias.setBounds(40, 99, 237, 20);
		getContentPane().add(comboMaterias);
		
		JButton btnGuardar = new JButton("Guardar inscripci\u00F3n");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardarInscripcion(lu);
			}
		});
		btnGuardar.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
		btnGuardar.setBounds(85, 149, 149, 23);
		getContentPane().add(btnGuardar);
		
		cargarCarreras(lu);
	}
	
	
	/**
	 * cargarCarreras: lleva a cabo la carga de cada uno de los modelos de carreras
	   en el combobox asociado a las carreras en las que el alumno con LU dado está
	   inscripto
	 * @param lu: número de libreta universitaria asociado al alumno
	 */
	private void cargarCarreras (int lu) {
		List<Carrera> listaCarreras;
		listaCarreras = ControladorInscripcion.controlador().carrerasInscripto(lu);
		for (Carrera c : listaCarreras) {
			comboCarreras.addItem(c);
		}
	}
	
	/**
	 * cargarCarreras: lleva a cabo la carga de cada uno de los modelos de materias,
	   en el combobox asociado a las materias las que el alumno con LU dado puede
	   inscribirse
	 * @param lu: número de libreta universitaria asociado al alumno
	 */
	private void cargarMaterias (int lu) {
		List<Materia> listaMaterias;
		Carrera carreraElegida = (Carrera) comboCarreras.getSelectedItem();
		//System.out.println("El alumno con LU: " + lu + " selecciono la carrera: " + carreraElegida.toString());
		comboMaterias.removeAllItems();
		int id = carreraElegida.obtenerId();
		listaMaterias = ControladorMateria.controlador().materiasAlumno(id, lu);
		for (Materia m : listaMaterias) {
			comboMaterias.addItem(m);
		}
	}
	
	/**
	 * guardarInscripcion: solicita el registro de la inscripción de un alumno con,
	   LU asociado, para una materia particular, cuyo modelo fue seleccionado
	 * @param lu: número de libreta universitaria asociado al alumno
	 */
	private void guardarInscripcion (int lu) {
		Materia materiaElegida = (Materia) comboMaterias.getSelectedItem();
		int idMateria = materiaElegida.obtenerId();
		// Registrar la inscripción a la materia de la carrera elegida
		try 
		{
			ControladorInscripcion.controlador().registrarInsMateria(lu, idMateria);
		}
		catch (ExcepcionRegistro ex) 
		{
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
					   ex.getMessage() + "\n",
					   "Fallo en inscripción a materia: " + materiaElegida.toString(),
					   JOptionPane.ERROR_MESSAGE);
		}
		dispose();
	}
	
}
