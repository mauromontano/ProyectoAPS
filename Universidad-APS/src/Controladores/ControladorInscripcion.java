package Controladores;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import Conector.ConectorBD;
import Excepciones.ExcepcionRegistro;
import Modelos.Carrera;
import Modelos.Plan;

public class ControladorInscripcion {
	
	private static ControladorInscripcion instancia = null;
	
	/**
	 * controlador: retorna la instancia asociada al controlador
	 * @return controlador asociado a inscripciones
	 */
	public static ControladorInscripcion controlador () {
		if (instancia == null) {
			instancia = new ControladorInscripcion();
		}
		return instancia;
	}
	
	
	/**
	 * carrerasInscripto: retorna la lista de modelos de carreras en las cuales
	   un alumno con LU dado está inscripto
	 * @param lu: número de libreta universitaria asociado al alumno
	 * @return lista de modelos de carreras en las cuales el alumno está inscripto
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
	
	
	/**
	 * registrarInsCarrera: realiza el registro de una inscripción de un alumno,
	   con LU particular, en una carrera registrada, con id asociado
	 * @param lu: número de libreta universitaria asociado al alumno
	 * @param id: identificador asociado a la carrera involucrada en la inscripción 
	 */
	public void registrarInsCarrera (int lu, int id) {
		Statement stmt;
		String sql;
		ConectorBD.obtenerConectorBD().conectarBD();
		try
		{	
			stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
			sql = "SELECT * FROM planes WHERE (id_carrera = " + id + ") ORDER BY id desc LIMIT 1;";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			Plan plan = Plan.siguienteModelo(rs);			
			stmt.close();
			// Creo un comando JDBC para realizar la inserción en la BD
			stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
			// Genero la sentencia de inserción
			sql = "INSERT INTO inscripciones_carreras (LU_alumno, id_plan, fecha) VALUES (" +
					lu + "," +
					plan.obtenerId() + "," +
					"curdate());";
			// Ejecuto la inserción del nuevo alumno
			stmt.executeUpdate(sql);
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
	}

	
	/**
	 * registrarInsMateria: realiza el registro de una inscripción de un alumno,
	   con LU particular, en una materia registrada, parte de su plan de carrera,
	   con id asociado
	 * @param lu: número de libreta universitaria asociado al alumno
	 * @param id: identificador asociado a la materia involucrada en la inscripción 
	 */
	public void registrarInsMateria (int lu, int idMateria) throws ExcepcionRegistro {
		Statement stmt;
		ResultSet rs;
		String sql;
		ConectorBD.obtenerConectorBD().conectarBD();
		boolean registroInvalidado = false;
		try
		{
			// Creo un comando JDBC para realizar la inserción en la BD
			stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
			sql = "SELECT * from inscripciones_materias WHERE (LU_alumno = " + lu + " AND id_materia = " + idMateria + ");";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				registroInvalidado = true;
			}
			else 
			{
				// Genero la sentencia de inserción
				sql = "INSERT INTO inscripciones_materias (LU_alumno, id_materia, fecha) VALUES (" +
						lu + "," +
						idMateria + "," +
						"curdate());";
				// Ejecuto la inserción del nuevo alumno
				stmt.executeUpdate(sql);
				stmt.close();
			}			
		}
		catch (SQLException ex)
		{
			registroInvalidado = true;
			// en caso de error, se muestra la causa en la consola
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		ConectorBD.obtenerConectorBD().desconectarBD();
		
		// Si el registro quedó invalidado, entonces la inscripción ya existía.
		if (registroInvalidado) {
			throw new ExcepcionRegistro("Registro de inscripción a materia fallido: " + 
					"usted ya está inscripto a la materia con ID: " + idMateria);
		}
		
	}

}
