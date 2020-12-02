package Controladores;

import java.sql.ResultSet;
import java.sql.SQLException;
import Conector.DriverBD;
import Excepciones.DBRetrieveException;
import Excepciones.DBUpdateException;
import Excepciones.InvalidActionException;
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
	 * registrarInsCarrera: realiza el registro de una inscripci�n de un alumno,
	   con LU particular, en una carrera registrada, con id asociado
	 * @param lu: n�mero de libreta universitaria asociado al alumno
	 * @param id: identificador asociado a la carrera involucrada en la inscripci�n 
	 */
	public void registrarInsCar (int lu, int id) throws DBUpdateException {
		ResultSet rs;
		Plan plan = null;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean operacionExitosa = true;
		try
		{
			sentenciaSQL = "SELECT * FROM planes WHERE (id_carrera = " + id + ") ORDER BY id desc LIMIT 1;";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			if (rs.next()) {
				plan = Plan.extraerModelo(rs);
			}			
			DriverBD.driver().cerrarConexion();
			if (plan != null) {
				sentenciaSQL = "INSERT INTO inscripciones_carreras (LU_alumno, id_plan, fecha) VALUES (" +
						lu + "," +
						plan.obtenerId() + "," +
						"CURDATE());";
				DriverBD.driver().nuevaConexion();
				DriverBD.driver().actualizar(sentenciaSQL);
				DriverBD.driver().cerrarConexion();
			}			
		}
		catch (SQLException ex)
		{
			operacionExitosa = false;
			mensajeError = ex.getMessage();
		}

		if (!operacionExitosa) {
			throw new DBUpdateException("�ERROR! fall� el registro de la inscripci�n del alumno con LU: " +
					lu + " para la carrera con ID: " + id + ".\n" + "Detalle: " + mensajeError);
		}
	}

	
	/**
	 * registrarInsMateria: realiza el registro de una inscripci�n de un alumno,
	   con LU particular, en una materia registrada, parte de su plan de carrera,
	   con id asociado
	 * @param lu: n�mero de libreta universitaria asociado al alumno
	 * @param id: identificador asociado a la materia involucrada en la inscripci�n 
	 * @throws DBUpdateException 
	 */
	public void registrarInsMat (int lu, int idMat, int idDict) throws InvalidActionException, DBUpdateException {
		ResultSet rs;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean operacionExitosa = true;
		boolean existeInscripcion = false;
		boolean cumple = true;
		try
		{
			cumple = cumpleConCorrelativas(lu, idMat);
			// Verifico si se pasa el control de correlativas para proceder al registro de la inscripci�n
			if (cumple) {
				sentenciaSQL = "SELECT * \r\n" + 
						"FROM (\r\n" + 
						"				SELECT * \r\n" + 
						"				FROM inscripciones_dictados\r\n" + 
						"				WHERE (LU_alumno = " + lu + " AND estado = 'Activa')\r\n" + 
						"			) as DS\r\n" + 
						"			NATURAL JOIN\r\n" + 
						"			(\r\n" + 
						"				SELECT id, id_materia_dictada \r\n" + 
						"				FROM dictados\r\n" + 
						"			) as DT\r\n" + 
						"WHERE (id = id_dictado AND id_materia_dictada = " + idMat + ");";
				DriverBD.driver().nuevaConexion();
				rs = DriverBD.driver().consultar(sentenciaSQL);
				if (rs.next()) {
					existeInscripcion = true;
				}
				else 
				{
					DriverBD.driver().cerrarConexion();
					// Genero la sentencia de inserci�n
					sentenciaSQL = "INSERT INTO inscripciones_dictados (LU_alumno, id_dictado, fecha) VALUES (" +
							lu + "," +
							idDict + "," +
							"curdate());";
					// Ejecuto la inserci�n del nuevo alumno
					DriverBD.driver().nuevaConexion();
					DriverBD.driver().actualizar(sentenciaSQL);
				}
				DriverBD.driver().cerrarConexion();			}
			else {
				operacionExitosa = false;
			}
			
		}
		catch (SQLException | DBRetrieveException | InvalidActionException ex)
		{
			operacionExitosa = false;
			mensajeError = ex.getMessage();
		}
		
		// Si existe una inscripci�n activa, hay un error
		if (existeInscripcion) {
			throw new InvalidActionException("Registro de inscripci�n a materia fallido: " + 
					"ya existe una inscripci�n activa para la materia con ID: " + idDict);
		}
		// Independientemente de si existe una inscripci�n activa, si falla el registro de la inscripci�n a la BD, hay error
		if (!operacionExitosa) {
			throw new DBUpdateException("�ERROR! fall� el registro de la inscripci�n del alumno con LU: " +
					lu + " al dictado de la materia con ID: " + idMat + ".\n" + "Detalle: " + mensajeError);
		}		
	}
	
	
	public void registrarInsFinal (int lu, int idMat, int idMesa) throws InvalidActionException, DBUpdateException {
		ResultSet rs;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean operacionExitosa = true;
		boolean existeInscripcion = false;
		try
		{
			sentenciaSQL = "SELECT *\r\n" + 
					"FROM inscripciones_finales\r\n" + 
					"WHERE (id_mesa = " + idMesa + " AND LU_alumno = " + lu + ");";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			if (rs.next()) {
				existeInscripcion = true;
			}
			else 
			{
				DriverBD.driver().cerrarConexion();
				// Genero la sentencia de inserci�n
				sentenciaSQL = "INSERT INTO inscripciones_finales (LU_alumno, id_mesa, fecha_inscripcion) VALUES (" +
						lu + "," +
						idMesa + "," +
						"CURDATE());";
				// Ejecuto la inserci�n del nuevo alumno
				DriverBD.driver().nuevaConexion();
				DriverBD.driver().actualizar(sentenciaSQL);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			operacionExitosa = false;
			mensajeError = ex.getMessage();
		}
		
		// Si existe una inscripci�n vigente, hay un error
		if (existeInscripcion) {
			throw new InvalidActionException("Registro de inscripci�n a materia fallido: " + 
					"ya existe una inscripci�n activa a la mesa de examen con ID: " + idMesa);
		}
		// Independientemente de si existe una inscripci�n activa, si falla el registro de la inscripci�n a la BD, hay error
		if (!operacionExitosa) {
			throw new DBUpdateException("�ERROR! fall� el registro de la inscripci�n del alumno con LU: " +
					lu + " para la mesa de examen " + idMesa + ", asociado a la materia con ID: " + idMat +
					".\n" + "Detalle: " + mensajeError);
		}
	}
	
	
	public boolean cumpleConCorrelativas (int lu, int idMatDeseada) throws InvalidActionException, DBRetrieveException {
		ResultSet rs;
		String sentenciaSQL;
		String mensajeError = null;
		int requeridas = 0;
		int aprobadas = 0;
		boolean cumpleControl = true;
		boolean operacionExitosa = true;
		try
		{
			sentenciaSQL = "SELECT id_materia as id_materia_dictada\r\n" + 
					"FROM correlativas\r\n" + 
					"WHERE (id_correlativa = " + idMatDeseada + ")";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			// Cuento el n�mero de materias, las cuales necesito tener cursado aprobado.
			while (rs.next()) {
				requeridas++;
			}
			DriverBD.driver().cerrarConexion();
			// Si la cantidad de materias requeridas es mayor a 1, entonces verifico cu�les est�n aprobadas
			if (requeridas > 0) {
				sentenciaSQL = "SELECT DISTINCT id_materia\r\n" + 
						"FROM (\r\n" + 
						"				SELECT LU_alumno, id_materia_dictada as id_materia, estado\r\n" + 
						"				FROM (\r\n" + 
						"						SELECT DISTINCT id as id_dictado, legajo_profesor, id_materia_dictada, id_plan_materia, anio, cuatrimestre\r\n" + 
						"						FROM (\r\n" + 
						"								SELECT id_materia as id_materia_dictada\r\n" + 
						"								FROM correlativas\r\n" + 
						"								WHERE (id_correlativa = " + idMatDeseada + ")\r\n" + 
						"							) as C\r\n" + 
						"							NATURAL JOIN\r\n" + 
						"							dictados\r\n" + 
						"					) as D\r\n" + 
						"					NATURAL JOIN\r\n" + 
						"					calificaciones_dictados\r\n" + 
						"	) as N\r\n" + 
						"WHERE (LU_alumno = " + lu + " AND estado = \"Aprobado\");";
				// Busco las materias con cursado aprobado, entre las requeridas
				DriverBD.driver().nuevaConexion();
				rs = DriverBD.driver().consultar(sentenciaSQL);
				// Cuento las aprobadas
				while (rs.next()) {
					aprobadas++;
				}
				cumpleControl = requeridas == aprobadas;
			}						
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			cumpleControl = false;
			mensajeError = ex.getMessage();
		}
		
		
		// Independientemente de si existe una inscripci�n activa, si falla el registro de la inscripci�n a la BD, hay error
		if (!operacionExitosa) {
			throw new DBRetrieveException("fall� el control de correlativas  de la inscripci�n del alumno con LU: " +
					lu + " a un dictado de la materia con ID: " + idMatDeseada + ".\n" + "Detalle: " + mensajeError);
		}
		else if (!cumpleControl) {
			throw new InvalidActionException("no se cumplen los requisitos requeridos para cursar la materia con ID: " + idMatDeseada);
		}
		return requeridas == aprobadas;
	}
	

}
