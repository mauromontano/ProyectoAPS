package Controladores;

import java.util.List;
import Excepciones.DBRetrieveException;
import Excepciones.DBUpdateException;
import Modelos.Modelo;
import quick.dbtable.DBTable;


public interface ControladorModelo {
	
	public void registrar (String [] inputs) throws DBUpdateException;
	
	public void modificar (String [] inputs) throws DBUpdateException;
	
	public void eliminar (String input) throws DBUpdateException;
	
	public Modelo recuperar (String [] inputs) throws DBRetrieveException;
	
	public List<Modelo> elementos () throws DBRetrieveException;
	
	public void volcar (DBTable tb) throws DBRetrieveException;
	
	//public void extraerModelo (ResultSet rs);

}
