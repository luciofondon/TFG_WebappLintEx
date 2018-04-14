package es.unex.giiis.tfg.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.CallDTO;
import es.unex.giiis.tfg.model.CallDTO.TypeCall;
import es.unex.giiis.tfg.model.ContactDTO;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.CallService;
import es.unex.giiis.tfg.service.ContactService;

@Controller
@ManagedBean
@SessionScoped
public class CallManagedBean extends BaseManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("CallManagedBean");

	@Autowired
	CallService service;

	@Autowired
	private ContactService contactService;

	// Historial de llamadas
	private List<CallDTO> listCall;
	
	
	private List<CallDTO> listFilteredCall;

	// Llamadas grabadas
	private List<CallDTO> listCallRecord;

	// Contactos para realizar una llamada
	private List<ContactDTO> listContact;

	// Activar la agenda o para introducir un telefono
	private boolean phoneBook;

	// Numero de telefono al que se realizara la llamada
	private String phoneNumber;

	@PostConstruct
	public void init() {
		this.phoneBook = false;
	}

	@SuppressWarnings("resource")
	public void doCall() {
		this.LOGGER.info("Realizando llamada con el dispositivo movil a " + this.phoneNumber);
		Object ip = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IP);
		if (ip != null) {
			try {

				if (this.phoneNumber != null && this.phoneNumber != "") {
					this.service.sendCmd(Protocol.DO_CALL, ip.toString());

					DatagramSocket ds = new DatagramSocket();

					byte[] phoneNumber = this.phoneNumber.getBytes();
					DatagramPacket dp1 = new DatagramPacket(phoneNumber, phoneNumber.length,
							InetAddress.getByName(ip.toString()), Protocol.PUERTO);
					ds.send(dp1);
					this.reset();
					Thread.sleep(Protocol.TIME_SLEEP);

					this.mostrarMensajeInformacion("Llamando...", "");
				} else {
					this.mostrarMensajeAlerta("Campos erróneos...", "");
				}

			} catch (ServiceException e2) {
				e2.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (SocketException | UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else
			this.mostrarMensajeError("Error:", "Dispositivo seleccionado.");
	}

	public void synchronizeLogCall() {
		this.LOGGER.info("Sincronizando llamadas con dispositivo movil");
		Object ip = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IP);
		if (ip != null) {
			try {
				this.service.sendCmd(Protocol.READ_LOG_CALL, ip.toString());

				Thread.sleep(Protocol.TIME_SLEEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ServiceException e1) {
				e1.printStackTrace();
			}
		}

	}

	public void reset() {
		this.phoneNumber = "";
		this.phoneBook = false;
		this.mostrarMensajeAlerta("Reseteando campos...", "");
	}

	// Metodo on/off cuando se activa o desactiva la agenda
	public void onOffPhoneBook() {
		if (!this.phoneBook)
			this.phoneNumber = "";
		this.mostrarMensajeInformacion(this.phoneBook ? "Agenda activada." : "Agenda desactivada.", "");
	}

	public StreamedContent downloadCall(CallDTO call) {
		this.LOGGER.info("Descargando grabacion de llamada...");
		StreamedContent fileDownload = null;
		try {
			File file = new File(call.getPathJava());
			InputStream stream = new FileInputStream(file);
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			fileDownload = new DefaultStreamedContent(stream, externalContext.getMimeType(file.getName()),
					file.getName());
		} catch (FileNotFoundException e) {
			this.LOGGER.info("Error al descar imagen...");
		}
		return fileDownload;

	}

	public List<ContactDTO> getListContact() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listContact = this.contactService.findAllByImei(imei.toString(), Protocol.TYPE_CONTACTDTO);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		return this.listContact;
	}

	public List<CallDTO> getListCallRecord() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listCallRecord = this.service.findAllCallByImeiAndTypeCall(imei.toString(), TypeCall.RECORD);

			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		return this.listCallRecord;
	}

	public List<CallDTO> getListCall() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				//Al ser distinto a type RECORD se obtienen todas
				this.listCall = this.service.findAllCallByImeiAndTypeCall(imei.toString(), TypeCall.INCOMING);

			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		return this.listCall;
	}

	public String getPhoneNumber() {
		return phoneNumber;
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

	public void deleteAllCall() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.service.deleteAllCallByImeiAndTypeCall(imei.toString(), TypeCall.INCOMING);
				this.service.deleteAllCallByImeiAndTypeCall(imei.toString(), TypeCall.OUTGOING);
				this.service.deleteAllCallByImeiAndTypeCall(imei.toString(), TypeCall.MISSED);
				this.service.deleteAllCallByImeiAndTypeCall(imei.toString(), TypeCall.ERROR);

			} catch (ServiceException e) {
				this.LOGGER.info("deleteAllAudio -> AundioManagedBean, EXCEPCION: " + e.toString());
			}
		}
		this.mostrarMensajeAlerta("Eliminando...", "");
	}

	public void deleteAllCallRecord() {
		this.LOGGER.info("Eliminando llamadas grabadas...");

		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listCallRecord = this.service.findAllCallByImeiAndTypeCall(imei.toString(), TypeCall.RECORD);

				for (CallDTO call : this.listCallRecord) {
					File file = new File(call.getPathJava());
					if (file.exists() && file.isFile()) {
						if (!file.delete())
							this.mostrarMensajeError("Error al eliminar el audio.", "");
					}
				}
				this.service.deleteAllCallByImeiAndTypeCall(imei.toString(), TypeCall.RECORD);
			} catch (ServiceException e) {
				this.LOGGER.info("deleteAllAudio -> AundioManagedBean, EXCEPCION: " + e.toString());
			}
		}
		this.mostrarMensajeAlerta("Eliminado.", "");
	}

	public boolean isPhoneBook() {
		return phoneBook;
	}

	public void setPhoneBook(boolean phoneBook) {
		this.phoneBook = phoneBook;
	}

	public List<CallDTO> getListFilteredCall() {
		return listFilteredCall;
	}

	public void setListFilteredCall(List<CallDTO> listFilteredCall) {
		this.listFilteredCall = listFilteredCall;
	}

	
}
