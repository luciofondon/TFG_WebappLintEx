package es.unex.giiis.tfg.controller;

import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.ContactDTO;
import es.unex.giiis.tfg.model.SmsDTO;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.ContactService;
import es.unex.giiis.tfg.service.SmsService;

@Controller
@ManagedBean
@SessionScoped
public class SmsManagedBean extends BaseManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("SmsManagedBean");

	@Autowired
	private SmsService service;

	@Autowired
	private ContactService contactService;

	// Historial de mensajes SMS
	private List<SmsDTO> listSms;
	
	private List<SmsDTO> listFilteredSms;

	// Contactos para enviar un mensaje utiliznado agenda
	private List<ContactDTO> listContact;

	// Numero de telefono al que se enviara el mensaje
	private String phoneNumber;

	// Contenido del mensaje que se enviara
	private String message;

	// Activar o desactiva agenda telefonica
	private boolean phoneBook;

	@PostConstruct
	public void init() {
		this.phoneBook = false;
	}

	// Metodo on/off para activar o desactiva la agenda
	public void on_off() {
		this.mostrarMensajeInformacion(phoneBook ? "Agenda activada." : "Agenda desactivada.", "");
		if (!phoneBook)
			this.phoneNumber = "";
	}

	public void synchronizeLogSms() {
		this.LOGGER.info("Sincronizando SMS con dispositivo movil");
		Object ip = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IP);
		if (ip != null) {
			try {
				this.service.sendCmd(Protocol.READ_LOG_SMS, ip.toString());
				Thread.sleep(Protocol.TIME_SLEEP);
				this.mostrarMensajeInformacion("Guardando...", "");
			} catch (InterruptedException e) {
				this.LOGGER.info("synchronizeLogCall -> SmsManagedBean, EXCEPCION: Sleep durante la sincronizacion...");
			} catch (ServiceException e) {
				this.LOGGER.info("getListSms -> SmsManagedBean, EXCEPCION: " + e.toString());
			}
		} else
			this.mostrarMensajeError("ERROR: ", "Dispositivo seleccionado.");
	}

	public void sendSms() {
		this.LOGGER.info("Enviando mensaje al numero: " + this.phoneNumber);
		Object ip = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IP);
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);

		try {
			if (this.message != null && this.message != "" && this.phoneNumber != null && this.phoneNumber != "") {
				if (ip != null && imei != null) {

					this.service.sendCmd(Protocol.SEND_SMS, ip.toString());

					SmsDTO sms = new SmsDTO();
					sms.setMessage(this.message);
					sms.setPhoneNumber(this.phoneNumber);
					System.out.println(this.message + " " + this.phoneNumber);
					DatagramSocket ds = new DatagramSocket();

					byte[] phoneNumber = this.phoneNumber.getBytes();
					DatagramPacket dp1 = new DatagramPacket(phoneNumber, phoneNumber.length,
							InetAddress.getByName(ip.toString()), Protocol.PUERTO);
					ds.send(dp1);

					byte[] mensaje = this.message.getBytes();
					DatagramPacket dp2 = new DatagramPacket(mensaje, mensaje.length,
							InetAddress.getByName(ip.toString()), Protocol.PUERTO);
					ds.send(dp2);

					ds.close();
					this.reset();
					this.LOGGER.info("Mensaje enviado...");
					Thread.sleep(Protocol.TIME_SLEEP);
					this.mostrarMensajeInformacion("Enviando mensaje...", "");

				}
			} else
				mostrarMensajeAlerta("Campos erróneos...", "");

		} catch (Exception e) {
			this.LOGGER.info("sendSms -> SmsManagedBean, EXCEPCION: " + e.toString());
		}

	}

	public void reset() {
		this.phoneNumber = "";
		this.phoneBook = false;
		this.message = "";
		this.mostrarMensajeAlerta("Reseteando campos...", "");
	}

	public void setPhoneNumber(String phoneNumber) {
		// Los numero de telefono en la agenda se guardan como 6666, 44444
		// en un mismo contato.
		// Quedarse con el primer numero del contacto
		String[] token = phoneNumber.split(",");
		if (token != null)
			this.phoneNumber = token[0];
		else
			this.phoneNumber = phoneNumber;
	}

	public List<SmsDTO> getListSms() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listSms = this.service.findAllByImei(imei.toString(), Protocol.TYPE_SMSDTO);
			} catch (ServiceException e) {
				this.LOGGER.info("getListSms -> SmsManagedBean, EXCEPCION: " + e.toString());
			}
		}
		return this.listSms;
	}

	public List<ContactDTO> getListContact() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listContact = this.contactService.findAllByImei(imei.toString(), Protocol.TYPE_CONTACTDTO);
			} catch (ServiceException e) {
				this.LOGGER.info("getListContact -> SmsManagedBean, EXCEPCION: " + e.toString());
			}
		}
		return this.listContact;
	}

	public void deleteAllSms() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.service.deleteAllByImei(imei.toString(), Protocol.TYPE_SMSDTO);
				Thread.sleep(Protocol.TIME_SLEEP);
				this.mostrarMensajeAlerta("Eliminados.", "");
			} catch (ServiceException e) {
				this.LOGGER.info("deleteAllSms -> SmsManagedBean, EXCEPCION: " + e.toString());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public boolean isPhoneBook() {
		return phoneBook;
	}

	public void setPhoneBook(boolean phoneBook) {
		this.phoneBook = phoneBook;
	}

	public void setListContact(List<ContactDTO> listContact) {
		this.listContact = listContact;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<SmsDTO> getListFilteredSms() {
		return listFilteredSms;
	}

	public void setListFilteredSms(List<SmsDTO> listFilteredSms) {
		this.listFilteredSms = listFilteredSms;
	}
	
	

}
