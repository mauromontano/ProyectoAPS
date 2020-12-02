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
import Controladores.ControladorVistas;
import java.awt.Color;

public abstract class VistaHome extends JPanel {
	
	protected static final long serialVersionUID = 1L;
	protected JLabel lblIdentidad;
	protected JPanel panelBienvenida;
	

	/**
	 * CONSTRUCTOR: nueva vista home
	 */
	public VistaHome() {
		setBackground(SystemColor.controlHighlight);
		setMinimumSize(new Dimension(1200, 728));
		this.setLayout(null);
		
		JButton btnAtras = new JButton("Atr\u00E1s");
        btnAtras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// Vuelvo a la vista anterior, la vista de inicio
        		ControladorVistas.controlador().mostrar(VistaInicio.vista());
        	}
        });
        btnAtras.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnAtras.setBounds(29, 73, 70, 23);
        add(btnAtras);
		
		lblIdentidad = new JLabel("|");
		lblIdentidad.setForeground(Color.DARK_GRAY);
		lblIdentidad.setHorizontalAlignment(SwingConstants.CENTER);
		lblIdentidad.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 16));
		lblIdentidad.setBounds(22, 11, 1151, 37);
		this.add(lblIdentidad);
		
		panelBienvenida = new JPanel();
		panelBienvenida.setForeground(SystemColor.activeCaptionBorder);
		panelBienvenida.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		panelBienvenida.setBounds(22, 11, 1151, 37);
		this.add(panelBienvenida);
		setMinimumSize(new Dimension(1200, 728));
	}
	
}
