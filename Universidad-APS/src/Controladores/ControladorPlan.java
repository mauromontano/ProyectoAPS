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
import Modelos.Plan;
import Modelos.Profesor;
import quick.dbtable.DBTable;

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
	 * registrar: permite el registro de un plan en base a datos de entrada asociados a sus
	   atributos
	 * @param inputs: arreglo de atributos del nuevo plan a registrar
	 */
	public void registrar (String [] inputs) throws DBUpdateException {
		String sentenciaSQL;
		String mensajeError = null;
		boolean solicitudExitosa = true;
		try
		{
			// Genero la sentencia de inserción
			sentenciaSQL = "INSERT INTO planes (id_carrera, version) VALUES " +
					"(" + inputs[0] + 
					"," + inputs[1] + ");";
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
			throw new DBUpdateException("¡ERROR! falló el registro del nuevo plan.\n" +
					"Detalle: " + mensajeError);
		}
	}
	
	
	/**
	 * modificar: modifica datos de registro de un plan en base a datos
	   de entrada asociados a sus atributos
	 * @param inputs: arreglo de atributos del plan a modificar
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
				sentenciaSQL = "UPDATE planes SET " + asignacionesSQL + " WHERE (id = " + inputs[0] + ");";
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
			throw new DBUpdateException("¡ERROR! falló la modificación de datos del plan con ID: " + inputs[0] + ".\n" +
					"Detalle: " + mensajeError);
		}
	}
	
	
	/**
	 * eliminar: elimina datos de registro de un plan en base a datos
	   de entrada asociados a sus atributos
	 * @param inputs: arreglo de atributos del plan a eliminar
	 */
	public void eliminar (String input) throws DBUpdateException {
		ResultSet rs;
		String sentenciaSQL;
		String mensajeError = null;
		boolean esPosibleAgregar = true;
		boolean solicitudExitosa = true;
		try
		{
			sentenciaSQL = "SELECT *\r\n" + 
					"FROM planes\r\n" + 
					"WHERE (id = " + input + ");";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			// Si no existe el plan ingresado, no puede ser eliminado
			if (!rs.next()) {
				esPosibleAgregar = false;
			}
			else {
				sentenciaSQL = "DELETE FROM planes WHERE (id = " + input + ")";
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
			throw new DBUpdateException("¡ERROR! falló la baja del plan con ID: " + input + ".\n" +
					"Detalle: " + mensajeError);
		}
		if (!esPosibleAgregar) {
			mensajeError = "no existe un plan con este ID";
			throw new DBUpdateException("¡ERROR! falló el registro de la eliminación del plan con ID: " + input + ".\n" +
					"Detalle: " + mensajeError);
		}
	}
	
	
	/**
	 * volcar: solicita la carga de datos asociados a los planes registrados en
	   el sistema.
	 * tb: objeto DBTable (tabla de datos) donde se aplicará la carga de los datos de planes.
	 */
	public void volcar (DBTable tb) throws DBRetrieveException {
		String sentenciaSQL;
		boolean consultaExitosa = true;
		
		try
	    {
			sentenciaSQL = "SELECT * FROM planes";
			DriverBD.driver().volcar(tb,sentenciaSQL);
	    }		
		catch (SQLException ex)
		{
			consultaExitosa = false;
	    }
		// Si la consulta no fue exitosa, entonces falló el volcado de datos
		if (!consultaExitosa) {
			throw new DBRetrieveException("¡ERROR! falló el volcado de los datos de planes en la tabla");
		}
	}
	
	
	/**
	 * recuperar: retorna un modelo correspondiente a un plan, cuyos atributos se especifican
	   como argumento en un arreglo de parámetros.
	 * @param inputs: arreglo de cadenas de caracteres, asociada a los parámetros
	 * @return moodelo de plan correspondiente con los atributos dados, si existe. Nulo en otro caso.
	 * @throws DBRetrieveException: si hay un error en la obtención del modelo de la BD.
	 */
	public Plan recuperar (String [] inputs) throws DBRetrieveException {
		ResultSet rs;
		String sentenciaSQL;
		String asignacionesSQL = null;
		String mensajeError = null;
		Plan plan = null;		
		boolean solicitudExitosa = true;	
		try
		{
			asignacionesSQL = generarAsignacionesSQL(inputs);
			sentenciaSQL = "SELECT * FROM planes WHERE (id = " + inputs[0];
			// Si hay asignaciones adicionales, las incorporo a la sentencia
			if (asignacionesSQL.length() > 0) {
				sentenciaSQL +=  ", " + asignacionesSQL;
			}
			sentenciaSQL += ")";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			
			// Si hay un resultado siguiente, lo recupero
			if (rs.next()) {
				plan = Plan.extraerModelo(rs);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación del plan con parámetros " + Arrays.deepToString(inputs) + ".\n" +
					"Detalle: " + mensajeError);
		}
		return plan;
	}
	
	
	private String generarAsignacionesSQL (String [] inputs) {
		String asignacionesSQL = "";
		// Genero la sentencia de inserción
		if (inputs[1] != null) {
			asignacionesSQL += "version = " + inputs[1];
		}
		
		return asignacionesSQL;
	}
	
	
	/**
	 * elementos: permite obtener una lista de modelos de profesores, asociados a
	   los profesores registrados en la base de datos
	 * @return lista de modelos de profesores
	 */
	public List<Modelo> elementos () throws DBRetrieveException {
		List<Modelo> listaMaterias = new LinkedList<Modelo>();
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
			DriverBD.driver().cerrarConexion();
			// Mientras haya un resultado siguiente, lo recupero
			while (rs.next()) {
				profesor = Profesor.extraerModelo(rs);
				listaMaterias.add(profesor);
			}
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
		return listaMaterias;
	}
	
	
	/**
	 * agregarMateriaAPlan: registra, para un plan asociado a cierta carrera,
	   una materia
	 * @param inputs: arreglo de datos de entrada para registrar la asociación
	   entre materia y plan
	 */
	public void agregarMateriaAPlan (String [] inputs) throws DBUpdateException {
		String sentenciaSQL;
		String mensajeError = null;
		boolean solicitudExitosa = true;
		try
		{
			// Genero la sentencia de inserción
			sentenciaSQL = "INSERT INTO planes_materias (id_plan, id_materia) VALUES (" + 
	        		 	 inputs[0] + "," +
	        		 	 inputs[1] + ")";
			// Ejecuto la inserción de la vinculación de una materia con su plan
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
			throw new DBUpdateException("¡ERROR! falló el registro de una materia con ID: " + inputs[1] + " " +
					"sobre el plan con ID: " + inputs[0] + ".\n" + "Detalle: " + mensajeError);
		}
		
	}
	

	public List<Plan> planesDeCarrera (int id) throws DBRetrieveException {
		List<Plan> listaPlanes = new LinkedList<Plan>();
		ResultSet rs;
		Plan plan;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;
		try
		{
			sentenciaSQL = "SELECT * FROM planes WHERE (id_carrera = " + id + ");";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);			
			while (rs.next()) {
				plan = Plan.extraerModelo(rs);
				listaPlanes.add(plan);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		
		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación de todos los planes de la carrera con ID: " + id
					+ ".\n" + "Detalle: " + mensajeError);
		}		
		return listaPlanes;
	}
	
	
	public Plan planDeCarreraAlumno (int lu, int id) throws DBRetrieveException {
		ResultSet rs;
		Plan plan = null;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;
		try
		{
			sentenciaSQL = "SELECT id, id_carrera, version\r\n" + 
					"FROM \r\n" + 
					"	inscripciones_carreras \r\n" + 
					"	NATURAL JOIN \r\n" + 
					"	planes \r\n" + 
					"WHERE (LU_alumno = " + lu + " AND id_carrera = " + id + " AND id_plan = id);";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			if (rs.next()) {
				plan = Plan.extraerModelo(rs);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		
		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación de todos los planes de la carrera con ID: " + id
					+ ".\n" + "Detalle: " + mensajeError);
		}		
		return plan;
	}
	
}
