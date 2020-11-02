package Modelos;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Profesor {
	
	private int matricula;
	private int dni;
	private int cuil;
	private String nombre;
	private String apellido;	
	private String genero;
	
	
	/**
	 * CONSTRUCTOR: modelo de profesor
	 */	
	public Profesor (int matr, int dni, int cuil, String nombre, String apellido, String genero ) {
		this.matricula = matr;
		this.dni = dni;
		this.cuil = cuil;
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
	public static Profesor siguienteModelo (ResultSet rs) {		
		int matr;
		int d;
		int c;
		String nom;
		String ap;
		String gen;
		try 
		{
			matr = Integer.parseInt(rs.getString(1));
			d = Integer.parseInt(rs.getString(2));
			c = Integer.parseInt(rs.getString(3));
			nom = rs.getString(4);
			ap = rs.getString(5);
			gen = rs.getString(6);
			rs.next();
			return new Profesor(matr, d, c, nom, ap, gen);
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
	public int obtenerMatricula () {
		return matricula;
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
	public int obtenerCuil () {
		return cuil;
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
