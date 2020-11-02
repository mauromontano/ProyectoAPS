package Vistas;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JTabbedPane;

public abstract class VistaHome extends JPanel {
	
	protected static final long serialVersionUID = 1L;
	protected JLabel lblIdentidad;
	protected JPanel panelBienvenida;
	protected JTabbedPane tabbedPane;
	protected JPanel panelDatos;
	

	/**
	 * CONSTRUCTOR: nueva vista home
	 */
	public VistaHome() {
		setMinimumSize(new Dimension(1200, 728));
		this.setLayout(null);
		
		JButton btnAtras = new JButton("Atr\u00E1s");
        btnAtras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setVisible(false);
        		VistaInicio.vista().setVisible(true);
        	}
        });
        btnAtras.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnAtras.setBounds(22, 73, 70, 23);
        add(btnAtras);
		
		lblIdentidad = new JLabel("Manuel Michelis - LU: 104434");
		lblIdentidad.setForeground(SystemColor.desktop);
		lblIdentidad.setHorizontalAlignment(SwingConstants.CENTER);
		lblIdentidad.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 16));
		lblIdentidad.setBounds(22, 11, 1151, 37);
		this.add(lblIdentidad);
		
		panelBienvenida = new JPanel();
		panelBienvenida.setForeground(SystemColor.controlHighlight);
		panelBienvenida.setBorder(new LineBorder(SystemColor.controlHighlight));
		panelBienvenida.setBounds(22, 11, 1151, 37);
		this.add(panelBienvenida);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
		tabbedPane.setBackground(SystemColor.menu);
		tabbedPane.setBorder(new LineBorder(new Color(227, 227, 227), 0));
		tabbedPane.setBounds(22, 125, 1151, 506);
		this.add(tabbedPane);
		
		panelDatos = new JPanel();
		panelDatos.setBackground(SystemColor.control);
		tabbedPane.addTab("Datos personales", null, panelDatos, null);
		setMinimumSize(new Dimension(1200, 728));
	}
	
}
