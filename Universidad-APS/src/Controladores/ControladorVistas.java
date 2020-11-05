package Controladores;

import javax.swing.JPanel;

import Vistas.VistaInicio;

public class ControladorVistas {
	
	private static ControladorVistas instancia = null;
	private JPanel vistaActual;
	
	/**
	 * CONSTRUCTOR
	 */
	private ControladorVistas () {
		vistaActual = VistaInicio.vista();
		vistaActual.setVisible(true);
	}
	
	/**
	 * controlador: retorna la instancia asociada al controlador
	 * @return controlador para el manejo del intercambio de vistas
	 */
	public static ControladorVistas controlador () {
		if (instancia == null) {
			instancia = new ControladorVistas();
		}
		return instancia;
	}
	
	/**
	 * mostrar: muestra en la interfaz de la aplicación la vista dada como parámetro,
	   pasando a ser la vista actual, quedando la anterior como invisible
	 * @param nuevaVista: vista a mostrar
	 */
	public void mostrar (JPanel nuevaVista) {
		vistaActual.setVisible(false);
		vistaActual = nuevaVista;
		vistaActual.setVisible(true);
		
	}
	
	
	
	

}
