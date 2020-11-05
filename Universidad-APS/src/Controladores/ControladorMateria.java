package Controladores;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import Conector.ConectorBD;
import Modelos.Materia;

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
	 * materiasCarrera: retorna una lista de modelos de materias, asociadas a
	   materias registradas en la base de datos, correspondientes a una carrera
	   (plan particular) en la que un alumno está inscripto
	 * @param idC: id de la carrera de la cual se requiere obtener sus materias
	 * @param lu
	 * @return
	 */
	public List<Materia> materiasAlumno (int idC, int lu) {
		List<Materia> listaMaterias = new LinkedList<Materia>();
		Statement stmt;
		ResultSet rs;
		Materia materia;
		String sql;
		ConectorBD.obtenerConectorBD().conectarBD();
		try
		{
			// Creo un comando JDBC para realizar la inserción en la BD
			stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
			// Genero la sentencia de inserción
			sql = "SELECT id, nombre, carga_horaria   \r\n" + 
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
			// Ejecuto la eliminación del alumno
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				materia = Materia.siguienteModelo(rs);
				listaMaterias.add(materia);
				System.out.println(materia.toString());
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
	     return listaMaterias;
	}

}
