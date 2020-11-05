package Modelos;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Materia {
	
	private int id;
	private String nombre;
	private int cargaHoraria;
	
	/**
	 * CONSTRUCTOR: modelo asocciado a una materia
	 * @param i: id correspondiente a la materia
	 * @param nom: nombre asociado a la materia
	 * @param c: carga horaria que posee la materia
	 */
	public Materia (int i, String nom, int c) {
		id = i;
		nombre = nom;
		cargaHoraria = c;
	}
	
	
	/**
	 * siguienteModelo: retorna el sigueinte Alumno de una coleccion de filas resultado
	   de una consulta SQL (result set) y desplaza el cursor a la fila próxima.
	 * @param rs: colección de filas de alumnos.
	 * @return primer modelo de alumno de la colección de filas del result set. 
	 */
	public static Materia siguienteModelo (ResultSet rs) {		
		int i;
		String nom;
		int c;
		try 
		{
			i = Integer.parseInt(rs.getString(1));
			nom = rs.getString(2);
			c = Integer.parseInt(rs.getString(3));
			return new Materia(i, nom, c);
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
	
	
	public int obtenerCargaHoraria () {
		return cargaHoraria;
	}
	
	public String toString () {
		return obtenerNombre() + " [" + obtenerId() + "]"; 
	}

	
	

}
