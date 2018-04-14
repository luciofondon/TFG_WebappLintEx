package es.unex.giiis.tfg.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.AudioDTO;
import es.unex.giiis.tfg.model.DeviceDTO;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.AudioService;
import es.unex.giiis.tfg.service.DeviceService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.primefaces.model.DefaultStreamedContent;

@Controller
@ManagedBean
@SessionScoped
public class AudioManagedBean extends BaseManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("AudioManagedBean");

	@Autowired
	private AudioService service;

	@Autowired
	private DeviceService deviceService;

	private boolean record;

	private List<AudioDTO> listAudio;

	private boolean save;

	@PostConstruct
	public void init() {
		this.save = false;
		// Inicializar a no grabar, por si no existe el dispositivo
		// Dependen de este parametro (mostrar grabar de la barra)
		this.record = false;
	}

	public StreamedContent getFileDownload(AudioDTO audio) {
		this.LOGGER.info("Descargando fichero...");
		StreamedContent fileDownload = null;
		try {
			File file = new File(audio.getPathJava());
			InputStream stream = new FileInputStream(file);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			fileDownload = new DefaultStreamedContent(stream, externalContext.getMimeType(file.getName()),
					file.getName());
		} catch (FileNotFoundException e) {
			this.LOGGER.info("getFileDownload -> AudioManagedBean, EXCEPCION: Descargar audio...");
		}
		return fileDownload;
	}

	public void record() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		Object ip = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IP);

		if (ip != null && imei != null) {
			try {
				// Actualizar el estado de la grabacion en el dispositivo
				DeviceDTO device = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO).get(0);
				if (this.record && !this.save) {
					this.service.sendCmd(Protocol.START_RECORD_AUDIO, ip.toString());
					device.setLastSynchronization(Calendar.getInstance().getTime());
					device.setAudioRecord(true);
					mostrarMensajeInformacion("Grabando audio...", "");
				} else if (this.record && this.save) {
					this.service.sendCmd(Protocol.START_RECORD_AUDIO_SAVE, ip.toString());
					device.setLastSynchronization(Calendar.getInstance().getTime());
					device.setAudioRecord(true);
					mostrarMensajeInformacion("Grabando audio...", "");
				} else {
					this.service.sendCmd(Protocol.STOP_RECORD_AUDIO, ip.toString());
					device.setLastSynchronization(Calendar.getInstance().getTime());
					device.setAudioRecord(false);
					mostrarMensajeInformacion("Grabación finalizada.", "");
					mostrarMensajeAlerta("Guardando...", "");
				}
				this.deviceService.delete(device);
				this.deviceService.add(device, Protocol.TYPE_DEVICEDTO);
				Thread.sleep(Protocol.TIME_SLEEP);

			} catch (ServiceException e) {
				this.LOGGER.info("record -> AundioManagedBean, EXCEPCION: " + e.toString());
			} catch (InterruptedException e) {
				this.LOGGER.info(
						"synchronizeLogBrowser -> BrowserManagedBean, EXCEPCION: Sleep durante la sincronizacion...");
			}

		} else
			this.mostrarMensajeError("Error: ", "No hay dispositivo.");

	}

	public List<AudioDTO> getListAudio() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listAudio = this.service.findAllByImei(imei.toString(), Protocol.TYPE_AUDIODTO);
			} catch (ServiceException e) {
				this.LOGGER.info("getListAudio -> AundioManagedBean, EXCEPCION: " + e.toString());
			}
		}
		return this.listAudio;
	}

	public void deleteAllAudio() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listAudio = this.service.findAllByImei(imei.toString(), Protocol.TYPE_AUDIODTO);
				for (AudioDTO audio : listAudio) {
					File file = new File(audio.getPathJava());
					if (file.exists() && file.isFile()) {
						if (!file.delete())
							this.mostrarMensajeError("Error al eliminar fichero.", "");
					}
				}
				this.service.deleteAllByImei(imei.toString(), Protocol.TYPE_AUDIODTO);
				Thread.sleep(Protocol.TIME_SLEEP);

			} catch (ServiceException e) {
				this.LOGGER.info("deleteAllAudio -> AundioManagedBean, EXCEPCION: " + e.toString());
			} catch (InterruptedException e) {
				this.LOGGER.info(
						"synchronizeLogBrowser -> BrowserManagedBean, EXCEPCION: Sleep durante la sincronizacion...");
			}
		}
		this.mostrarMensajeAlerta("Eliminando.", "");

	}

	public boolean isRecord() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				List<DeviceDTO> listDevice = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO);
				if (listDevice != null)
					this.record = listDevice.get(0).isAudioRecord();
				else
					this.record = false;
			} catch (ServiceException e) {
				this.LOGGER.info("isRecord -> VideoManagedBean, EXCEPCION: " + e.toString());
			}
		}
		return this.record;
	}

	public void selectSave() {
		this.mostrarMensajeInformacion(this.save ? "Guardar audio." : "No guardar audio.", "");
	}

	public void setRecord(boolean record) {
		this.record = record;
	}

	public void setListAudio(List<AudioDTO> listAudio) {
		this.listAudio = listAudio;
	}

	public boolean isSave() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
	}

}
