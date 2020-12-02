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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;

import Controladores.ControladorMateria;
import Controladores.ControladorPlan;
import Controladores.ControladorVistas;
import Excepciones.DBRetrieveException;
import Excepciones.DBUpdateException;
import Modelos.Plan;
import quick.dbtable.DBTable;

public class VistaAdminPlanes extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static VistaAdminPlanes instancia = null;
	private DBTable tabla;
	private JTextField txtNombre;
	protected int seleccionado = -1;
	
	
	public static VistaAdminPlanes vista () {
		if (instancia == null) {
			instancia = new VistaAdminPlanes();
		}
		return instancia;
	}
	
	// CONSTRUCTOR: Vista de alumnos	
	private VistaAdminPlanes() {
		
		this.setBackground(SystemColor.control);
		this.setBounds(0, 0, 1194, 699);
		this.setLayout(null);
		
		JPanel panelTabla = new JPanel();
		panelTabla.setBackground(SystemColor.controlHighlight);
		panelTabla.setBounds(10, 232, 1174, 456);
		this.add(panelTabla);
		
		JButton btnRegPlan = new JButton("Registrar plan");
		btnRegPlan.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 13));
		btnRegPlan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaRegPlan();
			}
		});
		btnRegPlan.setBounds(313, 39, 177, 40);
		this.add(btnRegPlan);
		
		tabla = new DBTable();
		tabla.setBounds(446, 5, 275, 427);
        
		tabla.addKeyListener(new KeyAdapter() {
           public void keyTyped(KeyEvent evt) {
              tablaKeyTyped(evt);
           }
        });
        
        tabla.addMouseListener(new MouseAdapter() {
           public void mouseClicked(MouseEvent evt) {
              tablaMouseClicked(evt);
           }
    	});
        panelTabla.setLayout(null);
        
    	// Agregar la tabla al frame (no necesita JScrollPane como Jtable)
        panelTabla.add(tabla);           
        tabla.setEditable(false);
        
        JButton btnModPlan = new JButton("Modificar plan");
        btnModPlan.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaBuscarPlan();
        	}
        });
        btnModPlan.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnModPlan.setBounds(313, 90, 177, 40);
        add(btnModPlan);
        
        JButton btnBajaPlan = new JButton("Dar de baja plan");
        btnBajaPlan.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaElimPlan();
        	}
        });
        btnBajaPlan.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnBajaPlan.setBounds(313, 141, 177, 40);
        add(btnBajaPlan);
        
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
        
        JButton btnAgregarMatPlan = new JButton("Agregar materia a plan");
        btnAgregarMatPlan.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 13));
        btnAgregarMatPlan.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaAgregarMat();
        	}
        });
        btnAgregarMatPlan.setBounds(696, 39, 177, 40);
        add(btnAgregarMatPlan);
        
        JButton btnRegCorrelativa = new JButton("Registrar correlativa");
        btnRegCorrelativa.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnRegCorrelativa.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaAgregarCor();
        	}
        });
        btnRegCorrelativa.setBounds(696, 90, 177, 40);
        add(btnRegCorrelativa);
                
        actualizarTabla();
	}
	
	
	private void tablaMouseClicked(MouseEvent evt) 
	{
		if ((this.tabla.getSelectedRow() != -1) && (evt.getClickCount() == 2)) 
		{
			this.seleccionarFila();
		}
	}
	
	
	private void tablaKeyTyped(KeyEvent evt) {
		if ((this.tabla.getSelectedRow() != -1) && (evt.getKeyChar() == ' ')) 
		{
	         this.seleccionarFila();
	    }
	}
	
	
	private void seleccionarFila()
	{
		this.seleccionado = this.tabla.getSelectedRow();
	    this.txtNombre.setText(this.tabla.getValueAt(this.tabla.getSelectedRow(), 0).toString());
	}
	
	
	private void actualizarTabla () {
		try
	    {
			ControladorPlan.controlador().volcar(tabla);
	    }		
		catch (DBRetrieveException ex)
		{
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Volcado de datos de planes", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	
	
	// CLASE PARA LA VENTANA DE INPUTS PARA EL REGISTRO
	
	private class VentanaRegPlan extends JFrame {
		
		private static final long serialVersionUID = 1L;	
		private JTextField inputIdCarrera;
		private JTextField inputVersion;
		private JLabel lblId;
		private JLabel lblVersion;
		private JButton btnSiguiente;
		
		
		public VentanaRegPlan() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Registro de nueva plan");
			setSize(395, 187);
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS
			
			inputIdCarrera = new JTextField();
			inputIdCarrera.setColumns(10);
			inputIdCarrera.setBounds(159, 40, 178, 20);
			getContentPane().add(inputIdCarrera);
			
			inputVersion = new JTextField();
			inputVersion.setBounds(159, 74, 97, 20);
			getContentPane().add(inputVersion);
			
			lblId = new JLabel("ID de carrera:");
			lblId.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 12));
			lblId.setBounds(67, 39, 82, 20);
			getContentPane().add(lblId);
			
			lblVersion = new JLabel("Version:");
			lblVersion.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 12));
			lblVersion.setBounds(97, 72, 54, 20);
			getContentPane().add(lblVersion);
			
			btnSiguiente = new JButton("Guardar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					registrarPlan();
					dispose();
				}
			});
			btnSiguiente.setBounds(132, 116, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
		
		
		private void registrarPlan () {
			String [] in = {inputIdCarrera.getText(), inputVersion.getText()};
			try
			{
				ControladorPlan.controlador().registrar(in);
				// Notifico éxito en la operación
				JOptionPane.showMessageDialog(this,"Plan registrado exitosamente");
				actualizarTabla();
				dispose();
			}
			catch (DBUpdateException ex)
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Registro de un plan", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
	
	private class VentanaBuscarPlan extends JFrame {
		private static final long serialVersionUID = 1L;
		private JTextField inputIdCarrera;
		private JButton btnSiguiente;
		;
		
		
		// CONSTRUCTOR: Ventana para el registro de un nuevo plan
		
		public VentanaBuscarPlan() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Modificación de un plan");
			setSize(new Dimension(356, 145));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS
			
			inputIdCarrera = new JTextField();
			inputIdCarrera.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputIdCarrera.setBounds(129, 29, 157, 20);
			getContentPane().add(inputIdCarrera);
			
			// CREACIÓN DE LABELS
			
			JLabel lblId = new JLabel("ID del plan:");		
			lblId.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblId.setBounds(50, 29, 69, 20);
			getContentPane().add(lblId);
			
			btnSiguiente = new JButton("A modificar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {						
					abrirEdicionPlan();
					dispose();
				}
			});
			btnSiguiente.setBounds(114, 75, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
		
		
		private void abrirEdicionPlan () {
			String [] inputs = {inputIdCarrera.getText(), null, null};
			Plan plan = null;
			try
			{
				plan = ControladorPlan.controlador().recuperar(inputs);
				if (plan == null) {
					JOptionPane.showMessageDialog(this,
							"ERROR! No existe un plan registrada con ID: " + inputIdCarrera.getText(),
							"Modificación de una materia", JOptionPane.ERROR_MESSAGE);
				}
				else {
					new VentanaEdicionPlan(plan);
				}
			}
			catch (DBRetrieveException ex)
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Modificación de una materia", JOptionPane.ERROR_MESSAGE);
			}
			dispose();
		}
		
		   
		   private class VentanaEdicionPlan extends JFrame {
			   
				private static final long serialVersionUID = 1L;
				private JTextField inputVersion;
				private JCheckBox checkBoxVersion;
				private JButton btnSiguiente;
				
				
				// CONSTRUCTOR: Ventana para el registro de un nuevo alumno
				
				public VentanaEdicionPlan(Plan plan) {
					super();
					getContentPane().setEnabled(false);
					getContentPane().setLayout(null);
			        setVisible(true);
			        
			        getContentPane().setBackground(SystemColor.controlHighlight);
					setTitle("Modificación de un plan");
					setSize(322, 139);
					setResizable(false);		
					setLocationRelativeTo(null);
						
					// CREACIÓN DE INPUTS
					
					inputVersion = new JTextField();
					inputVersion.setEnabled(false);
					inputVersion.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
					inputVersion.setBounds(107, 24, 127, 20);
					getContentPane().add(inputVersion);
					inputVersion.setColumns(10);
							
					// CREACIÓN DE CHECKBOXES
					
					// Nombre de materia
					checkBoxVersion = new JCheckBox("");
					checkBoxVersion.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							switchearEstadoInput(inputVersion);
						}
					});
					checkBoxVersion.setBackground(SystemColor.controlHighlight);
					checkBoxVersion.setBounds(245, 24, 26, 20);
					getContentPane().add(checkBoxVersion);
					
					// CREACIÓN DE LABELS
					
					JLabel lblId = new JLabel("Versi\u00F3n:");
					lblId.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));;
					lblId.setBounds(46, 24, 51, 20);
					getContentPane().add(lblId);	
					
					btnSiguiente = new JButton("Guardar");
					btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
					btnSiguiente.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {				
							modificarPlan(plan);
							dispose();
						}
					});
					btnSiguiente.setBounds(98, 73, 124, 23);
					getContentPane().add(btnSiguiente);
				}
				
				
				private void switchearEstadoInput (JComponent in) {
					if (in.isEnabled()) {
						in.setEnabled(false);
					}
					else in.setEnabled(true);
				}
				
				
				private void modificarPlan (Plan plan) {
					String [] in = {plan.obtenerId() + "", null, null};
					// Verifico cuáles atributos se quieren editar y los incorporo a la entrada para modificar
					if (inputVersion.isEnabled()) {
						in[1] = inputVersion.getText();
					}
					// Procedo a la modificación
					try
					{
						ControladorPlan.controlador().modificar(in);
						JOptionPane.showMessageDialog(this,"Materia con ID: " + plan.obtenerId() +" modificada exitosamente");
						actualizarTabla();
					}
					catch (DBUpdateException ex)
					{
						JOptionPane.showMessageDialog(this, ex.getMessage(), "Modificación de una materia", JOptionPane.ERROR_MESSAGE);
					}
					dispose();
				}
			}
	}
	
	
	
	// CLASE PARA LA VENTANA DE INPUTS PARA LA BAJA

	public class VentanaElimPlan extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JTextField inputNombre;
		private JButton btnSiguiente;
		
		
		// CONSTRUCTOR: Ventana para el registro de un nuevo alumno
		
		public VentanaElimPlan() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Baja de un plan");
			setSize(new Dimension(374, 157));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACIÓN DE INPUTS: registro de alumno
			
			inputNombre = new JTextField();
			inputNombre.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputNombre.setBounds(145, 41, 148, 20);
			getContentPane().add(inputNombre);
			
			// CREACIÓN DE LABELS: registro de alumno
			
			JLabel lblId = new JLabel("ID del plan:");		
			lblId.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblId.setBounds(63, 41, 72, 20);
			getContentPane().add(lblId);
			
			btnSiguiente = new JButton("Dar de baja");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {				
					eliminarPlan();
					dispose();
				}
			});
			btnSiguiente.setBounds(119, 84, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
		
		
		// CONEXIÓN Y DESCONEXIÓN DE LA BASE DE DATOS
		
		private void eliminarPlan () {
			try
			{
				ControladorPlan.controlador().eliminar(inputNombre.getText());
				JOptionPane.showMessageDialog(this,"Plan dado de baja exitosamente");
				actualizarTabla();
			}
			catch (DBUpdateException ex)
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Baja de un plan", JOptionPane.ERROR_MESSAGE);
			}
			dispose();
		}
	}
	
		
	private class VentanaAgregarCor extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JTextField inputPlan;
		private JTextField inputMat;
		private JTextField inputCor;		
		
		public VentanaAgregarCor() {
			setTitle("Registro de correlatividad");
			getContentPane().setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			setSize(360,232);
			getContentPane().setLayout(null);
			setVisible(true);
			setLocationRelativeTo(null);
			setResizable(false);
			
			inputPlan = new JTextField();
			inputPlan.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputPlan.setBounds(147, 29, 160, 20);
			getContentPane().add(inputPlan);
			inputPlan.setColumns(10);
			
			inputMat = new JTextField();
			inputMat.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputMat.setColumns(10);
			inputMat.setBounds(147, 70, 160, 20);
			getContentPane().add(inputMat);
			
			inputCor = new JTextField();
			inputCor.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputCor.setColumns(10);
			inputCor.setBounds(147, 111, 160, 20);
			getContentPane().add(inputCor);
			
			JLabel lblIdPlan = new JLabel("ID del plan:");
			lblIdPlan.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblIdPlan.setBounds(65, 32, 72, 14);
			getContentPane().add(lblIdPlan);
			
			JLabel lblIdMateria = new JLabel("ID de materia:");
			lblIdMateria.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblIdMateria.setBounds(51, 70, 89, 20);
			getContentPane().add(lblIdMateria);
			
			JLabel lblIdCorrelativa = new JLabel("ID de correlativa:");
			lblIdCorrelativa.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblIdCorrelativa.setBounds(34, 111, 103, 20);
			getContentPane().add(lblIdCorrelativa);
			
			JButton btnNewButton = new JButton("Guardar correlativa");
			btnNewButton.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			JFrame miVista = this;
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String [] inputs = {inputPlan.getText(), inputMat.getText(), inputCor.getText()};
					try
					{
						Integer.parseInt(inputPlan.getText());
						Integer.parseInt(inputMat.getText());
						Integer.parseInt(inputCor.getText());
						ControladorMateria.controlador().agregarCorrelatividad(inputs);
						JOptionPane.showMessageDialog(miVista, "El registro de la correlatividad sobre el plan " + inputPlan.getText() +
								", de la materia con ID: " + inputMat.getText() + " con su correlativa con ID: " + inputCor.getText());
					}
					catch (DBUpdateException ex) 
					{
						JOptionPane.showMessageDialog(miVista, ex.getMessage(), "Registro de correlatividad", JOptionPane.ERROR_MESSAGE);
					}
					catch (NumberFormatException ex) 
					{
						JOptionPane.showMessageDialog(miVista,
								"¡ERROR! El ID del plan, así como el de las materias a vincular, debe ser un entero positivo",
								"Registro de correlatividad", JOptionPane.ERROR_MESSAGE);
					}
					dispose();
				}
			});
			btnNewButton.setBounds(102, 159, 149, 27);
			getContentPane().add(btnNewButton);
		}
	}
	
		
	public class VentanaAgregarMat extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JTextField inputPlan;
		private JTextField inputMat;
		
		
		public VentanaAgregarMat() {
			setTitle("Agregaci\u00F3n de materia a plan");
			getContentPane().setLayout(null);
			this.setVisible(true);
			setSize(360,200);
			setResizable(false);
			setLocationRelativeTo(null);
			JFrame miVista = this;
			
			JLabel lblPlan = new JLabel("ID del plan:");
			lblPlan.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblPlan.setBounds(60, 40, 72, 14);
			getContentPane().add(lblPlan);
			
			JLabel lblMateria = new JLabel("ID de materia:");
			lblMateria.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblMateria.setBounds(46, 78, 89, 20);
			getContentPane().add(lblMateria);
			
			inputPlan = new JTextField();
			inputPlan.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputPlan.setColumns(10);
			inputPlan.setBounds(142, 37, 160, 20);
			getContentPane().add(inputPlan);
			
			inputMat = new JTextField();
			inputMat.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputMat.setColumns(10);
			inputMat.setBounds(142, 78, 160, 20);
			getContentPane().add(inputMat);
			
			JButton btnGuardar = new JButton("Guardar materia");
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String [] inputs = {inputPlan.getText(), inputMat.getText()};
					try
					{
						Integer.parseInt(inputPlan.getText());
						Integer.parseInt(inputMat.getText());					
						ControladorPlan.controlador().agregarMateriaAPlan(inputs);
					}
					catch (DBUpdateException ex) 
					{
						JOptionPane.showMessageDialog(miVista, ex.getMessage(), "Registro de correlatividad", JOptionPane.ERROR_MESSAGE);
					}
					catch (NumberFormatException ex) 
					{
						JOptionPane.showMessageDialog(miVista,
								"¡ERROR! El ID del plan, así como el de las materias a vincular, debe ser un entero positivo",
								"Registro de asociación de materia con plan", JOptionPane.ERROR_MESSAGE);
					}
					dispose();
				}
			});
			btnGuardar.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnGuardar.setBounds(102, 125, 149, 27);
			getContentPane().add(btnGuardar);
		}
	}	
	
	
}
