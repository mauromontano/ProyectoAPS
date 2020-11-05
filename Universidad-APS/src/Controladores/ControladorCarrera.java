package Controladores;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import Conector.ConectorBD;
import Modelos.Carrera;

public class ControladorCarrera {
	
	private static ControladorCarrera instancia = null;
	
	/**
	 * controlador: retorna la instancia asociada al controlador
	 * @return controlador asociado a carreras
	 */
	public static ControladorCarrera controlador () {
		if (instancia == null) {
			instancia = new ControladorCarrera();
		}
		return instancia;
	}
	
	/**
	 * obtenerCarreras: permite obtener una lista de modelos de carreras, asociados a
	   las carreras registradas en la base de datos
	 * @return lista de modelos de carreras
	 */
	public List<Carrera> carreras () {
		List<Carrera> listaCarreras = new LinkedList<Carrera>();
		Statement stmt;
		ResultSet rs;
		Carrera carrera;
		String sql;
		ConectorBD.obtenerConectorBD().conectarBD();
	      try
	      {
	         // Creo un comando JDBC para realizar la inserción en la BD
	         stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
	         // Genero la sentencia de inserción
	         sql = "SELECT * FROM carreras;";
	         carrera = null;
	         // Ejecuto la eliminación del alumno
	         rs = stmt.executeQuery(sql);
	         while (rs.next()) {
	        	 carrera = Carrera.siguienteModelo(rs);
	        	 listaCarreras.add(carrera);	        	 
	         }
	         stmt.close();         
	      }
	      catch (SQLException ex)
	      {
	         // en caso de error, se muestra la causa en la consola
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	      }	      
	      ConectorBD.obtenerConectorBD().desconectarBD();
	      
	      return listaCarreras;
	}
	
	/**
	 * carrerasInscripto: permite obtener la lista de modelos de carreras asociadas a
	   las carreras en las cuales un dado alumno, con cierto LU, está inscripto
	 * @param lu: número de libreta universitaria asociado al alumno
	 * @return lista de modelos de carreras en las cuales está inscripto el alumno
	 */
	public List<Carrera> carrerasInscripto (int lu) {
		List<Carrera> listaCarreras = new LinkedList<Carrera>();
		Statement stmt;
		ResultSet rs;
		Carrera carrera;
		String sql;
		ConectorBD.obtenerConectorBD().conectarBD();
	      try
	      {
	         // Creo un comando JDBC para realizar la inserción en la BD
	         stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
	         // Genero la sentencia de inserción 
	         sql = "SELECT id_carrera, nombre, duracion " + 
		         		"FROM (" +
		         			"SELECT lu_alumno, id_carrera " + 
		         				"FROM (" +
		         						"SELECT lu_alumno, id_plan " + 
		         							"FROM inscripciones_carreras " + 
		         							"WHERE (lu_alumno = " + lu + ") " + 
		         					") " + 
		         					"AS alumno_planes " + 
		         					"NATURAL JOIN " + 
		         					"planes " +
		         		
		         					"WHERE (id_plan = id) " +
		         			") AS alumno_carreras " + 
		         			"NATURAL JOIN " + 
		         			"carreras " +
		         		"WHERE (id = id_carrera);";
	         carrera = null;
	         // Ejecuto la eliminación del alumno
	         rs = stmt.executeQuery(sql);
	         while (rs.next()) {
	        	 carrera = Carrera.siguienteModelo(rs);
	        	 listaCarreras.add(carrera);	        	 
	         }
	         stmt.close();         
	      }
	      catch (SQLException ex)
	      {
	         // en caso de error, se muestra la causa en la consola
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	      }	      
	      ConectorBD.obtenerConectorBD().desconectarBD();
	      
	      return listaCarreras;
	}
	

}
