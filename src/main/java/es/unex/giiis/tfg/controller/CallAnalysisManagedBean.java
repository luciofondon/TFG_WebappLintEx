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
import es.unex.giiis.tfg.model.CallDTO;
import es.unex.giiis.tfg.model.CallDTO.TypeCall;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.CallService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

@Controller
@ManagedBean
@SessionScoped
public class CallAnalysisManagedBean extends BaseManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private CallService service;

	private List<CallDTO> listCall;

	private CartesianChartModel combinedModel;

	// Duracion total de todas las llamado por dia: DIA - DURACION
	private Map<Integer, Float> durationTotal;

	// Duracion total de las llamadas entrantes por dia: DIA - DURACION
	private Map<Integer, Float> durationIncoming;

	// Duracion total de las llamadas salientes por dia: DIA - DURACION
	private Map<Integer, Float> durationOutgoning;

	// Contador del numero total de llamadas por dia: DIA - TOTAL
	private Map<Integer, Integer> contTotal;

	private Map<Integer, Integer> contIncoming;

	private Map<Integer, Integer> contOutgoing;

	private Map<Integer, Integer> contMissed;

	// Mes que se esta visualizando
	private String monthView;

	// Meses disponibles para visualizar
	private List<String> months;

	@PostConstruct
	public void init() {
		// Mes a mostrar enero
		this.monthView = "Enero";
	}

	public List<String> getMonths() {
		this.months = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		for (int i = 0; i <= calendar.get(Calendar.MONTH); i++) {
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
		return this.months;
	}

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

	public int daysOfMonth() {
		Calendar cal = new GregorianCalendar(2016, this.monthViewSelected(), 1);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);

	}

	public void selectMontView() {
		this.mostrarMensajeInformacion("Mes a mostrar:", this.monthView + ".");
	}

	public String getMonthView() {
		return monthView;
	}

	public void setMonthView(String monthView) {
		this.monthView = monthView;
	}

	public void setUpContCall() {
		this.contTotal = new HashMap<Integer, Integer>();
		this.contIncoming = new HashMap<Integer, Integer>();
		this.contOutgoing = new HashMap<Integer, Integer>();
		this.contMissed = new HashMap<Integer, Integer>();

		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			List<CallDTO> list = this.getListCall();
			if (list != null) {
				for (CallDTO call : list) {
					// Calcular el dia
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(call.getDateAndroid());
					Integer day = calendar.get(Calendar.DATE);
					Integer month = calendar.get(Calendar.MONTH);
					if (month == this.monthViewSelected()) {
						this.contTotal.put(day, this.contTotal.get(day) != null ? (this.contTotal.get(day) + 1) : 1);
						if (call.getTypeCall() == TypeCall.INCOMING)
							this.contIncoming.put(day,
									this.contIncoming.get(day) != null ? (this.contIncoming.get(day) + 1) : 1);
						else if (call.getTypeCall() == TypeCall.OUTGOING)
							this.contOutgoing.put(day,
									this.contOutgoing.get(day) != null ? (this.contOutgoing.get(day) + 1) : 1);
						else if (call.getTypeCall() == TypeCall.MISSED)
							this.contMissed.put(day,
									this.contMissed.get(day) != null ? (this.contMissed.get(day) + 1) : 1);
						

					}
				}
			}
		}
	}

	public void setUpDurationCall() {
		this.durationTotal = new HashMap<Integer, Float>();
		this.durationIncoming = new HashMap<Integer, Float>();
		this.durationOutgoning = new HashMap<Integer, Float>();

		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			for (CallDTO call : this.getListCall()) {
				// Calcular el dia
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(call.getDateAndroid());
				Integer day = calendar.get(Calendar.DATE);
				Integer month = calendar.get(Calendar.MONTH);

				if (month == this.monthViewSelected()) {

					float aux = 0;
					aux += call.getDurationSecond();
					aux += (call.getDurationMinute() * 60);
					aux += (call.getDurationHour() * 60 * 60);
					aux = aux / 60;

					this.durationTotal.put(day,
							this.durationTotal.get(day) != null ? (aux + this.durationTotal.get(day)) : aux);

					if (call.getTypeCall() == TypeCall.INCOMING) {
						this.durationIncoming.put(day,
								this.durationIncoming.get(day) != null ? (aux + this.durationIncoming.get(day)) : aux);

					} else if (call.getTypeCall() == TypeCall.OUTGOING) {
						this.durationOutgoning.put(day, this.durationOutgoning.get(day) != null
								? (aux + this.durationOutgoning.get(day)) : aux);
					}
				}
			}
		}
	}

	public List<CallDTO> getListCall() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listCall = this.service.findAllByImei(imei.toString(), Protocol.TYPE_CALLDTO);
			} catch (ServiceException e) {
			}
		}
		return this.listCall;
	}

	public CartesianChartModel getCombinedModel() {
		setUpContCall();
		setUpDurationCall();
		this.combinedModel = new BarChartModel();

		// Configurando el grafico para la duracion total de las llamadas
		if (this.durationTotal != null) {
			LineChartSeries chartDuratinTotal = new LineChartSeries();
			chartDuratinTotal.setLabel("Mintuos totales (todas)");
			for (int i = 0; i < this.daysOfMonth(); i++)
				chartDuratinTotal.set(i, this.durationTotal.get(i) != null ? this.durationTotal.get(i) : 0);
			this.combinedModel.addSeries(chartDuratinTotal);
		}

		// Configurando el grafico para la duracion de las llamadas entrantes
		if (this.durationIncoming != null) {
			BarChartSeries chartDuratinIncoming = new BarChartSeries();
			chartDuratinIncoming.setLabel("Mintuos totales (llamadas entrantes)");
			for (int i = 0; i < this.daysOfMonth(); i++)
				chartDuratinIncoming.set(i, this.durationIncoming.get(i) != null ? this.durationIncoming.get(i) : 0);
			this.combinedModel.addSeries(chartDuratinIncoming);
		}

		// Configurando el grafico para la duracion de las llamadas salientes
		if (this.durationOutgoning != null) {
			BarChartSeries chartDuratinOutcoming = new BarChartSeries();
			chartDuratinOutcoming.setLabel("Mintuos totales (llamadas salientes)");
			for (int i = 0; i < this.daysOfMonth(); i++)
				chartDuratinOutcoming.set(i, this.durationOutgoning.get(i) != null ? this.durationOutgoning.get(i) : 0);
			this.combinedModel.addSeries(chartDuratinOutcoming);
		}

		// Configurando el grafico para el numero de llamadas totales por dia
		if (this.contTotal != null) {
			LineChartSeries chartContTotal = new LineChartSeries();
			chartContTotal.setLabel("Número total de llamadas(todas)");
			for (int i = 0; i < this.daysOfMonth(); i++)
				chartContTotal.set(i, this.contTotal.get(i) != null ? this.contTotal.get(i) : 0);
			this.combinedModel.addSeries(chartContTotal);
		}

		// Configurando el grafico para el numero de llamadas entrantes por dia
		if (this.contIncoming != null) {
			LineChartSeries chartContIncoming = new LineChartSeries();
			chartContIncoming.setLabel("Número total de llamadas (entrantes)");
			for (int i = 0; i < this.daysOfMonth(); i++)
				chartContIncoming.set(i, this.contIncoming.get(i) != null ? this.contIncoming.get(i) : 0);
			this.combinedModel.addSeries(chartContIncoming);
		}

		// Configurando el grafico para el numero de llamadas salientes por dia
		if (this.contOutgoing != null) {
			LineChartSeries chartContOutgoing = new LineChartSeries();
			chartContOutgoing.setLabel("Número total de llamadas (saliente)");
			for (int i = 0; i < this.daysOfMonth(); i++)
				chartContOutgoing.set(i, this.contOutgoing.get(i) != null ? this.contOutgoing.get(i) : 0);
			this.combinedModel.addSeries(chartContOutgoing);
		}

		// Configurando el grafico para el numero de llamadas perdidas por dia
		if (this.contMissed != null) {
			LineChartSeries chartContMissed = new LineChartSeries();
			chartContMissed.setLabel("Número total de llamadas (perdidas)");
			for (int i = 0; i < this.daysOfMonth(); i++)
				chartContMissed.set(i, this.contMissed.get(i) != null ? this.contMissed.get(i) : 0);
			this.combinedModel.addSeries(chartContMissed);
		}

		// Configuracion grafico
		this.combinedModel.setTitle("Análisis de las llamadas del dispositivo seleccionado");
		this.combinedModel.setLegendPosition("ne");
		this.combinedModel.setMouseoverHighlight(false);
		this.combinedModel.setShowDatatip(false);
		this.combinedModel.setShowPointLabels(true);
		Axis yAxis = combinedModel.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setLabel("Total");
		Axis xAxis = combinedModel.getAxis(AxisType.X);
		xAxis.setLabel("Día del mes");
		this.combinedModel.setAnimate(true);

		return this.combinedModel;
	}

	public void setCombinedModel(CartesianChartModel combinedModel) {
		this.combinedModel = combinedModel;
	}
}
