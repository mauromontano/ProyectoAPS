package Vistas;

import Modelos.Alumno;
import Modelos.Carrera;
import Modelos.Dictado;
import Modelos.Materia;
import Modelos.MesaDeExamen;
import Modelos.Modelo;
import Modelos.Plan;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.SystemColor;
import javax.swing.border.LineBorder;

import Controladores.ControladorCarrera;
import Controladores.ControladorDictado;
import Controladores.ControladorInscripcion;
import Controladores.ControladorMateria;
import Controladores.ControladorMesa;
import Controladores.ControladorPlan;
import Excepciones.DBRetrieveException;
import Excepciones.DBUpdateException;
import Excepciones.InvalidActionException;

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
		lblIdentidad.setSize(1157, 37);
		panelBienvenida.setSize(1157, 37);
		panelBienvenida.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		lblIdentidad.setText("|");
		setBackground(SystemColor.controlHighlight);
		lblIdentidad.setForeground(Color.DARK_GRAY);
		lblIdentidad.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 16));
		this.setBounds(0,0,1200,728);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		panel.setBackground(SystemColor.menu);
		panel.setBounds(472, 180, 256, 253);
		add(panel);
		panel.setLayout(null);
		
		JButton btnInsMateria = new JButton("Inscribirse a materia");
		btnInsMateria.setBounds(45, 26, 166, 31);
		panel.add(btnInsMateria);
		btnInsMateria.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
		
		JButton btnInsCarrera = new JButton("Inscribirse a carrera");
		btnInsCarrera.setBounds(45, 110, 166, 31);
		panel.add(btnInsCarrera);
		btnInsCarrera.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
		
		JButton btnInscripcionesActivas = new JButton("Inscripciones activas");
		btnInscripcionesActivas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnInscripcionesActivas.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
		btnInscripcionesActivas.setBounds(45, 152, 166, 31);
		panel.add(btnInscripcionesActivas);
		
		JButton btnRevisarCalificaciones = new JButton("Revisar calificaciones");
		btnRevisarCalificaciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnRevisarCalificaciones.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
		btnRevisarCalificaciones.setBounds(45, 194, 166, 31);
		panel.add(btnRevisarCalificaciones);
		
		JButton btnInsFinal = new JButton("Inscribirse a un final");
		btnInsFinal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaInsFinal(modeloAlumno);
			}
		});
		btnInsFinal.setBounds(45, 68, 166, 31);
		panel.add(btnInsFinal);
		btnInsFinal.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
		btnInsCarrera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaInsripcionCarrera(modeloAlumno);
			}
		});
		btnInsMateria.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaInscripcionMateria(modeloAlumno);
			}
		});
	}
	
	
	public void generarVistaAlumno (Alumno mod) {
		modeloAlumno = mod;
		lblIdentidad.setText(modeloAlumno.obtenerNombre() + " " + modeloAlumno.obtenerApellido() + " - LU: " + modeloAlumno.obtenerLU());
	}
	
	
	private class VentanaInsripcionCarrera extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JComboBox<Carrera> comboCarreras;
		
		/**
		 * CONSTRUCTOR: ventana para la inscripción a una carrera por parte de un
		   alumno particular	
		 * @param lu: número de libreta universitaria asociado al alumno involucrado
		 */
		public VentanaInsripcionCarrera (Alumno alumno) {
			setSize(375,176);
			getContentPane().setLayout(null);
			setVisible(true);
			setLocationRelativeTo(null);
			setTitle("Inscripción a una carrera");
			
			comboCarreras = new JComboBox<Carrera>();
			comboCarreras.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			comboCarreras.setBounds(54, 43, 257, 20);
			getContentPane().add(comboCarreras);
			
			cargarCarreras();
			
			JButton btnGuardarIns = new JButton("Guardar inscripci\u00F3n");
			JFrame miVista = this;
			btnGuardarIns.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnGuardarIns.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Carrera carrera = (Carrera) comboCarreras.getSelectedItem();
					int lu = alumno.obtenerLU();
					int idMat = carrera.obtenerId();
					try 
					{
						ControladorInscripcion.controlador().registrarInsCar(lu, idMat);
					}
					catch (DBUpdateException ex) 
					{
						JOptionPane.showMessageDialog(miVista, ex.getMessage(), "Inscripción a una carrera", JOptionPane.ERROR_MESSAGE);
					}
					
					dispose();
				}
			});
			btnGuardarIns.setBounds(101, 91, 166, 23);
			getContentPane().add(btnGuardarIns);
		}
		
		
		/**
		 * cargarCarreras: lleva a cabo la carga de cada uno de los modelos de carreras
		   en el combobox asociado a las carreras en las que el alumno con LU dado está
		   inscripto
		 * @param lu: número de libreta universitaria asociado al alumno
		 */
		private void cargarCarreras () {
			List<Modelo> modelosCarrera;
			Carrera carrera = null;
			try 
			{
				modelosCarrera = ControladorCarrera.controlador().elementos();
				for (Modelo mod : modelosCarrera) {
					carrera = (Carrera) mod;
					comboCarreras.addItem(carrera);
				}
			}
			catch (DBRetrieveException ex) 
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de carreras registradas", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	
	
	private class VentanaInscripcionMateria extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JComboBox<Carrera> comboCarreras;
		private JComboBox<Materia> comboMaterias;
		private JComboBox<Dictado> comboDictados;
		
		/**
		 * CONSTRUCTOR: vista para la inscripción de un alumno a una materia
		 */
		public VentanaInscripcionMateria (Alumno alumno) {
			setTitle("Inscripci\u00F3n a una materia");
			setSize(370,240);
			getContentPane().setLayout(null);
			setResizable(false);		
			setLocationRelativeTo(null);
			setVisible(true);
			
			comboCarreras = new JComboBox<Carrera>();
			comboCarreras.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						cargarMaterias(alumno);
					}				
				}
			});
			comboCarreras.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			comboCarreras.setBounds(65, 42, 237, 20);
			getContentPane().add(comboCarreras);
			
			comboMaterias = new JComboBox<Materia>();
			comboMaterias.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						cargarDictados(alumno);
					}				
				}
			});
			comboMaterias.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			comboMaterias.setBounds(65, 78, 237, 20);
			getContentPane().add(comboMaterias);
			
			comboDictados = new JComboBox<Dictado>();		
			comboDictados.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			comboDictados.setBounds(65, 114, 237, 20);
			getContentPane().add(comboDictados);
			
			JButton btnGuardar = new JButton("Guardar inscripci\u00F3n");
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					guardarInscripcion(alumno);
				}
			});
			btnGuardar.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnGuardar.setBounds(103, 165, 166, 23);
			getContentPane().add(btnGuardar);
			
			cargarCarreras(alumno);
		}
		
		
		/**
		 * cargarCarreras: lleva a cabo la carga de cada uno de los modelos de carreras
		   en el combobox asociado a las carreras en las que el alumno con LU dado está
		   inscripto
		 * @param lu: número de libreta universitaria asociado al alumno
		 */
		private void cargarCarreras (Alumno alumno) {
			List<Carrera> listaCarreras;
			String lu = alumno.obtenerLU() + "";
			try 
			{
				listaCarreras = ControladorCarrera.controlador().carrerasInscripto(lu);
				for (Carrera c : listaCarreras) {
					comboCarreras.addItem(c);
				}
			}
			catch (DBRetrieveException ex) 
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de carreras", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}		
		}
		
		
		private void cargarMaterias (Alumno alumno) {
			List<Materia> listaMaterias;
			Carrera carrera = (Carrera) comboCarreras.getSelectedItem();
			int lu = alumno.obtenerLU();
			int id = carrera.obtenerId();			
			comboMaterias.removeAllItems();
			try 
			{
				listaMaterias = ControladorMateria.controlador().materiasDelAlumno(id, lu);
				for (Materia m : listaMaterias) {
					comboMaterias.addItem(m);
				}
			}
			catch (DBRetrieveException ex) 
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de materias", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}		
		}
		
		
		private void cargarDictados (Alumno alumno) {
			List<Dictado> listaDictados;
			Carrera carrera = (Carrera) comboCarreras.getSelectedItem();	
			Materia materia = (Materia) comboMaterias.getSelectedItem();
			comboDictados.removeAllItems();
			try 
			{
				Plan plan = ControladorPlan.controlador().planDeCarreraAlumno(alumno.obtenerLU(),carrera.obtenerId());
				int idMat = materia.obtenerId();
				int idPlan = plan.obtenerId();
				listaDictados = ControladorDictado.controlador().dictadosMateriaDelPlan(idMat, idPlan);
				for (Dictado d : listaDictados) {
					comboDictados.addItem(d);
				}
			}
			catch (DBRetrieveException ex) 
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de dictados", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}			
		}
		
		
		/**
		 * guardarInscripcion: solicita el registro de la inscripción de un alumno con,
		   LU asociado, para un dictado particular de una materia, cuyo modelo fue seleccionado
		 * @param lu: número de libreta universitaria asociado al alumno
		 */
		private void guardarInscripcion (Alumno alumno) {
			Carrera carreraElegida = (Carrera) comboCarreras.getSelectedItem();
			Materia materiaElegida = (Materia) comboMaterias.getSelectedItem();
			Dictado dictadoElegido = (Dictado) comboDictados.getSelectedItem();
			int lu = alumno.obtenerLU();
			int idMat;
			int idDict;
			// Verifico si se especifican cada uno de los valores de entrada requeridos para la inscripción al dictado
			if (carreraElegida == null) {
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
						"Debe elegir una carrera, una materia y un dictado para realizar su inscripción. \n" + 
						"Si no está inscripto en ninguna carrera, realice la inscripción correspondiente." + "\n",
						   "Fallo en inscripción",
						   JOptionPane.ERROR_MESSAGE);
			}
			else if (materiaElegida == null) {
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
						"Debe elegir una materia y un dictado particular donde inscribirse. \n" + 
						"Si no hay ninguno, espere a que esté disponible." + "\n",
						"Fallo en inscripción",
						JOptionPane.ERROR_MESSAGE);
			}
			else if (dictadoElegido == null) {
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
						   "Debe elegir un dictado asociado a la materia. Si no hay ninguno, espere a que esté disponible." + "\n",
						   "Fallo en inscripción",
						   JOptionPane.ERROR_MESSAGE);
			}
			else {
				idMat = materiaElegida.obtenerId();
				idDict = dictadoElegido.obtenerId();
				try 
				{
					ControladorInscripcion.controlador().registrarInsMat(lu, idMat, idDict);
					JOptionPane.showMessageDialog(this,
							"Inscripción a " + materiaElegida.obtenerNombre() + " - " + dictadoElegido.toString() + " registrada con éxito");
				}
				catch (DBUpdateException | InvalidActionException ex) 
				{
					JOptionPane.showMessageDialog(this, ex.getMessage(), "Inscripción a una materia", JOptionPane.ERROR_MESSAGE);
					this.dispose();
				}
			}		
			dispose();
		}
	}
	
	
public class VentanaInsFinal extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JComboBox<Carrera> comboCarreras;
		private JComboBox<Materia> comboMaterias;
		private JComboBox<MesaDeExamen> comboMesas;
		private JButton btnSiguiente;
		
		
		// CONSTRUCTOR: Ventana para el registro de un nuevo profesor
		
		public VentanaInsFinal (Alumno alumno) {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Inscripci\u00F3n a un final");
			setSize(450,231);
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS: registro de profesor
			
			comboCarreras = new JComboBox<Carrera>();
			comboCarreras.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						cargarMaterias(alumno);
					}				
				}
			});
			comboCarreras.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			comboCarreras.setBounds(158, 36, 247, 20);
			getContentPane().add(comboCarreras);
			
			comboMaterias = new JComboBox<Materia>();
			comboMaterias.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						cargarMesas();
					}				
				}
			});
			comboMaterias.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			comboMaterias.setBounds(158, 71, 247, 20);
			getContentPane().add(comboMaterias);
			
			comboMesas = new JComboBox<MesaDeExamen>();
			comboMesas.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			comboMesas.setBounds(158, 107, 247, 20);
			getContentPane().add(comboMesas);
			
			// CREACIÓN DE LABELS: registro de dictado
			
			JLabel lblCarrera = new JLabel("Carrera:");
			lblCarrera.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblCarrera.setBounds(97, 36, 51, 20);
			getContentPane().add(lblCarrera);
			
			JLabel lblMateria = new JLabel();
			lblMateria = new JLabel("Materia:");
			lblMateria.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblMateria.setBounds(97, 71, 51, 20);
			getContentPane().add(lblMateria);
			
			JLabel lblMesa = new JLabel("Mesa de examen:");
			lblMesa.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblMesa.setBounds(44, 107, 105, 20);
			getContentPane().add(lblMesa);
			
			btnSiguiente = new JButton("Guardar inscripción");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {					
					guardarInscripcion(alumno);
					dispose();
				}
			});
			btnSiguiente.setBounds(146, 158, 149, 23);
			getContentPane().add(btnSiguiente);
			
			cargarCarreras(alumno);		
		}
		
		
		/**
		 * cargarCarreras: lleva a cabo la carga de cada uno de los modelos de carreras
		   en el combobox asociado a las carreras en las que el alumno con LU dado está
		   inscripto
		 * @param lu: número de libreta universitaria asociado al alumno
		 */
		private void cargarCarreras (Alumno alumno) {
			List<Carrera> listaCarreras;
			String lu = alumno.obtenerLU() + "";
			try 
			{
				listaCarreras = ControladorCarrera.controlador().carrerasInscripto(lu);
				for (Carrera c : listaCarreras) {
					comboCarreras.addItem(c);
				}
			}
			catch (DBRetrieveException ex) 
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de carreras", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}		
		}
		
		
		private void cargarMaterias (Alumno alumno) {
			List<Materia> listaMaterias;
			Carrera carrera = (Carrera) comboCarreras.getSelectedItem();
			int lu = alumno.obtenerLU();
			int id = carrera.obtenerId();			
			comboMaterias.removeAllItems();
			try 
			{
				listaMaterias = ControladorMateria.controlador().materiasDelAlumno(id, lu);
				for (Materia m : listaMaterias) {
					comboMaterias.addItem(m);
				}
			}
			catch (DBRetrieveException ex) 
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de materias", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}		
		}
		
		
		private void cargarMesas () {
			List<MesaDeExamen> listaMesas;	
			Materia m = (Materia) comboMaterias.getSelectedItem();
			int idMat = m.obtenerId();
			comboMesas.removeAllItems();
			try 
			{
				listaMesas = ControladorMesa.controlador().mesasFuturasDeMateria(idMat);
				for (MesaDeExamen d : listaMesas) {
					comboMesas.addItem(d);
				}
			}
			catch (DBRetrieveException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de mesa de examen", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}			
		}
		
		
		private void guardarInscripcion (Alumno alumno) {
			Carrera carreraElegida = (Carrera) comboCarreras.getSelectedItem();
			Materia materiaElegida = (Materia) comboMaterias.getSelectedItem();
			MesaDeExamen mesaElegida = (MesaDeExamen) comboMesas.getSelectedItem();
			int lu = alumno.obtenerLU();
			int idMat;
			int idMesa;
			// Verifico si se especifican cada uno de los valores de entrada requeridos para la inscripción al dictado
			if (carreraElegida == null) {
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
						"Debe elegir una carrera, una materia y un dictado para realizar su inscripción. \n" + 
						"Si no está inscripto en ninguna carrera, realice la inscripción correspondiente." + "\n",
						   "Fallo en inscripción",
						   JOptionPane.ERROR_MESSAGE);
			}
			else if (materiaElegida == null) {
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
						"Debe elegir una materia y un dictado particular donde inscribirse. \n" + 
						"Si no hay ninguno, espere a que esté disponible." + "\n",
						"Fallo en inscripción",
						JOptionPane.ERROR_MESSAGE);
			}
			else if (mesaElegida == null) {
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
						   "Debe elegir una mesa de examen asociada a la materia. Si no hay ninguna, espere a que una esté disponible." + "\n",
						   "Fallo en inscripción",
						   JOptionPane.ERROR_MESSAGE);
			}
			else {
				idMat = materiaElegida.obtenerId();
				idMesa = mesaElegida.obtenerId();
				try 
				{
					ControladorInscripcion.controlador().registrarInsFinal(lu, idMat, idMesa);
					JOptionPane.showMessageDialog(this,
							"Inscripción a la mesa de examen final " + materiaElegida.obtenerNombre() + " - " + mesaElegida.toString() +
							" registrada con éxito");
				}
				catch (DBUpdateException | InvalidActionException ex) 
				{
					JOptionPane.showMessageDialog(this, ex.getMessage(), "Inscripción a una materia", JOptionPane.ERROR_MESSAGE);
					this.dispose();
				}
			}		
			dispose();
		}
		
}
	
}


