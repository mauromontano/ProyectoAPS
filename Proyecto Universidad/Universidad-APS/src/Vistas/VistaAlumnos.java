package Vistas;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
        	}
        });
        btnModAlumno.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnModAlumno.setBounds(505, 85, 167, 40);
        add(btnModAlumno);
        
        JButton btnBajaAlumno = new JButton("Dar de baja alumno");
        btnBajaAlumno.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
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
        
        this.conectarBD();        
        cargarAlumnos();
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

	
	private void cargarAlumnos () {
		try
	    {
			String consultaAlumnos = "SELECT * FROM alumnos ";
			tabla.setSelectSql(consultaAlumnos.trim());
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
	
	public class VentanaRegAlumno extends JFrame {
		
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
		
		private Connection conexionBD = null;
		
		
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
				
			// CREACIÓN DE INPUTS
			
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
			
			// CREACIÓN DE LABELS
			
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
					conectarBD();
					registrarAlumno();
					desconectarBD();
					dispose();
				}
			});
			btnSiguiente.setBounds(95, 354, 124, 23);
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
		   
		   
		   private void registrarAlumno ()
		   {
		      try
		      {
		         // Creo un comando JDBC para realizar la inserción en la BD
		         Statement stmt = this.conexionBD.createStatement();
		         	         
		         // Genero la sentencia de inserción	         
		         String sql = "INSERT INTO alumnos (dni, nombre, apellido, genero) VALUES (" + 
		        		 	  inputDNI.getText() + 
		        		 	  ",\'" + inputNombre.getText() +
		        		 	  "\',\'" + inputApellido.getText() + 
		        		 	  "\',\'" + inputGenero.getSelectedItem() + "\');";

		         // Ejecuto la inserción del nuevo alumno
		         stmt.executeUpdate(sql);
		         // Notifico éxito en la operación
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
		   }
	}
}
