package Vistas;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Controladores.ControladorCarrera;
import Controladores.ControladorDictado;
import Controladores.ControladorMateria;
import Controladores.ControladorPlan;
import Controladores.ControladorProfesor;
import Controladores.ControladorVistas;
import Excepciones.DBRetrieveException;
import Excepciones.DBUpdateException;
import Modelos.Carrera;
import Modelos.Dictado;
import Modelos.Materia;
import Modelos.Modelo;
import Modelos.Plan;
import Modelos.Profesor;
import quick.dbtable.DBTable;

public class VistaAdminDictados extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static VistaAdminDictados instancia = null;
	private DBTable tabla;
	private JTextField txtNombre;
	protected int seleccionado = -1;
	
	/**
	 * vista: retorna la instancia de la vista de administración de profesors
	 * @return panel de la vista de administración de profesors
	 */
	public static VistaAdminDictados vista () {
		if (instancia == null) {
			instancia = new VistaAdminDictados();
		}
		return instancia;
	}
	
	/**
	 * CONSTRUCTOR: Vista para la administración de profesors
	 */ 	
	private VistaAdminDictados() {
		
		this.setBackground(SystemColor.control);
		this.setBounds(0, 0, 1194, 699);
		this.setLayout(null);
		
		JPanel panelTabla = new JPanel();
		panelTabla.setBackground(SystemColor.controlHighlight);
		panelTabla.setBounds(10, 232, 1174, 456);
		this.add(panelTabla);
		
		JButton btnRegDictado = new JButton("Registrar dictado");
		btnRegDictado.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 13));
		btnRegDictado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaRegDictado();
			}
		});
		btnRegDictado.setBounds(328, 85, 167, 40);
		this.add(btnRegDictado);
		
		tabla = new DBTable();
		tabla.setBounds(249, 5, 663, 427);
        
		tabla.addKeyListener(new KeyAdapter() {
           public void keyTyped(KeyEvent evt) {
              tablaKeyTyped(evt);
           }
        });
        
        tabla.addMouseListener(new MouseAdapter() {
           public void mouseClicked(MouseEvent evt) {
              tablaMouseClicked(evt);
           }
    	});
        panelTabla.setLayout(null);
        
    	// Agregar la tabla al frame (no necesita JScrollPane como Jtable)
        panelTabla.add(tabla);           
        tabla.setEditable(false);
        
        JButton btnModDictado = new JButton("Modificar dictado");
        btnModDictado.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaBuscarDictado();
        	}
        });
        btnModDictado.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnModDictado.setBounds(505, 85, 167, 40);
        add(btnModDictado);
        
        JButton btnBajaDictado = new JButton("Dar de baja dictado");
        btnBajaDictado.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaElimDictado();
        	}
        });
        btnBajaDictado.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnBajaDictado.setBounds(682, 85, 167, 40);
        add(btnBajaDictado);
        
        JButton btnAtras = new JButton("Atr\u00E1s");
        btnAtras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// Vuelvo a la vista anterior, la vista de administración
        		ControladorVistas.controlador().mostrar(VistaAdmin.vista());
        	}
        });
        btnAtras.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnAtras.setBounds(10, 11, 70, 23);
        add(btnAtras);        
             
        actualizarTabla();
	}
	
	
	private void tablaMouseClicked(MouseEvent evt) 
	{
		if ((this.tabla.getSelectedRow() != -1) && (evt.getClickCount() == 2)) 
		{
			this.seleccionarFila();
		}
	}
	
	
	private void tablaKeyTyped(KeyEvent evt) {
		if ((this.tabla.getSelectedRow() != -1) && (evt.getKeyChar() == ' ')) 
		{
	         this.seleccionarFila();
	    }
	}
	
	
	private void seleccionarFila()
	{
		this.seleccionado = this.tabla.getSelectedRow();
	    this.txtNombre.setText(this.tabla.getValueAt(this.tabla.getSelectedRow(), 0).toString());
	}
	
	/**
	 * actualizarTabla: permite actualizar el contenido de la db table para profesors,
	   con todos los profesors registrados y sus datos.
	 */
	private void actualizarTabla () {
		try
	    {
			ControladorDictado.controlador().volcar(tabla);
	    }		
		catch (DBRetrieveException ex)
		{
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Volcado de datos de dictados", JOptionPane.ERROR_MESSAGE);
	    }
	}	
	
	
	// CLASE PARA LA VENTANA DE INPUTS PARA EL REGISTRO
		
	private class VentanaRegDictado extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JComboBox<Carrera> comboCarreras;
		private JComboBox<Plan> comboPlanes;
		private JComboBox<Materia> comboMaterias;
		private JComboBox<Profesor> comboProfesores;
		private JComboBox<String> comboCuatri;
		private JButton btnSiguiente;
		
		
		// CONSTRUCTOR: Ventana para el registro de un nuevo profesor
		
		public VentanaRegDictado() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Registro de nuevo dictado");
			setSize(425,285);
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS: registro de profesor
			
			comboCarreras = new JComboBox<Carrera>();
			comboCarreras.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						Carrera c = (Carrera) comboCarreras.getSelectedItem();
						cargarPlanes(c.obtenerId());
					}				
				}
			});
			comboCarreras.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			comboCarreras.setBounds(136, 36, 226, 20);
			getContentPane().add(comboCarreras);
			
			comboPlanes = new JComboBox<Plan>();
			comboPlanes.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						Plan p = (Plan) comboPlanes.getSelectedItem();
						cargarMaterias(p.obtenerId());
					}				
				}
			});
			comboPlanes.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			comboPlanes.setBounds(136, 67, 226, 20);
			getContentPane().add(comboPlanes);
			
			comboMaterias = new JComboBox<Materia>();
			comboMaterias.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			comboMaterias.setBounds(136, 98, 226, 20);
			getContentPane().add(comboMaterias);
			
			comboProfesores = new JComboBox<Profesor>();
			comboProfesores.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			comboProfesores.setBounds(136, 129, 226, 20);
			getContentPane().add(comboProfesores);
			
			// CREACIÓN DE LABELS: registro de dictado
			
			JLabel lblCarrera = new JLabel("Carrera:");
			lblCarrera.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblCarrera.setBounds(77, 36, 51, 20);
			getContentPane().add(lblCarrera);
			
			JLabel lblPlan = new JLabel("  Plan:");		
			lblPlan.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblPlan.setBounds(85, 67, 41, 20);
			getContentPane().add(lblPlan);
			
			JLabel lblMateria = new JLabel();
			lblMateria = new JLabel("Materia:");
			lblMateria.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblMateria.setBounds(75, 98, 51, 20);
			getContentPane().add(lblMateria);
			
			JLabel lblProfesor = new JLabel(" Profesor:");
			lblProfesor.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblProfesor.setBounds(64, 129, 62, 20);
			getContentPane().add(lblProfesor);
			
			btnSiguiente = new JButton("Guardar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {					
					registrarDictado();
					dispose();
				}
			});
			btnSiguiente.setBounds(146, 215, 124, 23);
			getContentPane().add(btnSiguiente);
			
			comboCuatri = new JComboBox<String>();
			comboCuatri.setModel(new DefaultComboBoxModel<String>(new String[] {"1", "2"}));
			comboCuatri.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			comboCuatri.setBounds(136, 160, 66, 20);
			getContentPane().add(comboCuatri);
			
			JLabel lblCuatri = new JLabel("Cuatrimestre:");
			lblCuatri.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblCuatri.setBounds(45, 160, 82, 20);
			getContentPane().add(lblCuatri);
			
			cargarCarreras();
			cargarProfesores();	
		}
		
		
		/**
		 * cargarCarreras: lleva a cabo la carga de cada uno de los modelos de carreras
		   en el combobox asociado a las carreras en las que el alumno con LU dado está
		   inscripto
		 * @param lu: número de libreta universitaria asociado al alumno
		 */
		private void cargarCarreras () {
			List<Modelo> listaCarreras;
			Carrera carrera = null;
			comboCarreras.removeAllItems();
			try 
			{
				listaCarreras = ControladorCarrera.controlador().elementos();
				for (Modelo m : listaCarreras) {
					carrera = (Carrera) m;
					comboCarreras.addItem(carrera);
				}
			}
			catch (DBRetrieveException ex) 
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de carreras", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}			
		}
		
		
		/**
		 * cargarPlanes: lleva a cabo la carga de cada uno de los planes de una carrera
		   en el combobox asociado a los planes registrados
		 * @param id: número de identificación de la carrera de la cual se requiere obtener los planes
		 */
		private void cargarPlanes (int id) {
			List<Plan> listaPlanes;
			Plan plan = null;
			comboPlanes.removeAllItems();
			try 
			{
				listaPlanes = ControladorPlan.controlador().planesDeCarrera(id);
				for (Modelo m : listaPlanes) {
					plan = (Plan) m;
					comboPlanes.addItem(plan);
				}
			}
			catch (DBRetrieveException ex) 
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de planes", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}
		}
		
		
		/**
		 * cargarMaterias: lleva a cabo la carga de cada una de las materias de un plan
		   asociado a una carrera, en el combobox asociado a las materias
		 * @param id: número de identificación del plan de carrera al cual se asocian las materias
		 */
		private void cargarMaterias (int id) {
			List<Materia> listaMaterias;
			Materia materia = null;
			comboMaterias.removeAllItems();
			try 
			{
				listaMaterias = ControladorMateria.controlador().materiasDelPlan(id);
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
		 * cargarProfesores: lleva a cabo la carga de cada uno de los modelos de profesores
		   en el combobox asociado a los docentes registrados en el sistema
		 */
		private void cargarProfesores () {
			List<Modelo> listaProfesores;
			Profesor profesor = null;
			comboProfesores.removeAllItems();
			try 
			{
				listaProfesores = ControladorProfesor.controlador().elementos();
				for (Modelo m : listaProfesores) {
					profesor = (Profesor) m;
					comboProfesores.addItem(profesor);
				}
			}
			catch (DBRetrieveException ex) 
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de profesor", JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}
		}
		
		
		/**
		 * registrarProfesor: solicita el registro de un nuevo profesor en el sistema
		 */
		private void registrarDictado () {
			Plan plan = (Plan) comboPlanes.getSelectedItem();
			Materia materia = (Materia) comboMaterias.getSelectedItem();
			Profesor profesor = (Profesor) comboProfesores.getSelectedItem();
			String cuatrimestre = (String) comboCuatri.getSelectedItem();
			String [] inputs = {
					profesor.obtenerLegajo() + "",
					materia.obtenerId() + "",
					plan.obtenerId() + "",
					cuatrimestre					
			};
			try 
			{
				ControladorDictado.controlador().registrar(inputs);
				JOptionPane.showMessageDialog(this,"Dictado registrado exitosamente");
				actualizarTabla();
			}
			catch (DBUpdateException ex) 
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Registro de un nuevo dictado", JOptionPane.ERROR_MESSAGE);
			}
			
						
		}
	}
	
	
	// CLASE PARA LA VENTANA DE BÚSQUEDA DE ALUMNO PARA SU MODIFICACIÓN	
	
	private class VentanaBuscarDictado extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JTextField inputId;
		private JButton btnSiguiente;
		
		
		// CONSTRUCTOR: Ventana para el registro de un nuevo profesor		
		public VentanaBuscarDictado () {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Modificación de un dictado");
			setSize(new Dimension(325, 161));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS: registro de profesor
			
			inputId = new JTextField();
			inputId.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputId.setBounds(114, 40, 124, 20);
			getContentPane().add(inputId);
			
			// CREACIÓN DE LABELS: registro de profesor
			
			JLabel lblId = new JLabel("ID:");		
			lblId.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblId.setBounds(81, 40, 23, 20);
			getContentPane().add(lblId);
			
			btnSiguiente = new JButton("A modificar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {						
					abrirEdicionDictado();
					dispose();
				}
			});
			btnSiguiente.setBounds(101, 87, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
		
		/**
		 * abrirEdicionProfesor: abre una nueva ventana que permita ingresar datos de entrada
		   para la edición de datos de registro asociados a un profesor
		 */
		private void abrirEdicionDictado () {
			Dictado dictado;
			String [] in = {inputId.getText(), null, null, null, null, null};
			try 
			{
				dictado = ControladorDictado.controlador().recuperar(in);
				if (dictado == null) {
					JOptionPane.showMessageDialog(this,	"¡ERROR! No existe dictado registrado con ID: " + inputId.getText(), 
							"Modificación de un dictado", JOptionPane.ERROR_MESSAGE);
				}
				else {
					new VentanaEdicionDictado(dictado);
				}
			}
			catch (DBRetrieveException ex) 
			{
				JOptionPane.showMessageDialog(this,	ex.getMessage(), "Modificación de un dictado", JOptionPane.ERROR_MESSAGE);
			}			
			dispose();			
		}
		
		
		// CLASE ASOCIADA A LA VENTANA PARA INTRODUCIR DATOS PARA MODIFICAR UN ALUMNO
		
		private class VentanaEdicionDictado extends JFrame {
			
			private static final long serialVersionUID = 1L;
			private JComboBox<Profesor> comboProfesores;
			private JCheckBox checkBoxProfesor;
			private JButton btnSiguiente;		
				
			/**
			 * CONSTRUCTOR: Ventana para el registro de un nuevo profesor
			 * @param lu: número de libreta universitaria asociado al profesor a modificar
			 */
			public VentanaEdicionDictado (Dictado dictado) {
				super();
				getContentPane().setEnabled(false);
				getContentPane().setLayout(null);
				setVisible(true);				
				getContentPane().setBackground(SystemColor.controlHighlight);
				setTitle("Modificación de un profesor");
				setSize(445,163);
				setResizable(false);		
				setLocationRelativeTo(null);
				
				// CREACIÓN DE INPUTS: modificación de profesor
				comboProfesores = new JComboBox<Profesor>();
				comboProfesores.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				comboProfesores.setBounds(172, 37, 216, 20);
				getContentPane().add(comboProfesores);				
				
				// CREACIÓN DE CHECKBOXES:
				
				checkBoxProfesor = new JCheckBox("");
				checkBoxProfesor.setBackground(SystemColor.controlHighlight);
				checkBoxProfesor.setBounds(255, 37, 26, 20);				
				checkBoxProfesor.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(checkBoxProfesor);
					}
				});
				
				// CREACIÓN DE LABELS: registro de profesor

				JLabel lblProfesor = new JLabel("Profesor asignado:");
				lblProfesor.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblProfesor.setBounds(51, 37, 103, 20);
				getContentPane().add(lblProfesor);
				
				btnSiguiente = new JButton("Guardar");
				btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
				btnSiguiente.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						modificarDictado(dictado);
						dispose();
					}
				});
				btnSiguiente.setBounds(154, 89, 124, 23);
				getContentPane().add(btnSiguiente);
				
				cargarProfesores();
				
			}
			
			/**
			 * cargarProfesores: lleva a cabo la carga de cada uno de los modelos de profesores
			   en el combobox asociado a los docentes registrados en el sistema
			 */
			private void cargarProfesores () {
				List<Modelo> listaProfesores;
				Profesor profesor = null;
				comboProfesores.removeAllItems();
				try 
				{
					listaProfesores = ControladorProfesor.controlador().elementos();
					for (Modelo m : listaProfesores) {
						profesor = (Profesor) m;
						comboProfesores.addItem(profesor);
					}
				}
				catch (DBRetrieveException ex) 
				{
					JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de profesor", JOptionPane.ERROR_MESSAGE);
					this.dispose();
				}
			}
			
			
			/**
			 * switchearEstadoInput: permite alternar el estado de un componente
			   que puede ser habilitado o deshabilitado
			 * @param in: componente a habilitar/deshabilitar
			 */
			private void switchearEstadoInput (JComponent in) {
				if (in.isEnabled()) {
					in.setEnabled(false);
				}
				else in.setEnabled(true);
			}
			
			
			/**
			 * modificarProfesor: permite cambiar los datos de registro para un profesor
			   particular
			 * @param matr: número de matrícula asociado al profesor a modificar
			 */
			private void modificarDictado (Dictado dictado) {
				Profesor profesor = (Profesor) comboProfesores.getSelectedItem();
				String [] in = {dictado.obtenerId() + "", profesor.obtenerLegajo() + "", null, null, null, null};
				try
				{
					ControladorDictado.controlador().modificar(in);
					JOptionPane.showMessageDialog(this,"Dictado con ID: " + profesor.obtenerLegajo() + " modificado exitosamente");
					actualizarTabla();						
				}
				catch (DBUpdateException ex)
				{
					JOptionPane.showMessageDialog(this, ex.getMessage(), "Modificación de un dictado", JOptionPane.ERROR_MESSAGE);
				}					
				dispose();
			}
		}
	}
	
	
	// CLASE PARA LA VENTANA DE INPUTS PARA LA BAJA
	
	private class VentanaElimDictado extends JFrame {
		
		private static final long serialVersionUID = 1L;
			private JTextField inputId;
			private JButton btnSiguiente;
			
			/**
			 * CONSTRUCTOR: Ventana para el registro de un nuevo profesor
			 */
			public VentanaElimDictado() {
				super();
				getContentPane().setLayout(null);
		        setVisible(true);	        
		        getContentPane().setBackground(SystemColor.controlHighlight);
				setTitle("Baja de un dictado");
				setSize(new Dimension(325, 161));
				setResizable(false);		
				setLocationRelativeTo(null);
					
				// CREACIÓN DE INPUTS: registro de profesor
				
				inputId = new JTextField();
				inputId.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputId.setBounds(114, 40, 124, 20);
				getContentPane().add(inputId);
				
				// CREACIÓN DE LABELS: registro de profesor
				
				JLabel lblId = new JLabel("ID:");		
				lblId.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblId.setBounds(81, 40, 23, 20);
				getContentPane().add(lblId);
				
				btnSiguiente = new JButton("Dar de baja");
				btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
				btnSiguiente.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {						
						eliminarDictado();
						dispose();
					}
				});
				btnSiguiente.setBounds(101, 87, 124, 23);
				getContentPane().add(btnSiguiente);
				
			}

			
			/**
			 * eliminarDictado: solicita la baja de un dictado, con cierto ID, que está
			   registrado en el sistema
			 */
			private void eliminarDictado () {
				try
				{
					ControladorDictado.controlador().eliminar(inputId.getText());
					JOptionPane.showMessageDialog(this,"Dictado dado de baja exitosamente");
					actualizarTabla();
					dispose();
				}
				catch (DBUpdateException ex)
				{
					JOptionPane.showMessageDialog(this, ex.getMessage(), "Baja de un dictado", JOptionPane.ERROR_MESSAGE);
				}
			}
			   
		}

	}

