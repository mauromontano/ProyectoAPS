package Vistas;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import Controladores.ControladorLogin;
import Excepciones.ExcepcionAutenticacion;

import java.awt.SystemColor;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VistaInicio extends JPanel {


	private static final long serialVersionUID = 1L;
	private JComboBox<String> switchCategoria;
	private JTextField inputUsuario;
	private JPasswordField inputPswd;
	private static VistaInicio instancia = null;
	
	
	/**
	 * obtenerVista: retorna la instancia asociada a la vista inicial
	 * @return panel de la vista inicial
	 */
	public static VistaInicio vista () {
		
		if (instancia == null) {
			instancia = new VistaInicio();
		}
		return instancia;
	}
	
	/**
	 * CONSTRUCTOR
	 */
	public VistaInicio() {
		setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		this.setBounds(401, 199, 387, 280);
		setLayout(null);
		
		inputUsuario = new JTextField();
		inputUsuario.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
		inputUsuario.setBounds(146, 122, 177, 23);
		add(inputUsuario);
		inputUsuario.setColumns(10);
		
		inputPswd = new JPasswordField();
		inputPswd.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
		inputPswd.setColumns(10);
		inputPswd.setBounds(146, 165, 177, 23);
		add(inputPswd);
		
		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
		lblUsuario.setBounds(81, 122, 55, 23);
		add(lblUsuario);
		
		JLabel lblContrasea = new JLabel("Contrase\u00F1a:");
		lblContrasea.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
		lblContrasea.setBounds(57, 165, 79, 23);
		add(lblContrasea);
		
		switchCategoria = new JComboBox<String>();
		switchCategoria.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 12));
		switchCategoria.setModel(new DefaultComboBoxModel<String>(new String[] {"Alumno", "Profesor", "Administrador"}));
		switchCategoria.setBounds(146, 81, 122, 23);
		add(switchCategoria);
		
		JLabel lblCategoria = new JLabel("Categor\u00EDa:");
		lblCategoria.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
		lblCategoria.setBounds(68, 80, 68, 23);
		add(lblCategoria);
		
		JButton btnLogin = new JButton("Acceder");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accederAlPerfil();
			}
		});
		btnLogin.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 14));
		btnLogin.setBounds(139, 225, 115, 27);
		add(btnLogin);
		
		JLabel lblInicio = new JLabel("Inicio de sesi\u00F3n");
		lblInicio.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 16));
		lblInicio.setHorizontalAlignment(SwingConstants.CENTER);
		lblInicio.setBounds(120, 11, 148, 20);
		add(lblInicio);
	}
	
	/**
	 * accederAlPerfil: permite llevar adelante el acceso al home del usuario adecuado
	 */
	public void accederAlPerfil () {
		String categoria = (String) switchCategoria.getSelectedItem();
		String usuario = inputUsuario.getText();
		char[] contenidoPswd = inputPswd.getPassword();
		try 
		{
			ControladorLogin.controlador().login(categoria, usuario, contenidoPswd);
		}
		catch (ExcepcionAutenticacion ex) 
		{
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
					   ex.getMessage(),
					   "Fallo en la autenticación [" + categoria + "]",
					   JOptionPane.ERROR_MESSAGE);
		}
	}
}
