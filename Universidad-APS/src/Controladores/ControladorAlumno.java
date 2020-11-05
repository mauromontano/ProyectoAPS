package Controladores;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Conector.ConectorBD;
import Excepciones.ExcepcionEliminacion;
import Excepciones.ExcepcionRegistro;
import Modelos.Alumno;

public class ControladorAlumno {
	
	private static ControladorAlumno instancia = null;
	
	/**
	 * controlador: retorna la instancia asociada al controlador
	 * @return controlador asociado a alumnos
	 */
	public static ControladorAlumno controlador () {
		if (instancia == null) {
			instancia = new ControladorAlumno();
		}
		return instancia;
	}
	
	/**
	 * registrar: permite el registro de un alumno en base a datos de entrada asociados a sus
	   atributos
	 * @param inputs: arreglo de atributos del nuevo alumno a registrar
	 */
	public void registrar (String [] inputs) {
		Statement stmt;
		String sql;
		ConectorBD.obtenerConectorBD().conectarBD();
		try
		{
			// Creo un comando JDBC para realizar la inserción en la BD
			stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
			// Genero la sentencia de inserción
			sql = "INSERT INTO alumnos (dni, nombre, apellido, genero) VALUES " + 
	        		 	 "(" + Integer.parseInt(inputs[0]) + "," + 
	        		 	 "\'" + inputs[1] + "\'," +
	        		 	 "\'" + inputs[2] + "\'," +
	        		 	 "\'" + inputs[3] + "\');";
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
	 * modificar: modifica datos de registro de un alumno en base a datos
	   de entrada asociados a sus atributos
	 * @param inputs: arreglo de atributos del nuevo alumno a modificar
	 */
	public void modificar (String [] inputs) throws ExcepcionRegistro {
		Statement stmt;
		String sql = "";
		boolean hayAsignacionAnterior = false;
		ConectorBD.obtenerConectorBD().conectarBD();
		
		try
		{
			// Creo un comando JDBC para realizar la inserción en la BD
			stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
			// Genero la sentencia de inserción
			if (inputs[1] != null) {
				sql += "dni = " + inputs[0];
				hayAsignacionAnterior = true;
			}
			if (inputs[2] != null) {
				if (hayAsignacionAnterior) {
					sql += ", ";
				}
				sql += "nombre = \'" + inputs[1] + "\'";
			}
			if (inputs[3] != null) {
				if (hayAsignacionAnterior) {
					sql += ", ";
				}
				sql += "apellido = \'" + inputs[2] + "\'";
			}
			if (inputs[4] != null) {
				if (hayAsignacionAnterior) {
					sql += ", ";
				}
				sql += "genero = \'" + inputs[3] + "\'";
			}
			
			if (hayAsignacionAnterior) {
				sql = "UPDATE alumnos SET " + sql + " WHERE (LU = " + inputs[0] + ");";
			}
			
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
	 * eliminar: elimina datos de registro de un alumno en base a datos
	   de entrada asociados a sus atributos
	 * @param inputs: arreglo de atributos del nuevo alumno a eliminar
	 */
	public void eliminar (String lu) throws ExcepcionEliminacion {
		Statement stmt;
		String sql; 
		ConectorBD.obtenerConectorBD().conectarBD();
		try
		{
			// Creo un comando JDBC para realizar la inserción en la BD
			stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
			// Genero la sentencia de inserción
			sql = "DELETE FROM alumnos WHERE ( LU = " + lu + ")";
			// Ejecuto la eliminación del alumno
			stmt.executeUpdate(sql);
			//JOptionPane.showMessageDialog(this,"Alumno dado de baja exitosamente");
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
	
	
	public Alumno obtenerAlumno (int lu) {
		Statement stmt;
		String sql;
		Alumno alumno = null;
		ConectorBD.obtenerConectorBD().conectarBD();
		try
		{
			// Creo un comando JDBC para realizar la inserción en la BD
			stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
			// Genero la sentencia de inserción
			sql = "SELECT * FROM alumnos WHERE (LU = " + lu + ")";
			// Ejecuto la eliminación del alumno
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				alumno = Alumno.siguienteModelo(rs);
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
		return alumno;
	}

}
