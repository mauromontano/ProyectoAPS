package Modelos;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Profesor implements Modelo {
	
	private int legajo;
	private int cuil;
	private int matricula;
	private int dni;
	private String nombre;
	private String apellido;	
	private String genero;
	
	
	/**
	 * CONSTRUCTOR: modelo de profesor
	 */	
	public Profesor (int lg, int matr, int dni, int cuil, String nombre, String apellido, String genero) {
		this.legajo = lg;
		this.matricula = matr;
		this.dni = dni;
		this.cuil = cuil;
		this.nombre = nombre;
		this.apellido = apellido;
		this.genero = genero;	
	}
	
	
	/**
	 * siguienteModelo: retorna el sigueinte Profesor de una coleccion de filas resultado
	   de una consulta SQL (result set) y desplaza el cursor a la fila pr�xima.
	 * @param rs: colecci�n de filas de profesores.
	 * @return primer modelo de profesor de la colecci�n de filas del result set. 
	 */
	public static Profesor extraerModelo (ResultSet rs) {		
		int lg;
		int matr;
		int d;
		int c;
		String nom;
		String ap;
		String gen;
		try 
		{
			lg = Integer.parseInt(rs.getString(1));
			matr = Integer.parseInt(rs.getString(2));
			d = Integer.parseInt(rs.getString(3));
			c = Integer.parseInt(rs.getString(4));
			nom = rs.getString(5);
			ap = rs.getString(6);
			gen = rs.getString(7);
			return new Profesor(lg, matr, d, c, nom, ap, gen);
		}
		catch (NumberFormatException | SQLException ex) 
		{
			System.out.println("Fernandez la concha puta de tu madre");
		}
		return null;
	}
	
	/**
	 * obtenerLegajo: retorna el n�mero de legajo universitario del profesor.
	 * @return n�mero de legajo universitario asociado al profesor.
	 */
	public int obtenerLegajo () {
		return legajo;
	}	
	
	
	/**
	 * obtenerMatr�cula: retorna el n�mero de matr�cula del profesor.
	 * @return n�mero de matr�cula asociado al profesor.
	 */
	public int obtenerMatricula () {
		return matricula;
	}	
	
	/**
	 * obtenerDni: retorna el n�mero de documento del profesor.
	 * @return n�mero de documento asociado al profesor.
	 */
	public int obtenerDni () {
		return dni;
	}
	
	/**
	 * obtenerCuil: retorna el n�mero de CUIL del profesor.
	 * @return n�mero de CUIL asociado al profesor.
	 */
	public int obtenerCuil () {
		return cuil;
	}
	
	/**
	 * obtenerNombre: retorna el nombre de pila del profesor.
	 * @return nombre de pila correspondiente al profesor.
	 */
	public String obtenerNombre () {
		return new String(nombre);
	}	
	
	/**
	 * obtenerNombre: retorna el apellido del profesor.
	 * @return apellido correspondiente al profesor.
	 */
	public String obtenerApellido () {
		return new String(apellido);
	}
	
	/**
	 * obtenerGenero: retorna el g�nero alumno.
	 * @return g�nero correspondiente al alumno.
	 */
	public String obtenerGenero () {
		return new String(genero);
	}
	
	
	public String toString () {
		return nombre + " " + apellido + " [" + legajo + "]";
	}

}
