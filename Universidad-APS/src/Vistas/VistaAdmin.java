package Vistas;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import Controladores.ControladorVistas;


public class VistaAdmin extends JPanel {
		
	
	private static final long serialVersionUID = 1L;
	private static VistaAdmin instancia = null;
	
	
	public static VistaAdmin vista () {
		
		if (instancia == null) {
			instancia = new VistaAdmin();
		}
		return instancia;
	}
	
	
	private VistaAdmin() {
		
		JButton btnAtras = new JButton("Atr\u00E1s");
        btnAtras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// Vuelvo a la vista anterior, la vista de inicio
        		ControladorVistas.controlador().mostrar(VistaInicio.vista());
        	}
        });
        btnAtras.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnAtras.setBounds(10, 11, 70, 23);
        add(btnAtras);
				
		this.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		this.setBackground(SystemColor.menu);
		this.setBounds(420, 239, 368, 240);
		this.setLayout(null);
		
		JButton btnSecAlumnos = new JButton("Alumnos");
		btnSecAlumnos.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 14));
		btnSecAlumnos.setBounds(102, 43, 160, 31);
		btnSecAlumnos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Swap a la vista de administración de alumnos
				ControladorVistas.controlador().mostrar(VistaAdminAlumnos.vista());
			}
		});
		this.add(btnSecAlumnos);
		
		JButton btnSecCarreras = new JButton("Carreras");
		btnSecCarreras.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 14));
		btnSecCarreras.setBounds(102, 85, 160, 31);
		btnSecCarreras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Swap a la vista de administración de carreras
				ControladorVistas.controlador().mostrar(VistaAdminCarreras.vista());
			}
		});
		this.add(btnSecCarreras);
		
		JButton btnSecMaterias = new JButton("Materias");
		btnSecMaterias.setFont(new Font("Microsoft YaHei Light", Font.PLAIN, 14));
		btnSecMaterias.setBounds(102, 169, 160, 31);
		btnSecMaterias.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Swap a la vista de administración de materias
				ControladorVistas.controlador().mostrar(VistaAdminMaterias.vista());
			}
		});
		this.add(btnSecMaterias);
		
		JButton btnSecPlanes = new JButton("Planes");
		btnSecPlanes.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 14));
		btnSecPlanes.setBounds(102, 127, 160, 31);
		btnSecPlanes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Swap a la vista de administración de planes
				ControladorVistas.controlador().mostrar(VistaAdminPlanes.vista());
			}
		});
		this.add(btnSecPlanes);
		
	}
	
	

}
