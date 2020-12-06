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
import Modelos.ActaCursado;
import Modelos.Dictado;
import Modelos.Materia;
import Modelos.Profesor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;


public class VistaReporteActaCursado extends JPanel{
	

	private static final long serialVersionUID = 1L;
	private static VistaReporteActaCursado instancia = null;
	private Profesor modeloProfesor;
	private JPanel panelSeleccion;
	private JComboBox<Materia> comboMaterias;
	private JComboBox<Dictado> comboDictados;
	
	
	
	public static VistaReporteActaCursado vista () {
		if (instancia == null) {
			instancia = new VistaReporteActaCursado();
		}
		return instancia;
	}
	
	
	private VistaReporteActaCursado () {
		
		setBackground(SystemColor.controlHighlight);		
		this.setBounds(0,0,1200,704);
		setLayout(null);
		
		panelSeleccion = new JPanel();
		panelSeleccion.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		panelSeleccion.setBackground(SystemColor.menu);
		panelSeleccion.setBounds(343, 54, 516, 214);
		add(panelSeleccion);
		panelSeleccion.setLayout(null);
		
		
		JLabel lblRevision = new JLabel("Generar Acta de Cursado");
		lblRevision.setForeground(Color.DARK_GRAY);
		lblRevision.setHorizontalAlignment(SwingConstants.CENTER);
		lblRevision.setFont(new Font("Microsoft YaHei UI Light", Font.BOLD, 14));
		lblRevision.setBounds(10, 11, 484, 20);
		panelSeleccion.add(lblRevision);
		
		JLabel lblMateria = new JLabel("Materia:");
		lblMateria.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
		lblMateria.setBounds(64, 62, 51, 20);
		panelSeleccion.add(lblMateria);
		
		JLabel lblDictado = new JLabel("Dictado:");
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
		
		
		comboDictados = new JComboBox<Dictado>();
		comboDictados.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					/*if (tabla.getRowCount() > 0) {
						tabla.setVisible(false);
					}
					else {
						lblSinResultados.setVisible(false);
					}*/
				}				
			}
		});
		comboDictados.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
		comboDictados.setBounds(125, 119, 320, 20);
		panelSeleccion.add(comboDictados);
		
		
		
		JButton btnGenerarReport = new JButton("Generar Acta de Cursado");
		JPanel miVista = this;
		
		
		btnGenerarReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dictado dictado = (Dictado) comboDictados.getSelectedItem();
				int idDict = dictado.obtenerId();
				try 
				{
					
					List<ActaCursado> actas =ControladorDictado.controlador().recuperarActaCursados(idDict);
					
			
					
					
					try {
						//Indicamos la plantilla
						JasperReport reporte = (JasperReport) JRLoader.loadObject(new File("src/plantilla/ActaCursado.jasper"));
						
						HashMap<String, Object> map = new HashMap<String, Object>();
						Materia materia = (Materia) comboMaterias.getSelectedItem();
						
						map.put("materia",materia.obtenerNombre()+"("+materia.obtenerId()+")");
				        
				        String cuatrimestre = dictado.obtenerCuatrimestre() == 1 ? "Primer Cuatrimestre": "Segundo Cuatrimestre";
				      
				        map.put("cuatrimestre", cuatrimestre+" - "+dictado.obtenerAnio());
				        
				        map.put("profesor", modeloProfesor.obtenerNombre()+" "+modeloProfesor.obtenerApellido());
						
						//Cargamos los datos 
						JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, map, new JRBeanCollectionDataSource(actas));
						
						JasperViewer vista = new JasperViewer(jasperPrint,false);
						
						vista.setName("Acta Cursado");
					
						vista.setVisible(true);
						
					} catch (JRException e1) {
						// TODO Auto-generated catch block
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
	
	
	
	
	
	
	public void generarVistaReporteActaCursado (Profesor mod) {
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
		List<Dictado> listaDictados;
		Materia materia = (Materia) comboMaterias.getSelectedItem();
		int idMat = materia.obtenerId();
		int lg = modeloProfesor.obtenerLegajo();
		comboDictados.removeAllItems();
		try 
		{
			listaDictados = ControladorDictado.controlador().dictadosMateriaPorProfesor(idMat, lg);
			for (Dictado dictado : listaDictados) {
				comboDictados.addItem(dictado);
			}
		}
		catch (DBRetrieveException ex) 
		{
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Carga de datos de dictados", JOptionPane.ERROR_MESSAGE);
		}
	}


	
}
