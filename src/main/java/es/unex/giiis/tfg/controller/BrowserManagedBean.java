package es.unex.giiis.tfg.controller;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.BrowserDTO;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.BrowserService;

@Controller
@ManagedBean
@SessionScoped
public class BrowserManagedBean extends BaseManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("BrowserManagedBean");

	@Autowired
	private BrowserService service;

	private List<BrowserDTO> listBrowser;

	private List<BrowserDTO> listFilteredBrowser;

	public void synchronizeLogBrowser() {
		this.LOGGER.info("Sincronizando navegador con dispositivo movil...");
		Object ip = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IP);
		if (ip != null) {
			try {
				this.service.sendCmd(Protocol.READ_LOG_BROWSER, ip.toString());
				Thread.sleep(Protocol.TIME_SLEEP);
				this.mostrarMensajeInformacion("Guardando...", "");
			} catch (ServiceException e) {
				this.LOGGER.info("synchronizeLogBrowser -> BrowserManagedBean, EXCEPCION: " + e.toString());
			} catch (InterruptedException e) {
				this.LOGGER.info(
						"synchronizeLogBrowser -> BrowserManagedBean, EXCEPCION: Sleep durante la sincronizacion...");
			}
		} else
			this.mostrarMensajeError("Error.: ", "Dispositivo seleccionado.");
	}

	public List<BrowserDTO> getListBrowser() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listBrowser = service.findAllByImei(imei.toString(), Protocol.TYPE_BROWSERDTO);
			} catch (ServiceException e) {
				this.LOGGER.info("synchronizeLogBrowser -> BrowserManagedBean, EXCEPCION: " + e.toString());
			}
		}
		return listBrowser;
	}

	public void deleteAllBrowser() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.service.deleteAllByImei(imei.toString(), Protocol.TYPE_BROWSERDTO);
				Thread.sleep(Protocol.TIME_SLEEP);
				this.mostrarMensajeAlerta("Eliminados.", "");
			} catch (ServiceException e) {
				this.LOGGER.info("deleteAllBrowser -> BrowserManagedBean, EXCEPCION: " + e.toString());
			} catch (InterruptedException e) {
				this.LOGGER.info(
						"synchronizeLogBrowser -> BrowserManagedBean, EXCEPCION: Sleep durante la sincronizacion...");
			}
		}

	}

	public void setListBrowser(List<BrowserDTO> listBrowser) {
		this.listBrowser = listBrowser;
	}

	public List<BrowserDTO> getListFilteredBrowser() {
		return listFilteredBrowser;
	}

	public void setListFilteredBrowser(List<BrowserDTO> listFilteredBrowser) {
		this.listFilteredBrowser = listFilteredBrowser;
	}

}
