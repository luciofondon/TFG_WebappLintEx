package es.unex.giiis.tfg.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.SmsDTO;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.SmsService;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

@Controller
@ManagedBean
@SessionScoped
public class SmsTimeLineManagedBean extends BaseManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	final Logger LOGGER = Logger.getLogger("SmsTimeLineManagedBean");


	@Autowired
	private SmsService service;

	private List<SmsDTO> listSms;

	private TimelineModel model;

	private boolean axisOnTop;

	@PostConstruct
	protected void initialize() {
		
	}

	public TimelineModel getModel() {
		this.model = new TimelineModel();

		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listSms = this.service.findAllByImei(imei.toString(), Protocol.TYPE_SMSDTO);
			} catch (ServiceException e) {
				this.LOGGER.info("getModel -> SmsTimeLineManagedBean, EXCEPCION: " + e.toString());
			}
			Calendar cal = Calendar.getInstance();
			for (SmsDTO sms : this.listSms) {
				cal.setTime(sms.getDateAndroid());
				this.model.add(new TimelineEvent(
						"<div>" + sms.getName() + "</div><img src='" + "./../resources/image/ui_icon/sms_"
								+ sms.getTypeSms() + ".png' style='width:32px;height:26px;'>",
						cal.getTime()));
			}
		}
		return this.model;
	}

	public boolean isAxisOnTop() {
		return axisOnTop;
	}

	public void setAxisOnTop(boolean axisOnTop) {
		this.axisOnTop = axisOnTop;
	}

	public List<SmsDTO> getListSms() {
		return listSms;
	}

	public void setListSms(List<SmsDTO> listSms) {
		this.listSms = listSms;
	}

}
