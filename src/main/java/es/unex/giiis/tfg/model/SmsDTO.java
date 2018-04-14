package es.unex.giiis.tfg.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SMS")
public class SmsDTO extends BaseDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	public static enum TypeSms {
		INBOX, SENT, DRAFT, ERROR;

		public static Integer parseTypeSmsToInteger(TypeSms typeSms) {
			switch (typeSms) {
			case INBOX:
				return 0;
			case SENT:
				return 1;
			case DRAFT:
				return 2;
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

	@Column(name = "MESSAGE")
	private String message;

	@Column(name = "NAME")
	private String name;

	@Column(name = "TYPE_SMS")
	private TypeSms typeSms;

	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;

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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TypeSms getTypeSms() {
		return typeSms;
	}

	public void setTypeSms(TypeSms typeSms) {
		this.typeSms = typeSms;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "SmsDTO [id=" + id + ", imei=" + imei + ", dateAndroid=" + dateAndroid + ", dateSynchronize="
				+ dateSynchronize + ", message=" + message + ", name=" + name + ", typeSms=" + typeSms
				+ ", phoneNumber=" + phoneNumber + "]\n";
	}

}
