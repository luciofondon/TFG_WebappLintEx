package es.unex.giiis.tfg.controller;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.ContactDTO;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.ContactService;

@Controller
@ManagedBean
@RequestScoped
public class ContactManagedBean extends BaseManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("ContactManagedBean");

	@Autowired
	private ContactService service;

	private List<ContactDTO> listContact;
	
	private List<ContactDTO> listFilteredContact;

	public void synchronizeLogContact() {
		this.LOGGER.info("Sincronizando contactos con el dispositivo movil...");
		Object ip = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IP);
		if (ip != null) {
			try {
				this.service.sendCmd(Protocol.READ_LOG_CONTACT, ip.toString());
				Thread.sleep(Protocol.TIME_SLEEP);
				this.mostrarMensajeInformacion("Guardando...", "");
			} catch (ServiceException e) {
				this.LOGGER.info("synchronizeLogContact -> ContactManagedBean, EXCEPCION: " + e.toString());
			} catch (InterruptedException e) {
				this.LOGGER.info(
						"synchronizeLogContact -> ContactManagedBean, EXCEPCION: Sleep durante la sincronizacion...");
			}
		} else
			this.mostrarMensajeError("ERROR: ", "Dispositivo seleccionado.");
	}

	public List<ContactDTO> getListContact() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listContact = this.service.findAllByImei(imei.toString(), Protocol.TYPE_CONTACTDTO);
			} catch (ServiceException e) {
				this.LOGGER.info("synchronizeLogContact -> ContactManagedBean, EXCEPCION: " + e.toString());
			}
		}
		return this.listContact;
	}

	public void deleteAllContact() {
		try {
			this.service.deleteAll(Protocol.TYPE_CONTACTDTO);
			Thread.sleep(Protocol.TIME_SLEEP);

			this.mostrarMensajeAlerta("Eliminados.", "");
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			this.LOGGER
					.info("synchronizeLogContact -> ContactManagedBean, EXCEPCION: Sleep durante la sincronizacion...");
		}
	}

	public List<ContactDTO> getListFilteredContact() {
		return listFilteredContact;
	}

	public void setListFilteredContact(List<ContactDTO> listFilteredContact) {
		this.listFilteredContact = listFilteredContact;
	}
	
	

}
