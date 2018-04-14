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
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.DeviceDTO;
import es.unex.giiis.tfg.model.ImageDTO;
import es.unex.giiis.tfg.model.ImageDTO.TypeImage;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.DeviceService;
import es.unex.giiis.tfg.service.ImageService;

@Controller
@ManagedBean
@SessionScoped
public class ImageManagedBean extends BaseManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("ImageManagedBean");

	@Autowired
	private ImageService service;

	@Autowired
	private DeviceService deviceService;

	// Imagenes realizada con la camara del dispositivo
	private List<ImageDTO> listImageCamera;

	// Imagenes de la galeria del dispositivo
	private List<ImageDTO> listImageGallery;

	// Camara del dispositivo
	private int camera;

	// Tipo del flash de la camara del dispositivo
	private int flash;

	// Guardar en el dispositivo
	private boolean save;

	// Imagenes a sincronizar
	private int numImage;

	// Imagenes sincronizadas con anterioridad
	private int contLastImages;

	// Imagen seleccionada para visualizarla en detalle.
	private ImageDTO imageSelected;

	// Posicion en el mapa de la imagen seleccionada para ver detalle
	private MapModel markerGmap;

	// Mapa centrado con respecto a la coordenada de la imagen
	private LatLng centerGmap;

	@PostConstruct
	public void init() {
		// Incializar la camara a la principal
		this.camera = 0;
		// Incializar el flash a apagado
		this.flash = 0;
		// Incializar para que no se guarde en el dispositivo
		this.save = false;
		// Incializar para sincronizar el minimo de imagenes
		this.numImage = 5;
		// http://anavallasuiza.com/share/presentations/html5e/video.html

	}

	// Pasar al servicio
	public void takePhoto() {
		this.LOGGER.info("Realizando fotografia...");
		Object ip = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IP);
		if (ip != null) {
			try {
				switch (this.flash) {
				case 0:
					switch (this.camera) {
					case 0:
						if (save)
							this.service.sendCmd(Protocol.TAKE_PHOTO_PRIMARY_FLASH_MODE_OFF_SAVE, ip.toString());
						else
							this.service.sendCmd(Protocol.TAKE_PHOTO_PRIMARY_FLASH_MODE_OFF, ip.toString());
						break;
					case 1:
						if (save)
							this.service.sendCmd(Protocol.TAKE_PHOTO_SECUNDARY_FLASH_MODE_OFF_SAVE, ip.toString());
						else
							this.service.sendCmd(Protocol.TAKE_PHOTO_SECUNDARY_FLASH_MODE_OFF, ip.toString());
						break;
					}
					break;
				case 1:
					if (save)
						this.service.sendCmd(Protocol.TAKE_PHOTO_PRIMARY_FLASH_MODE_ON_SAVE, ip.toString());
					else
						this.service.sendCmd(Protocol.TAKE_PHOTO_PRIMARY_FLASH_MODE_ON, ip.toString());
					break;
				case 2:
					if (save)
						this.service.sendCmd(Protocol.TAKE_PHOTO_PRIMARY_FLASH_MODE_AUTO_SAVE, ip.toString());
					else
						this.service.sendCmd(Protocol.TAKE_PHOTO_PRIMARY_FLASH_MODE_AUTO, ip.toString());
					break;
				case 3:
					if (save)
						this.service.sendCmd(Protocol.TAKE_PHOTO_PRIMARY_FLASH_MODE_RED_EYE_SAVE, ip.toString());
					else
						this.service.sendCmd(Protocol.TAKE_PHOTO_PRIMARY_FLASH_MODE_RED_EYE, ip.toString());
					break;
				case 4:
					if (save)
						this.service.sendCmd(Protocol.TAKE_PHOTO_PRIMARY_FLASH_MODE_TORCH_SAVE, ip.toString());
					else
						this.service.sendCmd(Protocol.TAKE_PHOTO_PRIMARY_FLASH_MODE_TORCH, ip.toString());
					break;
				default:
					this.mostrarMensajeError("No se puede realizar la fotografia", null);
					break;
				}

				Thread.sleep(Protocol.TIME_SLEEP+3000);

			} catch (ServiceException e) {
				this.LOGGER.info("takePhoto -> ImageManagedBean, EXCEPCION: " + e.toString());
			} catch (InterruptedException e) {
				this.LOGGER.info("takePhoto -> ImageManagedBean, EXCEPCION: Sleep durante la sincronizacion...");
			}

		}

		this.mostrarMensajeInformacion("Realizando...", "");
		this.mostrarMensajeAlerta("Guardando imagen...", "");

	}

	public void selectCamera() {
		String tipo = "";
		switch (this.camera) {
		case 0:
			tipo = "Principal";
			break;
		case 1:
			// Apagamos el flash
			this.flash = 0;
			tipo = "Secundaria";
			break;
		default:
			tipo = "No disponible";
			break;
		}
		this.mostrarMensajeInformacion("Tipo camara:", tipo);
	}

	public void refreshTable() {
		this.mostrarMensajeInformacion("Actualizando imagenes.", "");
	}

	public void selectSave() {
		String tipo = "";
		if (save)
			tipo = "Guardar.";
		else
			tipo = "No guardar.";
		this.mostrarMensajeInformacion(tipo, "");
	}

	public void selectFlash() {
		String tipo = "";
		switch (this.flash) {
		case 0:
			tipo = "Apagado";
			break;
		case 1:
			tipo = "Encendido";
			break;
		case 2:
			tipo = "Automático";
			break;
		case 3:
			tipo = "Reducción de ojos rojos";
			break;
		case 4:
			tipo = "Emisión constante de luz";
			break;
		default:
			tipo = "No disponible";
			break;
		}
		this.mostrarMensajeInformacion("Modo flash:", tipo);

	}

	public List<ImageDTO> getListImageCamera() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listImageCamera = this.service.findAllImageByImeiAndTypeImage(imei.toString(), TypeImage.CAMERA);
			} catch (ServiceException e) {
				this.LOGGER.info("getListImageCamera -> ImageManagedBean, EXCEPCION: " + e.toString());
			}
		}
		return this.listImageCamera;
	}

	public List<ImageDTO> getListImageGallery() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listImageGallery = this.service.findAllImageByImeiAndTypeImage(imei.toString(), TypeImage.GALLERY);
			} catch (ServiceException e) {
				this.LOGGER.info("getListImageGallery -> ImageManagedBean, EXCEPCION: " + e.toString());
			}
		}
		return this.listImageGallery;
	}

	public void syncronizeGallery() {
		Object ip = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IP);
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);

		if (ip != null && imei != null) {
			try {
				int aux = getContLastImages();
				switch (this.numImage) {
				case 5:
					this.service.sendCmd(Protocol.READ_IMAGE_GALLERY_5, ip.toString());
					this.contLastImages += 5;
					break;
				case 10:
					this.service.sendCmd(Protocol.READ_IMAGE_GALLERY_10, ip.toString());
					this.contLastImages += 10;
					break;
				case 15:
					this.service.sendCmd(Protocol.READ_IMAGE_GALLERY_15, ip.toString());
					this.contLastImages += 15;
					break;
				default:
					this.service.sendCmd(Protocol.READ_IMAGE_GALLERY_5, ip.toString());
					this.contLastImages += 5;
					break;
				}

				// Actualizar el contador de las imagenes recuperadas en BD
				DeviceDTO device = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO).get(0);
				device.setContLatImage(this.contLastImages);
				this.deviceService.delete(device);
				this.deviceService.add(device, Protocol.TYPE_DEVICEDTO);

				// Envio del numero de imagenes ya sincronizadas de la galeria
				DatagramSocket ds = new DatagramSocket();
				byte[] data = Integer.toString(aux).getBytes();
				DatagramPacket dp1 = new DatagramPacket(data, data.length, InetAddress.getByName(ip.toString()),
						Protocol.PUERTO);
				ds.send(dp1);
				ds.close();
				this.mostrarMensajeInformacion("Sincronizando...", this.numImage + " imágenes.");
				this.mostrarMensajeAlerta("Guardando...", "");

				Thread.sleep(Protocol.TIME_SLEEP);

			} catch (ServiceException e) {
				this.LOGGER.info("syncronizeGallery -> ImageManagedBean, EXCEPCION: " + e.toString());
			} catch (SocketException | UnknownHostException e) {
				this.LOGGER.info("syncronizeGallery -> ImageManagedBean, EXCEPCION: Crear paquete...");
			} catch (InterruptedException e) {
				this.LOGGER
						.info("syncronizeGallery -> ImageManagedBean, EXCEPCION: Sleep durante la sincronizacion...");
			} catch (IOException e) {
				this.LOGGER.info("syncronizeGallery -> ImageManagedBean, EXCEPCION: Enviar paquete...");
			}
		} else
			this.mostrarMensajeError("Error: ", "Dispositivo seleccionado.");

	}

	public void selectNumImageGallery() {
		this.mostrarMensajeInformacion("Sincronizar: ", this.numImage + " imágenes.");

	}

	public void resetContLastImages() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				DeviceDTO device = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO).get(0);
				device.setContLatImage(0);
				this.deviceService.add(device, Protocol.TYPE_DEVICEDTO);
			} catch (ServiceException e) {
				this.LOGGER.info("resetContLastImages -> ImageManagedBean, EXCEPCION: " + e.toString());
			}
			this.mostrarMensajeInformacion("Número reseteado.", "");
		} else
			this.mostrarMensajeError("ERROR:", "Dispositivo no seleccionado.");
	}

	public int getContLastImages() {
		// Inicializar el contador de las imagenes recuperadas, a las ultimas
		// imagenes ya recuperadas con anterioridad (guardado en BD)
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				DeviceDTO device = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO).get(0);
				this.contLastImages = device.getContLatImage();
			} catch (ServiceException e) {
				this.LOGGER.info("syncronizeGallery -> ImageManagedBean, EXCEPCION: " + e.toString());
			}
		} else
			this.contLastImages = 0;
		return this.contLastImages;
	}

	public void setContLastImages(int contLastImages) {
		this.contLastImages = contLastImages;
	}

	public StreamedContent downloadImage(ImageDTO image) {
		this.LOGGER.info("Descargando imagen...");
		StreamedContent fileDownload = null;
		try {
			File file = new File(image.getPathJava());
			InputStream stream = new FileInputStream(file);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			fileDownload = new DefaultStreamedContent(stream, externalContext.getMimeType(file.getName()),
					file.getName());
		} catch (FileNotFoundException e) {
			this.LOGGER.info("Error al descar imagen...");
		}
		return fileDownload;

	}

	public MapModel getMarkerGmap() {
		this.markerGmap = new DefaultMapModel();
		if (this.imageSelected != null && this.imageSelected.getLatitude() != 0
				&& this.imageSelected.getLongitude() != 0) {
			LatLng latLng = new LatLng(this.imageSelected.getLatitude(), this.imageSelected.getLongitude());
			// Circulo de la coordenada
			Circle circle = new Circle(latLng, 10);
			circle.setStrokeColor("#BFFF00");
			circle.setFillColor("#BFFF00");
			circle.setFillOpacity(0.5);

			this.markerGmap.addOverlay(circle);
			this.markerGmap.addOverlay(new Marker(latLng, "GPS", "karaalioglu.png",
					"http://maps.google.com/mapfiles/ms/micons/yellow-dot.png"));
		}
		return this.markerGmap;
	}

	public LatLng getCenterGmap() {
		if (this.imageSelected != null && this.imageSelected.getLatitude() != 0
				&& this.imageSelected.getLongitude() != 0)
			this.centerGmap = new LatLng(this.imageSelected.getLatitude(), this.imageSelected.getLongitude());

		else
			this.centerGmap = new LatLng(39.4833, -6.3667);

		return this.centerGmap;
	}

	public void deleteAllImageGallery() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listImageGallery = this.service.findAllImageByImeiAndTypeImage(imei.toString(), TypeImage.GALLERY);

				for (ImageDTO image : this.listImageGallery) {
					File file = new File(image.getPathJava());
					if (file.exists() && file.isFile()) {
						if (!file.delete())
							this.mostrarMensajeError("Error al eliminar imagen.", "");
					}
				}

				this.service.deleteAllImageByImeiAndTypeImage(imei.toString(), TypeImage.GALLERY);
				Thread.sleep(Protocol.TIME_SLEEP);

			} catch (ServiceException e) {
				this.LOGGER.info("deleteAllAudio -> AundioManagedBean, EXCEPCION: " + e.toString());
			} catch (InterruptedException e) {
			}
		}
		this.mostrarMensajeAlerta("Eliminando.", "");
	}

	public void deleteAllImageCamera() {

		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				this.listImageCamera = this.service.findAllImageByImeiAndTypeImage(imei.toString(), TypeImage.CAMERA);

				for (ImageDTO image : this.listImageCamera) {
					File file = new File(image.getPathJava());
					if (file.exists() && file.isFile()) {
						if (!file.delete())
							this.mostrarMensajeError("Error al eliminar imagen.", "");
					}
				}

				this.service.deleteAllImageByImeiAndTypeImage(imei.toString(), TypeImage.CAMERA);
				Thread.sleep(Protocol.TIME_SLEEP);

			} catch (ServiceException e) {
				this.LOGGER.info("deleteAllAudio -> AundioManagedBean, EXCEPCION: " + e.toString());
			} catch (InterruptedException e) {
			}
		}
		this.mostrarMensajeAlerta("Eliminando.", "");
	}

	public void setCenterGmap(LatLng centerGmap) {
		this.centerGmap = centerGmap;
	}

	public void setMarkerGmap(MapModel markerGmap) {
		this.markerGmap = markerGmap;
	}

	public void setListImageGallery(List<ImageDTO> listImageGallery) {
		this.listImageGallery = listImageGallery;
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

	public int getNumImage() {
		return numImage;
	}

	public void setNumImage(int numImage) {
		this.numImage = numImage;
	}

	public ImageDTO getImageSelected() {
		return imageSelected;
	}

	public void setImageSelected(ImageDTO imageSelected) {
		this.imageSelected = imageSelected;
	}

	public void setListImageCamera(List<ImageDTO> listImageCamera) {
		this.listImageCamera = listImageCamera;
	}

}
