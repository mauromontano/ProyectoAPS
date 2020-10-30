package Vistas;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import Conector.ConectorBD;

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
import quick.dbtable.DBTable;


public class VistaMaterias extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static VistaMaterias instancia = null;
	private DBTable tabla;
	private JTextField txtNombre;
	protected int seleccionado = -1;
	
	
	public static VistaMaterias obtenerVistaMaterias () {
		if (instancia == null) {
			instancia = new VistaMaterias();
		}
		return instancia;
	}
	
	// CONSTRUCTOR: Vista de materias	
	private VistaMaterias() {
		
		this.setBackground(SystemColor.control);
		this.setBounds(0, 0, 1194, 699);
		this.setLayout(null);
		
		JPanel panelTabla = new JPanel();
		panelTabla.setBackground(SystemColor.controlHighlight);
		panelTabla.setBounds(10, 232, 1174, 456);
		this.add(panelTabla);
		
		JButton btnRegMateria = new JButton("Registrar materia");
		btnRegMateria.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 13));
		btnRegMateria.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaRegMateria();
			}
		});
		btnRegMateria.setBounds(328, 84, 167, 40);
		this.add(btnRegMateria);
		
		tabla = new DBTable();
		tabla.setBounds(396, 5, 379, 427);
        
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
        
        panelTabla.add(tabla);           
        tabla.setEditable(false);
        
        JButton btnModMateria = new JButton("Modificar materia");
        btnModMateria.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaBuscarMateria();
        	}
        });
        btnModMateria.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnModMateria.setBounds(505, 85, 167, 40);
        add(btnModMateria);
        
        JButton btnBajaMateria = new JButton("Dar de baja materia");
        btnBajaMateria.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaElimMateria();
        	}
        });
        btnBajaMateria.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnBajaMateria.setBounds(682, 85, 167, 40);
        add(btnBajaMateria);
        
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
        
        this.conectarBD();        
        actualizarListaMaterias();
        this.desconectarBD();
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
	
	
	private void conectarBD()
	   {
	         try
	         {
	            String driver ="com.mysql.cj.jdbc.Driver";
	        	String servidor = "localhost:3306";
	        	String baseDatos = "universidad"; 
	        	String usuario = "admin_uni";
	        	String clave = "pwadmin";
	            String uriConexion = "jdbc:mysql://" + servidor + "/" + 
	        	                     baseDatos +"?serverTimezone=America/Argentina/Buenos_Aires";
	   
	            tabla.connectDatabase(driver, uriConexion, usuario, clave);
	           
	         }
	         catch (SQLException ex)
	         {
	            JOptionPane.showMessageDialog(this,
	                           "Se produjo un error al intentar conectarse a la base de datos.\n" 
	                            + ex.getMessage(),
	                            "Error",
	                            JOptionPane.ERROR_MESSAGE);
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
	         }
	         catch (ClassNotFoundException e)
	         {
	            e.printStackTrace();
	         }
	      
	   }

	   private void desconectarBD()
	   {
		   try
		   {
			   tabla.close();            
	       }
	       catch (SQLException ex)
	       {
	    	   System.out.println("SQLException: " + ex.getMessage());
	           System.out.println("SQLState: " + ex.getSQLState());
	           System.out.println("VendorError: " + ex.getErrorCode());
	       }      
	   }

	
	   private void actualizarListaMaterias () {
			ConectorBD.obtenerConectorBD().conectarBD(tabla);
			try
		    {
				String consultaMaterias = "SELECT * FROM materias ";
				tabla.setSelectSql(consultaMaterias.trim());
				// Obtengo el modelo de la DB Table para actualizar el contenido de la lista
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
		        		 					   "ERROR! No se pudo cargar la lista de materias",
		                                       JOptionPane.ERROR_MESSAGE);
		    }
			ConectorBD.obtenerConectorBD().desconectarBD(tabla);
		}
	
	
	// CLASE PARA LA VENTANA DE INPUTS PARA EL REGISTRO
	
	public class VentanaRegMateria extends JFrame {
		
		private static final long serialVersionUID = 1L;	
		private JTextField inputNombre;
		private JTextField inputCarga;
		private JLabel lblNombre;
		private JLabel lblCarga;
		private JButton btnSiguiente;
		
		
		public VentanaRegMateria() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Registro de nueva materia");
			setMaximumSize(new Dimension(322, 245));
			setMinimumSize(new Dimension(322, 245));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS
			
			inputNombre = new JTextField();
			inputNombre.setColumns(10);
			inputNombre.setBounds(116, 55, 149, 20);
			getContentPane().add(inputNombre);
			
			inputCarga = new JTextField();
			inputCarga.setBounds(116, 89, 40, 20);
			getContentPane().add(inputCarga);
			
			lblNombre = new JLabel("Nombre:");
			lblNombre.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 12));
			lblNombre.setBounds(51, 54, 55, 20);
			getContentPane().add(lblNombre);
			
			lblCarga = new JLabel("Carga horaria:");
			lblCarga.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 12));
			lblCarga.setBounds(25, 88, 79, 20);
			getContentPane().add(lblCarga);
			
			btnSiguiente = new JButton("Guardar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					conectarBD();
					registrarMateria();
					desconectarBD();
					dispose();
				}
			});
			btnSiguiente.setBounds(94, 153, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}

		   

		private void registrarMateria ()
		{
			ConectorBD.obtenerConectorBD().conectarBD();
			try
			{
				// Creo un comando JDBC para realizar la inserción en la BD
				Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
		        // Genero la sentencia de inserción
		        String sql = "INSERT INTO materias (nombre, carga_horaria) VALUES (" + 
		        		 	  "\'" + inputNombre.getText() +
		        		 	  "\'," + inputCarga.getText() + ");";
		        // Ejecuto la inserción de la nueva materia
		        stmt.executeUpdate(sql);
		        // Notifico éxito en la operación
		        JOptionPane.showMessageDialog(this,"Materia registrada exitosamente");
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
			actualizarListaMaterias();
		}
		
	}
	
	
	// CLASE DE VENTANA DE BÚSQUEDA DE UNA MATERIA PARA SU MODIFICACIÓN	
	
	private class VentanaBuscarMateria extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JTextField inputId;
		private JButton btnSiguiente;
		
		
		// CONSTRUCTOR: Ventana para la modificación de una materia
		
		public VentanaBuscarMateria() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Modificación de una materia");
			setSize(new Dimension(353, 170));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS
			
			inputId = new JTextField();
			inputId.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputId.setBounds(128, 41, 100, 20);
			getContentPane().add(inputId);
			
			// CREACIÓN DE LABELS
			
			JLabel lblLU = new JLabel("ID:");		
			lblLU.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblLU.setBounds(89, 41, 29, 20);
			getContentPane().add(lblLU);
			
			btnSiguiente = new JButton("A modificar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {						
					abrirEdicionMateria();
					dispose();
				}
			});
			btnSiguiente.setBounds(115, 95, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
		
		   
		   private void abrirEdicionMateria()
		   {
			  ConectorBD.obtenerConectorBD().conectarBD();
		      try
		      {
		         // Creo un comando JDBC para realizar la inserción en la BD
		         Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
		         // Genero la sentencia de inserción	         
		         String sql = "SELECT * FROM materias WHERE (id = " + inputId.getText() + ")";

		         // Ejecuto la eliminación de la materia
		         ResultSet rs = stmt.executeQuery(sql);
		         if (!rs.next()) {
		        	 JOptionPane.showMessageDialog(this,
		        			 "ERROR! No existe un materia registrada con ID: " + inputId.getText(),
		        			 "Modificación de una materia", JOptionPane.ERROR_MESSAGE);
		         }
		         else {
		        	 new VentanaEdicionMateria(Integer.parseInt(inputId.getText()));			        	 
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
		      actualizarListaMaterias();  
		   }
		   
		   
		   private class VentanaEdicionMateria extends JFrame {
				private static final long serialVersionUID = 1L;
				private JTextField [] inputs;
				private JCheckBox [] checkBoxes;
				private JButton btnSiguiente;
				
				
				// CONSTRUCTOR: Ventana para la modificación de una materia
				
				public VentanaEdicionMateria(int id) {
					super();
					getContentPane().setEnabled(false);
					getContentPane().setLayout(null);
			        setVisible(true);
			        
			        getContentPane().setBackground(SystemColor.controlHighlight);
					setTitle("Modificación de una materia");
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
					lblNombre.setBounds(53, 24, 62, 20);
					getContentPane().add(lblNombre);
					
					JLabel lblCarga = new JLabel("Carga horaria:");		
					lblCarga.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					lblCarga.setBounds(28, 55, 87, 20);
					getContentPane().add(lblCarga);
					
					
					btnSiguiente = new JButton("Guardar");
					btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
					btnSiguiente.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {				
							modificarMateria(id);
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
				   
				   
				   private void modificarMateria (int id)
				   {
					  ConectorBD.obtenerConectorBD().conectarBD();
				      try
				      {
				    	  // Creo un comando JDBC para realizar la inserción en la BD
				    	  Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
				    	  int cantInputs = inputsHabilitados();
				    	  // Genero la sentencia de inserción
				    	  String sql = "UPDATE materias SET ";
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
				    		  sql += "carga_horaria = " + inputs[1].getText() + " ";
				    		  cantInputs--;
				    	  }
				    	  
				    	  sql += " WHERE (id = " + id + ");";

				         // Ejecuto la inserción de una nueva materia
				         stmt.executeUpdate(sql);
				         JOptionPane.showMessageDialog(this,"Materia con ID: " + id +" modificada exitosamente");
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
				      actualizarListaMaterias();
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
	
	public class VentanaElimMateria extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JTextField inputId;
		private JButton btnSiguiente;
		
		
		// CONSTRUCTOR: Ventana para eliminar una materia
		
		public VentanaElimMateria() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Baja de una materia");
			setSize(new Dimension(289, 170));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS
			
			inputId = new JTextField();
			inputId.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputId.setBounds(92, 41, 111, 20);
			getContentPane().add(inputId);
			
			// CREACIÓN DE LABELS
			
			JLabel lblId = new JLabel("ID:");		
			lblId.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblId.setBounds(64, 41, 29, 20);
			getContentPane().add(lblId);
			
			btnSiguiente = new JButton("Dar de baja");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {				
					eliminarMateria();
					dispose();
				}
			});
			btnSiguiente.setBounds(79, 82, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}   
		   
		   private void eliminarMateria ()
		   {
			  ConectorBD.obtenerConectorBD().conectarBD();
		      try
		      {
		    	  // Creo un comando JDBC para realizar la inserción en la BD
		    	  Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
		    	  // Genero la sentencia de inserción
		    	  String sql = "DELETE FROM materias WHERE ( id = " + inputId.getText() + ")";
		    	  // Ejecuto la eliminación de una materia
		    	  stmt.executeUpdate(sql);
		    	  JOptionPane.showMessageDialog(this,"Materia dada de baja exitosamente");
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
		      actualizarListaMaterias();
		   }
		   
	}

}
