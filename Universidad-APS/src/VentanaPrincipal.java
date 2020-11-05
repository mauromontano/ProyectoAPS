import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import Controladores.ControladorVistas;

import java.awt.Dimension;
import java.awt.SystemColor;
import Vistas.*;

public class VentanaPrincipal extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	// MÉTODO PRINCIPAL
	
	public static void main(String[] args) {
	      SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	            VentanaPrincipal inst = new VentanaPrincipal();
	            inst.setLocationRelativeTo(null);
	            inst.setVisible(true);
	         }
	      });
	}
	
	
	// CONSTRUCTOR: Ventana principal de la aplicación
	
	public VentanaPrincipal() {		
		super();
		getContentPane().setBackground(SystemColor.controlHighlight);
		setTitle("Universidad - APS");
		getContentPane().setMinimumSize(new Dimension(1200, 728));
		setMinimumSize(new Dimension(1200, 728));
		setResizable(false);		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		getContentPane().setLayout(null);
		
		getContentPane().add(VistaInicio.vista());
		getContentPane().add(VistaAlumno.vista());
		getContentPane().add(VistaProfesor.vista());
		getContentPane().add(VistaAdmin.vista());
		getContentPane().add(VistaAdminAlumnos.vista());
		getContentPane().add(VistaAdminCarreras.vista());
		getContentPane().add(VistaAdminMaterias.vista());
		getContentPane().add(VistaAdminPlanes.vista());
		
		ControladorVistas.controlador();
		VistaAlumno.vista().setVisible(false);
		VistaProfesor.vista().setVisible(false);
		VistaAdmin.vista().setVisible(false);
		VistaAdminAlumnos.vista().setVisible(false);
		VistaAdminCarreras.vista().setVisible(false);
		VistaAdminMaterias.vista().setVisible(false);
		VistaAdminPlanes.vista().setVisible(false);				
		
	}
	
}
