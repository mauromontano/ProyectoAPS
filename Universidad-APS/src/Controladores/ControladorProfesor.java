package Controladores;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import Conector.DriverBD;
import Excepciones.DBRetrieveException;
import Excepciones.DBUpdateException;
import Modelos.Modelo;
import Modelos.Profesor;
import quick.dbtable.DBTable;

public class ControladorProfesor {
	
	private static ControladorProfesor instancia = null;
	
	/**
	 * controlador: retorna la instancia asociada al controlador
	 * @return controlador asociado a alumnos
	 */
	public static ControladorProfesor controlador () {
		if (instancia == null) {
			instancia = new ControladorProfesor();
		}
		return instancia;
	}
	
	
	/**
	 * registrar: permite el registro de un profesor en base a datos de entrada asociados a sus
	   atributos
	 * @param inputs: arreglo de atributos del nuevo profesor a registrar
	 */
	public void registrar (String [] inputs) throws DBUpdateException {
		String sentenciaSQL;
		String mensajeError = null;
		boolean solicitudExitosa = true;
		try
		{
			// Genero la sentencia de inserción
			sentenciaSQL = "INSERT INTO profesores (matricula, dni, cuil, nombre, apellido, genero) VALUES " +
					"(" + inputs[0] + 
					"," + inputs[1] +
					"," + inputs[2] + "," +
					"\'" + inputs[3] + "\'," +
					"\'" + inputs[4] + "\'," +
					"\'" + inputs[5] + "\');";
			// Ejecuto la inserción de la nueva materia
			DriverBD.driver().nuevaConexion();
			DriverBD.driver().actualizar(sentenciaSQL);
			DriverBD.driver().cerrarConexion();
			
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
			
		}
		if (!solicitudExitosa) {
			throw new DBUpdateException("¡ERROR! falló el registro del nuevo profesor.\n" +
					"Detalle: " + mensajeError);
		}
	}
	
	
	/**
	 * modificar: modifica datos de registro de un profesor en base a datos
	   de entrada asociados a sus atributos
	 * @param inputs: arreglo de atributos del profesor a modificar
	 */
	public void modificar (String [] inputs) throws DBUpdateException {
		String sentenciaSQL = "";
		String asignacionesSQL = null;
		String mensajeError = null;
		boolean solicitudExitosa = true;	
		try
		{
			asignacionesSQL = generarAsignacionesSQL(inputs);
			
			if (asignacionesSQL.length() > 0) {
				sentenciaSQL = "UPDATE profesores SET " + asignacionesSQL + " WHERE (legajo = " + inputs[0] + ");";
				DriverBD.driver().nuevaConexion();
				DriverBD.driver().actualizar(sentenciaSQL);
				DriverBD.driver().cerrarConexion();
			}		
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBUpdateException("¡ERROR! falló la modificación de datos del profesor con legajo: " + inputs[0] + ".\n" +
					"Detalle: " + mensajeError);
		}
	}
	
	
	/**
	 * eliminar: elimina datos de registro de un profesor en base a datos
	   de entrada asociados a sus atributos
	 * @param inputs: arreglo de atributos del profesor a eliminar
	 */
	public void eliminar (String input) throws DBUpdateException {
		String sentenciaSQL;
		String mensajeError = null;
		boolean solicitudExitosa = true;
		try
		{
			// Genero la sentencia de inserción
			sentenciaSQL = "DELETE FROM profesores WHERE (legajo = " + input + ")";
			// Ejecuto la inserción del nuevo alumno
			DriverBD.driver().nuevaConexion();
			DriverBD.driver().actualizar(sentenciaSQL);
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
			
		}
		if (!solicitudExitosa) {
			throw new DBUpdateException("¡ERROR! falló la baja del profesor con legajo: " + input + ".\n" +
					"Detalle: " + mensajeError);
		}		
	}
	
	
	/**
	 * volcar: solicita la carga de datos asociados a los profesores registrados en
	   el sistema.
	 * tb: objeto DBTable (tabla de datos) donde se aplicará la carga de los datos de profesores.
	 */
	public void volcar (DBTable tb) throws DBRetrieveException {
		String sentenciaSQL;
		boolean consultaExitosa = true;
		
		try
	    {
			sentenciaSQL = "SELECT * FROM profesores";
			DriverBD.driver().volcar(tb,sentenciaSQL);
	    }		
		catch (SQLException ex)
		{
			consultaExitosa = false;
	    }
		// Si la consulta no fue exitosa, entonces falló el volcado de datos
		if (!consultaExitosa) {
			throw new DBRetrieveException("¡ERROR! falló el volcado de los datos de profesores en la tabla");
		}
	}
	
	
	/**
	 * recuperar: retorna un modelo correspondiente a un profesor, cuyos atributos se especifican
	   como argumento en un arreglo de parámetros.
	 * @param inputs: arreglo de cadenas de caracteres, asociada a los parámetros
	 * @return moodelo de profesor correspondiente con los atributos dados, si existe. Nulo en otro caso.
	 * @throws DBRetrieveException: si hay un error en la obtención del modelo de la BD.
	 */
	public Profesor recuperar (String [] inputs) throws DBRetrieveException {
		ResultSet rs;
		String sentenciaSQL;
		String asignacionesSQL = null;
		String mensajeError = null;
		Profesor profesor = null;		
		boolean solicitudExitosa = true;	
		try
		{
			asignacionesSQL = generarAsignacionesSQL(inputs);
			sentenciaSQL = "SELECT * FROM profesores WHERE (legajo = " + inputs[0];
			// Si hay asignaciones adicionales, las incorporo a la sentencia
			if (asignacionesSQL.length() > 0) {
				sentenciaSQL +=  ", " + asignacionesSQL;
			}
			sentenciaSQL += ")";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			
			// Si hay un resultado siguiente, lo recupero
			if (rs.next()) {
				profesor = Profesor.extraerModelo(rs);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación del profesor con parámetros " + 
					Arrays.deepToString(inputs) + ".\n" + "Detalle: " + mensajeError);
		}
		return profesor;
	}
	
	
	private String generarAsignacionesSQL (String [] inputs) {
		String asignacionesSQL = "";
		boolean hayAsignacionAnterior = false;
		// Genero la sentencia de inserción
		if (inputs[1] != null) {
			asignacionesSQL += "matricula = " + inputs[1];
			hayAsignacionAnterior = true;
		}
		if (inputs[2] != null) {			
			if (hayAsignacionAnterior) {
				asignacionesSQL += ", ";
			}
			else hayAsignacionAnterior = true;
			asignacionesSQL += "dni = " + inputs[2];
		}
		if (inputs[3] != null) {
			if (hayAsignacionAnterior) {
				asignacionesSQL += ", ";
			}
			else hayAsignacionAnterior = true;
			asignacionesSQL += "cuil = " + inputs[3];
		}
		if (inputs[4] != null) {
			if (hayAsignacionAnterior) {
				asignacionesSQL += ", ";
			}
			else hayAsignacionAnterior = true;
			asignacionesSQL += "nombre = \'" + inputs[4] + "\'";
		}
		if (inputs[5] != null) {
			if (hayAsignacionAnterior) {
				asignacionesSQL += ", ";
			}
			else hayAsignacionAnterior = true;
			asignacionesSQL += "apellido = \'" + inputs[5] + "\'";
		}
		if (inputs[6] != null) {
			if (hayAsignacionAnterior) {
				asignacionesSQL += ", ";
			}
			else hayAsignacionAnterior = true;
			asignacionesSQL += "genero = \'" + inputs[6] + "\'";
		}
		
		return asignacionesSQL;
	}
	
	
	/**
	 * elementos: permite obtener una lista de modelos de profesores, asociados a
	   los profesores registrados en la base de datos
	 * @return lista de modelos de profesores
	 */
	public List<Modelo> elementos () throws DBRetrieveException {
		List<Modelo> listaProfesores = new LinkedList<Modelo>();
		ResultSet rs;
		Profesor profesor;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;	
		try
		{
			sentenciaSQL = "SELECT * FROM profesores";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);			
			// Mientras haya un resultado siguiente, lo recupero
			while (rs.next()) {
				profesor = Profesor.extraerModelo(rs);
				listaProfesores.add(profesor);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación todos los profesores.\n" +
					"Detalle: " + mensajeError);
		}
		return listaProfesores;
	}

}
