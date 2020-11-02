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


public class VistaAdminPlanes extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static VistaAdminPlanes instancia = null;
	private DBTable tabla;
	private JTextField txtNombre;
	protected int seleccionado = -1;
	
	
	public static VistaAdminPlanes vista () {
		if (instancia == null) {
			instancia = new VistaAdminPlanes();
		}
		return instancia;
	}
	
	// CONSTRUCTOR: Vista de alumnos	
	private VistaAdminPlanes() {
		
		this.setBackground(SystemColor.control);
		this.setBounds(0, 0, 1194, 699);
		this.setLayout(null);
		
		JPanel panelTabla = new JPanel();
		panelTabla.setBackground(SystemColor.controlHighlight);
		panelTabla.setBounds(10, 232, 1174, 456);
		this.add(panelTabla);
		
		JButton btnRegPlan = new JButton("Registrar plan");
		btnRegPlan.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 13));
		btnRegPlan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaRegPlan();
			}
		});
		btnRegPlan.setBounds(328, 84, 167, 40);
		this.add(btnRegPlan);
		
		tabla = new DBTable();
		tabla.setBounds(446, 5, 275, 427);
        
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
        
        JButton btnModPlan = new JButton("Modificar plan");
        btnModPlan.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaBuscarPlan();
        	}
        });
        btnModPlan.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnModPlan.setBounds(505, 85, 167, 40);
        add(btnModPlan);
        
        JButton btnBajaPlan = new JButton("Dar de baja plan");
        btnBajaPlan.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaElimPlan();
        	}
        });
        btnBajaPlan.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnBajaPlan.setBounds(682, 85, 167, 40);
        add(btnBajaPlan);
        
        JButton btnAtras = new JButton("Atr\u00E1s");
        btnAtras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setVisible(false);
        		VistaAdmin.vista().setVisible(true);
        	}
        });
        btnAtras.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnAtras.setBounds(10, 11, 70, 23);
        add(btnAtras);
                
        actualizarListaPlanes();
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
	
	
	private void actualizarListaPlanes () {
		ConectorBD.obtenerConectorBD().conectarBD(tabla);
		try
	    {
			String consultaPlanes = "SELECT * FROM planes ";
			tabla.setSelectSql(consultaPlanes.trim());
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
	        		 					   "ERROR! No se pudo cargar la lista de planes",
	                                       JOptionPane.ERROR_MESSAGE);
	    }
		ConectorBD.obtenerConectorBD().desconectarBD(tabla);
	}
	
	
	
	// CLASE PARA LA VENTANA DE INPUTS PARA EL REGISTRO
	
	public class VentanaRegPlan extends JFrame {
		
		private static final long serialVersionUID = 1L;	
		private JTextField inputNombre;
		private JTextField inputVersion;
		private JLabel lblNombre;
		private JLabel lblVersion;
		private JButton btnSiguiente;
		
		
		public VentanaRegPlan() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Registro de nueva materia");
			setMaximumSize(new Dimension(395, 245));
			setMinimumSize(new Dimension(395, 245));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS
			
			inputNombre = new JTextField();
			inputNombre.setColumns(10);
			inputNombre.setBounds(159, 55, 178, 20);
			getContentPane().add(inputNombre);
			
			inputVersion = new JTextField();
			inputVersion.setBounds(159, 89, 48, 20);
			getContentPane().add(inputVersion);
			
			lblNombre = new JLabel("Carrera asociada:");
			lblNombre.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 12));
			lblNombre.setBounds(51, 54, 98, 20);
			getContentPane().add(lblNombre);
			
			lblVersion = new JLabel("Version:");
			lblVersion.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 12));
			lblVersion.setBounds(67, 88, 82, 20);
			getContentPane().add(lblVersion);
			
			btnSiguiente = new JButton("Guardar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					registrarPlan();
					dispose();
				}
			});
			btnSiguiente.setBounds(132, 153, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
		
		
		private void registrarPlan ()
		{
			ConectorBD.obtenerConectorBD().conectarBD();
			try
		      {
		         // Creo un comando JDBC para realizar la inserción en la BD
		         Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
		         	         
		         // Genero la sentencia de inserción	         
		         String sql = "INSERT INTO planes (nombre_carrera, version) VALUES (" + 
		        		 	  "\'" + inputNombre.getText() +
		        		 	  "\'," + inputVersion.getText() + ");";

		         // Ejecuto la inserción del nuevo alumno
		         stmt.executeUpdate(sql);
		         // Notifico éxito en la operación
		         JOptionPane.showMessageDialog(this,"Plan registrado exitosamente");	         
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
		}
		
	}
	
	
	private class VentanaBuscarPlan extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JTextField inputNombre;
		private JTextField inputVersion;
		private JButton btnSiguiente;
		;
		
		
		// CONSTRUCTOR: Ventana para el registro de un nuevo plan
		
		public VentanaBuscarPlan() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Modificación de un plan");
			setSize(new Dimension(400, 177));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS
			
			inputNombre = new JTextField();
			inputNombre.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputNombre.setBounds(159, 29, 188, 20);
			getContentPane().add(inputNombre);
			
			// CREACIÓN DE LABELS
			
			JLabel lblLU = new JLabel("Nombre de carrera:");		
			lblLU.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblLU.setBounds(41, 29, 108, 20);
			getContentPane().add(lblLU);
			
			btnSiguiente = new JButton("A modificar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {						
					abrirEdicionCarrera();
					dispose();
				}
			});
			btnSiguiente.setBounds(133, 104, 124, 23);
			getContentPane().add(btnSiguiente);
			
			inputVersion = new JTextField();
			inputVersion.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputVersion.setBounds(159, 60, 75, 20);
			getContentPane().add(inputVersion);
			
			JLabel lblVersion = new JLabel("Versi\u00F3n:");
			lblVersion.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblVersion.setBounds(104, 60, 45, 20);
			getContentPane().add(lblVersion);
			
		}
		
		
		   private void abrirEdicionCarrera ()
		   {
			  ConectorBD.obtenerConectorBD().conectarBD();
		      try
		      {
		         // Creo un comando JDBC para realizar la inserción en la BD
		         Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
		         // Genero la sentencia de inserción	         
		         String sql = "SELECT * FROM planes WHERE (nombre_carrera = " + inputNombre.getText() + ")";

		         // Ejecuto la eliminación del alumno
		         ResultSet rs = stmt.executeQuery(sql);
		         if (!rs.next()) {
		        	 JOptionPane.showMessageDialog(this,
		        			 "ERROR! No existe el plan \'" + inputVersion.getText() + "\'" + 
		        			 " para la carrera: " + inputNombre.getText(),
		        			 "Modificación de un plan", JOptionPane.ERROR_MESSAGE);
		         }
		         else {
		        	 new VentanaEdicionPlan(inputNombre.getText());			        	 
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
		      actualizarListaPlanes();
		   }
		   
		   
		   private class VentanaEdicionPlan extends JFrame {
			   
				private static final long serialVersionUID = 1L;
				private JTextField [] inputs;
				private JCheckBox [] checkBoxes;
				private JButton btnSiguiente;
				
				
				// CONSTRUCTOR: Ventana para el registro de un nuevo alumno
				
				public VentanaEdicionPlan(String nombreCarrera) {
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
							modificarPlan(nombreCarrera);
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
				   
				   
				   private void modificarPlan (String nombreCarrera)
				   {
					  ConectorBD.obtenerConectorBD().conectarBD();
				      try
				      {
				    	  // Creo un comando JDBC para realizar la inserción en la BD
				    	  Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
				    	  int cantInputs = inputsHabilitados();
				    	  // Genero la sentencia de inserción
				    	  String sql = "UPDATE planes SET ";
				    	  // Si quiero modificar el DNI...
				    	  if (inputs[0].isEnabled()) {
				    		  sql += "nombre_carrera = \'" + inputs[0].getText() + "\'";
				    		  cantInputs--;
				    		  if (cantInputs > 0) {
				    			  sql += ", ";
				    		  }
				    	  }
				    	// Si quiero modificar el Nombre...
				    	  if (inputs[1].isEnabled()) {
				    		  sql += "version = " + inputs[1].getText() + " ";
				    		  cantInputs--;
				    	  }
				    	  
				    	  sql += " WHERE (nombre = " + nombreCarrera + ");";

				         // Ejecuto la inserción del nuevo alumno
				         stmt.executeUpdate(sql);
				         JOptionPane.showMessageDialog(this,"Carrera con ID: " + nombreCarrera +" modificada exitosamente");
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
				      actualizarListaPlanes();
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
	
		public class VentanaElimPlan extends JFrame {
			
			private static final long serialVersionUID = 1L;
			private JTextField inputNombre;
			private JTextField inputNum;
			private JButton btnSiguiente;
			
			
			// CONSTRUCTOR: Ventana para el registro de un nuevo alumno
			
			public VentanaElimPlan() {
				super();
				getContentPane().setLayout(null);
		        setVisible(true);
		        
		        getContentPane().setBackground(SystemColor.controlHighlight);
				setTitle("Baja de una materia");
				setSize(new Dimension(374, 197));
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
				
				inputNum = new JTextField();
				inputNum.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputNum.setBounds(163, 72, 63, 20);
				getContentPane().add(inputNum);
				
				JLabel lblNum = new JLabel("Version:");
				lblNum.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblNum.setBounds(82, 72, 69, 20);
				getContentPane().add(lblNum);
				
				btnSiguiente = new JButton("Dar de baja");
				btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
				btnSiguiente.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {				
						eliminarPlan();
						dispose();
					}
				});
				btnSiguiente.setBounds(124, 119, 124, 23);
				getContentPane().add(btnSiguiente);
				
			}
			
			
			// CONEXIÓN Y DESCONEXIÓN DE LA BASE DE DATOS	   
			   
			   private void eliminarPlan ()
			   {
				  ConectorBD.obtenerConectorBD().conectarBD();
			      try
			      {
			    	  // Creo un comando JDBC para realizar la inserción en la BD
			    	  Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
			    	  // Genero la sentencia de inserción
			    	  String sql = "DELETE FROM planes WHERE (nombre_carrera = \'" + inputNombre.getText() +
			    			  	   "\' and version = " + inputNum.getText() + ")";
			    	  // Ejecuto la eliminación del alumno
			    	  stmt.executeUpdate(sql);
			    	  JOptionPane.showMessageDialog(this,"Plan dado de baja exitosamente");
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
			      actualizarListaPlanes();
			   }
		}

}
