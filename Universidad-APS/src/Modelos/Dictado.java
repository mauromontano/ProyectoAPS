package Modelos;

import java.sql.ResultSet;
import java.sql.SQLException;

import Controladores.ControladorProfesor;
import Excepciones.DBRetrieveException;

public class Dictado implements Modelo {

	private int id;
	private int legajoProfesor;
	private int idMateriaDictada;
	private int idPlanMateria;
	private int anio;
	private int cuatrimestre;
	
	
	/**
	 * CONSTRUCTOR: modelo de dictado
	 */	
	public Dictado (int id, int lg, int idm, int ipm, int a, int cuat) {
		this.id = id;
		this.legajoProfesor = lg;
		this.idMateriaDictada = idm;
		this.idPlanMateria = ipm;
		this.anio = a;
		this.cuatrimestre = cuat;
	}
	
	
	/**
	 * siguienteModelo: retorna el sigueinte Dictado de una coleccion de filas resultado
	   de una consulta SQL (result set) y desplaza el cursor a la fila pr�xima.
	 * @param rs: colecci�n de filas de dictados.
	 * @return primer modelo de dictado de la colecci�n de filas del result set. 
	 */
	public static Dictado extraerModelo (ResultSet rs) {		
		int id;
		int lg;
		int idm;
		int ipm;
		int a;
		int cuat;
		try 
		{
			id = Integer.parseInt(rs.getString(1));
			lg = Integer.parseInt(rs.getString(2));
			idm = Integer.parseInt(rs.getString(3));
			ipm = Integer.parseInt(rs.getString(4));
			a = Integer.parseInt(rs.getString(5));
			cuat = Integer.parseInt(rs.getString(6));
			return new Dictado(id, lg, idm, ipm, a, cuat);
		}
		catch (NumberFormatException | SQLException ex) 
		{
			ex.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * obtenerId: retorna el ID correspondiente al dictado.
	 * @return n�mero de identificaci�n asociado al dictado.
	 */
	public int obtenerId () {
		return id;
	}
	
	
	/**
	 * obtenerLegajoProfesor: retorna el n�mero de legajo universitario del profesor.
	   a cargo del dictado.
	 * @return n�mero de legajo universitario asociado al profesor encargado.
	 */
	public int obtenerLegajoProfesor () {
		return legajoProfesor;
	}	
	
	
	/**
	 * obtenerIdMateriaDictada: retorna el ID de la materia involucrada en el dictado.
	 * @return n�mero de identificaci�n de la materia asociada al dictado
	 */
	public int obtenerIdMateriaDictada () {
		return idMateriaDictada;
	}	
	
	/**
	 * obtenerIdPlanMateria: retorna el ID del plan al cual pertenece la materia dictada.
	 * @return n�mero de identificaci�n del plan de carrera al que pertenece la materia.
	 */
	public int obtenerIdPlanMateria () {
		return idPlanMateria;
	}
	
	/**
	 * obtenerAnio: retorna el a�o en el cual se efect�a el dictado.
	 * @return a�o de dictado.
	 */
	public int obtenerAnio () {
		return anio;
	}
	
	/**
	 * obtenerCuatrimestre: retorna el cuatrimestre del a�o en el cual se realiza el dictado.
	 * @return cuatrimestre en el cual se desarrolla el dictado.
	 */
	public int obtenerCuatrimestre () {
		return cuatrimestre;
	}
	
	
	public Profesor profesorACargo() {
		String [] in = {legajoProfesor + "", null, null, null, null, null, null};
		Profesor profesor = null;
		try 
		{
			profesor = ControladorProfesor.controlador().recuperar(in);
		} 
		catch (DBRetrieveException e) 
		{
			e.printStackTrace();
		}
		return profesor;
	}
	
	
	public String toString () {
		String nom;
		String ap;
		Profesor p = profesorACargo();
		nom = p.obtenerNombre();
		ap = p.obtenerApellido();
		return "Docente: " + nom + " " + ap + " - Dictado [" + id + "]";
	}
	

}
