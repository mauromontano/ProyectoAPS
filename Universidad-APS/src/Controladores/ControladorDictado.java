package Controladores;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import Conector.DriverBD;
import Excepciones.DBRetrieveException;
import Excepciones.DBUpdateException;
import Modelos.ActaCursado;
import Modelos.ActaFinal;
import Modelos.Dictado;
import Modelos.MesaDeExamen;
import Modelos.Modelo;
import Modelos.Profesor;
import quick.dbtable.DBTable;

public class ControladorDictado {
	
private static ControladorDictado instancia = null;
	
	/**
	 * controlador: retorna la instancia asociada al controlador
	 * @return controlador asociado a alumnos
	 */
	public static ControladorDictado controlador () {
		if (instancia == null) {
			instancia = new ControladorDictado();
		}
		return instancia;
	}

	
	/**
	 * registrar: permite el registro de un dictado en base a datos de entrada asociados a sus
	   atributos
	 * @param inputs: arreglo de atributos del nuevo dictado a registrar
	 */
	public void registrar (String [] inputs) throws DBUpdateException {
		String sentenciaSQL;
		String mensajeError = null;
		boolean solicitudExitosa = true;
		try
		{
			// Genero la sentencia de inserción
			sentenciaSQL = "INSERT INTO dictados (legajo_profesor, id_materia_dictada, id_plan_materia, anio, cuatrimestre) VALUES " +
					"(" + inputs[0] + 
					"," + inputs[1] +
					"," + inputs[2] +
					", YEAR(CURRENT_TIMESTAMP)," +
					"\'" + inputs[3] + "\');";
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
			throw new DBUpdateException("¡ERROR! falló el registro del nuevo dictado.\n" +
					"Detalle: " + mensajeError);
		}
	}
	
	
	/**
	 * modificar: modifica datos de registro de un dictado en base a datos
	   de entrada asociados a sus atributos
	 * @param inputs: arreglo de atributos del dictado a modificar
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
				sentenciaSQL = "UPDATE dictados SET " + asignacionesSQL + " WHERE (id = " + inputs[0] + ");";
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
			throw new DBUpdateException("¡ERROR! falló la modificación de datos del dictado con ID: " + inputs[0] + ".\n" +
					"Detalle: " + mensajeError);
		}
	}
	
	
	/**
	 * eliminar: elimina datos de registro de un dictado en base a datos
	   de entrada asociados a sus atributos
	 * @param inputs: arreglo de atributos del dictado a eliminar
	 */
	public void eliminar (String input) throws DBUpdateException {
		String sentenciaSQL;
		String mensajeError = null;
		boolean solicitudExitosa = true;
		try
		{
			// Genero la sentencia de inserción
			sentenciaSQL = "DELETE FROM dictados WHERE (id = " + input + ")";
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
			throw new DBUpdateException("¡ERROR! falló la baja del dictado con ID: " + input + ".\n" +
					"Detalle: " + mensajeError);
		}		
	}
	
	
	/**
	 * volcar: solicita la carga de datos asociados a los dictados registrados en
	   el sistema.
	 * tb: objeto DBTable (tabla de datos) donde se aplicará la carga de los datos de dictados.
	 */
	public void volcar (DBTable tb) throws DBRetrieveException {
		String sentenciaSQL;
		boolean consultaExitosa = true;
		
		try
	    {
			sentenciaSQL = "SELECT * FROM dictados";
			DriverBD.driver().volcar(tb,sentenciaSQL);
	    }		
		catch (SQLException ex)
		{
			consultaExitosa = false;
	    }
		// Si la consulta no fue exitosa, entonces falló el volcado de datos
		if (!consultaExitosa) {
			throw new DBRetrieveException("¡ERROR! falló el volcado de los datos de dictados en la tabla");
		}
	}
	
	
	/**
	 * recuperar: retorna un modelo correspondiente a un dictado, cuyos atributos se especifican
	   como argumento en un arreglo de parámetros.
	 * @param inputs: arreglo de cadenas de caracteres, asociada a los parámetros
	 * @return moodelo de dictado correspondiente con los atributos dados, si existe. Nulo en otro caso.
	 * @throws DBRetrieveException: si hay un error en la obtención del modelo de la BD.
	 */
	public Dictado recuperar (String [] inputs) throws DBRetrieveException {
		ResultSet rs;
		String sentenciaSQL;
		String asignacionesSQL = null;
		String mensajeError = null;
		Dictado dictado = null;		
		boolean solicitudExitosa = true;	
		try
		{
			asignacionesSQL = generarAsignacionesSQL(inputs);
			sentenciaSQL = "SELECT * FROM dictados WHERE (id = " + inputs[0];
			// Si hay asignaciones adicionales, las incorporo a la sentencia
			if (asignacionesSQL.length() > 0) {
				sentenciaSQL +=  ", " + asignacionesSQL;
			}
			sentenciaSQL += ")";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			// Si hay un resultado siguiente, lo recupero
			if (rs.next()) {
				dictado = Dictado.extraerModelo(rs);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación del dictado con parámetros " + Arrays.deepToString(inputs) + ".\n" +
					"Detalle: " + mensajeError);
		}
		return dictado;
	}
	
	
	private String generarAsignacionesSQL (String [] inputs) {
		String asignacionesSQL = "";
		// Genero la sentencia de inserción
		if (inputs[1] != null) {
			asignacionesSQL += "legajo_profesor = " + inputs[1];
		}
		
		return asignacionesSQL;
	}
	
	
	/**
	 * elementos: permite obtener una lista de modelos de profesores, asociados a
	   los profesores registrados en la base de datos
	 * @return lista de modelos de profesores
	 */
	public List<Modelo> elementos () throws DBRetrieveException {
		List<Modelo> listaDictados = new LinkedList<Modelo>();
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
				listaDictados.add(profesor);
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
		return listaDictados;
	}
	
	
	public List<Dictado> dictadosMateriaDelPlan (int idMat, int idPlan) throws DBRetrieveException {
		List<Dictado> listaDictados = new LinkedList<Dictado>();
		ResultSet rs;
		Dictado dictado = null;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;		
		try
		{
			sentenciaSQL = "SELECT * FROM dictados WHERE (id_materia_dictada = " + idMat + " AND id_plan_materia = " + idPlan + ");";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			while (rs.next()) {
				dictado = Dictado.extraerModelo(rs);
				listaDictados.add(dictado);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}

		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación los dictados para la materia con ID: " +
					idMat + " del plan con ID: " + idPlan + ".\n" +
					"Detalle: " + mensajeError);
		}
		return listaDictados;
	}
	

	public List<Dictado> dictadosMateriaPorProfesor(int idMat, int lg) throws DBRetrieveException {
		List<Dictado> listaDictados = new LinkedList<Dictado>();
		ResultSet rs;
		Dictado dictado = null;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;
		try
		{
			sentenciaSQL = "SELECT *\r\n" + 
					"FROM dictados\r\n" + 
					"WHERE (\r\n" + 
					"		legajo_profesor = " + lg + "\r\n" + 
					"		AND\r\n" + 
					"		id_materia_dictada = " + idMat + "\r\n" + 
					");";
			
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);			
			while (rs.next()) {
				dictado = Dictado.extraerModelo(rs);
				listaDictados.add(dictado);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}

		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación los dictados de la materia con ID: " +
					idMat + " del profesor con legajo: " + lg + ".\n" +
					"Detalle: " + mensajeError);
		}
		return listaDictados;
	}

	
	public void volcarAlumnosDictado (DBTable tb, int idDict) throws DBRetrieveException {
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;
		try
		{
			// Genero la sentencia de inserción
			sentenciaSQL = "SELECT LU, dni as 'DNI', nombre as 'Nombre', apellido as 'Apellido', genero as ' Género',"
					+ " fecha as 'Fecha de inscripción', estado as 'Estado de inscripción'\r\n" + 
					"FROM (\r\n" + 
					"				SELECT *\r\n" + 
					"				FROM inscripciones_dictados\r\n" + 
					"				WHERE (id_dictado = " + idDict + ")\r\n" + 
					"			) as ID\r\n" + 
					"			NATURAL JOIN\r\n" + 
					"			alumnos as A\r\n" + 
					"WHERE (ID.LU_alumno = A.LU);";
			DriverBD.driver().volcar(tb, sentenciaSQL);
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}

		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló el volcado de los alumnos del dictado con ID: " +
					idDict + ".\n" + "Detalle: " + mensajeError);
		}
	}

	
	public void asignarCalificacion(int lu, int idDict, String calif) throws DBUpdateException {
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;
		try
		{
			sentenciaSQL = "INSERT INTO calificaciones_dictados (LU_alumno, id_dictado, fecha_calificacion, estado) VALUES " + 
	        		 	 "(" + lu + "," + 
	        		 	 "" + idDict + "," +
	        		 	 " CURDATE()," +
	        		 	 "\'" + calif + "\');";
			
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
			throw new DBUpdateException("¡ERROR! falló la asignación de la calificación al alumno con LU: " + lu +
					" sobre el dictado con ID: " + idDict + ".\n" + "Detalle: " + mensajeError);
		}
	}
	
	
	
	//---------------------Lautaro-------------------
	public List<ActaCursado> recuperarActaCursados (int id_dictado) throws DBRetrieveException {
		
		
		List<ActaCursado> listaActaCursados = new LinkedList<ActaCursado>();
		
		ResultSet rs;
		ActaCursado actaCursado = null;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;		
		try
		{		
			sentenciaSQL = "SELECT LU,nombre,apellido,estado from calificaciones_dictados,alumnos where LU_alumno=LU and id_dictado="+id_dictado+";";
			
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			while (rs.next()) {
				actaCursado = ActaCursado.extraerModelo(rs);
				listaActaCursados.add(actaCursado);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}

		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación las calificaciones dictado para el dictado con ID: " +
					id_dictado+".\n" +
					"Detalle: " + mensajeError);
		}
		return listaActaCursados;
	}
	
	
	public List<MesaDeExamen> mesasMateriaPorProfesor(int idMat, int lg) throws DBRetrieveException {
	
		List<MesaDeExamen> listaMesas = new LinkedList<MesaDeExamen>();
		ResultSet rs;
		MesaDeExamen mesa = null;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;
		try
		{
			sentenciaSQL = "SELECT *\r\n" + 
					"FROM mesas_de_examen\r\n" + 
					"WHERE (\r\n" + 
					"		legajo_profesor = " + lg + "\r\n" + 
					"		AND\r\n" + 
					"		id_materia = " + idMat + "\r\n" + 
					");";
			
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);			
			while (rs.next()) {
				mesa = MesaDeExamen.extraerModelo(rs);
				listaMesas.add(mesa);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}

		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación los dictados de la materia con ID: " +
					idMat + " del profesor con legajo: " + lg + ".\n" +
					"Detalle: " + mensajeError);
		}
		return listaMesas;
	}
	
	
public List<ActaFinal> recuperarActaFinal (int id_mesa) throws DBRetrieveException {
		
		
		List<ActaFinal> listaActaFinales = new LinkedList<ActaFinal>();
		
		ResultSet rs;
		ActaFinal actaFinal = null;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;		
		try
		{		
			sentenciaSQL = "SELECT LU,nombre,apellido,puntaje from calificaciones_finales,alumnos where LU_alumno=LU and id_mesa="+id_mesa+";";
			
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			while (rs.next()) {
				actaFinal = ActaFinal.extraerModelo(rs);
				listaActaFinales.add(actaFinal);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}

		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación las calificaciones finales para la mesa con ID: " +
					id_mesa+".\n" +
					"Detalle: " + mensajeError);
		}
		return listaActaFinales;
	}
	
	
	
	//---------------------------------------------

}
