package Modelos;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActaCursado {
	
	private int LU;
	private String nombre;
	private String apellido;
	private String estado;
	
	
	

	public ActaCursado(int lU, String nombre, String apellido, String estado) {
		super();
		LU = lU;
		this.nombre = nombre;
		this.apellido = apellido;
		this.estado = estado;
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


	public int getLU() {
		return LU;
	}


	public void setLU(int lU) {
		LU = lU;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public static ActaCursado extraerModelo (ResultSet rs) {		
		int lu;
		String nom;
		String ap;
		String estado;
		try 
		{
			lu = Integer.parseInt(rs.getString(1));
			nom = rs.getString(2);
			ap = rs.getString(3);
			estado = rs.getString(4);
			return new ActaCursado(lu, nom, ap, estado);
		}
		catch (NumberFormatException | SQLException ex) 
		{
			ex.printStackTrace();
		}
		return null;
	}	

}
