package Controladores;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import Conector.DriverBD;
import Excepciones.DBRetrieveException;
import Excepciones.DBUpdateException;
import Modelos.MesaDeExamen;
import Modelos.Modelo;
import quick.dbtable.DBTable;

public class ControladorMesa implements ControladorModelo {
	
private static ControladorMesa instancia = null;
	
	/**
	 * controlador: retorna la instancia asociada al controlador
	 * @return controlador asociado a alumnos
	 */
	public static ControladorMesa controlador () {
		if (instancia == null) {
			instancia = new ControladorMesa();
		}
		return instancia;
	}
	
	/**
	 * registrar: permite el registro de una mesa de examen en base a datos de entrada asociados a sus
	   atributos
	 * @param inputs: arreglo de atributos de la nueva mesa de examen a registrar
	 */
	public void registrar (String [] inputs) throws DBUpdateException {
		String sentenciaSQL;
		String mensajeError = null;
		boolean solicitudExitosa = true;
		try
		{
			// Genero la sentencia de inserción
			sentenciaSQL = "INSERT INTO mesas_de_examen (legajo_profesor, id_materia, fecha_evaluacion, hora_evaluacion) VALUES " +
					"(" + inputs[0] + "," +
					"\'" + inputs[1] + "\'," +
					"str_to_date(\'" + inputs[2] + "\', '%d/%m/%Y')," +
					"\'" + inputs[3] + "\');";
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
			throw new DBUpdateException("¡ERROR! falló el registro de la nueva mesa de exámen.\n" +
					"Detalle: " + mensajeError);
		}
	}
	
	
	/**
	 * modificar: modifica datos de registro de una mesa de examen en base a datos
	   de entrada asociados a sus atributos
	 * @param inputs: arreglo de atributos de la mesa de examen a modificar
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
				sentenciaSQL = "UPDATE mesas_de_examen SET " + asignacionesSQL + " WHERE (id = " + inputs[0] + ")";
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
			throw new DBUpdateException("¡ERROR! falló la modificación de datos de la mesa de examen con ID: " + inputs[0] + ".\n" +
					"Detalle: " + mensajeError);
		}
	}
	
	
	/**
	 * eliminar: elimina datos de registro de una mesa de examen en base a datos
	   de entrada asociados a sus atributos
	 * @param inputs: arreglo de atributos de la mesa de examen a eliminar
	 */
	public void eliminar (String input) throws DBUpdateException {
		String sentenciaSQL;
		String mensajeError = null;
		boolean solicitudExitosa = true;
		try
		{
			// Genero la sentencia de inserción
			sentenciaSQL = "DELETE FROM mesas_de_examen WHERE (id = " + input + ")";
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
			throw new DBUpdateException("¡ERROR! falló la baja de la mesa de examen con ID: " + input + ".\n" +
					"Detalle: " + mensajeError);
		}		
	}
	
	
	/**
	 * volcar: solicita la carga de datos asociados a las mesas de examen registrados en
	   el sistema.
	 * tb: objeto DBTable (tabla de datos) donde se aplicará la carga de los datos de alumnos.
	 */
	public void volcar (DBTable tb) throws DBRetrieveException {
		String sentenciaSQL;
		boolean consultaExitosa = true;
		
		try
	    {
			sentenciaSQL = "SELECT * FROM mesas_de_examen";
			DriverBD.driver().volcar(tb,sentenciaSQL);
	    }		
		catch (SQLException ex)
		{
			consultaExitosa = false;
	    }
		// Si la consulta no fue exitosa, entonces falló el volcado de datos
		if (!consultaExitosa) {
			throw new DBRetrieveException("¡ERROR! falló el volcado de los datos de mesas_de_examen en la tabla");
		}
	}
	
	
	public MesaDeExamen recuperar (String [] inputs) throws DBRetrieveException {
		ResultSet rs;
		String sentenciaSQL;
		String asignacionesSQL = null;
		String mensajeError = null;
		MesaDeExamen mesa = null;		
		boolean solicitudExitosa = true;	
		try
		{
			asignacionesSQL = generarAsignacionesSQL(inputs);
			sentenciaSQL = "SELECT * FROM mesas_de_examen WHERE (id = " + inputs[0];
			// Si hay asignaciones adicionales, las incorporo a la sentencia
			if (asignacionesSQL.length() > 0) {
				sentenciaSQL +=  ", " + asignacionesSQL;
			}
			sentenciaSQL += ")";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			// Si hay un resultado siguiente, lo recupero
			if (rs.next()) {
				mesa = MesaDeExamen.extraerModelo(rs);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("¡ERROR! falló la recuperación de la mesa con parámetros " + Arrays.deepToString(inputs) + ".\n" +
					"Detalle: " + mensajeError);
		}
		return mesa;
	}
	
	
	private String generarAsignacionesSQL (String [] inputs) {
		String asignacionesSQL = "";
		boolean hayAsignacionAnterior = false;
		// Genero la sentencia de inserción
		if (inputs[1] != null) {
			asignacionesSQL += "legajo_profesor = " + inputs[1];
			hayAsignacionAnterior = true;
		}
		if (inputs[2] != null) {
			if (hayAsignacionAnterior) {
				asignacionesSQL += ", ";				
			}
			else hayAsignacionAnterior = true;
			asignacionesSQL += "id_materia = " + inputs[2];
			
		}
		if (inputs[3] != null) {
			if (hayAsignacionAnterior) {
				asignacionesSQL += ", ";
			}
			else hayAsignacionAnterior = true;
			asignacionesSQL += "fecha_evaluacion = str_to_date(\'" + inputs[3] + "\', '%d/%m/%Y')";
		}
		if (inputs[4] != null) {
			if (hayAsignacionAnterior) {
				asignacionesSQL += ", ";
			}
			else hayAsignacionAnterior = true;
			asignacionesSQL += "hora_evaluacion = \'" + inputs[4] + "\'";
		}
		return asignacionesSQL;
	}
	
	
	/**
	 * elementos: permite obtener una lista de modelos de alumnos, asociados a
	   las alumnos registradas en la base de datos
	 * @return lista de modelos de alumnos
	 */
	public List<Modelo> elementos () throws DBRetrieveException {
		List<Modelo> listaMesas = new LinkedList<Modelo>();
		ResultSet rs;
		MesaDeExamen mesa;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;	
		try
		{
			sentenciaSQL = "SELECT * FROM alumnos";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			// Mientras haya un resultado siguiente, lo recupero
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
			throw new DBRetrieveException("¡ERROR! falló la recuperación todas las mesas.\n" +
					"Detalle: " + mensajeError);
		}
		return listaMesas;
	}
	
	
	public List<MesaDeExamen> mesasProfesorMateria (int idMat, int lg) throws DBRetrieveException {
		List<MesaDeExamen> listaMesas = new LinkedList<MesaDeExamen>();
		ResultSet rs;
		MesaDeExamen mesa;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;	
		try
		{
			sentenciaSQL = "SELECT *\r\n" + 
					"FROM mesas_de_examen\r\n" + 
					"WHERE (legajo_profesor = " + lg + " AND id_materia = " + idMat + ");";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			// Mientras haya un resultado siguiente, lo recupero
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
			throw new DBRetrieveException("¡ERROR! falló la recuperación todas las mesas.\n" +
					"Detalle: " + mensajeError);
		}
		return listaMesas;
	}
	
	
	public List<MesaDeExamen> mesasFuturasDeMateria (int idMat) throws DBRetrieveException {
		List<MesaDeExamen> listaMesas = new LinkedList<MesaDeExamen>();
		ResultSet rs;
		MesaDeExamen mesa;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;	
		try
		{
			sentenciaSQL = "SELECT *\r\n" + 
					"FROM mesas_de_examen\r\n" + 
					"WHERE (id_materia = " + idMat + " AND fecha_evaluacion > CURDATE());";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			// Mientras haya un resultado siguiente, lo recupero
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
			throw new DBRetrieveException("¡ERROR! falló la recuperación todas las mesas.\n" +
					"Detalle: " + mensajeError);
		}
		return listaMesas;
	}

	
	public void asignarCalificacion(int lu, int idMesa, String estado, String nota) throws DBUpdateException {
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;
		try
		{
			sentenciaSQL = "INSERT INTO calificaciones_finales (LU_alumno, id_mesa, fecha_calificacion, estado, puntaje) VALUES " + 
	        		 	 "(" + lu + "," + 
	        		 	 "" + idMesa + "," +
	        		 	 " CURDATE()," +
	        		 	 "\'" + estado + "\'," +
	        		 	 "" + nota + ");";
			
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
					" sobre la mesa de examen con ID: " + idMesa + ".\n" + "Detalle: " + mensajeError);
		}
		
	}

}
