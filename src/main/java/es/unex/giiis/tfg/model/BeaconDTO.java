package es.unex.giiis.tfg.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BEACON")
public class BeaconDTO extends BaseDTO {
	public static enum TypeBeacon {
		GPS, NETWORK, PASSIVE, ERROR;

		public static Integer parseTypeBeaconToInteger(TypeBeacon typeBeacon) {
			switch (typeBeacon) {
			case GPS:
				return 0;
			case NETWORK:
				return 1;
			case PASSIVE:
				return 2;
			default:
				return 3;
			}
		}
	};

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "IMEI")
	private String imei;

	@Column(name = "DATE_SYNCHRONIZE")
	private Date dateSynchronize;

	@Column(name = "LATITUDE")
	private double latitude;

	@Column(name = "LONGITUDE")
	private double longitude;

	@Column(name = "TYPE_BEACON")
	private TypeBeacon typeBeacon;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Date getDateSynchronize() {
		return dateSynchronize;
	}

	public void setDateSynchronize(Date dateSynchronize) {
		this.dateSynchronize = dateSynchronize;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public TypeBeacon getTypeBeacon() {
		return typeBeacon;
	}

	public void setTypeBeacon(TypeBeacon typeBeacon) {
		this.typeBeacon = typeBeacon;
	}

	@Override
	public String toString() {
		return "BeaconDTO [id=" + id + ", imei=" + imei + ", dateSynchronize=" + dateSynchronize + ", latitude="
				+ latitude + ", longitude=" + longitude + ", typeBeacon=" + typeBeacon + "]";
	}

}
