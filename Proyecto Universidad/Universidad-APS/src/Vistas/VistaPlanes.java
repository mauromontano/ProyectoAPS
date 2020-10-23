package Vistas;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import quick.dbtable.DBTable;


public class VistaPlanes extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static VistaPlanes instancia = null;
	private DBTable tabla;
	private JTextField txtNombre;
	protected int seleccionado = -1;
	
	
	public static VistaPlanes obtenerVistaPlanes () {
		if (instancia == null) {
			instancia = new VistaPlanes();
		}
		return instancia;
	}
	
	// CONSTRUCTOR: Vista de alumnos	
	private VistaPlanes() {
		
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
        	}
        });
        btnModPlan.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnModPlan.setBounds(505, 85, 167, 40);
        add(btnModPlan);
        
        JButton btnBajaPlan = new JButton("Dar de baja plan");
        btnBajaPlan.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnBajaPlan.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnBajaPlan.setBounds(682, 85, 167, 40);
        add(btnBajaPlan);
        
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
        cargarMaterias();
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

	
	private void cargarMaterias () {
		try
	    {
			String consultaMaterias = "SELECT * FROM planes ";
			tabla.setSelectSql(consultaMaterias.trim());
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
	
	public class VentanaRegPlan extends JFrame {
		
		private static final long serialVersionUID = 1L;	
		private JTextField inputNombre;
		private JTextField inputVersion;
		private JLabel lblNombre;
		private JLabel lblVersion;
		private JButton btnSiguiente;
		
		private Connection conexionBD = null;
		
		
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
			
			lblVersion = new JLabel("Carga horaria:");
			lblVersion.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 12));
			lblVersion.setBounds(67, 88, 82, 20);
			getContentPane().add(lblVersion);
			
			btnSiguiente = new JButton("Guardar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					conectarBD();
					registrarPlan();
					desconectarBD();
					dispose();
				}
			});
			btnSiguiente.setBounds(132, 153, 124, 23);
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
		   
		   
		   private void registrarPlan ()
		   {
		      try
		      {
		         // Creo un comando JDBC para realizar la inserción en la BD
		         Statement stmt = this.conexionBD.createStatement();
		         	         
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
		   }
	}

}
