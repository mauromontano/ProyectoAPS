package Controladores;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import Conector.DriverBD;
import Excepciones.DBRetrieveException;
import Excepciones.DBUpdateException;
import Modelos.Carrera;
import Modelos.Modelo;
import quick.dbtable.DBTable;

public class ControladorCarrera implements ControladorModelo {
	
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
	 * registrar: permite el registro de una carrera en base a datos de entrada asociados a sus
	   atributos
	 * @param inputs: arreglo de atributos de la nueva carrera a registrar
	 */
	public void registrar (String [] inputs) throws DBUpdateException {
		String sentenciaSQL;
		String mensajeError = null;
		boolean solicitudExitosa = true;
		try
		{
			// Genero la sentencia de inserción
			sentenciaSQL = "INSERT INTO carreras (nombre, duracion) VALUES " +
					"(\'" + inputs[0] + "\'," +
					"\'" + Integer.parseInt(inputs[1]) + "\');";
			// Ejecuto la inserción del nueva carrera
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
			throw new DBUpdateException("¡ERROR! falló el registro de la nueva carrera.\n" +
					"Detalle: " + mensajeError);
		}
	}
	
	
	/**
	 * modificar: modifica datos de registro de una carrera en base a datos
	   de entrada asociados a sus atributos
	 * @param inputs: arreglo de atributos de la carrera a modificar
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
				sentenciaSQL = "UPDATE carreras SET " + asignacionesSQL + " WHERE (id = " + inputs[0] + ");";
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
			throw new DBUpdateException("¡ERROR! falló la modificación de datos de la carrera con ID: " + inputs[0] + ".\n" +
					"Detalle: " + mensajeError);
		}
	}
	
	
	/**
	 * eliminar: elimina datos de registro de una carrera en base a datos
	   de entrada asociados a sus atributos
	 * @param inputs: arreglo de atributos de la carrera a eliminar
	 */
	public void eliminar (String input) throws DBUpdateException {
		String sentenciaSQL;
		String mensajeError = null;
		boolean solicitudExitosa = true;
		try
		{
			// Genero la sentencia de inserción
			sentenciaSQL = "DELETE FROM carreras WHERE (id = " + input + ")";
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
			throw new DBUpdateException("¡ERROR! falló la baja de la carrera con ID: " + input + ".\n" +
					"Detalle: " + mensajeError);
		}		
	}
	
	
	/**
	 * volcar: solicita la carga de datos asociados a las carreras registrados en
	   el sistema.
	 * tb: objeto DBTable (tabla de datos) donde se aplicará la carga de los datos de carreras.
	 */
	public void volcar (DBTable tb) throws DBRetrieveException {
		String sentenciaSQL;
		boolean consultaExitosa = true;
		
		try
	    {
			sentenciaSQL = "SELECT * FROM carreras";
			DriverBD.driver().volcar(tb,sentenciaSQL);
	    }		
		catch (SQLException ex)
		{
			consultaExitosa = false;
	    }
		// Si la consulta no fue exitosa, entonces falló el volcado de datos
		if (!consultaExitosa) {
			throw new DBRetrieveException("¡ERROR! falló el volcado de los datos de carreras en la tabla");
		}
	}
	
	
	public Carrera recuperar (String [] inputs) throws DBRetrieveException {
		ResultSet rs;
		String sentenciaSQL;
		String asignacionesSQL = null;
		String mensajeError = null;
		Carrera carrera = null;		
		boolean solicitudExitosa = true;	
		try
		{
			asignacionesSQL = generarAsignacionesSQL(inputs);
			sentenciaSQL = "SELECT * FROM carreras WHERE (id = " + inputs[0];
			// Si hay asignaciones adicionales, las incorporo a la sentencia
			if (asignacionesSQL.length() > 0) {
				sentenciaSQL +=  ", " + asignacionesSQL;
			}
			sentenciaSQL += ")";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			// Si hay un resultado siguiente, lo recupero
			if (rs.next()) {
				carrera = Carrera.extraerModelo(rs);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación de la carrera con parámetros " + inputs + ".\n" +
					"Detalle: " + mensajeError);
		}
		return carrera;
	}
	
	
	private String generarAsignacionesSQL (String [] inputs) {
		String asignacionesSQL = "";
		boolean hayAsignacionAnterior = false;
		// Genero la sentencia de inserción
		if (inputs[1] != null) {
			if (hayAsignacionAnterior) {
				asignacionesSQL += ", ";
			}
			else hayAsignacionAnterior = true;
			asignacionesSQL += "nombre = \'" + inputs[1] + "\'";
		}
		if (inputs[2] != null) {
			if (hayAsignacionAnterior) {
				asignacionesSQL += ", ";
			}
			else hayAsignacionAnterior = true;
			asignacionesSQL += "duracion = " + inputs[2];
		}
		return asignacionesSQL;
	}
	
	
	/**
	 * elementos: permite obtener una lista de modelos de carreras, asociados a
	   las carreras registradas en la base de datos
	 * @return lista de modelos de carreras
	 */
	public List<Modelo> elementos () throws DBRetrieveException {
		List<Modelo> listaCarreras = new LinkedList<Modelo>();
		ResultSet rs;
		Carrera carrera;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;	
		try
		{
			sentenciaSQL = "SELECT * FROM carreras";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);			
			// Mientras haya un resultado siguiente, lo recupero
			while (rs.next()) {
				carrera = Carrera.extraerModelo(rs);
				listaCarreras.add(carrera);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación todas las carreras.\n" +
					"Detalle: " + mensajeError);
		}
		return listaCarreras;
	}
	
	
	/**
	 * carrerasInscripto: permite obtener la lista de modelos de carreras asociadas a
	   las carreras en las cuales un dado alumno, con cierto LU, está inscripto
	 * @param lu: número de libreta universitaria asociado al alumno
	 * @return lista de modelos de carreras en las cuales está inscripto el alumno
	 * @throws DBRetrieveException 
	 */
	public List<Carrera> carrerasInscripto (String lu) throws DBRetrieveException {
		List<Carrera> listaCarreras = new LinkedList<Carrera>();
		ResultSet rs;
		Carrera carrera;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;		
		try
		{
			// Genero la sentencia de inserción
			sentenciaSQL = "SELECT id_carrera, nombre, duracion " +
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
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			
			while (rs.next()) {
				carrera = Carrera.extraerModelo(rs);
				listaCarreras.add(carrera);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación las carreras donde un alumno está inscripto.\n" +
					"Detalle: " + mensajeError);
		}
		return listaCarreras;
	}

	
	

}
