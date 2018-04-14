package es.unex.giiis.tfg.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.BeaconDTO;
import es.unex.giiis.tfg.model.DeviceDTO;
import es.unex.giiis.tfg.model.BeaconDTO.TypeBeacon;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.BeaconService;
import es.unex.giiis.tfg.service.DeviceService;

@Controller
@ManagedBean
@SessionScoped
public class BeaconManagedBean extends BaseManagedBean implements Serializable {
	final Logger LOGGER = Logger.getLogger("BeaconManagedBean");

	private static final long serialVersionUID = 1L;

	@Autowired
	BeaconService service;

	@Autowired
	DeviceService deviceService;

	// Marcas en el mapa del gps
	private MapModel markerGmapGps;

	// Marcas en el mapa del gps
	private MapModel markerGmapNetwork;

	// Marcas en el mapa del gps
	private MapModel markerGmapPassive;

	// Arrancado o parado el balizamiento
	private Boolean startBeaconGps;

	// Arrancado o parado el balizamiento
	private Boolean startBeaconNetwork;

	// Arrancado o parado el balizamiento
	private Boolean startBeaconPassive;

	// Precision con la que se actualizan las coordenadas del balizamiento
	private int precision;

	// Mapa centrado con respecto a esta coordenada
	private LatLng centerGmapGps;

	// Mapa centrado con respecto a esta coordenada
	private LatLng centerGmapNetwork;

	// Mapa centrado con respecto a esta coordenada
	private LatLng centerGmapPassive;

	private Date dateView;

	// Fecha de hoy
	private Date dateToday;

	@PostConstruct
	public void init() {
		this.precision = 30;
		this.dateView = new GregorianCalendar().getTime();
		this.dateToday = new GregorianCalendar().getTime();
		this.startBeaconGps = false;
		this.startBeaconNetwork = false;
		this.startBeaconPassive = false;
		this.enabledGps = false;
	}

	public void setCenterGmapGps(LatLng centerGmapGps) {
		this.centerGmapGps = centerGmapGps;
	}

	public Date getDateToday() {
		return dateToday;
	}

	public void setDateToday(Date dateToday) {
		this.dateToday = dateToday;
	}

	public void onDateSelect(SelectEvent event) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		this.mostrarMensajeInformacion("Fecha seleccionada:", format.format(event.getObject()));
	}

	public void onPrecisionSelect() {
		if (this.precision == 30)
			this.mostrarMensajeInformacion("Precisión:", this.precision + " segundos.");
		else
			this.mostrarMensajeInformacion("Precisión:", this.precision + " minutos.");
	}

	public MapModel getMarkerGmapGps() {
		this.markerGmapGps = new DefaultMapModel();
		Circle circle = null;
		LatLng latLng = null;
		Polyline polyline = new Polyline();
		polyline.setStrokeWeight(5);
		polyline.setStrokeColor("#BFFF00");
		polyline.setStrokeOpacity(0.7);

		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {

				List<BeaconDTO> listBeacon = this.service.findAllBeaconByImeiAndTypeBeacon(imei.toString(),
						TypeBeacon.GPS);
				for (BeaconDTO beacon : listBeacon) {

					SimpleDateFormat dateFormatMes = new SimpleDateFormat("MM");
					SimpleDateFormat dateFormatDia = new SimpleDateFormat("dd");

					if (Integer.parseInt(dateFormatDia.format(dateView)) == Integer
							.parseInt(dateFormatDia.format(beacon.getDateSynchronize()))
							&& Integer.parseInt(dateFormatMes.format(dateView)) == Integer
									.parseInt(dateFormatMes.format(beacon.getDateSynchronize()))) {
						latLng = new LatLng(beacon.getLatitude(), beacon.getLongitude());
						this.markerGmapGps.addOverlay(new Marker(latLng, "GPS", "karaalioglu.png",
								"http://maps.google.com/mapfiles/ms/micons/yellow-dot.png"));
						polyline.getPaths().add(latLng);
					}

				}

				List<DeviceDTO> listDevice = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO);
				if (listDevice != null) {
					DeviceDTO device = listDevice.get(0);
					double latitudeGps = device.getLatitudeGps();
					double longitudeGps = device.getLongitudeGps();
					if (Double.compare(latitudeGps, 0.0) != 0 && Double.compare(longitudeGps, 0.0) != 0) {
						latLng = new LatLng(latitudeGps, longitudeGps);
						// Circulo de la coordenada
						circle = new Circle(latLng, 10);
						circle.setStrokeColor("#BFFF00");
						circle.setFillColor("#BFFF00");
						circle.setFillOpacity(0.5);
						this.markerGmapGps.addOverlay(circle);
						this.markerGmapGps.addOverlay(new Marker(latLng, "GPS", "karaalioglu.png",
								"http://maps.google.com/mapfiles/ms/micons/yellow-dot.png"));
						polyline.getPaths().add(latLng);

					}
				}

				this.markerGmapGps.addOverlay(polyline);

			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		return this.markerGmapGps;
	}

	public LatLng getCenterGmapGps() {
		double latitude = 0.0;
		double longitude = 0.0;
		int contPoint = 0;

		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {

				List<DeviceDTO> listDevice = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO);
				if (listDevice != null) {
					DeviceDTO device = listDevice.get(0);

					if (Double.compare(device.getLatitudeGps(), 0.0) != 0
							&& Double.compare(device.getLongitudeGps(), 0.0) != 0) {
						latitude += device.getLatitudeGps();
						longitude += device.getLongitudeGps();
						contPoint++;
					}
				}
				
				
				
				
				List<BeaconDTO> listBeacon = this.service.findAllBeaconByImeiAndTypeBeacon(imei.toString(),
						TypeBeacon.GPS);
				for (BeaconDTO beacon : listBeacon) {

					SimpleDateFormat dateFormatMes = new SimpleDateFormat("MM");
					SimpleDateFormat dateFormatDia = new SimpleDateFormat("dd");

					if (Integer.parseInt(dateFormatDia.format(dateView)) == Integer
							.parseInt(dateFormatDia.format(beacon.getDateSynchronize()))
							&& Integer.parseInt(dateFormatMes.format(dateView)) == Integer
									.parseInt(dateFormatMes.format(beacon.getDateSynchronize()))) {

						latitude += beacon.getLatitude();
						longitude += beacon.getLongitude();
						contPoint++;
					}
				}
				
				
				

			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}

		if (contPoint > 0) {
			latitude = latitude / contPoint;
			longitude = longitude / contPoint;
		} else if (contPoint == 0) {
			latitude = 39.4833;
			longitude = -6.3667;

		}
		this.centerGmapGps = new LatLng(latitude, longitude);

		return this.centerGmapGps;

	}

	public MapModel getMarkerGmapNetwork() {
		this.markerGmapNetwork = new DefaultMapModel();
		Circle circle = null;
		LatLng latLng = null;
		Polyline polyline = new Polyline();
		polyline.setStrokeWeight(5);
		polyline.setStrokeColor("#00FFFF");
		polyline.setStrokeOpacity(0.7);

		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {

				
				List<BeaconDTO> listBeacon = this.service.findAllBeaconByImeiAndTypeBeacon(imei.toString(),
						TypeBeacon.NETWORK);
				for (BeaconDTO beacon : listBeacon) {

					SimpleDateFormat dateFormatMes = new SimpleDateFormat("MM");
					SimpleDateFormat dateFormatDia = new SimpleDateFormat("dd");

					if (Integer.parseInt(dateFormatDia.format(dateView)) == Integer
							.parseInt(dateFormatDia.format(beacon.getDateSynchronize()))
							&& Integer.parseInt(dateFormatMes.format(dateView)) == Integer
									.parseInt(dateFormatMes.format(beacon.getDateSynchronize()))) {
						latLng = new LatLng(beacon.getLatitude(), beacon.getLongitude());
						this.markerGmapNetwork.addOverlay(new Marker(latLng, "Wifi y datos", "konyaalti.png",
								"http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
						polyline.getPaths().add(latLng);
					}

				}
				
				
				List<DeviceDTO> listDevice = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO);
				if (listDevice != null) {
					DeviceDTO device = listDevice.get(0);
					double latitudeNetwork = device.getLatitudeNetwork();
					double longitudeNetwork = device.getLongitudeNetwork();

					if (Double.compare(latitudeNetwork, 0.0) != 0 && Double.compare(longitudeNetwork, 0.0) != 0) {

						latLng = new LatLng(latitudeNetwork, longitudeNetwork);

						// Circulo de la coordenada
						circle = new Circle(latLng, 10);
						circle.setStrokeColor("#00FFFF");
						circle.setFillColor("#00FFFF");
						circle.setFillOpacity(0.5);

						this.markerGmapNetwork.addOverlay(circle);
						this.markerGmapNetwork.addOverlay(new Marker(latLng, "Wifi y datos", "konyaalti.png",
								"http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
						polyline.getPaths().add(latLng);

					}
				}

				
				this.markerGmapNetwork.addOverlay(polyline);

			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		return this.markerGmapNetwork;
	}

	public LatLng getCenterGmapNetwork() {
		double latitude = 0.0;
		double longitude = 0.0;
		int contPoint = 0;

		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {

				List<DeviceDTO> listDevice = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO);
				if (listDevice != null) {
					DeviceDTO device = listDevice.get(0);

					if (Double.compare(device.getLatitudeNetwork(), 0.0) != 0
							&& Double.compare(device.getLongitudeNetwork(), 0.0) != 0) {
						latitude += device.getLatitudeNetwork();
						longitude += device.getLongitudeNetwork();
						contPoint++;
					}
				}
				
				
				List<BeaconDTO> listBeacon = this.service.findAllBeaconByImeiAndTypeBeacon(imei.toString(),
						TypeBeacon.NETWORK);
				for (BeaconDTO beacon : listBeacon) {

					SimpleDateFormat dateFormatMes = new SimpleDateFormat("MM");
					SimpleDateFormat dateFormatDia = new SimpleDateFormat("dd");

					if (Integer.parseInt(dateFormatDia.format(dateView)) == Integer
							.parseInt(dateFormatDia.format(beacon.getDateSynchronize()))
							&& Integer.parseInt(dateFormatMes.format(dateView)) == Integer
									.parseInt(dateFormatMes.format(beacon.getDateSynchronize()))) {

						latitude += beacon.getLatitude();
						longitude += beacon.getLongitude();
						contPoint++;
					}
				}
				
				
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}

		if (contPoint > 0) {
			latitude = latitude / contPoint;
			longitude = longitude / contPoint;
		} else if (contPoint == 0) {
			latitude = 39.4833;
			longitude = -6.3667;

		}
		this.centerGmapNetwork = new LatLng(latitude, longitude);

		return this.centerGmapNetwork;
	}

	public MapModel getMarkerGmapPassive() {
		this.markerGmapPassive = new DefaultMapModel();
		Circle circle = null;
		LatLng latLng = null;
		Polyline polyline = new Polyline();
		polyline.setStrokeWeight(5);
		polyline.setStrokeColor("#FF0000");
		polyline.setStrokeOpacity(0.7);

		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {

				
				
				List<BeaconDTO> listBeacon = this.service.findAllBeaconByImeiAndTypeBeacon(imei.toString(),
						TypeBeacon.PASSIVE);
				for (BeaconDTO beacon : listBeacon) {

					SimpleDateFormat dateFormatMes = new SimpleDateFormat("MM");
					SimpleDateFormat dateFormatDia = new SimpleDateFormat("dd");

					if (Integer.parseInt(dateFormatDia.format(dateView)) == Integer
							.parseInt(dateFormatDia.format(beacon.getDateSynchronize()))
							&& Integer.parseInt(dateFormatMes.format(dateView)) == Integer
									.parseInt(dateFormatMes.format(beacon.getDateSynchronize()))) {
						latLng = new LatLng(beacon.getLatitude(), beacon.getLongitude());
						this.markerGmapPassive.addOverlay(new Marker(latLng, "Aplicaciones", "kaleici.png",
								"http://maps.google.com/mapfiles/ms/micons/pink-dot.png"));
						polyline.getPaths().add(latLng);
					}

				}
				
				
				
				
				List<DeviceDTO> listDevice = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO);
				if (listDevice != null) {
					DeviceDTO device = listDevice.get(0);
					double latitudePassive = device.getLatitudeNetwork();
					double longitudePassive = device.getLongitudeNetwork();

					if (Double.compare(latitudePassive, 0.0) != 0 && Double.compare(longitudePassive, 0.0) != 0) {

						latLng = new LatLng(latitudePassive, longitudePassive);

						// Circulo de la coordenada
						circle = new Circle(latLng, 10);
						circle.setStrokeColor("#FF0000");
						circle.setFillColor("#FF0000");
						circle.setFillOpacity(0.5);

						this.markerGmapPassive.addOverlay(circle);
						this.markerGmapPassive.addOverlay(new Marker(latLng, "Aplicaciones", "kaleici.png",
								"http://maps.google.com/mapfiles/ms/micons/pink-dot.png"));
						polyline.getPaths().add(latLng);

					}
				}
				
				this.markerGmapPassive.addOverlay(polyline);

			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		return this.markerGmapPassive;
	}

	public LatLng getCenterGmapPassive() {
		double latitude = 0.0;
		double longitude = 0.0;
		int contPoint = 0;

		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {

				List<DeviceDTO> listDevice = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO);
				if (listDevice != null) {
					DeviceDTO device = listDevice.get(0);

					if (Double.compare(device.getLatitudePassive(), 0.0) != 0
							&& Double.compare(device.getLongitudePassive(), 0.0) != 0) {
						latitude += device.getLatitudePassive();
						longitude += device.getLongitudePassive();
						contPoint++;
					} 
				} 
				
				
				
				
				
				List<BeaconDTO> listBeacon = this.service.findAllBeaconByImeiAndTypeBeacon(imei.toString(),
						TypeBeacon.PASSIVE);
				for (BeaconDTO beacon : listBeacon) {

					SimpleDateFormat dateFormatMes = new SimpleDateFormat("MM");
					SimpleDateFormat dateFormatDia = new SimpleDateFormat("dd");

					if (Integer.parseInt(dateFormatDia.format(dateView)) == Integer
							.parseInt(dateFormatDia.format(beacon.getDateSynchronize()))
							&& Integer.parseInt(dateFormatMes.format(dateView)) == Integer
									.parseInt(dateFormatMes.format(beacon.getDateSynchronize()))) {

						latitude += beacon.getLatitude();
						longitude += beacon.getLongitude();
						contPoint++;
					}
				}
				
				
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			
			
			
			
			
		}

		if (contPoint > 0) {
			latitude = latitude / contPoint;
			longitude = longitude / contPoint;
		} else if (contPoint == 0) {
			latitude = 39.4833;
			longitude = -6.3667;

		}
		this.centerGmapPassive = new LatLng(latitude, longitude);

		return this.centerGmapPassive;
	}

	public void synchronizeActualPosition() {
		this.LOGGER.info("Sincronizando datos basico del dispositivo");
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				List<DeviceDTO> listDevice = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO);
				this.service.sendCmd(Protocol.SINCRONIZE, listDevice.get(0).getIp());
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			this.mostrarMensajeInformacion("Actualizando...", "");
		}
	}

	public void startBeaconGps() {
		this.LOGGER.info("Comenzando / terminando balizamiento GPS...");
		Object ip = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IP);
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);

		if (ip != null && imei != null) {
			try {
				DeviceDTO device = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO).get(0);

				if (this.startBeaconGps) {
					switch (this.precision) {
					case 30:
						this.service.sendCmd(Protocol.START_BEACON_GPS_30, ip.toString());
						device.setStartBeaconGps(true);
						break;
					case 1:
						this.service.sendCmd(Protocol.START_BEACON_GPS_1, ip.toString());
						device.setStartBeaconGps(true);
						break;
					case 5:
						this.service.sendCmd(Protocol.START_BEACON_GPS_5, ip.toString());
						device.setStartBeaconGps(true);
						break;
					case 10:
						this.service.sendCmd(Protocol.START_BEACON_GPS_10, ip.toString());
						device.setStartBeaconGps(true);
						break;
					default:
						this.service.sendCmd(Protocol.START_BEACON_GPS_30, ip.toString());
						device.setStartBeaconGps(true);
						break;
					}
					this.mostrarMensajeInformacion("Iniciando...", "Balizamiento");
				} else {
					this.service.sendCmd(Protocol.STOP_BEACON_GPS, ip.toString());
					device.setStartBeaconGps(false);
					this.mostrarMensajeInformacion("Finalizado...", "Balizamiento");

				}
				this.deviceService.delete(device);
				device.setLastSynchronization(Calendar.getInstance().getTime());
				this.deviceService.add(device, Protocol.TYPE_DEVICEDTO);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}

	}

	public void startBeaconNetwork() {
		this.LOGGER.info("Comenzando / terminando balizamiento GPS...");
		Object ip = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IP);
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);

		if (ip != null && imei != null) {
			try {
				DeviceDTO device = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO).get(0);

				if (this.startBeaconGps) {
					switch (this.precision) {
					case 30:
						this.service.sendCmd(Protocol.START_BEACON_NETWORK_30, ip.toString());
						device.setStartBeaconNetwork(true);
						break;
					case 1:
						this.service.sendCmd(Protocol.START_BEACON_NETWORK_1, ip.toString());
						device.setStartBeaconNetwork(true);
						break;
					case 5:
						this.service.sendCmd(Protocol.START_BEACON_NETWORK_5, ip.toString());
						device.setStartBeaconNetwork(true);
						break;
					case 10:
						this.service.sendCmd(Protocol.START_BEACON_NETWORK_10, ip.toString());
						device.setStartBeaconNetwork(true);
						break;
					default:
						this.service.sendCmd(Protocol.START_BEACON_NETWORK_30, ip.toString());
						device.setStartBeaconNetwork(true);
						break;
					}
					this.mostrarMensajeInformacion("Iniciando...", "");
				} else {
					this.service.sendCmd(Protocol.STOP_BEACON_NETWORK, ip.toString());
					device.setStartBeaconNetwork(false);
					this.mostrarMensajeInformacion("Finalizado...", "Balizamiento");

				}
				this.deviceService.delete(device);
				device.setLastSynchronization(Calendar.getInstance().getTime());
				this.deviceService.add(device, Protocol.TYPE_DEVICEDTO);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
	}

	public void startBeaconPassive() {
		this.LOGGER.info("Comenzando / terminando balizamiento GPS...");
		Object ip = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IP);
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);

		if (ip != null && imei != null) {
			try {
				DeviceDTO device = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO).get(0);

				if (this.startBeaconGps) {
					switch (this.precision) {
					case 30:
						this.service.sendCmd(Protocol.START_BEACON_PASSIVE_30, ip.toString());
						device.setStartBeaconPassive(true);
						break;
					case 1:
						this.service.sendCmd(Protocol.START_BEACON_PASSIVE_1, ip.toString());
						device.setStartBeaconPassive(true);
						break;
					case 5:
						this.service.sendCmd(Protocol.START_BEACON_PASSIVE_5, ip.toString());
						device.setStartBeaconPassive(true);
						break;
					case 10:
						this.service.sendCmd(Protocol.START_BEACON_PASSIVE_10, ip.toString());
						device.setStartBeaconPassive(true);
						break;
					default:
						this.service.sendCmd(Protocol.START_BEACON_PASSIVE_30, ip.toString());
						device.setStartBeaconPassive(true);
						break;
					}
					this.mostrarMensajeInformacion("Iniciando...", "");
				} else {
					this.service.sendCmd(Protocol.STOP_BEACON_PASSIVE, ip.toString());
					device.setStartBeaconPassive(false);
					this.mostrarMensajeInformacion("Finalizado...", "Balizamiento");

				}
				this.deviceService.delete(device);
				device.setLastSynchronization(Calendar.getInstance().getTime());
				this.deviceService.add(device, Protocol.TYPE_DEVICEDTO);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
	}

	public Boolean getStartBeaconNetwork() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				List<DeviceDTO> listDevice = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO);
				if (listDevice != null)
					this.startBeaconNetwork = listDevice.get(0).isStartBeaconNetwork();
				else
					this.startBeaconNetwork = false;
			} catch (ServiceException e) {
				this.LOGGER.info("getStartBeaconNetwork -> BeaconManagedBean, EXCEPCION: " + e.toString());
			}
		}
		return this.startBeaconNetwork;

	}

	public Boolean getStartBeaconPassive() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				List<DeviceDTO> listDevice = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO);
				if (listDevice != null)
					this.startBeaconPassive = listDevice.get(0).isStartBeaconPassive();
				else
					this.startBeaconPassive = false;
			} catch (ServiceException e) {
				this.LOGGER.info("getStartBeaconPassive -> BeaconManagedBean, EXCEPCION: " + e.toString());
			}
		}
		return this.startBeaconPassive;
	}

	public Boolean getStartBeaconGps() {
		Object imei = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IMEI);
		if (imei != null) {
			try {
				List<DeviceDTO> listDevice = this.deviceService.findAllByImei(imei.toString(), Protocol.TYPE_DEVICEDTO);
				if (listDevice != null)
					this.startBeaconGps = listDevice.get(0).isStartBeaconGps();
				else
					this.startBeaconGps = false;
			} catch (ServiceException e) {
				this.LOGGER.info("getStartBeaconGps -> BeaconManagedBean, EXCEPCION: " + e.toString());
			}
		}
		return this.startBeaconGps;
	}

	public void enabledGps() {
		Object ip = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Protocol.IP);
		if (ip != null) {
			this.mostrarMensajeInformacion("Activando GPS...", "");
			try {
				this.service.sendCmd(Protocol.ENABLED_GPS, ip.toString());
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private boolean enabledGps;

	public boolean isEnabledGps() {
		return enabledGps;
	}

	public void setEnabledGps(boolean enabledGps) {
		this.enabledGps = enabledGps;
	}

	public void setStartBeaconNetwork(Boolean startBeaconNetwork) {
		this.startBeaconNetwork = startBeaconNetwork;
	}

	public void setStartBeaconPassive(Boolean startBeaconPassive) {
		this.startBeaconPassive = startBeaconPassive;
	}

	public void setStartBeaconGps(Boolean startBeaconGps) {
		this.startBeaconGps = startBeaconGps;
	}

	public void setMarkerGmapNetwork(MapModel markerGmapNetwork) {
		this.markerGmapNetwork = markerGmapNetwork;
	}

	public void setMarkerGmapPassive(MapModel markerGmapPassive) {
		this.markerGmapPassive = markerGmapPassive;
	}

	public void setCenterGmapNetwork(LatLng centerGmapNetwork) {
		this.centerGmapNetwork = centerGmapNetwork;
	}

	public void setCenterGmapPassive(LatLng centerGmapPassive) {
		this.centerGmapPassive = centerGmapPassive;
	}

	public void setMarkerGmapGps(MapModel markerGmapGps) {
		this.markerGmapGps = markerGmapGps;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public Date getDateView() {
		return dateView;
	}

	public void setDateView(Date dateView) {
		this.dateView = dateView;
	}

}
