package Conector;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.Statement;
import quick.dbtable.DBTable;

public class ConectorBD {
	
	private static ConectorBD instancia = null;
	private Connection conexionActualBD = null;
	
	
	public static ConectorBD obtenerConectorBD () {
		if (instancia == null) {
			instancia = new ConectorBD();
		}
		return instancia;
	}
	
	
	public void conectarBD ()
	{
		String servidor = "localhost:3306";
        String baseDatos = "universidad";
		String usuario = "admin_uni";
        String clave = "pwadmin";
        String uriConexion;
        
		// Si la conexión a la base de datos NO existe, la creo
		if (conexionActualBD == null) {			
			try {
	            uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos + "?serverTimezone=America/Argentina/Buenos_Aires";
	            // Procedo a establecer la conexión a la BD indicada, con el usuario especificado
	            conexionActualBD = DriverManager.getConnection(uriConexion,usuario,clave);
	         }
	         catch (SQLException ex)
	         {
	            JOptionPane.showMessageDialog(null,
	                        "Se produjo un error al intentar conectarse a la base de datos " + baseDatos + ".\n", 
	                         "ERROR: " + ex.getMessage(),
	                         JOptionPane.ERROR_MESSAGE);
	         }
	      }
	}
	

	public void conectarBD (DBTable tabla)
	{
		String driver ="com.mysql.cj.jdbc.Driver";
		String servidor = "localhost:3306";
		String baseDatos = "universidad";
		String usuario = "admin_uni";
        String clave = "pwadmin";
        String uriConexion;
		try
		{
			uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos +"?serverTimezone=America/Argentina/Buenos_Aires";
			// Procedo a establecer la conexión a la BD indicada, con el usuario especificado, empleando una DBTable
			tabla.connectDatabase(driver, uriConexion, usuario, clave);
		}
		catch (SQLException ex)
		{
			JOptionPane.showMessageDialog(null,
                    "Se produjo un error al intentar conectarse a la base de datos " + baseDatos + ".\n", 
                     "ERROR: " + ex.getMessage(),
                     JOptionPane.ERROR_MESSAGE);
		}
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
	
	}
	
	
	public void desconectarBD ()
	{
		if (conexionActualBD != null)
		{
			try
			{
				conexionActualBD.close();
				conexionActualBD = null;
	         }
	         catch (SQLException ex)
	         {
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
	         }
	     }
	}
	   

	public void desconectarBD (DBTable tabla)
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
	
	
	public Statement nuevoStatement () throws SQLException {
		return conexionActualBD.createStatement();
	}
	
}
