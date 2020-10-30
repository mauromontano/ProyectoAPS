package Vistas;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import Conector.ConectorBD;
import quick.dbtable.DBTable;


public class VistaAlumnos extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static VistaAlumnos instancia = null;
	private DBTable tabla;
	private JTextField txtNombre;
	protected int seleccionado = -1;
	
	
	public static VistaAlumnos obtenerVistaAlumnos () {
		if (instancia == null) {
			instancia = new VistaAlumnos();
		}
		return instancia;
	}
	
	// CONSTRUCTOR: Vista de alumnos	
	private VistaAlumnos() {
		
		this.setBackground(SystemColor.control);
		this.setBounds(0, 0, 1194, 699);
		this.setLayout(null);
		
		JPanel panelTabla = new JPanel();
		panelTabla.setBackground(SystemColor.controlHighlight);
		panelTabla.setBounds(10, 232, 1174, 456);
		this.add(panelTabla);
		
		JButton btnRegAlumno = new JButton("Registrar alumno");
		btnRegAlumno.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 13));
		btnRegAlumno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaRegAlumno();
			}
		});
		btnRegAlumno.setBounds(328, 85, 167, 40);
		this.add(btnRegAlumno);
		
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
        
        JButton btnModAlumno = new JButton("Modificar alumno");
        btnModAlumno.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaBuscarAlumno();
        	}
        });
        btnModAlumno.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnModAlumno.setBounds(505, 85, 167, 40);
        add(btnModAlumno);
        
        JButton btnBajaAlumno = new JButton("Dar de baja alumno");
        btnBajaAlumno.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaElimAlumno();
        	}
        });
        btnBajaAlumno.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnBajaAlumno.setBounds(682, 85, 167, 40);
        add(btnBajaAlumno);
        
        JButton btnAtras = new JButton("Atr\u00E1s");
        btnAtras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setVisible(false);
        		VistaPrincipal.obtenerVistaPrincipal().setVisible(true);
        	}
        });
        btnAtras.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnAtras.setBounds(10, 11, 70, 23);
        add(btnAtras);        
             
        actualizarListaAlumnos();
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
	
	
	private void actualizarListaAlumnos () {
		ConectorBD.obtenerConectorBD().conectarBD(tabla);
		try
	    {
			String consultaAlumnos = "SELECT * FROM alumnos ";
			tabla.setSelectSql(consultaAlumnos.trim());
			// Obtengo el modelo de la DB Table para actualizar el contenido de la lista de alumnos
	    	tabla.createColumnModelFromQuery();
	    	// Actualizo el contenido de la tabla   	     	  
	    	tabla.refresh();
	    }
		
		catch (SQLException ex)
		{
	         // en caso de error, se muestra la causa en la consola
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	         JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
	        		 					   ex.getMessage() + "\n",
	        		 					   "ERROR! No se pudo cargar la lista de alumnos",
	                                       JOptionPane.ERROR_MESSAGE);
	    }
		ConectorBD.obtenerConectorBD().desconectarBD(tabla);
	}
	
	
	
	// CLASE PARA LA VENTANA DE INPUTS PARA EL REGISTRO
	
	private class VentanaRegAlumno extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JTextField inputDNI;	
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
		
		
		// CONSTRUCTOR: Ventana para el registro de un nuevo alumno
		
		public VentanaRegAlumno() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Registro de nuevo alumno");
			setMaximumSize(new Dimension(322, 430));
			setMinimumSize(new Dimension(322, 430));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS: registro de alumno
			
			inputDNI = new JTextField();
			inputDNI.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputDNI.setBounds(125, 24, 124, 20);
			getContentPane().add(inputDNI);
			inputDNI.setColumns(10);
			
			inputNombre = new JTextField();
			inputNombre.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputNombre.setColumns(10);
			inputNombre.setBounds(124, 55, 125, 20);
			getContentPane().add(inputNombre);
			
			inputApellido = new JTextField();
			inputApellido.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputApellido.setColumns(10);
			inputApellido.setBounds(124, 88, 125, 20);
			getContentPane().add(inputApellido);
			
			inputGenero = new JComboBox<String>();
			inputGenero.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputGenero.setBounds(125, 119, 124, 20);
			inputGenero.addItem("Masculino");
			inputGenero.addItem("Femenino");
			getContentPane().add(inputGenero);
			
			inputMail = new JTextField();
			inputMail.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputMail.setEnabled(false);
			inputMail.setColumns(10);
			inputMail.setBounds(124, 153, 125, 20);
			getContentPane().add(inputMail);
			
			inputTelefono = new JTextField();
			inputTelefono.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputTelefono.setEnabled(false);
			inputTelefono.setColumns(10);
			inputTelefono.setBounds(124, 184, 125, 20);
			getContentPane().add(inputTelefono);
			
			inputCalle = new JTextField();
			inputCalle.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputCalle.setEnabled(false);
			inputCalle.setColumns(10);
			inputCalle.setBounds(124, 215, 125, 20);
			getContentPane().add(inputCalle);
			
			inputNum = new JTextField();
			inputNum.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputNum.setEnabled(false);
			inputNum.setColumns(10);
			inputNum.setBounds(124, 246, 125, 20);
			getContentPane().add(inputNum);
			
			inputPiso = new JTextField();
			inputPiso.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputPiso.setEnabled(false);
			inputPiso.setColumns(10);
			inputPiso.setBounds(124, 277, 125, 20);
			getContentPane().add(inputPiso);
			
			inputDepto = new JTextField();
			inputDepto.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputDepto.setEnabled(false);
			inputDepto.setColumns(10);
			inputDepto.setBounds(125, 308, 124, 20);
			getContentPane().add(inputDepto);
			
			// CREACIÓN DE LABELS: registro de alumno
			
			JLabel lblDNI = new JLabel("DNI:");		
			lblDNI.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblDNI.setBounds(74, 24, 41, 20);
			getContentPane().add(lblDNI);
			
			JLabel lblNombre = new JLabel("Nombre:");
			lblNombre.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblNombre.setBounds(56, 55, 59, 20);
			getContentPane().add(lblNombre);
			
			JLabel lblApellido = new JLabel("Apellido:");
			lblApellido.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblApellido.setBounds(56, 88, 59, 20);
			getContentPane().add(lblApellido);
			
			JLabel lblGenero = new JLabel("G\u00E9nero:");
			lblGenero.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblGenero.setBounds(58, 119, 57, 20);
			getContentPane().add(lblGenero);
			
			JLabel lblMail = new JLabel("Mail:");
			lblMail.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblMail.setBounds(74, 153, 41, 20);
			getContentPane().add(lblMail);
			
			JLabel lblTelfono = new JLabel("Tel\u00E9fono:");
			lblTelfono.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblTelfono.setBounds(44, 184, 71, 20);
			getContentPane().add(lblTelfono);
			
			JLabel lblCalle = new JLabel("Calle:");
			lblCalle.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblCalle.setBounds(72, 215, 41, 20);
			getContentPane().add(lblCalle);
			
			JLabel lblNum = new JLabel("N\u00B0:");
			lblNum.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblNum.setBounds(74, 246, 41, 20);
			getContentPane().add(lblNum);
			
			JLabel lblPiso = new JLabel("Piso:");
			lblPiso.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblPiso.setBounds(74, 277, 39, 20);
			getContentPane().add(lblPiso);
			
			JLabel lblDepto = new JLabel("Depto:");
			lblDepto.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblDepto.setBounds(56, 308, 59, 20);
			getContentPane().add(lblDepto);
			
			btnSiguiente = new JButton("Guardar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					registrarAlumno();
					dispose();
				}
			});
			btnSiguiente.setBounds(95, 354, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
		
		
		// CONEXIÓN Y DESCONEXIÓN DE LA BASE DE DATOS	   
		   
		   private void registrarAlumno ()
		   {
			  ConectorBD.obtenerConectorBD().conectarBD();
		      try
		      {
		         // Creo un comando JDBC para realizar la inserción en la BD
		         Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
		         // Genero la sentencia de inserción	         
		         String sql = "INSERT INTO alumnos (dni, nombre, apellido, genero) VALUES (" + 
		        		 	  inputDNI.getText() + 
		        		 	  ",\'" + inputNombre.getText() +
		        		 	  "\',\'" + inputApellido.getText() + 
		        		 	  "\',\'" + inputGenero.getSelectedItem() + "\');";

		         // Ejecuto la inserción del nuevo alumno
		         stmt.executeUpdate(sql);
		         JOptionPane.showMessageDialog(this,"Alumno registrado exitosamente");
		         stmt.close();
		         dispose();	         
		      }
		      catch (SQLException ex)
		      {
		         // en caso de error, se muestra la causa en la consola
		         System.out.println("SQLException: " + ex.getMessage());
		         System.out.println("SQLState: " + ex.getSQLState());
		         System.out.println("VendorError: " + ex.getErrorCode());
		      }
		      
		      ConectorBD.obtenerConectorBD().desconectarBD();
		      actualizarListaAlumnos();
		   }
		   
	}
	
	
	// CLASE PARA LA VENTANA DE BÚSQUEDA DE ALUMNO PARA SU MODIFICACIÓN
	
	
	private class VentanaBuscarAlumno extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JTextField inputLU;
		private JButton btnSiguiente;
		
		
		// CONSTRUCTOR: Ventana para el registro de un nuevo alumno
		
		public VentanaBuscarAlumno() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Modificación de un alumno");
			setSize(new Dimension(353, 170));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS: registro de alumno
			
			inputLU = new JTextField();
			inputLU.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputLU.setBounds(128, 52, 100, 20);
			getContentPane().add(inputLU);
			
			// CREACIÓN DE LABELS: registro de alumno
			
			JLabel lblLU = new JLabel("LU:");		
			lblLU.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblLU.setBounds(89, 52, 29, 20);
			getContentPane().add(lblLU);
			
			btnSiguiente = new JButton("A modificar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {						
					abrirEdicionAlumno();
					dispose();
				}
			});
			btnSiguiente.setBounds(115, 95, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
		
		
		// CONEXIÓN Y DESCONEXIÓN DE LA BASE DE DATOS	   
		   
		   private void abrirEdicionAlumno ()
		   {
			  ConectorBD.obtenerConectorBD().conectarBD();
		      try
		      {
		         // Creo un comando JDBC para realizar la inserción en la BD
		         Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
		         // Genero la sentencia de inserción	         
		         String sql = "SELECT * FROM alumnos WHERE (LU = " + inputLU.getText() + ")";

		         // Ejecuto la eliminación del alumno
		         ResultSet rs = stmt.executeQuery(sql);
		         if (!rs.next()) {
		        	 JOptionPane.showMessageDialog(this,
		        			 "ERROR! No existe un alumno registrado con LU: " + inputLU.getText(),
		        			 "Modificación de un alumno", JOptionPane.ERROR_MESSAGE);
		         }
		         else {
		        	 new VentanaEdicionAlumno(Integer.parseInt(inputLU.getText()));			        	 
		         }
		         stmt.close();
		         dispose();	         
		      }
		      catch (SQLException ex)
		      {
		         // en caso de error, se muestra la causa en la consola
		         System.out.println("SQLException: " + ex.getMessage());
		         System.out.println("SQLState: " + ex.getSQLState());
		         System.out.println("VendorError: " + ex.getErrorCode());
		      }
		      
		      ConectorBD.obtenerConectorBD().desconectarBD();
		      actualizarListaAlumnos();
		   }
		   
		   
		   private class VentanaEdicionAlumno extends JFrame {
				private static final long serialVersionUID = 1L;
				private JTextField [] inputs;
				private JCheckBox [] checkBoxes;
				private JComboBox<String> switchGenero;
				private JButton btnSiguiente;
				
				
				// CONSTRUCTOR: Ventana para el registro de un nuevo alumno
				
				public VentanaEdicionAlumno(int lu) {
					super();
					getContentPane().setEnabled(false);
					getContentPane().setLayout(null);
			        setVisible(true);
			        
			        getContentPane().setBackground(SystemColor.controlHighlight);
					setTitle("Modificación de un alumno");
					setMaximumSize(new Dimension(322, 430));
					setMinimumSize(new Dimension(322, 430));
					setResizable(false);		
					setLocationRelativeTo(null);
						
					// CREACIÓN DE INPUTS: modificación de alumno
					inputs = new JTextField[9];
					checkBoxes = new JCheckBox[10];
					
					// DNI
					inputs[0] = new JTextField();
					inputs[0].setEnabled(false);
					inputs[0].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					inputs[0].setBounds(125, 24, 124, 20);
					getContentPane().add(inputs[0]);
					inputs[0].setColumns(10);
					
					// Nombre
					inputs[1] = new JTextField();
					inputs[1].setEnabled(false);
					inputs[1].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					inputs[1].setColumns(10);
					inputs[1].setBounds(124, 55, 125, 20);
					getContentPane().add(inputs[1]);
					
					// Apellido
					inputs[2] = new JTextField();
					inputs[2].setEnabled(false);
					inputs[2].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					inputs[2].setColumns(10);
					inputs[2].setBounds(124, 88, 125, 20);
					getContentPane().add(inputs[2]);
					
					// Genero
					switchGenero = new JComboBox<String>();
					switchGenero.setEnabled(false);
					switchGenero.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					switchGenero.setBounds(125, 119, 124, 20);
					switchGenero.addItem("Masculino");
					switchGenero.addItem("Femenino");
					getContentPane().add(switchGenero);
					
					// Mail
					inputs[3] = new JTextField();
					inputs[3].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					inputs[3].setEnabled(false);
					inputs[3].setColumns(10);
					inputs[3].setBounds(124, 153, 125, 20);
					getContentPane().add(inputs[3]);
					
					// Telefono
					inputs[4] = new JTextField();
					inputs[4].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					inputs[4].setEnabled(false);
					inputs[4].setColumns(10);
					inputs[4].setBounds(124, 184, 125, 20);
					getContentPane().add(inputs[4]);
					
					// Calle
					inputs[5] = new JTextField();
					inputs[5].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					inputs[5].setEnabled(false);
					inputs[5].setColumns(10);
					inputs[5].setBounds(124, 215, 125, 20);
					getContentPane().add(inputs[5]);
					
					// Numero
					inputs[6] = new JTextField();
					inputs[6].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					inputs[6].setEnabled(false);
					inputs[6].setColumns(10);
					inputs[6].setBounds(124, 246, 125, 20);
					getContentPane().add(inputs[6]);
					
					// Piso
					inputs[7] = new JTextField();
					inputs[7].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					inputs[7].setEnabled(false);
					inputs[7].setColumns(10);
					inputs[7].setBounds(124, 277, 125, 20);
					getContentPane().add(inputs[7]);
					
					// Depto
					inputs[8] = new JTextField();
					inputs[8].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					inputs[8].setEnabled(false);
					inputs[8].setColumns(10);
					inputs[8].setBounds(125, 308, 124, 20);
					getContentPane().add(inputs[8]);
							
					// CREACIÓN DE CHECKBOXES:
					
					// DNI
					checkBoxes[0] = new JCheckBox("");
					checkBoxes[0].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							switchearEstadoInput(inputs[0]);
						}
					});
					checkBoxes[0].setBackground(SystemColor.controlHighlight);
					checkBoxes[0].setBounds(255, 24, 26, 20);
					getContentPane().add(checkBoxes[0]);
					
					// Nombre
					checkBoxes[1] = new JCheckBox("");
					checkBoxes[1].setBackground(SystemColor.controlHighlight);
					checkBoxes[1].setBounds(255, 55, 26, 20);
					checkBoxes[1].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							switchearEstadoInput(inputs[1]);
						}
					});
					getContentPane().add(checkBoxes[1]);
					
					// Apellido
					checkBoxes[2] = new JCheckBox("");
					checkBoxes[2].setBackground(SystemColor.controlHighlight);
					checkBoxes[2].setBounds(255, 88, 26, 20);
					checkBoxes[2].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							switchearEstadoInput(inputs[2]);
						}
					});
					getContentPane().add(checkBoxes[2]);
					
					// Genero
					checkBoxes[9] = new JCheckBox("");
					checkBoxes[9].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							switchearEstadoInput(switchGenero);
						}
					});
					checkBoxes[9].setBackground(SystemColor.controlHighlight);
					checkBoxes[9].setBounds(255, 119, 26, 20);
					getContentPane().add(checkBoxes[9]);
					
					// Mail
					checkBoxes[3] = new JCheckBox("");
					checkBoxes[3].setEnabled(false);
					checkBoxes[3].setBackground(SystemColor.controlHighlight);
					checkBoxes[3].setBounds(255, 153, 26, 20);
					checkBoxes[3].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							switchearEstadoInput(inputs[3]);
						}
					});
					getContentPane().add(checkBoxes[3]);
					
					// Telefono
					checkBoxes[4] = new JCheckBox("");
					checkBoxes[4].setEnabled(false);
					checkBoxes[4].setBackground(SystemColor.controlHighlight);
					checkBoxes[4].setBounds(255, 184, 26, 20);
					checkBoxes[4].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							switchearEstadoInput(inputs[4]);
						}
					});
					getContentPane().add(checkBoxes[4]);
					
					// Calle
					checkBoxes[5] = new JCheckBox("");
					checkBoxes[5].setEnabled(false);
					checkBoxes[5].setBackground(SystemColor.controlHighlight);
					checkBoxes[5].setBounds(255, 215, 26, 20);
					checkBoxes[5].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							switchearEstadoInput(inputs[5]);
						}
					});
					getContentPane().add(checkBoxes[5]);
				
					// Numero
					checkBoxes[6] = new JCheckBox("");
					checkBoxes[6].setEnabled(false);
					checkBoxes[6].setBackground(SystemColor.controlHighlight);
					checkBoxes[6].setBounds(255, 246, 26, 20);
					checkBoxes[6].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							switchearEstadoInput(inputs[6]);
						}
					});
					getContentPane().add(checkBoxes[6]);
					
					// Piso
					checkBoxes[7] = new JCheckBox("");
					checkBoxes[7].setEnabled(false);
					checkBoxes[7].setBackground(SystemColor.controlHighlight);
					checkBoxes[7].setBounds(255, 277, 26, 20);
					checkBoxes[7].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							switchearEstadoInput(inputs[7]);
						}
					});
					getContentPane().add(checkBoxes[7]);
					
					// Depto
					checkBoxes[8] = new JCheckBox("");
					checkBoxes[8].setEnabled(false);
					checkBoxes[8].setBackground(SystemColor.controlHighlight);
					checkBoxes[8].setBounds(255, 308, 26, 20);
					checkBoxes[8].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							switchearEstadoInput(inputs[8]);
						}
					});
					getContentPane().add(checkBoxes[9]);
					
					// CREACIÓN DE LABELS: registro de alumno
					
					JLabel lblDNI = new JLabel("DNI:");		
					lblDNI.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					lblDNI.setBounds(74, 24, 41, 20);
					getContentPane().add(lblDNI);
					
					JLabel lblNombre = new JLabel("Nombre:");
					lblNombre.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					lblNombre.setBounds(56, 55, 59, 20);
					getContentPane().add(lblNombre);
					
					JLabel lblApellido = new JLabel("Apellido:");
					lblApellido.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					lblApellido.setBounds(56, 88, 59, 20);
					getContentPane().add(lblApellido);
					
					JLabel lblGenero = new JLabel("G\u00E9nero:");
					lblGenero.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					lblGenero.setBounds(58, 119, 57, 20);
					getContentPane().add(lblGenero);
					
					JLabel lblMail = new JLabel("Mail:");
					lblMail.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					lblMail.setBounds(74, 153, 41, 20);
					getContentPane().add(lblMail);
					
					JLabel lblTelfono = new JLabel("Tel\u00E9fono:");
					lblTelfono.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					lblTelfono.setBounds(44, 184, 71, 20);
					getContentPane().add(lblTelfono);
					
					JLabel lblCalle = new JLabel("Calle:");
					lblCalle.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					lblCalle.setBounds(72, 215, 41, 20);
					getContentPane().add(lblCalle);
					
					JLabel lblNum = new JLabel("N\u00B0:");
					lblNum.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					lblNum.setBounds(74, 246, 41, 20);
					getContentPane().add(lblNum);
					
					JLabel lblPiso = new JLabel("Piso:");
					lblPiso.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					lblPiso.setBounds(74, 277, 39, 20);
					getContentPane().add(lblPiso);
					
					JLabel lblDepto = new JLabel("Depto:");
					lblDepto.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					lblDepto.setBounds(56, 308, 59, 20);
					getContentPane().add(lblDepto);
					
					btnSiguiente = new JButton("Guardar");
					btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
					btnSiguiente.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {				
							modificarAlumno(lu);
							dispose();
						}
					});
					btnSiguiente.setBounds(107, 354, 124, 23);
					getContentPane().add(btnSiguiente);	
				}
				
				
				private void switchearEstadoInput (JComponent in) {
					if (in.isEnabled()) {
						in.setEnabled(false);
					}
					else in.setEnabled(true);
				}
				   
				   
				   private void modificarAlumno (int lu)
				   {
					  ConectorBD.obtenerConectorBD().conectarBD();
				      try
				      {
				    	  // Creo un comando JDBC para realizar la inserción en la BD
				    	  Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
				    	  int cantInputs = inputsHabilitados();
				    	  // Genero la sentencia de inserción
				    	  String sql = "UPDATE alumnos SET ";
				    	  // Si quiero modificar el DNI...
				    	  if (inputs[0].isEnabled()) {
				    		  sql += "dni = " + inputs[0].getText();
				    		  cantInputs--;
				    		  if (cantInputs > 0) {
				    			  sql += ", ";
				    		  }
				    	  }
				    	// Si quiero modificar el Nombre...
				    	  if (inputs[1].isEnabled()) {
				    		  sql += "nombre = \'" + inputs[1].getText() + "\'";
				    		  cantInputs--;
				    		  if (cantInputs > 0) {
				    			  sql += ", ";
				    		  }
				    	  }
				    	// Si quiero modificar el Apellido...
				    	  if (inputs[2].isEnabled()) {
				    		  sql += "apellido = \'" + inputs[2].getText() + "\'";
				    		  cantInputs--;
				    		  if (cantInputs > 0) {
				    			  sql += ", ";
				    		  }
				    	  }
				    	// Si quiero modificar el Género...
				    	  if (switchGenero.isEnabled()) {
				    		  sql += "genero = \'" + switchGenero.getSelectedItem() + "\'";
				    	  }
				    	  sql += " WHERE (LU = " + lu + ");";

				         // Ejecuto la inserción del nuevo alumno
				         stmt.executeUpdate(sql);
				         JOptionPane.showMessageDialog(this,"Alumno con LU: " + lu +" modificado exitosamente");
				         stmt.close();
				         dispose();	         
				      }
				      catch (SQLException ex)
				      {
				         // en caso de error, se muestra la causa en la consola
				         System.out.println("SQLException: " + ex.getMessage());
				         System.out.println("SQLState: " + ex.getSQLState());
				         System.out.println("VendorError: " + ex.getErrorCode());
				      }
				      
				      ConectorBD.obtenerConectorBD().desconectarBD();
				      //actualizarListaAlumnos();
				   }
				   

				   private int inputsHabilitados () {
					   int cant = 0;
					   int i;
					   for (i=0; i < inputs.length; i++) {
						   if (inputs[i].isEnabled()) {
							   cant++;
						   }
					   }
					   return cant;		
				   }
			}
		   
		   
		   
	}
	
	
	
	// CLASE PARA LA VENTANA DE INPUTS PARA LA BAJA
	
		private class VentanaElimAlumno extends JFrame {
			
			private static final long serialVersionUID = 1L;
			private JTextField inputLU;
			private JButton btnSiguiente;
			
			
			// CONSTRUCTOR: Ventana para el registro de un nuevo alumno
			
			public VentanaElimAlumno() {
				super();
				getContentPane().setLayout(null);
		        setVisible(true);
		        
		        getContentPane().setBackground(SystemColor.controlHighlight);
				setTitle("Baja de un alumno");
				setSize(new Dimension(289, 170));
				setResizable(false);		
				setLocationRelativeTo(null);
					
				// CREACIÓN DE INPUTS: registro de alumno
				
				inputLU = new JTextField();
				inputLU.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputLU.setBounds(95, 52, 100, 20);
				getContentPane().add(inputLU);
				
				// CREACIÓN DE LABELS: registro de alumno
				
				JLabel lblLU = new JLabel("LU:");		
				lblLU.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblLU.setBounds(56, 52, 29, 20);
				getContentPane().add(lblLU);
				
				btnSiguiente = new JButton("Dar de baja");
				btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
				btnSiguiente.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						eliminarAlumno();
						dispose();
					}
				});
				btnSiguiente.setBounds(82, 98, 124, 23);
				getContentPane().add(btnSiguiente);
				
			}
			
			
			// CONEXIÓN Y DESCONEXIÓN DE LA BASE DE DATOS	   
			   
			   private void eliminarAlumno ()
			   {
				  ConectorBD.obtenerConectorBD().conectarBD();
			      try
			      {
			         // Creo un comando JDBC para realizar la inserción en la BD
			         Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
			         // Genero la sentencia de inserción	         
			         String sql = "DELETE FROM alumnos WHERE ( LU = " + inputLU.getText() + ")";

			         // Ejecuto la eliminación del alumno
			         stmt.executeUpdate(sql);
			         JOptionPane.showMessageDialog(this,"Alumno dado de baja exitosamente");
			         stmt.close();
			         dispose();	         
			      }
			      catch (SQLException ex)
			      {
			         // en caso de error, se muestra la causa en la consola
			         System.out.println("SQLException: " + ex.getMessage());
			         System.out.println("SQLState: " + ex.getSQLState());
			         System.out.println("VendorError: " + ex.getErrorCode());
			      }
			      
			      ConectorBD.obtenerConectorBD().desconectarBD();
			      actualizarListaAlumnos();
			   }
			   
		}
}