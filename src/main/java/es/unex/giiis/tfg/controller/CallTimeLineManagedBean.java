package es.unex.giiis.tfg.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.extensions.event.timeline.TimelineSelectEvent;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.CallDTO;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.CallService;

import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;

@Controller
@ManagedBean
@SessionScoped
public class CallTimeLineManagedBean extends BaseManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private CallService service;

	private List<CallDTO> listCall;

	private TimelineModel model;

	private String eventStyle = "box";
	private boolean axisOnTop;

	@PostConstruct
	protected void initialize() {
		
	}

	public void onSelect(TimelineSelectEvent e) {
		System.out.println("onsele");
		TimelineEvent timelineEvent = e.getTimelineEvent();
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Llamada:", timelineEvent.getData().toString());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public TimelineModel getModel() {
		this.model = new TimelineModel();

		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listCall = this.service.findAllByImei(imei.toString(), Protocol.TYPE_CALLDTO);
			} catch (ServiceException e) {
				e.printStackTrace();
			}

			Calendar cal = Calendar.getInstance();

			for (CallDTO call : this.listCall) {
				cal.setTime(call.getDateAndroid());
				model.add(new TimelineEvent(
						"<div>" + call.getNameContact() + "</div><img src='" + "./../resources/image/ui_icon/call_"
								+ call.getTypeCall() + ".png' style='width:32px;height:26px;'>",
						cal.getTime()));
			}
		}

		return this.model;
	}

	public String getEventStyle() {
		return eventStyle;
	}

	public void setEventStyle(String eventStyle) {
		this.eventStyle = eventStyle;
	}

	public boolean isAxisOnTop() {
		return axisOnTop;
	}

	public void setAxisOnTop(boolean axisOnTop) {
		this.axisOnTop = axisOnTop;
	}

	public List<CallDTO> getListCall() {
		return listCall;
	}

	public void setListCall(List<CallDTO> listCall) {
		this.listCall = listCall;
	}

}
