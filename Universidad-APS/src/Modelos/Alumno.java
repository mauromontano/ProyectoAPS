package Modelos;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Alumno {
	
	private int LU;
	private int dni;
	private String nombre;
	private String apellido;
	private String genero;
	
	
	/**
	 * CONSTRUCTOR: modelo de alumno
	 */	
	public Alumno (int LU, int dni, String nombre, String apellido, String genero ) {
		this.LU = LU;
		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
		this.genero = genero;	
	}
	
	
	/**
	 * siguienteModelo: retorna el sigueinte Alumno de una coleccion de filas resultado
	   de una consulta SQL (result set) y desplaza el cursor a la fila próxima.
	 * @param rs: colección de filas de alumnos.
	 * @return primer modelo de alumno de la colección de filas del result set. 
	 */
	public static Alumno siguienteModelo (ResultSet rs) {		
		int lu;
		int d;
		String nom;
		String ap;
		String gen;
		try 
		{
			lu = Integer.parseInt(rs.getString(1));
			d = Integer.parseInt(rs.getString(2));
			nom = rs.getString(3);
			ap = rs.getString(4);
			gen = rs.getString(5);
			rs.next();
			return new Alumno(lu, d, nom, ap, gen);
		}
		catch (NumberFormatException | SQLException ex) 
		{
			ex.printStackTrace();
		}
		return null;
	}	
	
	/**
	 * obtenerLU: retorna el número de libreta universitaria del alumno.
	 * @return número de libreta universitaria asociada al alumno.
	 */
	public int obtenerLU () {
		return LU;
	}	
	
	/**
	 * obtenerLU: retorna el número de libreta universitaria del alumno.
	 * @return número de libreta universitaria asociada al alumno.
	 */
	public int obtenerDni () {
		return dni;
	}	
	
	/**
	 * obtenerLU: retorna el número de libreta universitaria del alumno.
	 * @return número de libreta universitaria asociada al alumno.
	 */
	public String obtenerNombre () {
		return new String(nombre);
	}	
	
	/**
	 * obtenerLU: retorna el número de libreta universitaria del alumno.
	 * @return número de libreta universitaria asociada al alumno.
	 */
	public String obtenerApellido () {
		return new String(apellido);
	}
	
	/**
	 * obtenerLU: retorna el número de libreta universitaria del alumno.
	 * @return número de libreta universitaria asociada al alumno.
	 */
	public String obtenerGenero () {
		return new String(genero);
	}	

}
