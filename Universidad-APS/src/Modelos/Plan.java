package Modelos;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Plan implements Modelo {
	
	private int id;
	private int idCarrera;
	private int version;
	
	
	public Plan (int i, int ic, int v) {
		id = i;
		idCarrera = ic;
		version = v;
	}	
	
	/**
	 * siguienteModelo: retorna el sigueinte Alumno de una coleccion de filas resultado
	   de una consulta SQL (result set) y desplaza el cursor a la fila próxima.
	 * @param rs: colección de filas de alumnos.
	 * @return primer modelo de alumno de la colección de filas del result set. 
	 */
	public static Plan extraerModelo (ResultSet rs) {		
		int i;
		int ic;
		int v;
		try 
		{
			i = Integer.parseInt(rs.getString(1));
			ic = Integer.parseInt(rs.getString(2));
			v = Integer.parseInt(rs.getString(3));
			return new Plan(i, ic, v);
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
	
	
	public int obtenerNombreCarrera () {
		return idCarrera;
	}
	
	
	public int obtenerVersion () {
		return version;
	}
	
	public String toString () {
		return "" + version;
	}


}
