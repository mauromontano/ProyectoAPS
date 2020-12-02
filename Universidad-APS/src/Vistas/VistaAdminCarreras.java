package Vistas;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import Controladores.ControladorCarrera;
import Controladores.ControladorVistas;
import Excepciones.DBRetrieveException;
import Excepciones.DBUpdateException;
import Modelos.Carrera;
import quick.dbtable.DBTable;

public class VistaAdminCarreras extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static VistaAdminCarreras instancia = null;
	private DBTable tabla;
	protected int seleccionado = -1;
	
	/**
	 * vista: retorna la instancia de la vista de administración de carreras
	 * @return panel de la vista de administración de carreras
	 */
	public static VistaAdminCarreras vista () {
		if (instancia == null) {
			instancia = new VistaAdminCarreras();
		}
		return instancia;
	}
	
	/**
	 * CONSTRUCTOR: Vista para la administración de carreras
	 */
	private VistaAdminCarreras() {
		
		this.setBackground(SystemColor.control);
		this.setBounds(0, 0, 1194, 699);
		this.setLayout(null);
		
		JPanel panelTabla = new JPanel();
		panelTabla.setBackground(SystemColor.controlHighlight);
		panelTabla.setBounds(10, 232, 1174, 456);
		this.add(panelTabla);
		
		JButton btnRegCarrera = new JButton("Registrar carrera");
		btnRegCarrera.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 13));
		btnRegCarrera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaRegCarrera();
			}
		});
		btnRegCarrera.setBounds(328, 84, 167, 40);
		this.add(btnRegCarrera);
		
		tabla = new DBTable();
		tabla.setBounds(396, 5, 377, 427);
        
        panelTabla.setLayout(null);
        
        panelTabla.add(tabla);           
        tabla.setEditable(false);
        
        JButton btnModCarrera = new JButton("Modificar carrera");
        btnModCarrera.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaBuscarCarrera();
        	}
        });
        btnModCarrera.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnModCarrera.setBounds(505, 85, 167, 40);
        add(btnModCarrera);
        
        JButton btnBajaCarrera = new JButton("Dar de baja carrera");
        btnBajaCarrera.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaElimCarrera();
        	}
        });
        btnBajaCarrera.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnBajaCarrera.setBounds(682, 85, 167, 40);
        add(btnBajaCarrera);
        
        JButton btnAtras = new JButton("Atr\u00E1s");
        btnAtras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// Vuelvo a la vista anterior, la vista de administración
        		ControladorVistas.controlador().mostrar(VistaAdmin.vista());
        	}
        });
        btnAtras.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnAtras.setBounds(10, 11, 70, 23);
        add(btnAtras);
              
        actualizarTabla();
	}
	
	/**
	 * actualizarTabla: permite actualizar el contenido de la db table para carreras,
	   con todas las carreras registradas y sus datos.
	 */
	private void actualizarTabla () {
		try
	    {
			ControladorCarrera.controlador().volcar(tabla);
	    }		
		catch (DBRetrieveException ex)
		{
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Registro de una carrera", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	
	// CLASE PARA LA VENTANA DE INPUTS PARA EL REGISTRO
	
	public class VentanaRegCarrera extends JFrame {
		
		private static final long serialVersionUID = 1L;	
		private JTextField inputNombre;
		private JTextField inputDuracion;
		private JLabel lblNombre;
		private JLabel lblDuracion;
		private JButton btnSiguiente;
		
		/*
		 * CONSTRUCTOR: ventana para el registro de una nueva carrera
		 */
		public VentanaRegCarrera() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Registro de nueva carrera");
			setMaximumSize(new Dimension(322, 245));
			setMinimumSize(new Dimension(322, 245));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS
			
			inputNombre = new JTextField();
			inputNombre.setColumns(10);
			inputNombre.setBounds(94, 55, 171, 20);
			getContentPane().add(inputNombre);
			
			inputDuracion = new JTextField();
			inputDuracion.setBounds(94, 88, 23, 20);
			getContentPane().add(inputDuracion);
			
			lblNombre = new JLabel("Nombre:");
			lblNombre.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 12));
			lblNombre.setBounds(25, 55, 59, 20);
			getContentPane().add(lblNombre);
			
			lblDuracion = new JLabel("Duraci\u00F3n:");
			lblDuracion.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 12));
			lblDuracion.setBounds(25, 88, 59, 20);
			getContentPane().add(lblDuracion);
			
			btnSiguiente = new JButton("Guardar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					registrarCarrera();
					dispose();
				}
			});
			btnSiguiente.setBounds(94, 153, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
		
		/**
		 * registrarCarrera: solicita el registro de una nueva carrera en el sistema
		 */
		private void registrarCarrera () {
			String [] inputs = {inputNombre.getText(), inputDuracion.getText()};
			try
			{
				ControladorCarrera.controlador().registrar(inputs);
				// Notifico éxito en la operación
				JOptionPane.showMessageDialog(this,"Carrera registrada exitosamente");
				dispose();
			}
			catch (DBUpdateException ex)
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Registro de una carrera", JOptionPane.ERROR_MESSAGE);
			}
			actualizarTabla();
		}
		
	}
	
	
	// CLASE PARA LA BÚSQUEDA DE UNA CARRERA QUE SE DESEA MODIFICAR
	
	
	private class VentanaBuscarCarrera extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JTextField inputId;
		private JButton btnSiguiente;
		
		
		// CONSTRUCTOR: Ventana para el registro de una nueva carrera
		
		public VentanaBuscarCarrera() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Modificación de una carrera");
			setSize(new Dimension(353, 170));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS: registro de alumno
			
			inputId = new JTextField();
			inputId.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputId.setBounds(128, 41, 100, 20);
			getContentPane().add(inputId);
			
			// CREACIÓN DE LABELS: registro de alumno
			
			JLabel lblLU = new JLabel("ID:");		
			lblLU.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblLU.setBounds(89, 41, 29, 20);
			getContentPane().add(lblLU);
			
			btnSiguiente = new JButton("A modificar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {						
					abrirEdicionCarrera();
					dispose();
				}
			});
			btnSiguiente.setBounds(115, 95, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
			   
		   
		   private void abrirEdicionCarrera ()
		   {
			  String [] inputs = {inputId.getText(), null, null};
			  Carrera carrera = null;
		      try
		      {
		    	
		         carrera = ControladorCarrera.controlador().recuperar(inputs);
		         if (carrera == null) {
		        	 JOptionPane.showMessageDialog(this,
		        			 "ERROR! No existe una carrera registrada con ID: " + inputId.getText(),
		        			 "Modificación de una carrera", JOptionPane.ERROR_MESSAGE);
		         }
		         else {
		        	 new VentanaEdicionCarrera(carrera);			        	 
		         }		         	         
		      }
		      catch (DBRetrieveException ex)
		      {
		    	  JOptionPane.showMessageDialog(this, ex.getMessage(), "Modificación de una carrera", JOptionPane.ERROR_MESSAGE);
		      }
		      dispose();
		   }
		   
		   
		   private class VentanaEdicionCarrera extends JFrame {
			   
				private static final long serialVersionUID = 1L;
				private JTextField [] inputs;
				private JCheckBox [] checkBoxes;
				private JButton btnSiguiente;
				
				
				// CONSTRUCTOR: Ventana para el registro de un nuevo alumno
				
				public VentanaEdicionCarrera (Carrera carrera) {
					super();
					getContentPane().setEnabled(false);
					getContentPane().setLayout(null);
			        setVisible(true);
			        
			        getContentPane().setBackground(SystemColor.controlHighlight);
					setTitle("Modificación de una carrera");
					setSize(new Dimension(322, 185));
					setMinimumSize(new Dimension(322, 185));
					setResizable(false);		
					setLocationRelativeTo(null);
						
					// CREACIÓN DE INPUTS
					
					inputs = new JTextField[2];
					checkBoxes = new JCheckBox[2];
					
					// Nombre de materia
					inputs[0] = new JTextField();
					inputs[0].setEnabled(false);
					inputs[0].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					inputs[0].setBounds(125, 24, 124, 20);
					getContentPane().add(inputs[0]);
					inputs[0].setColumns(10);
					
					// Carga horaria
					inputs[1] = new JTextField();
					inputs[1].setEnabled(false);
					inputs[1].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					inputs[1].setColumns(10);
					inputs[1].setBounds(124, 55, 125, 20);
					getContentPane().add(inputs[1]);
							
					// CREACIÓN DE CHECKBOXES
					
					// Nombre de materia
					checkBoxes[0] = new JCheckBox("");
					checkBoxes[0].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							switchearEstadoInput(inputs[0]);
						}
					});
					checkBoxes[0].setBackground(SystemColor.controlHighlight);
					checkBoxes[0].setBounds(255, 24, 26, 20);
					getContentPane().add(checkBoxes[0]);
					
					// Carga horaria
					checkBoxes[1] = new JCheckBox("");
					checkBoxes[1].setBackground(SystemColor.controlHighlight);
					checkBoxes[1].setBounds(255, 55, 26, 20);
					checkBoxes[1].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							switchearEstadoInput(inputs[1]);
						}
					});
					getContentPane().add(checkBoxes[1]);
					
					// CREACIÓN DE LABELS
					
					JLabel lblNombre = new JLabel("Nombre:");
					lblNombre.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));;
					lblNombre.setBounds(47, 24, 68, 20);
					getContentPane().add(lblNombre);
					
					JLabel lblCarga = new JLabel("Duraci\u00F3n:");		
					lblCarga.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					lblCarga.setBounds(47, 55, 68, 20);
					getContentPane().add(lblCarga);		
					
					btnSiguiente = new JButton("Guardar");
					btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
					btnSiguiente.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {				
							modificarCarrera(carrera);
							dispose();
						}
					});
					btnSiguiente.setBounds(95, 101, 124, 23);
					getContentPane().add(btnSiguiente);
				}
				
				
				private void switchearEstadoInput (JComponent in) {
					if (in.isEnabled()) {
						in.setEnabled(false);
					}
					else in.setEnabled(true);
				}
			
				
				private void modificarCarrera (Carrera carrera) {
					String [] in = {carrera.obtenerId() + "", null, null};
					// Verifico cuáles atributos se quieren editar y los incorporo a la entrada para modificar
					if (inputs[0].isEnabled()) {
						in[1] = inputs[0].getText();
					}
					if (inputs[1].isEnabled()) {
						in[2] = inputs[1].getText();
					}
					// Procedo a la modificación
					try
					{
						ControladorCarrera.controlador().modificar(in);
						JOptionPane.showMessageDialog(this,"Carrera con ID: " + carrera.obtenerId() +" modificada exitosamente");
						actualizarTabla();
					}
					catch (DBUpdateException ex)
					{
						JOptionPane.showMessageDialog(this, ex.getMessage(), "Modificación de una carrera", JOptionPane.ERROR_MESSAGE);
					}
					dispose();
				}
				
			}		   
	}
	
	
	
	// CLASE PARA LA VENTANA DE INPUTS PARA LA BAJA
	
	public class VentanaElimCarrera extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JTextField inputId;
		private JButton btnSiguiente;
		
		
		// CONSTRUCTOR: Ventana para el registro de un nuevo alumno
		
		public VentanaElimCarrera () {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Baja de una materia");
			setSize(new Dimension(380, 170));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS: registro de alumno
			
			inputId = new JTextField();
			inputId.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputId.setBounds(163, 41, 176, 20);
			getContentPane().add(inputId);
			
			// CREACIÓN DE LABELS: registro de alumno
			
			JLabel lblId = new JLabel("ID de carrera:");		
			lblId.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblId.setBounds(23, 41, 128, 20);
			getContentPane().add(lblId);
			
			btnSiguiente = new JButton("Dar de baja");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {				
					eliminarCarrera();
					dispose();
				}
			});
			btnSiguiente.setBounds(123, 86, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
		
				
		private void eliminarCarrera () {
			try
			{
				ControladorCarrera.controlador().eliminar(inputId.getText());
				JOptionPane.showMessageDialog(this,"Carrera dada de baja exitosamente");
				dispose();
			}
			catch (DBUpdateException ex)
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Baja de una carrera", JOptionPane.ERROR_MESSAGE);
			}
			actualizarTabla();
		}
		
	}

}
