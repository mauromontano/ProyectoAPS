package Modelos;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Carrera implements Modelo {
	
	private int id;
	private String nombre;
	private int duracion;
	
	/**
	 * CONSTRUCTOR: modelo asociado a una carrera
	 * @param i
	 * @param nom
	 * @param d
	 */
	public Carrera (int i, String nom, int d) {
		id = i;
		nombre = nom;
		duracion = d;
	}
	
	
	/**
	 * siguienteModelo: retorna el sigueinte Alumno de una coleccion de filas resultado
	   de una consulta SQL (result set) y desplaza el cursor a la fila próxima.
	 * @param rs: colección de filas de alumnos.
	 * @return primer modelo de alumno de la colección de filas del result set. 
	 */
	public static Carrera extraerModelo (ResultSet rs) {		
		int i;
		String nom;
		int d;
		try 
		{
			i = Integer.parseInt(rs.getString(1));
			nom = rs.getString(2);
			d = Integer.parseInt(rs.getString(3));
			return new Carrera(i, nom, d);
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
	
	
	public String obtenerNombre () {
		return new String(nombre);
	}
	
	
	public int obtenerDuracion () {
		return duracion;
	}
	
	public String toString () {
		return obtenerNombre() + " [" + obtenerId() + "]"; 
	}

}
