package es.unex.giiis.tfg.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.SmsDTO;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.SmsService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

@Controller
@ManagedBean
@SessionScoped
public class SmsAnalysisManagedBean extends BaseManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("SmsAnalysisManagedBean");

	@Autowired
	private SmsService service;

	private List<SmsDTO> listSms;

	private CartesianChartModel combinedModel;

	private Map<Integer, Integer> contTotal;

	private Map<Integer, Integer> contInbox;

	private Map<Integer, Integer> contSent;

	private Map<Integer, Integer> contDraft;

	private List<String> months;

	private String monthView;

	@PostConstruct
	public void init() {
		// Mes a mostrar enero
		this.monthView = "Enero";

	}

	// Devuelve solamente los meses a dia de hoy
	public List<String> getMonths() {
		this.months = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		for (int i = 0; i <= calendar.get(Calendar.MONTH); i++) {
			System.out.println("Dia i: " + i);
			switch (i) {
			case 0:
				this.months.add("Enero");
				break;
			case 1:
				this.months.add("Febrero");
				break;
			case 2:
				this.months.add("Marzo");
				break;
			case 3:
				this.months.add("Abril");
				break;
			case 4:
				this.months.add("Mayo");
				break;
			case 5:
				this.months.add("Junio");
				break;
			case 6:
				this.months.add("Julio");
				break;
			case 7:
				this.months.add("Agosto");
				break;
			case 8:
				this.months.add("Septiembre");
				break;
			case 9:
				this.months.add("Octubre");
				break;
			case 10:
				this.months.add("Noviembre");
				break;
			case 11:
				this.months.add("Diciembre");
				break;
			default:
				break;
			}

		}
		return months;
	}

	// Devuelve el numero del mes seleccionado para ver
	public int monthViewSelected() {
		switch (this.monthView) {
		case "Enero":
			return Calendar.JANUARY;
		case "Febrero":
			return Calendar.FEBRUARY;
		case "Marzo":
			return Calendar.MARCH;
		case "Abril":
			return Calendar.APRIL;
		case "Mayo":
			return Calendar.MAY;
		case "Junio":
			return Calendar.JUNE;
		case "Julio":
			return Calendar.JULY;
		case "Agosto":
			return Calendar.AUGUST;
		case "Septiembre":
			return Calendar.SEPTEMBER;
		case "Octubre":
			return Calendar.OCTOBER;
		case "Noviembre":
			return Calendar.NOVEMBER;
		case "Diciembre":
			return Calendar.DECEMBER;
		}
		return -1;
	}

	// Devuelve el numero de dia del mes que se este visualizando
	public int daysOfMonth() {
		int anio = Calendar.getInstance().get(Calendar.YEAR);
		Calendar cal = new GregorianCalendar(anio, this.monthViewSelected(), 1);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public void selectMontView() {
		this.mostrarMensajeInformacion("Mes a mostrar:", this.monthView + ".");
	}

	// Recuento de los mensajes
	@SuppressWarnings("incomplete-switch")
	public void setUp() {
		this.contTotal = new HashMap<Integer, Integer>();
		this.contInbox = new HashMap<Integer, Integer>();
		this.contSent = new HashMap<Integer, Integer>();
		this.contDraft = new HashMap<Integer, Integer>();

		List<SmsDTO> list = getListSms();
		if (list != null) {
			for (SmsDTO sms : list) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sms.getDateAndroid());
				Integer day = calendar.get(Calendar.DATE);
				Integer month = calendar.get(Calendar.MONTH);
				if (month == this.monthViewSelected()) {
					switch (sms.getTypeSms()) {
					case INBOX:
						this.contInbox.put(day, this.contInbox.get(day) == null ? 1 : (this.contInbox.get(day) + 1));
						break;
					case SENT:
						this.contSent.put(day, this.contSent.get(day) == null ? 1 : (this.contSent.get(day) + 1));
						break;
					case DRAFT:
						this.contDraft.put(day, this.contDraft.get(day) == null ? 1 : (this.contDraft.get(day) + 1));
						break;
					}
					this.contTotal.put(day, this.contTotal.get(day) == null ? 1 : (this.contTotal.get(day) + 1));

				}
			}
		}

	}

	// Devuelve y configura todo el grafico
	public CartesianChartModel getCombinedModel() {
		setUp();
		this.combinedModel = new BarChartModel();

		// Configurando el grafico para el numero de mensajes SMS enviados
		if (this.contSent != null) {
			BarChartSeries chartContSent = new BarChartSeries();
			chartContSent.setLabel("Número total de mensajes (enviados)");
			for (int i = 0; i < daysOfMonth(); i++)
				chartContSent.set(i, this.contSent.get(i) != null ? this.contSent.get(i) : 0);
			this.combinedModel.addSeries(chartContSent);
		}

		// Configurando el grafico para el numero de mensajes SMS recibidos
		if (this.contInbox != null) {
			BarChartSeries chartContInbox = new BarChartSeries();
			chartContInbox.setLabel("Número total de mensajes (recibidos)");
			for (int i = 0; i < daysOfMonth(); i++)
				chartContInbox.set(i, this.contInbox.get(i) != null ? this.contInbox.get(i) : 0);
			this.combinedModel.addSeries(chartContInbox);
		}

		// Configurando el grafico para el numero de mensajes SMS recibidos
		/*
		 * if (this.contDraft != null) { BarChartSeries chartContDraft= new
		 * BarChartSeries(); chartContDraft.setLabel(
		 * "Número total de mensajes (borradores)"); for (int i = 0; i <
		 * daysOfMonth(); i++) chartContDraft.set(i, this.contDraft.get(i) !=
		 * null ? this.contDraft.get(i) : 0);
		 * this.combinedModel.addSeries(chartContDraft); }
		 */

		// Configurando el grafico para el numero de mensajes SMS borradores
		if (this.contDraft != null) {
			BarChartSeries chartContDraft = new BarChartSeries();
			chartContDraft.setLabel("Número total de mensajes (borradores)");
			for (int i = 0; i < daysOfMonth(); i++)
				chartContDraft.set(i, this.contDraft.get(i) != null ? this.contDraft.get(i) : 0);
			// this.combinedModel.addSeries(chartContDraft);
		}

		// Configurando el grafico para el numero total de mensajes SMS
		if (this.contTotal != null) {
			LineChartSeries chartContTotal = new LineChartSeries();
			chartContTotal.setLabel("Número total de mensajes (todos)");
			for (int i = 0; i < daysOfMonth(); i++)
				chartContTotal.set(i, this.contTotal.get(i) != null ? this.contTotal.get(i) : 0);
			this.combinedModel.addSeries(chartContTotal);
		}

		// Configuracion grafico
		this.combinedModel.setTitle("Análisis de las mensajes SMS del dispositivo seleccionado");
		this.combinedModel.setLegendPosition("ne");
		this.combinedModel.setMouseoverHighlight(false);
		this.combinedModel.setShowDatatip(false);
		this.combinedModel.setShowPointLabels(true);
		Axis yAxis = this.combinedModel.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setLabel("Nº de mensajes");
		Axis xAxis = combinedModel.getAxis(AxisType.X);
		xAxis.setLabel("Día del mes");
		this.combinedModel.setShowDatatip(false);
		this.combinedModel.setMouseoverHighlight(false);
		this.combinedModel.setAnimate(true);
		return this.combinedModel;
	}

	public List<SmsDTO> getListSms() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listSms = this.service.findAllByImei(imei.toString(), Protocol.TYPE_SMSDTO);
				if (this.listSms != null)
					Collections.reverse(this.listSms);
			} catch (ServiceException e) {
				this.LOGGER.info("getListSms -> SmsManagedBean, EXCEPCION: " + e.toString());
			}
		}
		return this.listSms;
	}

	public void setCombinedModel(CartesianChartModel combinedModel) {
		this.combinedModel = combinedModel;
	}

	public String getMonthView() {
		return monthView;
	}

	public void setMonthView(String monthView) {
		this.monthView = monthView;
	}
}
