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
import Vistas.VistaInicio;
import Vistas.VistaProfesor;

public class ControladorLogin {
	
	private static ControladorLogin instancia = null;
	
	
	public static ControladorLogin controlador () {
		if (instancia == null) {
			instancia = new ControladorLogin();
		}
		return instancia;
	}
	
	public void login (String cat, String us, char [] ps) throws ExcepcionAutenticacion {
		String pswd = new String(ps);
		
		// Si el usuario es administrador, accedo a su correspondiente vista
		if (cat.contentEquals("Administrador")) {
			if (us.contentEquals("admin_uni") && pswd.contentEquals("pwadmin")) {
				VistaInicio.obtenerVistaInicio().setVisible(false);
				VistaAdmin.obtenerVistaAdmin().setVisible(true);
			}
			else 
			{
				throw new ExcepcionAutenticacion (
						"Acceso fallido [Admin]: usuario o contraseña especificados son incorrectos.");
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
						VistaAlumno.obtenerVistaAlumno().generarVistaAlumno(alumno);
						// Activo la vista correspondiente y desactivo la actual
						VistaInicio.obtenerVistaInicio().setVisible(false);
						VistaAlumno.obtenerVistaAlumno().setVisible(true);
					}
					else 
					{
						throw new ExcepcionAutenticacion (
								"Acceso fallido [Alumno]: usuario o contraseña especificados son incorrectos.");
					}
				}
				else 
				{
					consultaSQL += "profesores WHERE (dni = " + us + " AND matricula = " + pswd + ");";
					ResultSet rs = stmt.executeQuery(consultaSQL);
					if (rs.next()) {
						Profesor profesor = Profesor.siguienteModelo(rs);
						VistaProfesor.obtenerVistaProfesor().generarVistaProfesor(profesor);
						// Activo la vista correspondiente y desactivo la actual
						VistaInicio.obtenerVistaInicio().setVisible(false);
						VistaProfesor.obtenerVistaProfesor().setVisible(true);
					}
					else 
					{
						throw new ExcepcionAutenticacion (
								"Acceso fallido [Profesor]: usuario o contraseña especificados son incorrectos.");
					}
				}				
				stmt.close();
			}
			catch (SQLException ex)
			{
				// en caso de error, se muestra la causa en la consola
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}
			
			ConectorBD.obtenerConectorBD().desconectarBD();
		}
	}
	
	
	public void generarVistaAlumno () {
		
	}
	
	
	public void generarVistaProfesor () {
		
	}

}
