package Vistas;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import Controladores.ControladorProfesor;
import Controladores.ControladorVistas;
import Excepciones.DBRetrieveException;
import Excepciones.DBUpdateException;
import Modelos.Profesor;
import quick.dbtable.DBTable;

public class VistaAdminProfesores extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static VistaAdminProfesores instancia = null;
	private DBTable tabla;
	private JTextField txtNombre;
	protected int seleccionado = -1;
	
	/**
	 * vista: retorna la instancia de la vista de administración de profesors
	 * @return panel de la vista de administración de profesors
	 */
	public static VistaAdminProfesores vista () {
		if (instancia == null) {
			instancia = new VistaAdminProfesores();
		}
		return instancia;
	}
	
	/**
	 * CONSTRUCTOR: Vista para la administración de profesors
	 */ 	
	private VistaAdminProfesores() {
		
		this.setBackground(SystemColor.control);
		this.setBounds(0, 0, 1194, 699);
		this.setLayout(null);
		
		JPanel panelTabla = new JPanel();
		panelTabla.setBackground(SystemColor.controlHighlight);
		panelTabla.setBounds(10, 232, 1174, 456);
		this.add(panelTabla);
		
		JButton btnRegProfesor = new JButton("Registrar profesor");
		btnRegProfesor.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 13));
		btnRegProfesor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaRegProfesor();
			}
		});
		btnRegProfesor.setBounds(328, 85, 167, 40);
		this.add(btnRegProfesor);
		
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
        
        JButton btnModProfesor = new JButton("Modificar profesor");
        btnModProfesor.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaBuscarProfesor();
        	}
        });
        btnModProfesor.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnModProfesor.setBounds(505, 85, 167, 40);
        add(btnModProfesor);
        
        JButton btnBajaProfesor = new JButton("Dar de baja profesor");
        btnBajaProfesor.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaElimProfesor();
        	}
        });
        btnBajaProfesor.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnBajaProfesor.setBounds(682, 85, 167, 40);
        add(btnBajaProfesor);
        
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
			ControladorProfesor.controlador().volcar(tabla);
	    }		
		catch (DBRetrieveException ex)
		{
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Volcado de datos de profesores", JOptionPane.ERROR_MESSAGE);
	    }
	}	
	
	
	// CLASE PARA LA VENTANA DE INPUTS PARA EL REGISTRO
		
	private class VentanaRegProfesor extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JTextField inputMatricula;
		private JTextField inputDNI;
		private JTextField inputCuil;
		private JTextField inputNombre;
		private JTextField inputApellido;
		private JComboBox<String> inputGenero;
		private JTextField inputMail;
		private JTextField inputTelefono;
		private JTextField inputCalle;
		private JTextField inputNum;
		private JTextField inputPiso;
		private JTextField inputDepto;
		private JButton btnSiguiente;
		
		
		// CONSTRUCTOR: Ventana para el registro de un nuevo profesor
		
		public VentanaRegProfesor() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Registro de nuevo profesor");
			setMaximumSize(new Dimension(312, 506));
			setMinimumSize(new Dimension(312, 506));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS: registro de profesor
			
			inputMatricula = new JTextField();
			inputMatricula.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputMatricula.setColumns(10);
			inputMatricula.setBounds(123, 37, 124, 20);
			getContentPane().add(inputMatricula);
			
			inputDNI = new JTextField();
			inputDNI.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputDNI.setBounds(123, 67, 124, 20);
			getContentPane().add(inputDNI);
			inputDNI.setColumns(10);
			
			inputCuil = new JTextField();
			inputCuil.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputCuil.setColumns(10);
			inputCuil.setBounds(123, 98, 124, 20);
			getContentPane().add(inputCuil);
			
			inputNombre = new JTextField();
			inputNombre.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputNombre.setColumns(10);
			inputNombre.setBounds(122, 129, 125, 20);
			getContentPane().add(inputNombre);
			
			inputApellido = new JTextField();
			inputApellido.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputApellido.setColumns(10);
			inputApellido.setBounds(122, 162, 125, 20);
			getContentPane().add(inputApellido);
			
			inputGenero = new JComboBox<String>();
			inputGenero.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputGenero.setBounds(123, 193, 124, 20);
			inputGenero.addItem("Masculino");
			inputGenero.addItem("Femenino");
			getContentPane().add(inputGenero);
			
			inputMail = new JTextField();
			inputMail.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputMail.setEnabled(false);
			inputMail.setColumns(10);
			inputMail.setBounds(122, 227, 125, 20);
			getContentPane().add(inputMail);
			
			inputTelefono = new JTextField();
			inputTelefono.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputTelefono.setEnabled(false);
			inputTelefono.setColumns(10);
			inputTelefono.setBounds(122, 258, 125, 20);
			getContentPane().add(inputTelefono);
			
			inputCalle = new JTextField();
			inputCalle.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputCalle.setEnabled(false);
			inputCalle.setColumns(10);
			inputCalle.setBounds(122, 289, 125, 20);
			getContentPane().add(inputCalle);
			
			inputNum = new JTextField();
			inputNum.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputNum.setEnabled(false);
			inputNum.setColumns(10);
			inputNum.setBounds(122, 320, 125, 20);
			getContentPane().add(inputNum);
			
			inputPiso = new JTextField();
			inputPiso.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputPiso.setEnabled(false);
			inputPiso.setColumns(10);
			inputPiso.setBounds(122, 351, 125, 20);
			getContentPane().add(inputPiso);
			
			inputDepto = new JTextField();
			inputDepto.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputDepto.setEnabled(false);
			inputDepto.setColumns(10);
			inputDepto.setBounds(123, 382, 124, 20);
			getContentPane().add(inputDepto);
			
			// CREACIÓN DE LABELS: registro de profesor
			
			JLabel lblMatricula = new JLabel(" Matr\u00EDcula:");
			lblMatricula.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblMatricula.setBounds(48, 36, 66, 20);
			getContentPane().add(lblMatricula);
			
			JLabel lblDNI = new JLabel("DNI:");		
			lblDNI.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblDNI.setBounds(80, 67, 33, 20);
			getContentPane().add(lblDNI);
			
			JLabel lblCuil = new JLabel();
			lblCuil = new JLabel(" CUIL:");
			lblCuil.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblCuil.setBounds(72, 98, 41, 20);
			getContentPane().add(lblCuil);
			
			JLabel lblNombre = new JLabel("Nombre:");
			lblNombre.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblNombre.setBounds(54, 129, 59, 20);
			getContentPane().add(lblNombre);
			
			JLabel lblApellido = new JLabel("Apellido:");
			lblApellido.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblApellido.setBounds(54, 162, 59, 20);
			getContentPane().add(lblApellido);
			
			JLabel lblGenero = new JLabel("G\u00E9nero:");
			lblGenero.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblGenero.setBounds(56, 193, 57, 20);
			getContentPane().add(lblGenero);
			
			JLabel lblMail = new JLabel("Mail:");
			lblMail.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblMail.setBounds(72, 227, 41, 20);
			getContentPane().add(lblMail);
			
			JLabel lblTelfono = new JLabel("Tel\u00E9fono:");
			lblTelfono.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblTelfono.setBounds(42, 258, 71, 20);
			getContentPane().add(lblTelfono);
			
			JLabel lblCalle = new JLabel("Calle:");
			lblCalle.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblCalle.setBounds(70, 289, 41, 20);
			getContentPane().add(lblCalle);
			
			JLabel lblNum = new JLabel("N\u00B0:");
			lblNum.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblNum.setBounds(80, 320, 33, 20);
			getContentPane().add(lblNum);
			
			JLabel lblPiso = new JLabel("Piso:");
			lblPiso.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblPiso.setBounds(72, 351, 39, 20);
			getContentPane().add(lblPiso);
			
			JLabel lblDepto = new JLabel(" Depto:");
			lblDepto.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblDepto.setBounds(56, 382, 57, 20);
			getContentPane().add(lblDepto);
			
			btnSiguiente = new JButton("Guardar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {					
					registrarProfesor();
					dispose();
				}
			});
			btnSiguiente.setBounds(94, 431, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
		
		
		/**
		 * registrarProfesor: solicita el registro de un nuevo profesor en el sistema
		 */
		private void registrarProfesor () {
			String [] inputs = {
					inputMatricula.getText(),
					inputDNI.getText(),
					inputCuil.getText(),
					inputNombre.getText(),
					inputApellido.getText(),
					(String) inputGenero.getSelectedItem()
					};			
			try 
			{
				ControladorProfesor.controlador().registrar(inputs);
				JOptionPane.showMessageDialog(this,"Profesor registrado exitosamente");
				actualizarTabla();
			} 
			catch (DBUpdateException ex)
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Registro de un profesor", JOptionPane.ERROR_MESSAGE);
			}			
			dispose();			
		}
	}
	
	
	// CLASE PARA LA VENTANA DE BÚSQUEDA DE ALUMNO PARA SU MODIFICACIÓN	
	
	private class VentanaBuscarProfesor extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JTextField inputLegajo;
		private JButton btnSiguiente;
		
		
		// CONSTRUCTOR: Ventana para el registro de un nuevo profesor		
		public VentanaBuscarProfesor() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Modificación de un profesor");
			setSize(new Dimension(350, 161));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS: registro de profesor
			
			inputLegajo = new JTextField();
			inputLegajo.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputLegajo.setBounds(141, 42, 124, 20);
			getContentPane().add(inputLegajo);
			
			// CREACIÓN DE LABELS: registro de profesor
			
			JLabel lblLegajo = new JLabel("Legajo:");		
			lblLegajo.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblLegajo.setBounds(80, 40, 62, 20);
			getContentPane().add(lblLegajo);
			
			btnSiguiente = new JButton("A modificar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {						
					abrirEdicionProfesor();
					dispose();
				}
			});
			btnSiguiente.setBounds(114, 87, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
		
		/**
		 * abrirEdicionProfesor: abre una nueva ventana que permita ingresar datos de entrada
		   para la edición de datos de registro asociados a un profesor
		 */
		private void abrirEdicionProfesor () {
			String [] in = {inputLegajo.getText(), null, null, null, null, null, null};
			Profesor profesor = null;
			try
			{
				profesor = ControladorProfesor.controlador().recuperar(in);
				if (profesor == null) {
					JOptionPane.showMessageDialog(this,
							"ERROR! No existe un profesor registrado con legajo: " + inputLegajo.getText(),
							"Modificación de un profesor", JOptionPane.ERROR_MESSAGE);
				}
				else {
					new VentanaEdicionProfesor(profesor);
				}
			}
			catch (DBRetrieveException ex)
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Modificación de una materia", JOptionPane.ERROR_MESSAGE);
			}
			dispose();			
		}
		
		
		// CLASE ASOCIADA A LA VENTANA PARA INTRODUCIR DATOS PARA MODIFICAR UN ALUMNO
		
		private class VentanaEdicionProfesor extends JFrame {
			
			private static final long serialVersionUID = 1L;
			private JTextField [] inputs;
			private JCheckBox [] checkBoxes;
			private JComboBox<String> switchGenero;
			private JButton btnSiguiente;		
				
			/**
			 * CONSTRUCTOR: Ventana para el registro de un nuevo profesor
			 * @param lu: número de libreta universitaria asociado al profesor a modificar
			 */
			public VentanaEdicionProfesor (Profesor profesor) {
				super();
				getContentPane().setEnabled(false);
				getContentPane().setLayout(null);
				setVisible(true);				
				getContentPane().setBackground(SystemColor.controlHighlight);
				setTitle("Modificación de un profesor");
				setMaximumSize(new Dimension(312, 506));
				setMinimumSize(new Dimension(312, 506));
				setResizable(false);		
				setLocationRelativeTo(null);
				
				// CREACIÓN DE INPUTS: modificación de profesor
				inputs = new JTextField[11];
				checkBoxes = new JCheckBox[12];
				// Matrícula
				inputs[0] = new JTextField();
				inputs[0].setEnabled(false);
				inputs[0].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[0].setBounds(126, 37, 124, 20);
				getContentPane().add(inputs[0]);
				inputs[0].setColumns(10);
				// CUIL
				inputs[1] = new JTextField();
				inputs[1].setEnabled(false);
				inputs[1].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[1].setBounds(126, 68, 124, 20);
				getContentPane().add(inputs[1]);
				inputs[1].setColumns(10);
				// DNI
				inputs[2] = new JTextField();
				inputs[2].setEnabled(false);
				inputs[2].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[2].setBounds(126, 99, 124, 20);
				getContentPane().add(inputs[2]);
				inputs[2].setColumns(10);
				// Nombre
				inputs[3] = new JTextField();
				inputs[3].setEnabled(false);
				inputs[3].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[3].setColumns(10);
				inputs[3].setBounds(126, 130, 125, 20);
				getContentPane().add(inputs[3]);
				// Apellido
				inputs[4] = new JTextField();
				inputs[4].setEnabled(false);
				inputs[4].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[4].setColumns(10);
				inputs[4].setBounds(126, 161, 125, 20);
				getContentPane().add(inputs[4]);
				// Genero
				switchGenero = new JComboBox<String>();
				switchGenero.setEnabled(false);
				switchGenero.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				switchGenero.setBounds(126, 192, 124, 20);
				switchGenero.addItem("Masculino");
				switchGenero.addItem("Femenino");
				getContentPane().add(switchGenero);
				// Mail
				inputs[5] = new JTextField();
				inputs[5].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[5].setEnabled(false);
				inputs[5].setColumns(10);
				inputs[5].setBounds(126, 223, 125, 20);
				getContentPane().add(inputs[5]);
				// Telefono
				inputs[6] = new JTextField();
				inputs[6].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[6].setEnabled(false);
				inputs[6].setColumns(10);
				inputs[6].setBounds(126, 254, 125, 20);
				getContentPane().add(inputs[6]);
				// Calle
				inputs[7] = new JTextField();
				inputs[7].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[7].setEnabled(false);
				inputs[7].setColumns(10);
				inputs[7].setBounds(126, 285, 125, 20);
				getContentPane().add(inputs[7]);
				// Numero
				inputs[8] = new JTextField();
				inputs[8].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[8].setEnabled(false);
				inputs[8].setColumns(10);
				inputs[8].setBounds(126, 316, 125, 20);
				getContentPane().add(inputs[8]);
				// Piso
				inputs[9] = new JTextField();
				inputs[9].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[9].setEnabled(false);
				inputs[9].setColumns(10);
				inputs[9].setBounds(126, 347, 125, 20);
				getContentPane().add(inputs[9]);
				// Depto
				inputs[10] = new JTextField();
				inputs[10].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[10].setEnabled(false);
				inputs[10].setColumns(10);
				inputs[10].setBounds(126, 378, 124, 20);
				getContentPane().add(inputs[10]);
				
				// CREACIÓN DE CHECKBOXES:
				
				// Matrícula
				checkBoxes[0] = new JCheckBox("");
				checkBoxes[0].setBackground(SystemColor.controlHighlight);
				checkBoxes[0].setBounds(255, 37, 26, 20);				
				checkBoxes[0].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[0]);
					}
				});
				getContentPane().add(checkBoxes[0]);
				
				checkBoxes[1] = new JCheckBox("");
				checkBoxes[1].setBackground(SystemColor.controlHighlight);
				checkBoxes[1].setBounds(255, 68, 26, 20);
				checkBoxes[1].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[1]);
					}
				});
				getContentPane().add(checkBoxes[1]);
				
				// DNI
				checkBoxes[2] = new JCheckBox("");
				checkBoxes[2].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[2]);
					}
				});
				checkBoxes[2].setBackground(SystemColor.controlHighlight);
				checkBoxes[2].setBounds(255, 99, 26, 20);
				getContentPane().add(checkBoxes[2]);
				// Nombre
				checkBoxes[3] = new JCheckBox("");
				checkBoxes[3].setBackground(SystemColor.controlHighlight);
				checkBoxes[3].setBounds(255, 130, 26, 20);
				checkBoxes[3].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[3]);
					}
				});
				getContentPane().add(checkBoxes[3]);
				// Apellido
				checkBoxes[4] = new JCheckBox("");
				checkBoxes[4].setBackground(SystemColor.controlHighlight);
				checkBoxes[4].setBounds(255, 161, 26, 20);
				checkBoxes[4].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[4]);
						}
					});
				getContentPane().add(checkBoxes[4]);
				// Genero
				checkBoxes[11] = new JCheckBox("");
				checkBoxes[11].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(switchGenero);
					}
				});
				checkBoxes[11].setBackground(SystemColor.controlHighlight);
				checkBoxes[11].setBounds(255, 192, 26, 20);
				getContentPane().add(checkBoxes[11]);
				// Mail
				checkBoxes[5] = new JCheckBox("");
				checkBoxes[5].setEnabled(false);
				checkBoxes[5].setBackground(SystemColor.controlHighlight);
				checkBoxes[5].setBounds(255, 223, 26, 20);
				checkBoxes[5].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[5]);
					}
				});
				getContentPane().add(checkBoxes[5]);
				// Telefono
				checkBoxes[6] = new JCheckBox("");
				checkBoxes[6].setEnabled(false);
				checkBoxes[6].setBackground(SystemColor.controlHighlight);
				checkBoxes[6].setBounds(255, 254, 26, 20);
				checkBoxes[6].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[6]);
					}
				});
				getContentPane().add(checkBoxes[6]);
				// Calle
				checkBoxes[7] = new JCheckBox("");
				checkBoxes[7].setEnabled(false);
				checkBoxes[7].setBackground(SystemColor.controlHighlight);
				checkBoxes[7].setBounds(255, 285, 26, 20);
				checkBoxes[7].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[7]);
					}
				});
				getContentPane().add(checkBoxes[7]);
				// Numero
				checkBoxes[8] = new JCheckBox("");
				checkBoxes[8].setEnabled(false);
				checkBoxes[8].setBackground(SystemColor.controlHighlight);
				checkBoxes[8].setBounds(255, 316, 26, 20);
				checkBoxes[8].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[8]);
					}
				});
				getContentPane().add(checkBoxes[8]);
				// Piso
				checkBoxes[9] = new JCheckBox("");
				checkBoxes[9].setEnabled(false);
				checkBoxes[9].setBackground(SystemColor.controlHighlight);
				checkBoxes[9].setBounds(255, 347, 26, 20);
				checkBoxes[9].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[9]);
					}
				});
				getContentPane().add(checkBoxes[9]);
				// Depto
				checkBoxes[10] = new JCheckBox("");
				checkBoxes[10].setEnabled(false);
				checkBoxes[10].setBackground(SystemColor.controlHighlight);
				checkBoxes[10].setBounds(255, 378, 26, 20);
				checkBoxes[10].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[10]);
					}
				});
				getContentPane().add(checkBoxes[10]);
				
				// CREACIÓN DE LABELS: registro de profesor

				JLabel lblMatricula = new JLabel(" Matr\u00EDcula:");
				lblMatricula.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblMatricula.setBounds(45, 37, 71, 20);
				getContentPane().add(lblMatricula);
				
				JLabel lblCuil = new JLabel(" CUIL:");
				lblCuil.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblCuil.setBounds(68, 68, 48, 20);
				getContentPane().add(lblCuil);
				
				JLabel lblDNI = new JLabel("DNI:");
				lblDNI.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblDNI.setBounds(75, 99, 41, 20);
				getContentPane().add(lblDNI);				
				
				JLabel lblNombre = new JLabel("Nombre:");
				lblNombre.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblNombre.setBounds(57, 130, 59, 20);
				getContentPane().add(lblNombre);
				
				JLabel lblApellido = new JLabel("Apellido:");
				lblApellido.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblApellido.setBounds(57, 163, 59, 20);
				getContentPane().add(lblApellido);
				
				JLabel lblGenero = new JLabel("G\u00E9nero:");
				lblGenero.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblGenero.setBounds(59, 194, 57, 20);
				getContentPane().add(lblGenero);
				
				JLabel lblMail = new JLabel("Mail:");
				lblMail.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblMail.setBounds(75, 228, 41, 20);
				getContentPane().add(lblMail);
				
				JLabel lblTelfono = new JLabel("Tel\u00E9fono:");
				lblTelfono.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblTelfono.setBounds(45, 259, 71, 20);
				getContentPane().add(lblTelfono);
				
				JLabel lblCalle = new JLabel("Calle:");
				lblCalle.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblCalle.setBounds(73, 290, 41, 20);
				getContentPane().add(lblCalle);
				
				JLabel lblNum = new JLabel("N\u00B0:");
				lblNum.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblNum.setBounds(75, 321, 41, 20);
				getContentPane().add(lblNum);
				
				JLabel lblPiso = new JLabel("Piso:");
				lblPiso.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblPiso.setBounds(75, 352, 39, 20);
				getContentPane().add(lblPiso);
				
				JLabel lblDepto = new JLabel("Depto:");
				lblDepto.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblDepto.setBounds(57, 383, 59, 20);
				getContentPane().add(lblDepto);
				
				btnSiguiente = new JButton("Guardar");
				btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
				btnSiguiente.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						modificarProfesor(profesor);
						dispose();
					}
				});
				btnSiguiente.setBounds(98, 431, 124, 23);
				getContentPane().add(btnSiguiente);
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
				private void modificarProfesor (Profesor profesor) {
					String [] in = {profesor.obtenerLegajo() + "", null, null, null, null, null, null};
					boolean modificoAlgo = false;
					try
					{
						// Verifico si se quiere modificar la matrícula
						if (inputs[0].isEnabled()) {
							in[1] = inputs[0].getText();
							modificoAlgo = true;
						}
						// Verifico si se quiere modificar CUIL
						if (inputs[1].isEnabled()) {
							in[2] = inputs[1].getText();
							modificoAlgo = true;
						}
						// Verifico si se quiere modificar DNI
						if (inputs[2].isEnabled()) {
							in[3] = inputs[2].getText();
							modificoAlgo = true;
						}
						// Verifico si se quiere modificar Nombre
						if (inputs[3].isEnabled()) {
							in[4] = inputs[3].getText();
							modificoAlgo = true;
						}
						// Verifico si se quiere modificar Apellido
						if (inputs[4].isEnabled()) {
							in[5] = inputs[4].getText();
							modificoAlgo = true;
						}						
						// Verifico si se quiere modificar Genero
						if (switchGenero.isEnabled()) {
							in[6] = (String) switchGenero.getSelectedItem();
							modificoAlgo = true;
						}
						// Ejecuto la inserción del nuevo profesor
						ControladorProfesor.controlador().modificar(in);
						if (modificoAlgo) {
							JOptionPane.showMessageDialog(this,"Profesor con legajo: " + profesor.obtenerLegajo() + " modificado exitosamente");
							actualizarTabla();
						}
					}
					catch (DBUpdateException ex)
					{
						// en caso de error, se muestra la causa en la consola
						JOptionPane.showMessageDialog(this,"ERROR");
					}					
					dispose();
				}		   
		   }		   
		}	
	
	
		// CLASE PARA LA VENTANA DE INPUTS PARA LA BAJA
	
		private class VentanaElimProfesor extends JFrame {
			
			private static final long serialVersionUID = 1L;
			private JTextField inputLegajo;
			private JButton btnSiguiente;
			
			/**
			 * CONSTRUCTOR: Ventana para el registro de un nuevo profesor
			 */
			public VentanaElimProfesor() {
				super();
				getContentPane().setLayout(null);
		        setVisible(true);		        
		        getContentPane().setBackground(SystemColor.controlHighlight);
				setTitle("Baja de un profesor");
				setSize(new Dimension(350, 161));
				setResizable(false);		
				setLocationRelativeTo(null);
					
				// CREACIÓN DE INPUTS: registro de profesor
				
				inputLegajo = new JTextField();
				inputLegajo.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputLegajo.setBounds(141, 42, 124, 20);
				getContentPane().add(inputLegajo);
				
				// CREACIÓN DE LABELS: registro de profesor
				
				JLabel lblLegajo = new JLabel("Legajo:");		
				lblLegajo.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblLegajo.setBounds(80, 40, 62, 20);
				getContentPane().add(lblLegajo);
				
				btnSiguiente = new JButton("Dar de baja");
				btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
				btnSiguiente.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {						
						eliminarProfesor();
						dispose();
					}
				});
				btnSiguiente.setBounds(112, 87, 124, 23);
				getContentPane().add(btnSiguiente);
				
			}

			
			/**
			 * eliminarProfesor: solicita la baja de un profesor, con cierta matrícula, que está
			   registrado en el sistema
			 */
			private void eliminarProfesor () {
				try
				{
					ControladorProfesor.controlador().eliminar(inputLegajo.getText());
					JOptionPane.showMessageDialog(this,"Profesor dado de baja exitosamente");
					actualizarTabla();
					dispose();
				}
				catch (DBUpdateException ex)
				{
					JOptionPane.showMessageDialog(this, ex.getMessage(), "Baja de una materia", JOptionPane.ERROR_MESSAGE);
				}
			}
			   
		}
	
		
}