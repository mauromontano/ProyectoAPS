package Modelos;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MesaDeExamen implements Modelo {
	
	private int id;
	private int legajoProfesor;
	private int idMateria;
	private String fechaEvaluacion;
	private String horaEvaluacion;
	
	/**
	 * CONSTRUCTOR: modelo asocciado a una mesa de examen
	 */
	public MesaDeExamen (int i, int lp, int im, String f, String h) {
		id = i;
		legajoProfesor = lp;
		idMateria = im;
		fechaEvaluacion = f;
		horaEvaluacion = h;
	}
	
	
	/**
	 * extrarModelo: retorna el sigueinte Alumno de una coleccion de filas resultado
	   de una consulta SQL (result set).
	 * @param rs: colección de filas de alumnos.
	 * @return primer modelo de alumno de la colección de filas del result set. 
	 */
	public static MesaDeExamen extraerModelo (ResultSet rs) {		
		int i;
		int lp;
		int im;
		String fecha;
		String hora;
		try 
		{
			i = Integer.parseInt(rs.getString(1));
			lp = Integer.parseInt(rs.getString(2));
			im = Integer.parseInt(rs.getString(3));
			fecha = rs.getString(4);
			hora = rs.getString(5);
			return new MesaDeExamen(i, lp, im, fecha, hora);
		}
		catch (NumberFormatException | SQLException ex) 
		{
			ex.printStackTrace();
		}
		return null;
	}
	
	
	public int obtenerId () {
		return id;
	}
	
	
	public int obtenerLegajoProfesor () {
		return legajoProfesor;
	}
	
	
	public int obtenerIdMateria () {
		return idMateria;
	}
	
	
	public String obtenerFechaEval () {
		return fechaEvaluacion;
	}
	
	
	public String obtenerHoraEval () {
		return horaEvaluacion;
	}	
	
	
	public String toString () {
		return "Mesa " + id + " [Materia: " + idMateria + " / Docente: " + legajoProfesor + "]"; 
	}

}
