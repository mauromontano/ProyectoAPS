package Vistas;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import Conector.ConectorBD;
import quick.dbtable.DBTable;


public class VistaCarreras extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static VistaCarreras instancia = null;
	private DBTable tabla;
	protected int seleccionado = -1;
	
	
	public static VistaCarreras obtenerVistaCarreras () {
		if (instancia == null) {
			instancia = new VistaCarreras();
		}
		return instancia;
	}
	
	// CONSTRUCTOR: Vista de alumnos	
	private VistaCarreras() {
		
		this.setBackground(SystemColor.control);
		this.setBounds(0, 0, 1194, 699);
		this.setLayout(null);
		
		JPanel panelTabla = new JPanel();
		panelTabla.setBackground(SystemColor.controlHighlight);
		panelTabla.setBounds(10, 232, 1174, 456);
		this.add(panelTabla);
		
		JButton btnRegCarrera = new JButton("Registrar carrera");
		btnRegCarrera.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 13));
		btnRegCarrera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaRegCarrera();
			}
		});
		btnRegCarrera.setBounds(328, 84, 167, 40);
		this.add(btnRegCarrera);
		
		tabla = new DBTable();
		tabla.setBounds(460, 5, 248, 427);
        
        panelTabla.setLayout(null);
        
        panelTabla.add(tabla);           
        tabla.setEditable(false);
        
        JButton btnModCarrera = new JButton("Modificar carrera");
        btnModCarrera.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaBuscarCarrera();
        	}
        });
        btnModCarrera.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnModCarrera.setBounds(505, 85, 167, 40);
        add(btnModCarrera);
        
        JButton btnBajaCarrera = new JButton("Dar de baja carrera");
        btnBajaCarrera.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaElimCarrera();
        	}
        });
        btnBajaCarrera.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnBajaCarrera.setBounds(682, 85, 167, 40);
        add(btnBajaCarrera);
        
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
              
        actualizarListaCarreras();
	}
	
	
	private void actualizarListaCarreras () {
		ConectorBD.obtenerConectorBD().conectarBD(tabla);
		try
	    {
			String consultaCarreras = "SELECT * FROM carreras ";
			tabla.setSelectSql(consultaCarreras.trim());
			// Obtengo el modelo de la DB Table para actualizar el contenido de la lista de alumnos
	    	tabla.createColumnModelFromQuery();
	    	// actualizamos el contenido de la tabla.   	     	  
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
	        		 					   "ERROR! No se pudo cargar la lista de carreras",
	                                       JOptionPane.ERROR_MESSAGE);
	    }
		ConectorBD.obtenerConectorBD().desconectarBD(tabla);
	}
	
	
	
	// CLASE PARA LA VENTANA DE INPUTS PARA EL REGISTRO
	
	public class VentanaRegCarrera extends JFrame {
		
		private static final long serialVersionUID = 1L;	
		private JTextField inputNombre;
		private JTextField inputDuracion;
		private JLabel lblNombre;
		private JLabel lblDuracion;
		private JButton btnSiguiente;
		
		
		public VentanaRegCarrera() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Registro de nueva carrera");
			setMaximumSize(new Dimension(322, 245));
			setMinimumSize(new Dimension(322, 245));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS
			
			inputNombre = new JTextField();
			inputNombre.setColumns(10);
			inputNombre.setBounds(94, 55, 171, 20);
			getContentPane().add(inputNombre);
			
			inputDuracion = new JTextField();
			inputDuracion.setBounds(94, 88, 23, 20);
			getContentPane().add(inputDuracion);
			
			lblNombre = new JLabel("Nombre:");
			lblNombre.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 12));
			lblNombre.setBounds(25, 55, 59, 20);
			getContentPane().add(lblNombre);
			
			lblDuracion = new JLabel("Duraci\u00F3n:");
			lblDuracion.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 12));
			lblDuracion.setBounds(25, 88, 59, 20);
			getContentPane().add(lblDuracion);
			
			btnSiguiente = new JButton("Guardar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					registrarCarrera();
					dispose();
				}
			});
			btnSiguiente.setBounds(94, 153, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
		
		   
		private void registrarCarrera ()
		   {
			   ConectorBD.obtenerConectorBD().conectarBD();
			   try
			   {
		         // Creo un comando JDBC para realizar la inserción en la BD
		         Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
		         	         
		         // Genero la sentencia de inserción	         
		         String sql = "INSERT INTO carreras (nombre, duracion) VALUES (" + 
		        		 	  "\'" + inputNombre.getText() +
		        		 	  "\'," + inputDuracion.getText() + ");";

		         // Ejecuto la inserción del nuevo alumno
		         stmt.executeUpdate(sql);
		         // Notifico éxito en la operación
		         JOptionPane.showMessageDialog(this,"Carrera registrada exitosamente");	         
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
		      actualizarListaCarreras();
		   }
	}
	
	
	private class VentanaBuscarCarrera extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JTextField inputId;
		private JButton btnSiguiente;
		
		
		// CONSTRUCTOR: Ventana para el registro de una nueva carrera
		
		public VentanaBuscarCarrera() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Modificación de una carrera");
			setSize(new Dimension(353, 170));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS: registro de alumno
			
			inputId = new JTextField();
			inputId.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputId.setBounds(128, 41, 100, 20);
			getContentPane().add(inputId);
			
			// CREACIÓN DE LABELS: registro de alumno
			
			JLabel lblLU = new JLabel("ID:");		
			lblLU.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblLU.setBounds(89, 41, 29, 20);
			getContentPane().add(lblLU);
			
			btnSiguiente = new JButton("A modificar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {						
					abrirEdicionCarrera();
					dispose();
				}
			});
			btnSiguiente.setBounds(115, 95, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
			   
		   
		   private void abrirEdicionCarrera ()
		   {
			  ConectorBD.obtenerConectorBD().conectarBD();
		      try
		      {
		         // Creo un comando JDBC para realizar la inserción en la BD
		         Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
		         // Genero la sentencia de inserción	         
		         String sql = "SELECT * FROM carreras WHERE (id = " + inputId.getText() + ")";

		         // Ejecuto la eliminación del alumno
		         ResultSet rs = stmt.executeQuery(sql);
		         if (!rs.next()) {
		        	 JOptionPane.showMessageDialog(this,
		        			 "ERROR! No existe una carrera registrada con ID: " + inputId.getText(),
		        			 "Modificación de una carrera", JOptionPane.ERROR_MESSAGE);
		         }
		         else {
		        	 new VentanaEdicionCarrera(Integer.parseInt(inputId.getText()));			        	 
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
		      actualizarListaCarreras();
		   }
		   
		   
		   private class VentanaEdicionCarrera extends JFrame {
			   
				private static final long serialVersionUID = 1L;
				private JTextField [] inputs;
				private JCheckBox [] checkBoxes;
				private JButton btnSiguiente;
				
				
				// CONSTRUCTOR: Ventana para el registro de un nuevo alumno
				
				public VentanaEdicionCarrera(int id) {
					super();
					getContentPane().setEnabled(false);
					getContentPane().setLayout(null);
			        setVisible(true);
			        
			        getContentPane().setBackground(SystemColor.controlHighlight);
					setTitle("Modificación de una carrera");
					setSize(new Dimension(322, 185));
					setMinimumSize(new Dimension(322, 185));
					setResizable(false);		
					setLocationRelativeTo(null);
						
					// CREACIÓN DE INPUTS
					
					inputs = new JTextField[2];
					checkBoxes = new JCheckBox[2];
					
					// Nombre de materia
					inputs[0] = new JTextField();
					inputs[0].setEnabled(false);
					inputs[0].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					inputs[0].setBounds(125, 24, 124, 20);
					getContentPane().add(inputs[0]);
					inputs[0].setColumns(10);
					
					// Carga horaria
					inputs[1] = new JTextField();
					inputs[1].setEnabled(false);
					inputs[1].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					inputs[1].setColumns(10);
					inputs[1].setBounds(124, 55, 125, 20);
					getContentPane().add(inputs[1]);
							
					// CREACIÓN DE CHECKBOXES
					
					// Nombre de materia
					checkBoxes[0] = new JCheckBox("");
					checkBoxes[0].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							switchearEstadoInput(inputs[0]);
						}
					});
					checkBoxes[0].setBackground(SystemColor.controlHighlight);
					checkBoxes[0].setBounds(255, 24, 26, 20);
					getContentPane().add(checkBoxes[0]);
					
					// Carga horaria
					checkBoxes[1] = new JCheckBox("");
					checkBoxes[1].setBackground(SystemColor.controlHighlight);
					checkBoxes[1].setBounds(255, 55, 26, 20);
					checkBoxes[1].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							switchearEstadoInput(inputs[1]);
						}
					});
					getContentPane().add(checkBoxes[1]);
					
					// CREACIÓN DE LABELS
					
					JLabel lblNombre = new JLabel("Nombre:");
					lblNombre.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));;
					lblNombre.setBounds(47, 24, 68, 20);
					getContentPane().add(lblNombre);
					
					JLabel lblCarga = new JLabel("Duraci\u00F3n:");		
					lblCarga.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					lblCarga.setBounds(47, 55, 68, 20);
					getContentPane().add(lblCarga);		
					
					btnSiguiente = new JButton("Guardar");
					btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
					btnSiguiente.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {				
							modificarCarrera(id);
							dispose();
						}
					});
					btnSiguiente.setBounds(95, 101, 124, 23);
					getContentPane().add(btnSiguiente);
				}
				
				
				private void switchearEstadoInput (JComponent in) {
					if (in.isEnabled()) {
						in.setEnabled(false);
					}
					else in.setEnabled(true);
				}
				   
				   
				   private void modificarCarrera (int id)
				   {
					  ConectorBD.obtenerConectorBD().conectarBD();
				      try
				      {
				    	  // Creo un comando JDBC para realizar la inserción en la BD
				    	  Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
				    	  int cantInputs = inputsHabilitados();
				    	  // Genero la sentencia de inserción
				    	  String sql = "UPDATE carreras SET ";
				    	  // Si quiero modificar el DNI...
				    	  if (inputs[0].isEnabled()) {
				    		  sql += "nombre = \'" + inputs[0].getText() + "\'";
				    		  cantInputs--;
				    		  if (cantInputs > 0) {
				    			  sql += ", ";
				    		  }
				    	  }
				    	// Si quiero modificar el Nombre...
				    	  if (inputs[1].isEnabled()) {
				    		  sql += "duracion = " + inputs[1].getText() + " ";
				    		  cantInputs--;
				    	  }
				    	  
				    	  sql += " WHERE (id = " + id + ");";

				         // Ejecuto la inserción del nuevo alumno
				         stmt.executeUpdate(sql);
				         JOptionPane.showMessageDialog(this,"Carrera con ID: " + id +" modificada exitosamente");
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
				      actualizarListaCarreras();
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
	
	public class VentanaElimCarrera extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JTextField inputNombre;
		private JButton btnSiguiente;
		
		
		// CONSTRUCTOR: Ventana para el registro de un nuevo alumno
		
		public VentanaElimCarrera() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Baja de una materia");
			setSize(new Dimension(380, 170));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS: registro de alumno
			
			inputNombre = new JTextField();
			inputNombre.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputNombre.setBounds(163, 41, 176, 20);
			getContentPane().add(inputNombre);
			
			// CREACIÓN DE LABELS: registro de alumno
			
			JLabel lblId = new JLabel("Nombre de la carrera:");		
			lblId.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblId.setBounds(23, 41, 128, 20);
			getContentPane().add(lblId);
			
			btnSiguiente = new JButton("Dar de baja");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {				
					eliminarCarrera();
					dispose();
				}
			});
			btnSiguiente.setBounds(123, 86, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
		
		
		// CONEXIÓN Y DESCONEXIÓN DE LA BASE DE DATOS	   
		   
		   private void eliminarCarrera ()
		   {
			  ConectorBD.obtenerConectorBD().conectarBD();
		      try
		      {
		    	  // Creo un comando JDBC para realizar la inserción en la BD
		    	  Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
		    	  // Genero la sentencia de inserción
		    	  String sql = "DELETE FROM carreras WHERE (nombre = \'" + inputNombre.getText() + "\')";
		    	  // Ejecuto la eliminación del alumno
		    	  stmt.executeUpdate(sql);
		    	  JOptionPane.showMessageDialog(this,"Carrera dada de baja exitosamente");
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
		      actualizarListaCarreras();
		   }
	}

}
