package Controladores;

import javax.swing.JPanel;

public class ControladorVistas {
	
	private static ControladorVistas instancia = null;
	private JPanel vistaActual = null;
	
	
	public static ControladorVistas controlador () {
		if (instancia == null) {
			instancia = new ControladorVistas();
		}
		return instancia;
	}
	
	
	public void mostrar (JPanel nuevaVista) {
		vistaActual.setVisible(false);
		nuevaVista.setVisible(true);
		vistaActual = nuevaVista;
		
	}
	
	
	
	

}
