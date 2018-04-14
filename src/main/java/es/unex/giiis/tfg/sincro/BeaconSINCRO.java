package es.unex.giiis.tfg.sincro;

import java.util.Date;

import es.unex.giiis.tfg.model.BeaconDTO;
import es.unex.giiis.tfg.model.BeaconDTO.TypeBeacon;

public class BeaconSINCRO extends BaseSINCRO {
	public static enum TypeBeaconSINCRO {
		GPS, NETWORK, PASSIVE, ERROR;
	};

	private String imei;

	private Date date;

	private double longitude;

	private double latitude;

	private TypeBeaconSINCRO typeBeaconSincro;

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public TypeBeaconSINCRO getTypeBeaconSincro() {
		return typeBeaconSincro;
	}

	public void setTypeBeaconSincro(TypeBeaconSINCRO typeBeaconSincro) {
		this.typeBeaconSincro = typeBeaconSincro;
	}

	public static BeaconDTO parseSincroBeacon(BeaconSINCRO beaconSincro) {
		BeaconDTO beacon = new BeaconDTO();
		beacon.setDateSynchronize(beaconSincro.getDate());
		beacon.setImei(beaconSincro.getImei());
		beacon.setLatitude(beaconSincro.getLatitude());
		beacon.setLongitude(beaconSincro.getLongitude());
		beacon.setTypeBeacon(parseTypeBeaconSincro(beaconSincro.getTypeBeaconSincro()));
		return beacon;
	}

	public static TypeBeacon parseTypeBeaconSincro(TypeBeaconSINCRO typeBeaconSincro) {
		switch (typeBeaconSincro) {
		case GPS:
			return TypeBeacon.GPS;
		case NETWORK:
			return TypeBeacon.NETWORK;
		case PASSIVE:
			return TypeBeacon.PASSIVE;
		default:
			return TypeBeacon.ERROR;
		}
	}

}
