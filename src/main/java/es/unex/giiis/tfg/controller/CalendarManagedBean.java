package es.unex.giiis.tfg.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.CallDTO;
import es.unex.giiis.tfg.model.ImageDTO;
import es.unex.giiis.tfg.model.SmsDTO;
import es.unex.giiis.tfg.model.ImageDTO.TypeImage;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.CallService;
import es.unex.giiis.tfg.service.ImageService;
import es.unex.giiis.tfg.service.SmsService;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@Controller
@ManagedBean
@SessionScoped
public class CalendarManagedBean extends BaseManagedBean implements Serializable {

	final Logger LOGGER = Logger.getLogger("CalendarManagedBean");

	private static final long serialVersionUID = 1L;

	private ScheduleModel calendar;

	@Autowired
	private SmsService smsService;

	@Autowired
	private CallService callService;

	@Autowired
	private ImageService imageService;

	private List<SmsDTO> listSms;

	private List<CallDTO> listCall;

	private List<ImageDTO> listImage;

	@PostConstruct
	public void init() {
		this.calendar = new DefaultScheduleModel();
		setUpSms();
		setUpCall();
	}

	public void setUpSms() {
		if (getListSms() != null) {
			for (SmsDTO sms : getListSms()) {
				switch (sms.getTypeSms()) {
				case SENT:
					this.calendar.addEvent(new DefaultScheduleEvent("SMS env. a " + sms.getName(),
							sms.getDateAndroid(), sms.getDateAndroid()));
					break;
				case INBOX:
					this.calendar.addEvent(new DefaultScheduleEvent("SMS rec. de " + sms.getName(),
							sms.getDateAndroid(), sms.getDateAndroid()));
					break;
				default:
					break;
				}
			}
		}
	}

	public void setUpImage() {
		if (getListSms() != null) {
			for (ImageDTO image : getListImage()) {
				this.calendar.addEvent(new DefaultScheduleEvent("Fotografía: " + image.getName(),
						image.getDateAndroid(), image.getDateAndroid()));

			}
		}
	}

	public void setUpCall() {
		if (getListCall() != null) {
			for (CallDTO call : getListCall()) {
				switch (call.getTypeCall()) {
				case INCOMING:
					this.calendar.addEvent(new DefaultScheduleEvent("Llamada real. a " + call.getNameContact(),
							call.getDateAndroid(), call.getDateAndroid()));
					break;
				case MISSED:
					this.calendar.addEvent(new DefaultScheduleEvent("Llamada perd. de  " + call.getNameContact(),
							call.getDateAndroid(), call.getDateAndroid()));
					break;
				case OUTGOING:
					this.calendar.addEvent(new DefaultScheduleEvent("Llamada entr. de " + call.getNameContact(),
							call.getDateAndroid(), call.getDateAndroid()));
					break;
				default:
					break;
				}
			}
		}
	}

	public List<SmsDTO> getListSms() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listSms = this.smsService.findAllByImei(imei.toString(), Protocol.TYPE_SMSDTO);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}

		return this.listSms;
	}

	public List<CallDTO> getListCall() {

		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listCall = this.callService.findAllByImei(imei.toString(), Protocol.TYPE_CALLDTO);
			} catch (ServiceException e) {
				e.printStackTrace();
			}

		}

		return this.listCall;
	}

	public List<ImageDTO> getListImage() {

		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listImage = this.imageService.findAllImageByImeiAndTypeImage(imei.toString(), TypeImage.GALLERY);
			} catch (ServiceException e) {
				this.LOGGER.info("getListImageGallery -> ImageManagedBean, EXCEPCION: " + e.toString());
			}
		}
		return this.listImage;
	}

	public ScheduleModel getSchedule() {
		this.calendar = new DefaultScheduleModel();
		this.setUpSms();
		this.setUpCall();
		this.setUpImage();
		return this.calendar;
	}

}
