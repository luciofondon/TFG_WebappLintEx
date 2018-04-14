package es.unex.giiis.tfg.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DEVICE")
public class DeviceDTO extends BaseDTO {
	@Id
	@Column(name = "IMEI")
	private String imei;

	@Column(name = "IP")
	private String ip;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "AUDIO_RECORD")
	private boolean audioRecord;

	@Column(name = "BATTERY")
	private Integer battery;

	@Column(name = "BLUETOOTH_STATE")
	private String bluetoothState;

	@Column(name = "CALL_STATE")
	private String callState;

	@Column(name = "CONNECTION_INTERNET")
	private String connectionInternet;

	@Column(name = "CONT_LAST_IMAGE")
	private int contLatImage;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "DEVICE_STATE")
	private boolean deviceState;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "GPS_STATE")
	private String gpsState;

	@Column(name = "LAST_CONNECTION")
	private Date lastConnection;

	@Column(name = "LAST_SYNCHRONIZATION")
	private Date lastSynchronization;

	@Column(name = "LOCALITY")
	private String locality;

	@Column(name = "LATITUDE_GPS")
	private double latitudeGps;

	@Column(name = "LONGITUDE_GPS")
	private double longitudeGps;

	@Column(name = "LATITUDE_NETWORK")
	private double latitudeNetwork;

	@Column(name = "LONGITUDE_NETWORK")
	private double longitudeNetwork;

	@Column(name = "LATITUDE_PASSIVE")
	private double latitudePassive;

	@Column(name = "LONGITUDE_PASSIVE")
	private double longitudePassive;

	@Column(name = "MOBILE_STATE")
	private String mobileState;

	@Column(name = "MODEL")
	private String model;

	@Column(name = "NAME_COMPANY")
	private String nameCompany;

	@Column(name = "NFC_STATE")
	private String nfcState;

	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;

	@Column(name = "POWER_STATE")
	private String powerState;

	@Column(name = "START_BEACON_GPS")
	private boolean startBeaconGps;

	@Column(name = "START_BEACON_NETWORK")
	private boolean startBeaconNetwork;

	@Column(name = "START_BEACON_PASSIVE")
	private boolean startBeaconPassive;

	@Column(name = "VERSION")
	private String version;

	@Column(name = "VIDEO_RECORD")
	private boolean videoRecord;

	@Column(name = "WIFI_STATE")
	private String wifiState;

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isAudioRecord() {
		return audioRecord;
	}

	public void setAudioRecord(boolean audioRecord) {
		this.audioRecord = audioRecord;
	}

	public Integer getBattery() {
		return battery;
	}

	public void setBattery(Integer battery) {
		this.battery = battery;
	}

	public String getBluetoothState() {
		return bluetoothState;
	}

	public void setBluetoothState(String bluetoothState) {
		this.bluetoothState = bluetoothState;
	}

	public String getCallState() {
		return callState;
	}

	public void setCallState(String callState) {
		this.callState = callState;
	}

	public String getConnectionInternet() {
		return connectionInternet;
	}

	public void setConnectionInternet(String connectionInternet) {
		this.connectionInternet = connectionInternet;
	}

	public int getContLatImage() {
		return contLatImage;
	}

	public void setContLatImage(int contLatImage) {
		this.contLatImage = contLatImage;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isDeviceState() {
		return deviceState;
	}

	public void setDeviceState(boolean deviceState) {
		this.deviceState = deviceState;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGpsState() {
		return gpsState;
	}

	public void setGpsState(String gpsState) {
		this.gpsState = gpsState;
	}

	public Date getLastConnection() {
		return lastConnection;
	}

	public void setLastConnection(Date lastConnection) {
		this.lastConnection = lastConnection;
	}

	public Date getLastSynchronization() {
		return lastSynchronization;
	}

	public void setLastSynchronization(Date lastSynchronization) {
		this.lastSynchronization = lastSynchronization;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public double getLatitudeGps() {
		return latitudeGps;
	}

	public void setLatitudeGps(double latitudeGps) {
		this.latitudeGps = latitudeGps;
	}

	public double getLongitudeGps() {
		return longitudeGps;
	}

	public void setLongitudeGps(double longitudeGps) {
		this.longitudeGps = longitudeGps;
	}

	public double getLatitudeNetwork() {
		return latitudeNetwork;
	}

	public void setLatitudeNetwork(double latitudeNetwork) {
		this.latitudeNetwork = latitudeNetwork;
	}

	public double getLongitudeNetwork() {
		return longitudeNetwork;
	}

	public void setLongitudeNetwork(double longitudeNetwork) {
		this.longitudeNetwork = longitudeNetwork;
	}

	public double getLatitudePassive() {
		return latitudePassive;
	}

	public void setLatitudePassive(double latitudePassive) {
		this.latitudePassive = latitudePassive;
	}

	public double getLongitudePassive() {
		return longitudePassive;
	}

	public void setLongitudePassive(double longitudePassive) {
		this.longitudePassive = longitudePassive;
	}

	public String getMobileState() {
		return mobileState;
	}

	public void setMobileState(String mobileState) {
		this.mobileState = mobileState;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getNameCompany() {
		return nameCompany;
	}

	public void setNameCompany(String nameCompany) {
		this.nameCompany = nameCompany;
	}

	public String getNfcState() {
		return nfcState;
	}

	public void setNfcState(String nfcState) {
		this.nfcState = nfcState;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPowerState() {
		return powerState;
	}

	public void setPowerState(String powerState) {
		this.powerState = powerState;
	}

	public boolean isStartBeaconGps() {
		return startBeaconGps;
	}

	public void setStartBeaconGps(boolean startBeaconGps) {
		this.startBeaconGps = startBeaconGps;
	}

	public boolean isStartBeaconNetwork() {
		return startBeaconNetwork;
	}

	public void setStartBeaconNetwork(boolean startBeaconNetwork) {
		this.startBeaconNetwork = startBeaconNetwork;
	}

	public boolean isStartBeaconPassive() {
		return startBeaconPassive;
	}

	public void setStartBeaconPassive(boolean startBeaconPassive) {
		this.startBeaconPassive = startBeaconPassive;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isVideoRecord() {
		return videoRecord;
	}

	public void setVideoRecord(boolean videoRecord) {
		this.videoRecord = videoRecord;
	}

	public String getWifiState() {
		return wifiState;
	}

	public void setWifiState(String wifiState) {
		this.wifiState = wifiState;
	}

	@Override
	public String toString() {
		return "DeviceDTO [imei=" + imei + ", ip=" + ip + ", address=" + address + ", audioRecord=" + audioRecord
				+ ", battery=" + battery + ", bluetoothState=" + bluetoothState + ", callState=" + callState
				+ ", connectionInternet=" + connectionInternet + ", contLatImage=" + contLatImage + ", country="
				+ country + ", deviceState=" + deviceState + ", email=" + email + ", gpsState=" + gpsState
				+ ", lastConnection=" + lastConnection + ", lastSynchronization=" + lastSynchronization + ", locality="
				+ locality + ", latitudeGps=" + latitudeGps + ", longitudeGps=" + longitudeGps + ", latitudeNetwork="
				+ latitudeNetwork + ", longitudeNetwork=" + longitudeNetwork + ", latitudePassive=" + latitudePassive
				+ ", longitudePassive=" + longitudePassive + ", mobileState=" + mobileState + ", model=" + model
				+ ", nameCompany=" + nameCompany + ", nfcState=" + nfcState + ", phoneNumber=" + phoneNumber
				+ ", powerState=" + powerState + ", startBeaconGps=" + startBeaconGps + ", startBeaconNetwork="
				+ startBeaconNetwork + ", startBeaconPassive=" + startBeaconPassive + ", version=" + version
				+ ", videoRecord=" + videoRecord + ", wifiState=" + wifiState + "]";
	}

}