package Vistas;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;


public class VistaPrincipal extends JPanel {
		
	
	private static final long serialVersionUID = 1L;
	private static VistaPrincipal instancia = null;
	
	
	public static VistaPrincipal obtenerVistaPrincipal () {
		
		if (instancia == null) {
			instancia = new VistaPrincipal();
		}
		return instancia;
	}
	
	
	private VistaPrincipal() {
				
		this.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		this.setBackground(SystemColor.menu);
		this.setBounds(420, 239, 368, 240);
		this.setLayout(null);
		
		JButton btnSecAlumnos = new JButton("Alumnos");
		btnSecAlumnos.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 14));
		btnSecAlumnos.setBounds(102, 43, 160, 31);
		btnSecAlumnos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				VistaAlumnos.obtenerVistaAlumnos().setVisible(true);
			}
		});
		this.add(btnSecAlumnos);
		
		JButton btnSecCarreras = new JButton("Carreras");
		btnSecCarreras.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 14));
		btnSecCarreras.setBounds(102, 85, 160, 31);
		btnSecCarreras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				VistaCarreras.obtenerVistaCarreras().setVisible(true);
			}
		});
		this.add(btnSecCarreras);
		
		JButton btnSecMaterias = new JButton("Materias");
		btnSecMaterias.setFont(new Font("Microsoft YaHei Light", Font.PLAIN, 14));
		btnSecMaterias.setBounds(102, 169, 160, 31);
		btnSecMaterias.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				VistaMaterias.obtenerVistaMaterias().setVisible(true);
			}
		});
		this.add(btnSecMaterias);
		
		JButton btnSecPlanes = new JButton("Planes");
		btnSecPlanes.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 14));
		btnSecPlanes.setBounds(102, 127, 160, 31);
		btnSecPlanes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				VistaPlanes.obtenerVistaPlanes().setVisible(true);
			}
		});
		this.add(btnSecPlanes);
		
	}
	
	

}
