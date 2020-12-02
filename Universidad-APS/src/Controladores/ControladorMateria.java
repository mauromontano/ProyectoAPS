package Controladores;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import Conector.DriverBD;
import Excepciones.DBRetrieveException;
import Excepciones.DBUpdateException;
import Modelos.Materia;
import Modelos.Modelo;
import quick.dbtable.DBTable;

public class ControladorMateria {
	
	private static ControladorMateria instancia = null;
	
	/**
	 * controlador: retorna la instancia asociada al controlador
	 * @return controlador asociado a materias
	 */
	public static ControladorMateria controlador () {
		if (instancia == null) {
			instancia = new ControladorMateria();
		}
		return instancia;
	}
	
	
	/**
	 * registrar: permite el registro de una materia en base a datos de entrada asociados a sus
	   atributos
	 * @param inputs: arreglo de atributos de la nueva materia a registrar
	 */
	public void registrar (String [] inputs) throws DBUpdateException {
		String sentenciaSQL;
		String mensajeError = null;
		boolean solicitudExitosa = true;
		try
		{
			// Genero la sentencia de inserción
			sentenciaSQL = "INSERT INTO materias (nombre, carga_horaria) VALUES (\'" + inputs[0] + "\'," + inputs[1] + ");";
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
			throw new DBUpdateException("¡ERROR! falló el registro de la nueva materia.\n" +
					"Detalle: " + mensajeError);
		}
	}
	
	
	/**
	 * modificar: modifica datos de registro de una materia en base a datos
	   de entrada asociados a sus atributos
	 * @param inputs: arreglo de atributos de la materia a modificar
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
				sentenciaSQL = "UPDATE materias SET " + asignacionesSQL + " WHERE (id = " + inputs[0] + ");";
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
			throw new DBUpdateException("¡ERROR! falló la modificación de datos de la materia con ID: " + inputs[0] + ".\n" +
					"Detalle: " + mensajeError);
		}
	}
	
	
	/**
	 * eliminar: elimina datos de registro de una materia en base a datos
	   de entrada asociados a sus atributos
	 * @param inputs: arreglo de atributos de la materia a eliminar
	 */
	public void eliminar (String input) throws DBUpdateException {
		String sentenciaSQL;
		String mensajeError = null;
		boolean solicitudExitosa = true;
		try
		{
			// Genero la sentencia de inserción
			sentenciaSQL = "DELETE FROM materias WHERE (id = " + input + ")";
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
			throw new DBUpdateException("¡ERROR! falló la baja de la materia con ID: " + input + ".\n" +
					"Detalle: " + mensajeError);
		}		
	}
	
	
	/**
	 * volcar: solicita la carga de datos asociados a las materias registrados en
	   el sistema.
	 * tb: objeto DBTable (tabla de datos) donde se aplicará la carga de los datos de materias.
	 */
	public void volcar (DBTable tb) throws DBRetrieveException {
		String sentenciaSQL;
		boolean consultaExitosa = true;
		
		try
	    {
			sentenciaSQL = "SELECT * FROM materias";
			DriverBD.driver().volcar(tb,sentenciaSQL);
	    }		
		catch (SQLException ex)
		{
			consultaExitosa = false;
	    }
		// Si la consulta no fue exitosa, entonces falló el volcado de datos
		if (!consultaExitosa) {
			throw new DBRetrieveException("¡ERROR! falló el volcado de los datos de materias en la tabla");
		}
	}
	
	
	/**
	 * agregarCorrelatividad: registra, para una materia, asociada a un plan particular,
	   una materia correlativa
	 * @param inputs: arreglo de datos de entrada para registrar la asociación
	   entre materia y plan
	 * @throws DBUpdateException 
	 */
	public void agregarCorrelatividad (String [] inputs) throws DBUpdateException {
		ResultSet rs;
		String sentenciaSQL;
		String mensajeError = null;
		boolean esPosibleAgregar = true;
		boolean solicitudExitosa = true;
		try
		{
			sentenciaSQL = "SELECT *\r\n" + 
					"FROM planes\r\n" + 
					"WHERE (id = " + inputs[0] + ");";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			// Si no existe el plan ingresado, no puedo continuar con el registro de la correlatividad
			if (!rs.next()) {
				esPosibleAgregar = false;
			}
			else {
				sentenciaSQL = "SELECT *\r\n" + 
						"FROM materias\r\n" + 
						"WHERE (id = " + inputs[1] + ");";
				rs = DriverBD.driver().consultar(sentenciaSQL);
				// Si no existe el la materia predecesora, no puedo continuar con el registro de la correlatividad
				if (!rs.next()) {
					esPosibleAgregar = false;
				}
				else {
					sentenciaSQL = "SELECT *\r\n" + 
							"FROM materias\r\n" + 
							"WHERE (id = " + inputs[2] + ");";
					rs = DriverBD.driver().consultar(sentenciaSQL);
					// Si no existe el la materia correlativa, no puedo continuar con el registro de la correlatividad
					if (!rs.next()) {
						esPosibleAgregar = false;
					}
					else {
						DriverBD.driver().cerrarConexion();
						sentenciaSQL = "INSERT INTO correlativas (id_plan, id_materia, id_correlativa) VALUES " +
								"(" + Integer.parseInt(inputs[0]) + "," +
								Integer.parseInt(inputs[1]) + "," +
								Integer.parseInt(inputs[2]) + ");";
						
						DriverBD.driver().nuevaConexion();
						DriverBD.driver().actualizar(sentenciaSQL);
						DriverBD.driver().cerrarConexion();
					}
				}
			}
			DriverBD.driver().cerrarConexion();			
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		
		if (!solicitudExitosa) {
			throw new DBUpdateException("¡ERROR! falló el registro de la correlatividad entre la materia con ID: " + inputs[1] + " " +
					"con la de ID: " + inputs[2] + ".\n" + "Detalle: " + mensajeError);
		}
		if (!esPosibleAgregar) {
			mensajeError = "los IDs especificados como entrada son incorrectos. Especifique IDs de plan y materias existentes";
			throw new DBUpdateException("¡ERROR! falló el registro de la correlatividad entre la materia con ID: " + inputs[1] + " " +
					"con la de ID: " + inputs[2] + ".\n" + "Detalle: " + mensajeError);
		}
	}
	
	
	/**
	 * recuperar: retorna un modelo correspondiente a una materia, cuyos atributos se especifican
	   como argumento en un arreglo de parámetros.
	 * @param inputs: arreglo de cadenas de caracteres, asociada a los parámetros
	 * @return moodelo de materia correspondiente con los atributos dados, si existe. Nulo en otro caso.
	 * @throws DBRetrieveException: si hay un error en la obtención del modelo de la BD.
	 */
	public Materia recuperar (String [] inputs) throws DBRetrieveException {
		ResultSet rs;
		String sentenciaSQL;
		String asignacionesSQL = null;
		String mensajeError = null;
		Materia materia = null;		
		boolean solicitudExitosa = true;	
		try
		{
			asignacionesSQL = generarAsignacionesSQL(inputs);
			sentenciaSQL = "SELECT * FROM materias WHERE (id = " + inputs[0];
			// Si hay asignaciones adicionales, las incorporo a la sentencia
			if (asignacionesSQL.length() > 0) {
				sentenciaSQL +=  ", " + asignacionesSQL;
			}
			sentenciaSQL += ")";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			// Si hay un resultado siguiente, lo recupero
			if (rs.next()) {
				materia = Materia.extraerModelo(rs);
			}			
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación de la materia con parámetros " + inputs + ".\n" +
					"Detalle: " + mensajeError);
		}
		return materia;
	}
	
	
	private String generarAsignacionesSQL (String [] inputs) {
		String asignacionesSQL = "";
		boolean hayAsignacionAnterior = false;
		// Genero la sentencia de inserción
		if (inputs[1] != null) {
			asignacionesSQL += "nombre = \'" + inputs[1] + "\'";
			hayAsignacionAnterior = true;
		}
		if (inputs[2] != null) {
			if (hayAsignacionAnterior) {
				asignacionesSQL += ", ";
			}
			else hayAsignacionAnterior = true;
			asignacionesSQL += "carga_horaria = " + inputs[2];
		}
		return asignacionesSQL;
	}
	
	
	/**
	 * elementos: permite obtener una lista de modelos de alumnos, asociados a
	   las alumnos registradas en la base de datos
	 * @return lista de modelos de alumnos
	 */
	public List<Modelo> elementos () throws DBRetrieveException {
		List<Modelo> listaMaterias = new LinkedList<Modelo>();
		ResultSet rs;
		Materia materia;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;	
		try
		{
			sentenciaSQL = "SELECT * FROM materias";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			// Mientras haya un resultado siguiente, lo recupero
			while (rs.next()) {
				materia = Materia.extraerModelo(rs);
				listaMaterias.add(materia);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación todas las materias.\n" +
					"Detalle: " + mensajeError);
		}
		return listaMaterias;
	}
	
	
	/**
	 * materiasAlumno: retorna una lista de modelos de materias, asociadas a
	   materias registradas en la base de datos, correspondientes a una carrera
	   (plan particular) en la que un alumno está inscripto
	 * @param idC: id de la carrera de la cual se requiere obtener sus materias
	 * @param lu
	 * @return
	 */
	public List<Materia> materiasDelAlumno (int idC, int lu) throws DBRetrieveException {
		List<Materia> listaMaterias = new LinkedList<Materia>();
		ResultSet rs;
		String sentenciaSQL = null;
		String mensajeError = null;
		Materia materia;		
		boolean solicitudExitosa = true;
		try
		{
			// Genero la sentencia de inserción
			sentenciaSQL = "SELECT id, nombre, carga_horaria   \r\n" + 
					"	FROM (  \r\n" + 
					"			SELECT id_materia   \r\n" + 
					"			FROM (  \r\n" + 
					"					SELECT LU_alumno, id_plan   \r\n" + 
					"					FROM (  \r\n" + 
					"							SELECT id_carrera, id  \r\n" + 
					"							FROM ( \r\n" + 
					"									SELECT id AS id_carrera FROM carreras WHERE (id = " + idC + ") \r\n" + 
					"								 ) AS C NATURAL JOIN planes  \r\n" + 
					"						) AS T1   \r\n" + 
					"						NATURAL JOIN   \r\n" + 
					"						(  \r\n" + 
					"							SELECT LU_alumno, id_plan FROM inscripciones_carreras WHERE (LU_alumno = " + lu + ")  \r\n" + 
					"						) AS T2   \r\n" + 
					"					WHERE (id = id_plan)   \r\n" + 
					"				) AS alumno_planes \r\n" + 
					"				NATURAL JOIN   \r\n" + 
					"				planes_materias  \r\n" + 
					"		) AS ids_materias_alumno   \r\n" + 
					"		NATURAL JOIN   \r\n" + 
					"		materias   \r\n" + 
					"	WHERE (id_materia = id);";
			materia = null;
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			while (rs.next()) {
				materia = Materia.extraerModelo(rs);
				listaMaterias.add(materia);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación de las materias de la carrera ID: " +
					idC + " para el plan del alumno con LU: " + lu + ".\n" + "Detalle: " + mensajeError);
		}
		return listaMaterias;
	}


	public List<Materia> materiasDelPlan (int id) throws DBRetrieveException {
		List<Materia> listaMaterias = new LinkedList<Materia>();
		ResultSet rs;
		String sentenciaSQL = null;
		String mensajeError = null;
		Materia materia;		
		boolean solicitudExitosa = true;
		try
		{
			sentenciaSQL = "SELECT id, nombre, carga_horaria\r\n" +
					"FROM \r\n" + 
	         		"			(\r\n" + 
	         		"				SELECT id_materia as id \r\n" + 
	         		"				FROM planes_materias\r\n" + 
	         		"				WHERE (id_plan = " + id + ")\r\n" + 
	         		"			) as PM\r\n" + 
	         		"			NATURAL JOIN\r\n" + 
	         		"			(\r\n" + 
	         		"				SELECT *\r\n" + 
	         		"				FROM materias\r\n" + 
	         		"			) as M";
			materia = null;
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			while (rs.next()) {
				materia = Materia.extraerModelo(rs);
				listaMaterias.add(materia);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación todas las materias del plan con ID: " +
					id + ".\n" + "Detalle: " + mensajeError);
		}
		return listaMaterias;
	}


	public List<Materia> materiasDictadasPorProfesor (int lg) throws DBRetrieveException {
		List<Materia> listaMaterias = new LinkedList<Materia>();
		ResultSet rs;
		String sentenciaSQL = null;
		String mensajeError = null;
		Materia materia;		
		boolean solicitudExitosa = true;;
		try
		{
			// Genero la sentencia de inserción
			sentenciaSQL = "SELECT DISTINCT * \r\n" + 
					"FROM (\r\n" + 
					"				SELECT id_materia_dictada as id \r\n" + 
					"				FROM dictados \r\n" + 
					"				WHERE (legajo_profesor = " + lg + ")\r\n" + 
					"			) as D\r\n" + 
					"			NATURAL JOIN \r\n" + 
					"			materias;";
			// Ejecuto la recuperación de los dictados
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			while (rs.next()) {
				materia = Materia.extraerModelo(rs);
				listaMaterias.add(materia);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación todas las materias de la carrera de un alumno.\n" +
					"Detalle: " + mensajeError);
		}
		return listaMaterias;
	}
	
	
	public List<Materia> materiasConMesa (int lg) throws DBRetrieveException {
		List<Materia> listaMaterias = new LinkedList<Materia>();
		ResultSet rs;
		String sentenciaSQL = null;
		String mensajeError = null;
		Materia materia;		
		boolean solicitudExitosa = true;;
		try
		{
			// Genero la sentencia de inserción
			sentenciaSQL = "SELECT DISTINCT id, nombre, carga_horaria\r\n" + 
					"FROM (\r\n" + 
					"				SELECT id_materia\r\n" + 
					"				FROM mesas_de_examen\r\n" + 
					"				WHERE (legajo_profesor = " + lg + ")\r\n" + 
					"			) as M\r\n" + 
					"			NATURAL JOIN\r\n" + 
					"			materias\r\n" + 
					"WHERE (id_materia = id);";
			// Ejecuto la recuperación de los dictados
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			while (rs.next()) {
				materia = Materia.extraerModelo(rs);
				listaMaterias.add(materia);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación todas las materias evaluadas en finales por el profesor "
					+ "con legajo: " + lg + ".\n" + "Detalle: " + mensajeError);
		}
		return listaMaterias;
	}

}
