package Conector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.Statement;
import quick.dbtable.DBTable;

public class DriverBD {
	
	private static DriverBD instancia = null;
	private Statement stmtActual = null;
	private Connection conexionActual = null;
	
	
	public static DriverBD driver () {
		if (instancia == null) {
			instancia = new DriverBD();
		}
		return instancia;
	}
	
	
	/**
	 * actualizar: realiza una invocación de una sentencia sql para la actualización
	   de información en la BD.
	 * @param sql: cadena de caracteres correspondiente a la consulta a realizar.
	 */
	public void actualizar (String sql) throws SQLException {
		// Ejecuto la consulta SQL para realizar una actualización en la BD
		stmtActual.executeUpdate(sql);
	}
	
	
	/**
	 * consultar: realiza una invocación de una sentencia sql para la consulta
	   de información de la BD.
	 * @param sql: cadena de caracteres correspondiente a la consulta a realizar.
	 * @return objeto ResultSet que contiene el resultado de la consulta a efectuar.
	 */
	public ResultSet consultar (String sql) throws SQLException {
		ResultSet rs = null;
		rs = stmtActual.executeQuery(sql);
		return rs;
	}
	
	
	public void volcar (DBTable tb, String sql) throws SQLException {
		conectarDBTable(tb);
		tb.setSelectSql(sql.trim());
		// Obtengo el modelo de la DB Table para actualizar el contenido de la tabla
		tb.createColumnModelFromQuery();
    	// Actualizo el contenido de la tabla   	     	  
		tb.refresh();
		desconectarDBTable(tb);
	}
	
	
	/**
	 * nuevaConexionDB: crea una nueva conexión a la BD del sistema para realizar consultas
	   o actualizaciones.
	 * @return objeto de conexión a la BD del sistema.
	 */
	public void nuevaConexion () {
		String servidor = "localhost:3306";
        String baseDatos = "universidad";
		String usuario = "admin_uni";
        String clave = "pwadmin";
        String uriConexion;
        
		// Si la conexión a la base de datos NO existe, la creo
		try 
		{
			uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos + "?serverTimezone=America/Argentina/Buenos_Aires";
			// Procedo a establecer la conexión a la BD indicada, con el usuario especificado
			conexionActual = DriverManager.getConnection(uriConexion,usuario,clave);
			stmtActual = conexionActual.createStatement();
		}
		catch (SQLException ex)
		{
			JOptionPane.showMessageDialog(null, 
					"Se produjo un error al intentar conectarse a la base de datos " + baseDatos + ".\n",
					"ERROR: " + ex.getMessage(),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	/**
	 * cerrarConexion: realiza el cierre de una conexión cuyo objeto correspondiente es
	   pasado como parámetro.
	 * @param cn: objeto de conexión correspondiente a la conexión a la BD por cerrar.
	 */
	public void cerrarConexion () {
		if (conexionActual != null)
		{
			try
			{
				stmtActual.close();
				conexionActual.close();
				stmtActual = null;
				conexionActual = null;
			}
			catch (SQLException ex)
			{
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}
		}
	}
	

	private void conectarDBTable (DBTable tabla)
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
	   

	private void desconectarDBTable (DBTable tabla)
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

	
}
