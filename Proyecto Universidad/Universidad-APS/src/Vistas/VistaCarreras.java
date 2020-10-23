package Vistas;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
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
        
    	// Agregar la tabla al frame (no necesita JScrollPane como Jtable)
        panelTabla.add(tabla);           
        tabla.setEditable(false);
        
        JButton btnModCarrera = new JButton("Modificar carrera");
        btnModCarrera.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnModCarrera.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnModCarrera.setBounds(505, 85, 167, 40);
        add(btnModCarrera);
        
        JButton btnBajaCarrera = new JButton("Dar de baja carrera");
        btnBajaCarrera.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
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
        
        this.conectarBD();        
        cargarCarreras();
        this.desconectarBD();
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
	   
	            //establece una conexión con la  B.D. "batallas"  usando directamante una tabla DBTable    
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

	
	private void cargarCarreras () {
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
	        		 					   "ERROR! No se pudo cargar la lista de alumnos",
	                                       JOptionPane.ERROR_MESSAGE);
	      }
	}
	
	
	
	// CLASE PARA LA VENTANA DE INPUTS PARA EL REGISTRO
	
	public class VentanaRegCarrera extends JFrame {
		
		private static final long serialVersionUID = 1L;	
		private JTextField inputNombre;
		private JTextField inputDuracion;
		private JLabel lblNombre;
		private JLabel lblDuracion;
		private JButton btnSiguiente;
		
		private Connection conexionBD = null;
		
		
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
					conectarBD();
					registrarCarrera();
					desconectarBD();
					dispose();
				}
			});
			btnSiguiente.setBounds(94, 153, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
		
		
		// CONEXIÓN Y DESCONEXIÓN DE LA BASE DE DATOS
		
		
		private void conectarBD()
		   {
		      if (this.conexionBD == null)
		      {             
		         try
		         {  //se genera el string que define los datos de la conexión 
		            String servidor = "localhost:3306";
		            String baseDatos = "universidad";
		            String usuario = "admin_uni";
		            String clave = "pwadmin";
		            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos + 
	      		          "?serverTimezone=America/Argentina/Buenos_Aires";
		            //se intenta establecer la conexión
		            this.conexionBD = DriverManager.getConnection(uriConexion,usuario,clave);
		         }
		         catch (SQLException ex)
		         {
		            JOptionPane.showMessageDialog(this,
		                        "Se produjo un error al intentar conectarse a la base de datos.\n" + 
		                         ex.getMessage(),
		                         "Error",
		                         JOptionPane.ERROR_MESSAGE);
		            System.out.println("SQLException: " + ex.getMessage());
		            System.out.println("SQLState: " + ex.getSQLState());
		            System.out.println("VendorError: " + ex.getErrorCode());
		         }
		      }
		   }
		

		   private void desconectarBD()
		   {
		      if (this.conexionBD != null)
		      {
		         try
		         {
		            this.conexionBD.close();
		            this.conexionBD = null;
		         }
		         catch (SQLException ex)
		         {
		            System.out.println("SQLException: " + ex.getMessage());
		            System.out.println("SQLState: " + ex.getSQLState());
		            System.out.println("VendorError: " + ex.getErrorCode());
		         }
		      }
		   }
		   
		   
		   private void registrarCarrera ()
		   {
		      try
		      {
		         // Creo un comando JDBC para realizar la inserción en la BD
		         Statement stmt = this.conexionBD.createStatement();
		         	         
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
		   }
	}

}
