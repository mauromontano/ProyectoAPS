package Vistas;

import javax.swing.JFrame;
import javax.swing.JTextField;

import Controladores.ControladorPlan;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaAgregarMat extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JTextField inputPlan;
	private JTextField inputMat;
	
	
	public VentanaAgregarMat() {
		getContentPane().setLayout(null);
		this.setVisible(true);
		
		inputPlan = new JTextField();
		inputPlan.setBounds(109, 37, 133, 20);
		getContentPane().add(inputPlan);
		inputPlan.setColumns(10);
		
		inputMat = new JTextField();
		inputMat.setColumns(10);
		inputMat.setBounds(109, 95, 133, 20);
		getContentPane().add(inputMat);
		
		JLabel lblIdPlan = new JLabel("id plan");
		lblIdPlan.setBounds(22, 40, 46, 14);
		getContentPane().add(lblIdPlan);
		
		JLabel lblIdMateria = new JLabel("id materia");
		lblIdMateria.setBounds(22, 98, 65, 14);
		getContentPane().add(lblIdMateria);
		
		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String [] inputs = {inputPlan.getText(), inputMat.getText()};
				ControladorPlan.controlador().agregarMateriaAPlan(inputs);
				dispose();
			}
		});
		btnGuardar.setBounds(128, 155, 89, 23);
		getContentPane().add(btnGuardar);
	}
}
