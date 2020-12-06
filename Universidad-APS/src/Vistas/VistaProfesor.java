package Vistas;

import Modelos.Alumno;
import Modelos.Dictado;
import Modelos.Materia;
import Modelos.MesaDeExamen;
import Modelos.Modelo;
import Modelos.Profesor;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import Controladores.ControladorAlumno;
import Controladores.ControladorDictado;
import Controladores.ControladorMateria;
import Controladores.ControladorMesa;
import Controladores.ControladorVistas;
import Excepciones.DBRetrieveException;
import Excepciones.DBUpdateException;
import java.awt.SystemColor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;

public class VistaProfesor extends VistaHome {
	
	private static final long serialVersionUID = 1L;
	private Profesor modeloProfesor;
	private static VistaProfesor instancia = null;
	
	
	public static VistaProfesor vista () {
		if (instancia == null) {
			instancia = new VistaProfesor();
		}
		return instancia;
	}
	
	
	private VistaProfesor () {
		super();
		panelBienvenida.setBounds(22, 11, 1157, 37);
		lblIdentidad.setBounds(22, 11, 1157, 37);
		this.setBounds(0,0,1200,728);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		panel.setBounds(472, 204, 256, 291);
		add(panel);
		panel.setLayout(null);
		
		JButton btnRevisionDictado = new JButton("Revisar dictado");
		btnRevisionDictado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VistaDictados.vista().generarVistaDictados(modeloProfesor);
				ControladorVistas.controlador().mostrar(VistaDictados.vista());
			}
		});
		btnRevisionDictado.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
		btnRevisionDictado.setBounds(47, 65, 166, 31);
		panel.add(btnRevisionDictado);
		
		JButton btnCalificarDict = new JButton("Calificar un dictado");
		btnCalificarDict.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaCalificacionDictado(modeloProfesor);
			}
		});
		btnCalificarDict.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
		btnCalificarDict.setBounds(47, 107, 166, 31);
		panel.add(btnCalificarDict);
		
		JButton btnCalificarExamenFinal = new JButton("Calificar examen final");
		btnCalificarExamenFinal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaCalificacionFinal(modeloProfesor);
			}
		});
		btnCalificarExamenFinal.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
		btnCalificarExamenFinal.setBounds(47, 149, 166, 31);
		panel.add(btnCalificarExamenFinal);
		
		
	//------------------------Lautaro------------------------------------------------------
		JButton btnGenerarActaCursado = new JButton("Generar Acta Cursado");
		btnGenerarActaCursado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				VistaReporteActaCursado.vista().generarVistaReporteActaCursado(modeloProfesor);
				
				ControladorVistas.controlador().mostrar(VistaReporteActaCursado.vista());
				
				
			}
		});
		btnGenerarActaCursado.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
		btnGenerarActaCursado.setBounds(47, 191, 166, 31);
		panel.add(btnGenerarActaCursado);
		
		JButton btnGenerarActaFinal = new JButton("Generar Acta Final");
		btnGenerarActaFinal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				VistaReporteActaFinal.vista().generarVistaReporteActaFinal(modeloProfesor);
				ControladorVistas.controlador().mostrar(VistaReporteActaFinal.vista());
				
			}
		});
		btnGenerarActaFinal.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
		btnGenerarActaFinal.setBounds(47, 233, 166, 31);
		panel.add(btnGenerarActaFinal);
		
		//-----------------------------------------------------------------
		
		
		JButton btnRegistrarMesa = new JButton("Nueva mesa de final");
		btnRegistrarMesa.setBounds(47, 23, 166, 31);
		panel.add(btnRegistrarMesa);
		btnRegistrarMesa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaRegMesa(modeloProfesor);
			}
		});
		btnRegistrarMesa.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
		
		
		
	}
	
	
	public void generarHomeProfesor (Profesor mod) {
		modeloProfesor = mod;
		lblIdentidad.setText(
				"Profesor: " + modeloProfesor.obtenerNombre() + " " +
				modeloProfesor.obtenerApellido() +
				" (Legajo: " + modeloProfesor.obtenerLegajo() + ")");
	}
	
	
	
	// VISTA PARA LA CALIFICACIÓN DE UN DICTADO
	
	private class VentanaCalificacionDictado extends JFrame {
		
		private static final long serialVersionUID = -1;
		private Profesor modeloProfesor;
		private JComboBox<Materia> comboMaterias;
		private JComboBox<Dictado> comboDictados;
		private JComboBox<Alumno> comboAlumnos;
		private JComboBox<String> comboCalificaciones;
		

		public VentanaCalificacionDictado (Profesor mod) {
			modeloProfesor = mod;
			setTitle("Calificaci\u00F3n de un dictado");
			setVisible(true);
			setBackground(SystemColor.controlHighlight);		
			this.setBounds(0,0,478,321);		
			getContentPane().setLayout(null);
			setResizable(false);		
			setLocationRelativeTo(null);
			comboMaterias = new JComboBox<Materia>();
			comboMaterias.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			comboMaterias.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						cargarDictados();
					}
				}
			});
				comboMaterias.setBounds(112, 35, 312, 20);
				getContentPane().add(comboMaterias);
				
				JLabel lblMateria = new JLabel("Materia:");
				lblMateria.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblMateria.setBounds(37, 35, 65, 20);
				getContentPane().add(lblMateria);
				
				comboDictados = new JComboBox<Dictado>();
				comboDictados.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				comboDictados.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							cargarAlumnos();
						}				
					}
				});
				comboDictados.setBounds(112, 85, 312, 20);
				getContentPane().add(comboDictados);
				
				JLabel lblDictado = new JLabel("Dictado:");
				lblDictado.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblDictado.setBounds(37, 85, 65, 20);
				getContentPane().add(lblDictado);
				
				comboAlumnos = new JComboBox<Alumno>();
				comboAlumnos.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				comboAlumnos.setBounds(112, 135, 312, 20);
				getContentPane().add(comboAlumnos);
				
				JLabel lblAlumno = new JLabel("Alumno:");
				lblAlumno.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblAlumno.setBounds(37, 134, 65, 20);
				getContentPane().add(lblAlumno);
				
				comboCalificaciones = new JComboBox<String>();
				comboCalificaciones.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				comboCalificaciones.setModel(new DefaultComboBoxModel<String>(new String[] {"Ausente", "Aprobado", "Desaprobado"}));
				comboCalificaciones.setBounds(112, 185, 147, 20);
				getContentPane().add(comboCalificaciones);
				
				JLabel lblNota = new JLabel("Nota:");
				lblNota.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblNota.setBounds(54, 185, 48, 20);
				getContentPane().add(lblNota);
				
				JButton btnCalificar = new JButton("Guardar nota");
				btnCalificar.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
				btnCalificar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						guardarCalificacion();
						dispose();
					}
				});
				btnCalificar.setBounds(163, 242, 135, 27);
				getContentPane().add(btnCalificar);	
				
				cargarMaterias();
				
		}
		
		/**
		 * cargarCarreras: lleva a cabo la carga de cada uno de los modelos de materias,
		   en el combobox asociado a las materias las que el alumno con LU dado puede
		   inscribirse
		 * @param lu: número de libreta universitaria asociado al alumno
		 */
		private void cargarMaterias () {
			List<Materia> listaMaterias;
			Materia materia = null;
			int lg = modeloProfesor.obtenerLegajo();
			
			comboMaterias.removeAllItems();
			try 
			{
				listaMaterias = ControladorMateria.controlador().materiasDictadasPorProfesor(lg);
				for (Modelo m : listaMaterias) {
					materia = (Materia) m;
					comboMaterias.addItem(materia);
				}
			}
			catch (DBRetrieveException ex) 
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de materias", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}
		}
		
		
		/**
		 * cargarCarreras: lleva a cabo la carga de cada uno de los modelos de carreras
		   en el combobox asociado a las carreras en las que el alumno con LU dado está
		   inscripto
		 * @param lu: número de libreta universitaria asociado al alumno
		 */
		private void cargarDictados () {
			List<Dictado> listaDictados;	
			Materia m = (Materia) comboMaterias.getSelectedItem();
			int lg = modeloProfesor.obtenerLegajo();
			int idMat = m.obtenerId();
			comboDictados.removeAllItems();
			try 
			{
				listaDictados = ControladorDictado.controlador().dictadosMateriaPorProfesor(idMat, lg);
				for (Dictado d : listaDictados) {
					comboDictados.addItem(d);
				}
			}
			catch (DBRetrieveException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de dictados", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}
			
		}
		
		/**
		 * cargarCarreras: lleva a cabo la carga de cada uno de los modelos de carreras
		   en el combobox asociado a las carreras en las que el alumno con LU dado está
		   inscripto
		 * @param lu: número de libreta universitaria asociado al alumno
		 */
		private void cargarAlumnos () {
			List<Alumno> listaAlumno;	
			Dictado d = (Dictado) comboDictados.getSelectedItem();
			int idDict = d.obtenerId();
			comboAlumnos.removeAllItems();
			try 
			{
				listaAlumno = ControladorAlumno.controlador().alumnosDelDictado(idDict);
				for (Alumno a : listaAlumno) {
					comboAlumnos.addItem(a);
				}
			}
			catch (DBRetrieveException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de alumnos", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}			
		}
		
		
		private void guardarCalificacion () {
			Dictado d = (Dictado) comboDictados.getSelectedItem();
			Alumno a = (Alumno) comboAlumnos.getSelectedItem();		
			String calif = (String) comboCalificaciones.getSelectedItem();
			int lu = a.obtenerLU();
			int idDict = d.obtenerId();
			try 
			{
				ControladorDictado.controlador().asignarCalificacion(lu, idDict, calif);
			}
			catch (DBUpdateException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Registro de calificación de dictado", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}
		}
	}
	
	
	// VISTA PARA LA CREACIÓN DE UNA NUEVA MESA DE EXAMEN FINAL
	
	public class VentanaRegMesa extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JComboBox<Materia> comboMaterias;
		private JTextField inputFecha;
		private JTextField inputHoras;
		private JTextField inputMinutos;
		
		
		public VentanaRegMesa (Profesor profesor) {
			setTitle("Registro de una nueva mesa de ex\u00E1men");
			getContentPane().setLayout(null);
			setVisible(true);
			setSize(470, 249);
			setResizable(false);
			setLocationRelativeTo(null);
			
			comboMaterias = new JComboBox<Materia>();
			comboMaterias.setBounds(135, 49, 285, 20);
			getContentPane().add(comboMaterias);
			
			JLabel lblMateria = new JLabel("Materia:");
			lblMateria.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblMateria.setBounds(71, 49, 54, 20);
			getContentPane().add(lblMateria);
			
			inputFecha = new JTextField();
			inputFecha.setBounds(135, 86, 118, 20);
			getContentPane().add(inputFecha);
			inputFecha.setColumns(10);
			
			inputHoras = new JTextField();
			inputHoras.setColumns(10);
			inputHoras.setBounds(135, 123, 22, 20);
			getContentPane().add(inputHoras);
			
			inputMinutos = new JTextField();
			inputMinutos.setBounds(172, 123, 22, 20);
			getContentPane().add(inputMinutos);
			inputMinutos.setColumns(10);
			
			JLabel lblFecha = new JLabel("Fecha:");
			lblFecha.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblFecha.setBounds(81, 86, 44, 20);
			getContentPane().add(lblFecha);
			
			JLabel lblHora = new JLabel("Hora (hs/min):");
			lblHora.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblHora.setBounds(37, 126, 86, 17);
			getContentPane().add(lblHora);
			
			JButton btnGuardar = new JButton("Guardar mesa");
			JFrame miVista = this;
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String fecha = inputFecha.getText();
					System.out.println(fecha);
					String hora = inputHoras.getText() + ":" + inputMinutos.getText();
					System.out.println(hora);
					boolean fechaValida = fechaValida(fecha);
					boolean horaValida = horaValida(hora);
					if (fechaValida && horaValida) {
						guardarMesaDeExamen(profesor);
					}
					else {
						if (!fechaValida) {
							JOptionPane.showMessageDialog(miVista, 
									"La fecha no es correcta. Se requiere una fecha con formato dd/MM/yyyy válido",
									"Carga de datos de materias", 
									JOptionPane.ERROR_MESSAGE);
							dispose();
						}
						
						if (!horaValida) {
							JOptionPane.showMessageDialog(miVista, 
									"La hora no es correcta. Se requiere una hora con formato HH:mm válido",
									"Carga de datos de materias", 
									JOptionPane.ERROR_MESSAGE);
							dispose();
						}
					}
					
					
					
				}
			});
			btnGuardar.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnGuardar.setBounds(168, 175, 123, 23);
			getContentPane().add(btnGuardar);
			
			JLabel lblSeparador = new JLabel(":");
			lblSeparador.setHorizontalAlignment(SwingConstants.CENTER);
			lblSeparador.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblSeparador.setBounds(158, 123, 13, 20);
			getContentPane().add(lblSeparador);	
			
			cargarMaterias();		
		}
		
		
		private void cargarMaterias () {
			List<Modelo> listaMaterias;
			Materia materia = null;
			comboMaterias.removeAllItems();
			try 
			{
				listaMaterias = ControladorMateria.controlador().elementos();
				for (Modelo m : listaMaterias) {
					materia = (Materia) m;
					comboMaterias.addItem(materia);
				}
			}
			catch (DBRetrieveException ex) 
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de materias", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}
		}
		
		
		private void guardarMesaDeExamen (Profesor profesor) {
			Materia materia = (Materia) comboMaterias.getSelectedItem();
			String fecha = inputFecha.getText();
			String hora = inputHoras.getText() + ":" + inputMinutos.getText();
			String [] in = {profesor.obtenerLegajo() + "", materia.obtenerId() + "", fecha, hora};
			try 
			{
				ControladorMesa.controlador().registrar(in);
				JOptionPane.showMessageDialog(this, "La nueva mesa de examen se registró exitosamente");
				
			}
			catch (DBUpdateException ex) 
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Registro de nueva mesa de examen", JOptionPane.ERROR_MESSAGE);
			}
			this.dispose();
		}
		
		
		private boolean fechaValida (String fecha) {
			boolean valida = true;
			if (fecha.trim().equals(""))
			{
				valida = false;
			}
			else
			{
			    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			    formato.setLenient(false);
			    try
			    {
					formato.parse(fecha);
			    }
			    catch (ParseException e)
			    {
			    	valida = false;		    	
			    }
			}
			return valida;
		}
		
		
		private boolean horaValida (String fecha) {
	        String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
	        Pattern p = Pattern.compile(regex);
	        
	        if (fecha == null) { 
	            return false; 
	        } 
	        Matcher m = p.matcher(fecha); 
	        return m.matches(); 
		}		
	}
	

	public class VentanaCalificacionFinal extends JFrame {
		
		private static final long serialVersionUID = -1;
		private Profesor modeloProfesor;
		private JComboBox<Materia> comboMaterias;
		private JComboBox<MesaDeExamen> comboMesas;
		private JComboBox<Alumno> comboAlumnos;
		private JComboBox<String> comboEstados;
		private JComboBox<String> comboNotas;
		

		public VentanaCalificacionFinal (Profesor mod) {
			modeloProfesor = mod;
			setTitle("Calificaci\u00F3n de un examen final");
			setVisible(true);
			setBackground(SystemColor.controlHighlight);		
			this.setBounds(0,0,500,327);		
			getContentPane().setLayout(null);
			setResizable(false);		
			setLocationRelativeTo(null);
			comboMaterias = new JComboBox<Materia>();
			comboMaterias.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			comboMaterias.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						cargarMesas();
					}
				}
			});
				comboMaterias.setBounds(148, 35, 312, 20);
				getContentPane().add(comboMaterias);
				
				comboMesas = new JComboBox<MesaDeExamen>();
				comboMesas.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				comboMesas.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							cargarAlumnos();
						}				
					}
				});
				comboMesas.setBounds(148, 75, 312, 20);
				getContentPane().add(comboMesas);
				
				comboAlumnos = new JComboBox<Alumno>();
				comboAlumnos.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				comboAlumnos.setBounds(148, 115, 312, 20);
				getContentPane().add(comboAlumnos);
				
				comboEstados = new JComboBox<String>();
				comboEstados.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							modificarNotas();
						}				
					}
				});
				comboEstados.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				comboEstados.setModel(new DefaultComboBoxModel<String>(new String[] {"Ausente", "Aprobado", "Desaprobado"}));
				comboEstados.setBounds(148, 155, 147, 20);
				getContentPane().add(comboEstados);
				
				comboNotas = new JComboBox<String>();
				comboNotas.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				comboNotas.setBounds(148, 195, 72, 20);
				getContentPane().add(comboNotas);
				
				JLabel lblMateria = new JLabel("Materia:");
				lblMateria.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblMateria.setBounds(78, 35, 58, 20);
				getContentPane().add(lblMateria);
				
				JLabel lblMesa = new JLabel("Mesa de examen:");
				lblMesa.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblMesa.setBounds(26, 75, 112, 20);
				getContentPane().add(lblMesa);
				
				JLabel lblAlumno = new JLabel("Alumno:");
				lblAlumno.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblAlumno.setBounds(76, 115, 58, 20);
				getContentPane().add(lblAlumno);
				
				JLabel lblEstado = new JLabel("Estado:");
				lblEstado.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblEstado.setBounds(86, 155, 51, 20);
				getContentPane().add(lblEstado);
				
				JLabel lblNota = new JLabel("Nota:");
				lblNota.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblNota.setBounds(94, 195, 43, 20);
				getContentPane().add(lblNota);
				
				JButton btnCalificar = new JButton("Guardar nota");
				btnCalificar.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
				btnCalificar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						guardarCalificacion();
						dispose();
					}
				});
				btnCalificar.setBounds(179, 253, 135, 27);
				getContentPane().add(btnCalificar);	
				
				cargarMaterias();
				
		}
		
		/**
		 * cargarCarreras: lleva a cabo la carga de cada uno de los modelos de materias,
		   en el combobox asociado a las materias las que el alumno con LU dado puede
		   inscribirse
		 * @param lu: número de libreta universitaria asociado al alumno
		 */
		private void cargarMaterias () {
			List<Materia> listaMaterias;
			Materia materia = null;
			int lg = modeloProfesor.obtenerLegajo();
			
			comboMaterias.removeAllItems();
			try 
			{
				listaMaterias = ControladorMateria.controlador().materiasConMesa(lg);
				for (Modelo m : listaMaterias) {
					materia = (Materia) m;
					comboMaterias.addItem(materia);
				}
			}
			catch (DBRetrieveException ex) 
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de materias", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}
		}
		
		
		/**
		 * cargarCarreras: lleva a cabo la carga de cada uno de los modelos de carreras
		   en el combobox asociado a las carreras en las que el alumno con LU dado está
		   inscripto
		 * @param lu: número de libreta universitaria asociado al alumno
		 */
		private void cargarMesas () {
			List<MesaDeExamen> listaMesas;	
			Materia m = (Materia) comboMaterias.getSelectedItem();
			int lg = modeloProfesor.obtenerLegajo();
			int idMat = m.obtenerId();
			comboMesas.removeAllItems();
			try 
			{
				listaMesas = ControladorMesa.controlador().mesasProfesorMateria(idMat, lg);
				for (MesaDeExamen d : listaMesas) {
					comboMesas.addItem(d);
				}
			}
			catch (DBRetrieveException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de mesa de examen", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}
			
		}
		
		/**
		 * cargarCarreras: lleva a cabo la carga de cada uno de los modelos de carreras
		   en el combobox asociado a las carreras en las que el alumno con LU dado está
		   inscripto
		 * @param lu: número de libreta universitaria asociado al alumno
		 */
		private void cargarAlumnos () {
			List<Alumno> listaAlumno;	
			MesaDeExamen m = (MesaDeExamen) comboMesas.getSelectedItem();
			int idMesa = m.obtenerId();
			comboAlumnos.removeAllItems();
			try 
			{
				listaAlumno = ControladorAlumno.controlador().alumnosEnMesa(idMesa);
				for (Alumno a : listaAlumno) {
					comboAlumnos.addItem(a);
				}
			}
			catch (DBRetrieveException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de alumnos", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}	
		}
		
		
		private void modificarNotas () {
			comboNotas.removeAllItems();
			String estado = (String) comboEstados.getSelectedItem();
			if (estado.contentEquals("Aprobado")) {
				comboNotas.addItem("6");
				comboNotas.addItem("7");
				comboNotas.addItem("8");
				comboNotas.addItem("9");
				comboNotas.addItem("10");
			}
			else if (estado.contentEquals("Desaprobado")) {
				comboNotas.addItem("1");
				comboNotas.addItem("2");
				comboNotas.addItem("3");
				comboNotas.addItem("4");
				comboNotas.addItem("5");
			}
		}
		
		
		private void guardarCalificacion () {
			MesaDeExamen m = (MesaDeExamen) comboMesas.getSelectedItem();
			Alumno a = (Alumno) comboAlumnos.getSelectedItem();		
			String estado = (String) comboEstados.getSelectedItem();
			String nota = null;
			int lu = a.obtenerLU();
			int idMesa = m.obtenerId();
			try 
			{
				if (!estado.contentEquals("Ausente")) {
					nota = (String) comboNotas.getSelectedItem();
				}
				ControladorMesa.controlador().asignarCalificacion(lu, idMesa, estado, nota);
				JOptionPane.showMessageDialog(this, "El alumno con LU: " + lu + " ha sido calificado exitosamente con " +
						estado + " para la mesa de examen con ID: " + idMesa);
			}
			catch (DBUpdateException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Registro de calificación de dictado", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}
		}
	}
	

	
}
