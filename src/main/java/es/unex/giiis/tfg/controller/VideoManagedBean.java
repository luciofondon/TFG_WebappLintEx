package es.unex.giiis.tfg.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
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
import es.unex.giiis.tfg.model.DeviceDTO;
import es.unex.giiis.tfg.model.VideoDTO;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.DeviceService;
import es.unex.giiis.tfg.service.VideoService;

@Controller
@ManagedBean
@SessionScoped
public class VideoManagedBean extends BaseManagedBean implements Serializable {
	final Logger LOGGER = Logger.getLogger("VideoManagedBean");

	private static final long serialVersionUID = 1L;

	@Autowired
	private VideoService service;

	@Autowired
	private DeviceService deviceService;

	private boolean record;

	private List<VideoDTO> listVideo;

	private StreamedContent media;

	private int camera;

	private int flash;

	private boolean save;

	private StreamedContent fileDownload;

	@PostConstruct
	public void init() {
		// Incializar la camara a la principal
		this.camera = 0;
		// Incializar el flash a apagado
		this.flash = 0;
		// Inicializar a no grabar, por si no existe el dispositivo
		// Dependen de este parametro (mostrar grabar de la barra)
		this.record = false;

	}

	public StreamedContent getFileDownload(VideoDTO video) {
		this.LOGGER.info("Descargando fichero.");
		try {
			File file = new File(video.getPathJava());
			InputStream stream = new FileInputStream(file);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			fileDownload = new DefaultStreamedContent(stream, externalContext.getMimeType(file.getName()),
					file.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fileDownload;
	}

	public StreamedContent getMedia() {
		return media;
	}

	public void setMedia(StreamedContent media) {
		this.media = media;
	}

	public void record() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		Object ip = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IP);

		if (ip != null && imei != null) {
			try {
				// Actualizar el estado de la grabacion en el dispositivo
				DeviceDTO device = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO).get(0);
				if (this.record && !this.save) {
					if (this.camera == 0) {
						if (this.flash == 0)
							this.service.sendCmd(Protocol.START_RECORD_VIDEO_PRIMARY_FLASH_MODE_OFF, ip.toString());
						else if (this.flash == 1)
							this.service.sendCmd(Protocol.START_RECORD_VIDEO_PRIMARY_FLASH_MODE_TORCH, ip.toString());
						else
							this.service.sendCmd(Protocol.START_RECORD_VIDEO_PRIMARY_FLASH_MODE_AUTO, ip.toString());

					} else
						this.service.sendCmd(Protocol.START_RECORD_VIDEO_SECUNDARY, ip.toString());
					device.setVideoRecord(true);

					mostrarMensajeInformacion("Grabando video...", "");
				} else if (this.record && this.save) {
					if (this.camera == 0) {
						if (this.flash == 0)
							this.service.sendCmd(Protocol.START_RECORD_VIDEO_PRIMARY_FLASH_MODE_OFF_SAVE,
									ip.toString());
						else if (this.flash == 1)
							this.service.sendCmd(Protocol.START_RECORD_VIDEO_PRIMARY_FLASH_MODE_TORCH_SAVE,
									ip.toString());
						else
							this.service.sendCmd(Protocol.START_RECORD_VIDEO_PRIMARY_FLASH_MODE_AUTO_SAVE,
									ip.toString());

					} else
						this.service.sendCmd(Protocol.START_RECORD_VIDEO_SECUNDARY_SAVE, ip.toString());
					device.setVideoRecord(true);
					mostrarMensajeInformacion("Grabando video...", "");
				} else {
					this.service.sendCmd(Protocol.STOP_RECORD_VIDEO, ip.toString());
					device.setVideoRecord(false);
					mostrarMensajeInformacion("Grabación finalizada.", "");
					mostrarMensajeAlerta("Guardando...", "");
				}
				this.deviceService.delete(device);
				this.deviceService.add(device, Protocol.TYPE_DEVICEDTO);
				Thread.sleep(Protocol.TIME_SLEEP);
			} catch (ServiceException e) {
				this.LOGGER.info("record -> CallListener, EXCEPCION: " + e.toString());
			} catch (InterruptedException e) {
				this.LOGGER.info(
						"synchronizeLogBrowser -> BrowserManagedBean, EXCEPCION: Sleep durante la sincronizacion...");
			}

		} else
			this.mostrarMensajeError("Error: ", "No hay dispositivo.");

	}

	public List<VideoDTO> getListVideo() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listVideo = this.service.findAllByImei(imei.toString(), Protocol.TYPE_VIDEODTO);
			} catch (ServiceException e) {
				this.mostrarMensajeError("ERROR: ", "Leer videos");
			}
		}

		return listVideo;
	}

	public void setListVideo(List<VideoDTO> listVideo) {
		this.listVideo = listVideo;
	}

	public void selectCamera() {
		String tipo = "";
		switch (this.camera) {
		case 0:
			tipo = "Principal.";
			break;
		case 1:
			// Apagar flash
			this.flash = 0;
			tipo = "Secundaria.";
			break;
		default:
			tipo = "No disponible.";
			break;
		}
		this.mostrarMensajeInformacion("Tipo de cámara:", tipo);

	}

	public void selectFlash() {
		String tipo = "";
		switch (this.flash) {
		case 0:
			tipo = "Apagado";
			break;
		case 1:
			tipo = "Encendido.";
			break;
		case 2:
			tipo = "Automático.";
			break;
		case 3:
			tipo = "Reducción de ojos rojos.";
			break;
		case 4:
			tipo = "Emisión constante de luz.";
			break;
		default:
			tipo = "No disponible.";
			break;
		}
		this.mostrarMensajeInformacion("Modo del flash:", tipo);

	}

	public void selectSave() {
		String tipo = "";
		if (save)
			tipo = "Guardar en el dispositivo.";
		else
			tipo = "No guardar en el dispositivo.";
		this.mostrarMensajeInformacion(tipo, "");

	}

	public void deleteAllVideo() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listVideo = this.service.findAllByImei(imei.toString(), Protocol.TYPE_VIDEODTO);
				for (VideoDTO video : listVideo) {
					File file = new File(video.getPathJava());
					if (file.exists() && file.isFile()) {
						if (!file.delete())
							this.mostrarMensajeError("Error al eliminar fichero.", "");
					}
				}
				this.service.deleteAllByImei(imei.toString(), Protocol.TYPE_VIDEODTO);
				Thread.sleep(Protocol.TIME_SLEEP);
			} catch (ServiceException e) {
				this.LOGGER.info("deleteAllVideo -> VideoManagedBean, EXCEPCION: " + e.toString());
			} catch (InterruptedException e) {
				this.LOGGER.info(
						"synchronizeLogBrowser -> BrowserManagedBean, EXCEPCION: Sleep durante la sincronizacion.");
			}
		}
		this.mostrarMensajeAlerta("Eliminando...", "");
	}

	public boolean isRecord() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				List<DeviceDTO> listDevice = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO);
				if (listDevice != null)
					this.record = listDevice.get(0).isVideoRecord();
				else
					this.record = false;

			} catch (ServiceException e) {
				this.LOGGER.info("isRecord -> VideoManagedBean, EXCEPCION: " + e.toString());
			}
		}

		return record;
	}

	public void setRecord(boolean record) {
		this.record = record;
	}

	public int getFlash() {
		return flash;
	}

	public void setFlash(int flash) {
		this.flash = flash;
	}

	public int getCamera() {
		return camera;
	}

	public void setCamera(int camera) {
		this.camera = camera;
	}

	public boolean isSave() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
	}

}
