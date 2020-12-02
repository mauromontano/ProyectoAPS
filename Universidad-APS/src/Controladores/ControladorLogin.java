package Controladores;

import java.sql.ResultSet;
import java.sql.SQLException;
import Conector.DriverBD;
import Excepciones.AuthException;
import Modelos.Alumno;
import Modelos.Profesor;
import Vistas.VistaAdmin;
import Vistas.VistaAlumno;
import Vistas.VistaProfesor;

public class ControladorLogin {
	
	private static ControladorLogin instancia = null;
	
	/**
	 * controlador: retorna la instancia asociada al controlador
	 * @return panel de la vista inicial
	 */
	public static ControladorLogin controlador () {
		if (instancia == null) {
			instancia = new ControladorLogin();
		}
		return instancia;
	}
	
	/**
	 * login: lleva adelante el acceso de un usuario de, cierta categoría, a su home.
	 * @param cat: cadena de caracteres asociada a la categoría del usuario que desea acceder a su home.
	 * @param us: cadena de caracteres asociada al id del usuario que desea acceder a su home.
	 * @param ps: arreglo de chars asociado a la contraseña del usuario que desea acceder a su home.
	 * @throws ExcepcionAutenticacion: cuando los datos ingresados para la autenticación son inválidos.
	 */
	public void login (String cat, String us, char [] ps) throws AuthException {
		String pswd = new String(ps);
		boolean formatoInvalido = false;
		// Si el usuario es administrador, accedo a su correspondiente vista
		if (cat.contentEquals("Administrador")) {			
			if (us.contentEquals("admin") && pswd.contentEquals("pwadmin")) {
				// Solicito al controlador de vistas el swap a la vista home de administrador
				ControladorVistas.controlador().mostrar(VistaAdmin.vista());
			}
			else 
			{
				formatoInvalido = true;
			}
		}		
		// El usuario no es de tipo administrador, debo confirmar la autenticación con datos de la BD
		else {
			try 
			{
				// Creo un comando JDBC para realizar una consulta en la BD
				String sentenciaSQL = "SELECT * FROM ";
				// Determino el tipo de categoría para saber en qué tabla de la BD debo corroborar las consultas
				if (cat.contentEquals("Alumno")) {
					Alumno alumno;
					sentenciaSQL += "alumnos WHERE (LU = " + us + " AND dni = " + pswd + ");";
					DriverBD.driver().nuevaConexion();
					ResultSet rs = DriverBD.driver().consultar(sentenciaSQL);
					if (rs.next()) {
						alumno = Alumno.extraerModelo(rs);
						VistaAlumno.vista().generarVistaAlumno(alumno);
						ControladorVistas.controlador().mostrar(VistaAlumno.vista());
					}
					else 
					{
						formatoInvalido = true;
					}
					DriverBD.driver().cerrarConexion();
				}
				else 
				{
					Profesor profesor;
					sentenciaSQL += "profesores WHERE (dni = " + us + " AND matricula = " + pswd + ");";					
					DriverBD.driver().nuevaConexion();
					ResultSet rs = DriverBD.driver().consultar(sentenciaSQL);
					if (rs.next()) {
						profesor = Profesor.extraerModelo(rs);
						VistaProfesor.vista().generarHomeProfesor(profesor);
						// Activo la vista correspondiente y desactivo la actual
						ControladorVistas.controlador().mostrar(VistaProfesor.vista());
					}
					else 
					{
						formatoInvalido = true;
					}
					DriverBD.driver().cerrarConexion();
				}
			}
			catch (SQLException ex)
			{
				formatoInvalido = true;
			}
		}
		
		if (formatoInvalido) {
			throw new AuthException("Acceso fallido: usuario o contraseña especificados son incorrectos.");
		}
	}

}
