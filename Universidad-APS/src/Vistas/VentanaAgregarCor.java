package Vistas;

import javax.swing.JFrame;
import javax.swing.JTextField;

import Controladores.ControladorPlan;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaAgregarCor extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JTextField inputPlan;
	private JTextField inputMat;
	private JTextField inputCor;
	
	
	public VentanaAgregarCor() {
		setSize(328,272);
		getContentPane().setLayout(null);
		setVisible(true);
		
		inputPlan = new JTextField();
		inputPlan.setBounds(121, 29, 128, 20);
		getContentPane().add(inputPlan);
		inputPlan.setColumns(10);
		
		inputMat = new JTextField();
		inputMat.setColumns(10);
		inputMat.setBounds(121, 84, 128, 20);
		getContentPane().add(inputMat);
		
		inputCor = new JTextField();
		inputCor.setColumns(10);
		inputCor.setBounds(121, 135, 128, 20);
		getContentPane().add(inputCor);
		
		JLabel lblIdPlan = new JLabel("id plan");
		lblIdPlan.setBounds(65, 32, 31, 14);
		getContentPane().add(lblIdPlan);
		
		JLabel lblIdMateria = new JLabel("id materia");
		lblIdMateria.setBounds(50, 87, 61, 14);
		getContentPane().add(lblIdMateria);
		
		JLabel lblIdCorrelativa = new JLabel("id correlativa");
		lblIdCorrelativa.setBounds(34, 138, 77, 14);
		getContentPane().add(lblIdCorrelativa);
		
		JButton btnNewButton = new JButton("Guardar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String [] inputs = {inputPlan.getText(), inputMat.getText(), inputCor.getText()};
				ControladorPlan.controlador().agregarCorrelatividad(inputs);
				dispose();
			}
		});
		btnNewButton.setBounds(108, 181, 89, 23);
		getContentPane().add(btnNewButton);
	}

}
