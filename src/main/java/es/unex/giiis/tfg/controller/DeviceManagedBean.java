package es.unex.giiis.tfg.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.DeviceDTO;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.DeviceService;

@Controller
@ManagedBean
@SessionScoped
public class DeviceManagedBean extends BaseManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("DeviceManagedBean");

	@Autowired
	private DeviceService service;

	private List<DeviceDTO> listDevice;

	// Dispositivo para monitorizar
	private DeviceDTO deviceSelected;

	// Dispositivo para visualizar los detalles (seleccionado)
	private DeviceDTO deviceView;

	// Parametro de la url
	private String imei;

	private LatLng centerGmap;

	private MapModel markerGmap;

	@PostConstruct
	public void init() {
		this.deviceView = new DeviceDTO();
	}

	public void selected() {
		mostrarMensajeInformacion("Dispositivo:", deviceSelected.getImei());
		// Indicamos en sesion el dispositivo seleccionado.
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(Protocol.IMEI,
				this.deviceSelected.getImei());
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(Protocol.IP,
				this.deviceSelected.getIp());

	}

	public void update() {

		try {
			this.service.updateConnection();
			Thread.sleep(Protocol.TIME_SLEEP);
			this.mostrarMensajeInformacion("Dispositivos sincronizados.", "");
		} catch (ServiceException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public boolean disabledMap() {
		if (Double.compare(this.deviceView.getLatitudeGps(), 0.0) != 0
				&& Double.compare(this.deviceView.getLongitudeGps(), 0.0) != 0)
			return false;
		if (Double.compare(this.deviceView.getLatitudeNetwork(), 0.0) != 0
				&& Double.compare(this.deviceView.getLongitudeNetwork(), 0.0) != 0)
			return false;

		if (Double.compare(this.deviceView.getLatitudePassive(), 0.0) != 0
				&& Double.compare(this.deviceView.getLongitudePassive(), 0.0) != 0)
			return false;

		return true;

	}

	public DeviceDTO getDeviceView() {
		// Se busca el dispositivo por el parametro de la url.
		try {
			List<DeviceDTO> listDevice = this.service.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO);
			for (DeviceDTO d : listDevice) {
				if (d.getImei().equals(imei)) {
					this.deviceView = d;
				}

			}

		} catch (ServiceException e) {
			this.mostrarMensajeAlerta("ERRO", "dd");
		}
		return this.deviceView;
	}

	public MapModel getMarkerGmap() {
		Circle circle = null;
		LatLng coordenada = null;
		this.markerGmap = new DefaultMapModel();

		double latitudeGps = this.deviceView.getLatitudeGps();
		double longitudeGps = this.deviceView.getLongitudeGps();
		double latitudeNetwork = this.deviceView.getLatitudeNetwork();
		double longitudeNetwork = this.deviceView.getLongitudeNetwork();
		double latitudePassive = this.deviceView.getLatitudePassive();
		double longitudePassive = this.deviceView.getLongitudePassive();

		// Ultima coordenada del Gps
		if (Double.compare(latitudeGps, 0.0) != 0 && Double.compare(longitudeGps, 0.0) != 0) {

			coordenada = new LatLng(latitudeGps, longitudeGps);

			// Circulo de la coordenada
			circle = new Circle(coordenada, 10);
			circle.setStrokeColor("#BFFF00");
			circle.setFillColor("#BFFF00");
			circle.setFillOpacity(0.5);

			this.markerGmap.addOverlay(circle);
			this.markerGmap.addOverlay(new Marker(coordenada, "GPS", "karaalioglu.png",
					"http://maps.google.com/mapfiles/ms/micons/yellow-dot.png"));
		}

		// Ultima coordenada de Wifi y Datos
		if (Double.compare(latitudeNetwork, 0.0) != 0 && Double.compare(longitudeNetwork, 0.0) != 0) {
			// Mover la coordenada porque si coincide solo se veria un puntero
			if (Double.compare(latitudeNetwork, latitudeGps) == 0
					&& Double.compare(longitudeNetwork, longitudeGps) == 0) {
				coordenada = new LatLng((latitudeNetwork + 0.00001), (longitudeNetwork + 0.00001));
			} else if (Double.compare(latitudeNetwork, latitudePassive) == 0
					&& Double.compare(longitudeNetwork, longitudePassive) == 0)
				coordenada = new LatLng((latitudeNetwork + 0.00001), (longitudeNetwork + 0.00001));
			else
				coordenada = new LatLng(latitudeNetwork, longitudeNetwork);

			circle = new Circle(coordenada, 10);

			// Circulo de la coordenada
			circle.setStrokeColor("#00FFFF");
			circle.setFillColor("#00FFFF");
			circle.setFillOpacity(0.5);

			this.markerGmap.addOverlay(circle);
			this.markerGmap.addOverlay(new Marker(coordenada, "Wifi y datos", "konyaalti.png",
					"http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
		}

		// Ultima coordenada de Pasivo
		if (Double.compare(latitudePassive, 0.0) != 0 && Double.compare(longitudePassive, 0.0) != 0) {
			// Mover la coordenada porque si coincide solo se veria un puntero
			if (Double.compare(latitudePassive, latitudeGps) == 0
					&& Double.compare(longitudePassive, longitudeGps) == 0) {
				coordenada = new LatLng((latitudePassive + 0.00001), (longitudePassive + 0.00001));
			} else
				coordenada = new LatLng(latitudePassive, longitudePassive);

			circle = new Circle(coordenada, 10);

			// Circulo de la coordenada

			circle.setStrokeColor("#FF0000");
			circle.setFillColor("#FF0000");
			circle.setFillOpacity(0.5);

			this.markerGmap.addOverlay(circle);
			this.markerGmap.addOverlay(new Marker(coordenada, "Aplicaciones", "kaleici.png",
					"http://maps.google.com/mapfiles/ms/micons/pink-dot.png"));
		}
		return this.markerGmap;
	}

	// Obtener el punto medio del mapa
	public LatLng getCenterGmap() {
		double latitude = 0.0;
		double longitude = 0.0;
		int contPoint = 0;

		if (Double.compare(this.deviceView.getLatitudeGps(), 0.0) != 0
				&& Double.compare(this.deviceView.getLongitudeGps(), 0.0) != 0) {
			latitude += this.deviceView.getLatitudeGps();
			longitude += this.deviceView.getLongitudeGps();
			contPoint++;
		}
		if (Double.compare(this.deviceView.getLatitudeNetwork(), 0.0) != 0
				&& Double.compare(this.deviceView.getLongitudeNetwork(), 0.0) != 0) {
			latitude += this.deviceView.getLatitudeNetwork();
			longitude += this.deviceView.getLongitudeNetwork();
			contPoint++;
		}
		if (Double.compare(this.deviceView.getLatitudePassive(), 0.0) != 0
				&& Double.compare(this.deviceView.getLongitudePassive(), 0.0) != 0) {
			latitude += this.deviceView.getLatitudePassive();
			longitude += this.deviceView.getLongitudePassive();
			contPoint++;
		}

		if (contPoint > 0) {
			latitude = latitude / contPoint;
			longitude = longitude / contPoint;
		}
		this.centerGmap = new LatLng(latitude, longitude);

		return this.centerGmap;
	}

	public void setCenterGmap(LatLng centerGmap) {
		this.centerGmap = centerGmap;
	}

	public DeviceDTO getDevice(String paramImei) {

		try {
			this.listDevice = this.service.findAll(Protocol.TYPE_DEVICEDTO);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		for (DeviceDTO d : listDevice) {
			if (d.getImei().equals(paramImei))
				return d;
		}
		return null;

	}

	public void detail() {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("resizable", false);
		RequestContext.getCurrentInstance().openDialog("DetailDevice", options, null);
	}

	public void onRowSelect(SelectEvent event) {
		FacesMessage msg = new FacesMessage("Seleccionado", ((DeviceDTO) event.getObject()).getImei());
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	public List<DeviceDTO> getListDevice() {
		try {
			this.listDevice = this.service.findAll(Protocol.TYPE_DEVICEDTO);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return listDevice;
	}

	public void deleteAllDevice() {
		try {
			this.service.deleteAll(Protocol.TYPE_DEVICEDTO);
			this.mostrarMensajeAlerta("Eliminados.", "");
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public void setListDevice(List<DeviceDTO> listDevice) {
		this.listDevice = listDevice;
	}

	public DeviceDTO getDeviceSelected() {
		return deviceSelected;
	}

	public void setDeviceSelected(DeviceDTO deviceSelected) {
		this.deviceSelected = deviceSelected;
	}

	public void setDeviceView(DeviceDTO deviceView) {
		this.deviceView = deviceView;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

}
