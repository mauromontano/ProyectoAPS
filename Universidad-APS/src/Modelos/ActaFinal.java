package Modelos;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActaFinal {
	
	
	private int LU;
	private String nombre;
	private String apellido;
	private int nota;
	
	
	
	
	
	
	public ActaFinal(int lU, String nombre, String apellido, int nota) {
		super();
		LU = lU;
		this.nombre = nombre;
		this.apellido = apellido;
		this.nota = nota;
	}






	public int getLU() {
		return LU;
	}






	public void setLU(int lU) {
		LU = lU;
	}






	public String getNombre() {
		return nombre;
	}






	public void setNombre(String nombre) {
		this.nombre = nombre;
	}






	public String getApellido() {
		return apellido;
	}






	public void setApellido(String apellido) {
		this.apellido = apellido;
	}






	public int getNota() {
		return nota;
	}






	public void setNota(int nota) {
		this.nota = nota;
	}






	public static ActaFinal extraerModelo (ResultSet rs) {		
		int lu;
		String nom;
		String ap;
		int puntaje;
		try 
		{
			lu = Integer.parseInt(rs.getString(1));
			nom = rs.getString(2);
			ap = rs.getString(3);
			puntaje = Integer.parseInt(rs.getString(4));
			return new ActaFinal(lu, nom, ap, puntaje);
		}
		catch (NumberFormatException | SQLException ex) 
		{
			ex.printStackTrace();
		}
		return null;
	}	

}
