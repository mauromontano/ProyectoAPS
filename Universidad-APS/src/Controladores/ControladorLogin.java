package Controladores;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Conector.ConectorBD;
import Excepciones.ExcepcionAutenticacion;
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
	public void login (String cat, String us, char [] ps) throws ExcepcionAutenticacion {
		String pswd = new String(ps);
		boolean formatoInvalido = false;
		// Si el usuario es administrador, accedo a su correspondiente vista
		if (cat.contentEquals("Administrador")) {
			if (us.contentEquals("admin_uni") && pswd.contentEquals("pwadmin")) {
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
			ConectorBD.obtenerConectorBD().conectarBD();
			try 
			{
				// Creo un comando JDBC para realizar una consulta en la BD
				String consultaSQL;
				Statement stmt = ConectorBD.obtenerConectorBD().nuevoStatement();
				consultaSQL = "SELECT * FROM ";
				// Determino el tipo de categoría para saber en qué tabla de la BD debo corroborar las consultas
				if (cat.contentEquals("Alumno")) 
				{
					consultaSQL += "alumnos WHERE (LU = " + us + " AND dni = " + pswd + ");";
					ResultSet rs = stmt.executeQuery(consultaSQL);
					if (rs.next()) {
						Alumno alumno = Alumno.siguienteModelo(rs);
						VistaAlumno.vista().generarVistaAlumno(alumno);
						// Solicito al controlador de vistas el swap a la vista home de alumno
						ControladorVistas.controlador().mostrar(VistaAlumno.vista());
					}
					else 
					{
						formatoInvalido = true;
					}
				}
				else 
				{
					consultaSQL += "profesores WHERE (dni = " + us + " AND matricula = " + pswd + ");";
					ResultSet rs = stmt.executeQuery(consultaSQL);
					if (rs.next()) {
						Profesor profesor = Profesor.siguienteModelo(rs);
						VistaProfesor.vista().generarVistaProfesor(profesor);
						// Activo la vista correspondiente y desactivo la actual
						ControladorVistas.controlador().mostrar(VistaProfesor.vista());
					}
					else 
					{
						formatoInvalido = true;
					}
				}				
				stmt.close();
			}
			catch (SQLException ex)
			{
				formatoInvalido = true;
			}
			
			ConectorBD.obtenerConectorBD().desconectarBD();
			
			if (formatoInvalido) {
				throw new ExcepcionAutenticacion("Acceso fallido: usuario o contraseña especificados son incorrectos.");
			}
		}
	}

}
