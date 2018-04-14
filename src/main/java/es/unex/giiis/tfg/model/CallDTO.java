package es.unex.giiis.tfg.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CALL")
public class CallDTO extends BaseDTO {

	public static enum TypeCall {
		INCOMING, OUTGOING, MISSED, RECORD, ERROR;

		public static Integer parseTypeCallToInteger(TypeCall typeCall) {
			switch (typeCall) {
			case INCOMING:
				return 0;
			case OUTGOING:
				return 1;
			case MISSED:
				return 2;
			case RECORD:
				return 3;
			default:
				return 4;
			}
		}
	};

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "IMEI")
	private String imei;

	@Column(name = "DATE_ANDROID")
	private Date dateAndroid;

	@Column(name = "DATE_SYNCHRONIZE")
	private Date dateSynchronize;

	@Column(name = "DURATION_HOUR")
	private int durationHour;

	@Column(name = "DURATION_MINUTE")
	private int durationMinute;

	@Column(name = "DURATION_SECOND")
	private int durationSecond;

	@Column(name = "NAME_CONTACT")
	private String nameContact;

	@Column(name = "NAME_FILE")
	private String nameFile;

	@Column(name = "PATH_JAVA")
	private String pathJava;

	@Column(name = "PATH_WEB")
	private String pathWeb;

	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;

	@Column(name = "TYPE_CALL")
	private TypeCall typeCall;

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

	public Date getDateAndroid() {
		return dateAndroid;
	}

	public void setDateAndroid(Date dateAndroid) {
		this.dateAndroid = dateAndroid;
	}

	public Date getDateSynchronize() {
		return dateSynchronize;
	}

	public void setDateSynchronize(Date dateSynchronize) {
		this.dateSynchronize = dateSynchronize;
	}

	public int getDurationHour() {
		return durationHour;
	}

	public void setDurationHour(int durationHour) {
		this.durationHour = durationHour;
	}

	public int getDurationMinute() {
		return durationMinute;
	}

	public void setDurationMinute(int durationMinute) {
		this.durationMinute = durationMinute;
	}

	public int getDurationSecond() {
		return durationSecond;
	}

	public void setDurationSecond(int durationSecond) {
		this.durationSecond = durationSecond;
	}

	public String getNameContact() {
		return nameContact;
	}

	public void setNameContact(String nameContact) {
		this.nameContact = nameContact;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public String getPathJava() {
		return pathJava;
	}

	public void setPathJava(String pathJava) {
		this.pathJava = pathJava;
	}

	public String getPathWeb() {
		return pathWeb;
	}

	public void setPathWeb(String pathWeb) {
		this.pathWeb = pathWeb;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public TypeCall getTypeCall() {
		return typeCall;
	}

	public void setTypeCall(TypeCall typeCall) {
		this.typeCall = typeCall;
	}

	@Override
	public String toString() {
		return "CallDTO [id=" + id + ", imei=" + imei + ", dateAndroid=" + dateAndroid + ", dateSynchronize="
				+ dateSynchronize + ", durationHour=" + durationHour + ", durationMinute=" + durationMinute
				+ ", durationSecond=" + durationSecond + ", nameContact=" + nameContact + ", nameFile=" + nameFile
				+ ", pathJava=" + pathJava + ", pathWeb=" + pathWeb + ", phoneNumber=" + phoneNumber + ", typeCall="
				+ typeCall + "]\n";
	}

}
