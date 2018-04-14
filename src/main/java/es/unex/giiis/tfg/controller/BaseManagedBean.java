package es.unex.giiis.tfg.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.DeviceDTO;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.DeviceService;

@Controller
@ManagedBean
@SessionScoped
public class BaseManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private DeviceService deviceService;

	// Grabando mediante la grabadora de audio o de video
	private boolean record;

	// Habilitar o deshabilitar botones.
	private boolean disabled;

	// Desactivar opciones de sincronización del dispositivo si no esta
	// conectado
	private boolean disabledDevice;

	// Mensaje para mostrar en el listado de la tabla cuando no hay resultados,
	private String emptyMessage;

	// Mensaje mostrado cuando se empiza la sincronizacion
	private String synchronizeMessage;

	public boolean isDisabled() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null)
			disabled = false;
		else {
			disabled = true;
			mostrarMensajeAlerta("No hay ningún dispositivo", "");

		}
		return this.disabled;
	}

	public void disabled() {
		mostrarMensajeAlerta("Hola", "");
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isRecord() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null)
			return record;
		else
			return false;
	}

	public void setRecord(boolean record) {
		this.record = record;
	}

	public String getEmptyMessage() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null)
			this.emptyMessage = "No hay ningún listado asignado al dispositivo.";
		else
			this.emptyMessage = "No hay ningún dispositivo seleccionado para monitorizar. Por favor, primero elija un dispositivo en la pestaña Dispositivos.";

		return emptyMessage;
	}

	public String getSynchronizeMessage() {
		this.synchronizeMessage = "Sincronizados recursos con el dispositivo seleccionado...";
		return synchronizeMessage;
	}

	public void setSynchronizeMessage(String synchronizeMessage) {
		this.synchronizeMessage = synchronizeMessage;
	}

	public void setEmptyMessage(String emptyMessage) {
		this.emptyMessage = emptyMessage;
	}

	public void mostrarMensajeAlerta(String titulo, String mensaje) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, titulo, mensaje));

	}

	public void mostrarMensajeInformacion(String titulo, String mensaje) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, mensaje));

	}

	public void mostrarMensajeError(String titulo, String mensaje) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, mensaje));

	}

	public boolean isDisabledDevice() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				DeviceDTO device = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO).get(0);
				return !device.isDeviceState();

			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return disabled;

	}

	public void setDisabledDevice(boolean disabledDevice) {
		this.disabledDevice = disabledDevice;
	}

}
