package Controladores;

import java.sql.SQLException;
import java.sql.Statement;
import Conector.ConectorBD;

public class ControladorPlan {
	
	private static ControladorPlan instancia = null;
	
	/**
	 * controlador: retorna la instancia asociada al controlador
	 * @return controlador asociado a planes de carrera
	 */
	public static ControladorPlan controlador () {
		if (instancia == null) {
			instancia = new ControladorPlan();
		}
		return instancia;
	}
	
	
	/**
	 * agregarMateriaAPlan: registra, para un plan asociado a cierta carrera,
	   una materia
	 * @param inputs: arreglo de datos de entrada para registrar la asociación
	   entre materia y plan
	 */
	public void agregarMateriaAPlan (String [] inputs) {
		ConectorBD.obtenerConectorBD().conectarBD();
		try
		{
			// Creo un comando JDBC para realizar la inserción en la BD
			Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
			// Genero la sentencia de inserción
			String sql = "INSERT INTO planes_materias (id_plan, id_materia) VALUES (" + 
	        		 	 Integer.parseInt(inputs[0]) + "," +
	        		 	 Integer.parseInt(inputs[1]) + ")";
			// Ejecuto la inserción de la vinculación de una materia con su plan
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
	 * agregarCorrelatividad: registra, para una materia, asociada a un plan particular,
	   una materia correlativa
	 * @param inputs: arreglo de datos de entrada para registrar la asociación
	   entre materia y plan
	 */
	public void agregarCorrelatividad (String [] inputs) {
		ConectorBD.obtenerConectorBD().conectarBD();
		try
		{
			// Creo un comando JDBC para realizar la inserción en la BD
			Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
			// Genero la sentencia de inserción
			String sql = "INSERT INTO correlativas (id_plan, id_materia, id_correlativa) VALUES " + 
	        		 	 "(" + Integer.parseInt(inputs[0]) + "," +
	        		 	 Integer.parseInt(inputs[1]) + "," +
	        		 	 Integer.parseInt(inputs[2]) + ");";
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
}
