package Vistas;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import Controladores.ControladorDictado;
import Controladores.ControladorMateria;
import Controladores.ControladorVistas;
import Excepciones.DBRetrieveException;
import Modelos.ActaFinal;
import Modelos.Materia;
import Modelos.MesaDeExamen;
import Modelos.Profesor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class VistaReporteActaFinal extends JPanel{

	
	private static final long serialVersionUID = 1L;
	private static VistaReporteActaFinal instancia = null;
	private Profesor modeloProfesor;
	private JPanel panelSeleccion;
;
	private JComboBox<Materia> comboMaterias;
	private JComboBox<MesaDeExamen> comboMesas;
	
	
	
	public static VistaReporteActaFinal vista () {
		if (instancia == null) {
			instancia = new VistaReporteActaFinal();
		}
		return instancia;
	}
	
	
	private VistaReporteActaFinal () {
		
		setBackground(SystemColor.controlHighlight);		
		this.setBounds(0,0,1200,704);
		setLayout(null);
		
		panelSeleccion = new JPanel();
		panelSeleccion.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		panelSeleccion.setBackground(SystemColor.menu);
		panelSeleccion.setBounds(343, 54, 516, 214);
		add(panelSeleccion);
		panelSeleccion.setLayout(null);
		
		
		
		JLabel lblRevision = new JLabel("Generar Acta de Final");
		lblRevision.setForeground(Color.DARK_GRAY);
		lblRevision.setHorizontalAlignment(SwingConstants.CENTER);
		lblRevision.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 14));
		lblRevision.setBounds(10, 11, 484, 20);
		panelSeleccion.add(lblRevision);
		
		JLabel lblMateria = new JLabel("Materia:");
		lblMateria.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
		lblMateria.setBounds(64, 62, 51, 20);
		panelSeleccion.add(lblMateria);
		
		JLabel lblDictado = new JLabel("Mesa:");
		lblDictado.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
		lblDictado.setBounds(64, 118, 51, 20);
		panelSeleccion.add(lblDictado);
		
		JLabel lblSinResultados = new JLabel("A\u00FAn no existen inscriptos para este dictado");
		lblSinResultados.setForeground(Color.DARK_GRAY);
		lblSinResultados.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 21));
		lblSinResultados.setHorizontalAlignment(SwingConstants.CENTER);
		lblSinResultados.setBounds(293, 100, 578, 37);
		lblSinResultados.setVisible(false);
		
		

		
		comboMaterias = new JComboBox<Materia>();
		comboMaterias.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					
					cargarDictados();
				}				
			}
		});
		comboMaterias.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 12));
		comboMaterias.setBounds(125, 63, 320, 20);
		panelSeleccion.add(comboMaterias);
		
		
		comboMesas = new JComboBox<MesaDeExamen>();
		comboMesas.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					
				}				
			}
		});
		comboMesas.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
		comboMesas.setBounds(125, 119, 320, 20);
		panelSeleccion.add(comboMesas);
		
		
		
		JButton btnGenerarReport = new JButton("Generar Acta de Final");
		JPanel miVista = this;
		
		
		btnGenerarReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MesaDeExamen mesa = (MesaDeExamen) comboMesas.getSelectedItem();
				int idMesa = mesa.obtenerId();
				
				
				try 
				{
				
					List<ActaFinal> actas =ControladorDictado.controlador().recuperarActaFinal(idMesa);
		
			
					
					try {
						//Indicamos la plantilla
						JasperReport reporte = (JasperReport) JRLoader.loadObject(new File("src/plantilla/ActaFinal.jasper"));
						
						HashMap<String, Object> map = new HashMap<String, Object>();
						Materia materia = (Materia) comboMaterias.getSelectedItem();
						
				        map.put("materia",materia.obtenerNombre()+"("+materia.obtenerId()+")");
				        map.put("fecha", mesa.obtenerFechaEval());
				        map.put("profesor", modeloProfesor.obtenerNombre()+" "+modeloProfesor.obtenerApellido());
				        
				        
				       
				  
				        
				        map.put("profesor_nombre", modeloProfesor.obtenerNombre());
				        map.put("profesor_apellido", modeloProfesor.obtenerApellido());
						
						//Cargamos los datos 
						JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, map, new JRBeanCollectionDataSource(actas));
						
						JasperViewer vista = new JasperViewer(jasperPrint,false);
						
						vista.setName("Acta Finaal");
					
						vista.setVisible(true);
						
					} catch (JRException e1) {
						
						e1.printStackTrace();
					}
					
					
					
				
				
				}catch (DBRetrieveException ex) 
				{
					JOptionPane.showMessageDialog(miVista, ex.getMessage(), "Volcado de alumnos de un dictado", JOptionPane.ERROR_MESSAGE);
				}				
			}
		});
		btnGenerarReport.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
		btnGenerarReport.setBounds(150, 170, 236, 25);
		panelSeleccion.add(btnGenerarReport);
		
		JButton btnAtras = new JButton("Atr\u00E1s");
		btnAtras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ControladorVistas.controlador().mostrar(VistaProfesor.vista());
			}
		});
		btnAtras.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
		btnAtras.setBounds(23, 26, 70, 23);
		add(btnAtras);
		
	
	
	}
	
	
	
	
	
	
	public void generarVistaReporteActaFinal (Profesor mod) {
		modeloProfesor = mod;
		cargarMaterias();
	}
	
	
	/**
	 * cargarCarreras: lleva a cabo la carga de cada uno de los modelos de materias,
	   en el combobox asociado a las materias las que el alumno con LU dado puede
	   inscribirse
	 * @param lu: número de libreta universitaria asociado al alumno
	 */
	private void cargarMaterias () {
		List<Materia> listaMaterias;
		int lg = modeloProfesor.obtenerLegajo();
		comboMaterias.removeAllItems();
		try 
		{
			listaMaterias = ControladorMateria.controlador().materiasDictadasPorProfesor(lg);
			for (Materia materia : listaMaterias) {
				comboMaterias.addItem(materia);
			}
		}
		catch (DBRetrieveException ex) 
		{
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de materias", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	/**
	 * cargarCarreras: lleva a cabo la carga de cada uno de los modelos de carreras
	   en el combobox asociado a las carreras en las que el alumno con LU dado está
	   inscripto
	 * @param lu: número de libreta universitaria asociado al alumno
	 */
	private void cargarDictados () {
		List<MesaDeExamen> listaMesas;
		Materia materia = (Materia) comboMaterias.getSelectedItem();
		int idMat = materia.obtenerId();
		int lg = modeloProfesor.obtenerLegajo();
		comboMesas.removeAllItems();
		try 
		{
			listaMesas = ControladorDictado.controlador().mesasMateriaPorProfesor(idMat, lg);
			for (MesaDeExamen mesa : listaMesas) {
				comboMesas.addItem(mesa);
			}
		}
		catch (DBRetrieveException ex) 
		{
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de dictados", JOptionPane.ERROR_MESSAGE);
		}
	}


	
}
