import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.SystemColor;
import Vistas.*;

public class VentanaPrincipal extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	// M�TODO PRINCIPAL
	
	public static void main(String[] args) {
	      SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	            VentanaPrincipal inst = new VentanaPrincipal();
	            inst.setLocationRelativeTo(null);
	            inst.setVisible(true);
	         }
	      });
	}
	
	
	// CONSTRUCTOR: Ventana principal de la aplicaci�n
	
	public VentanaPrincipal() {		
		super();
		getContentPane().setBackground(SystemColor.controlHighlight);
		setTitle("Universidad - APS");
		getContentPane().setMinimumSize(new Dimension(1200, 728));
		setMinimumSize(new Dimension(1200, 728));
		setResizable(false);		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		getContentPane().setLayout(null);
		
		getContentPane().add(VistaPrincipal.obtenerVistaPrincipal());
		getContentPane().add(VistaAlumnos.obtenerVistaAlumnos());
		getContentPane().add(VistaCarreras.obtenerVistaCarreras());
		getContentPane().add(VistaMaterias.obtenerVistaMaterias());
		getContentPane().add(VistaPlanes.obtenerVistaPlanes());
		
		VistaPrincipal.obtenerVistaPrincipal().setVisible(true);
		VistaAlumnos.obtenerVistaAlumnos().setVisible(false);
		VistaCarreras.obtenerVistaCarreras().setVisible(false);
		VistaMaterias.obtenerVistaMaterias().setVisible(false);
		VistaPlanes.obtenerVistaPlanes().setVisible(false);				
		
	}
	
}