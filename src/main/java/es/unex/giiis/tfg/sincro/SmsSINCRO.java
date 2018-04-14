package es.unex.giiis.tfg.sincro;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.unex.giiis.tfg.model.SmsDTO;
import es.unex.giiis.tfg.model.SmsDTO.TypeSms;

public class SmsSINCRO extends BaseSINCRO {
	public static enum TypeSmsSINCRO {
		INBOX, SENT, DRAFT, ERROR
	};

	private String imei;

	private Date dateAndroid;

	private String message;

	private String name;

	private TypeSmsSINCRO typeSms;

	private String phoneNumber;

	@Override
	public String toString() {
		return "SmsSINCRO [imei=" + imei + ", dateAndroid=" + dateAndroid + ", message=" + message + ", name=" + name
				+ ", typeSms=" + typeSms + ", phoneNumber=" + phoneNumber + "]\n";
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

	public TypeSmsSINCRO getTypeSms() {
		return typeSms;
	}

	public void setTypeSms(TypeSmsSINCRO typeSms) {
		this.typeSms = typeSms;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public static List<SmsDTO> parseSincroListSms(List<SmsSINCRO> list) {
		List<SmsDTO> listSms = new ArrayList<SmsDTO>();
		for (SmsSINCRO s : list) {
			SmsDTO sms = new SmsDTO();
			sms.setDateAndroid(s.getDateAndroid());
			sms.setDateSynchronize(Calendar.getInstance().getTime());
			sms.setImei(s.getImei());
			sms.setPhoneNumber(s.getPhoneNumber());
			sms.setTypeSms(parseTypeCallSincro(s.getTypeSms()));
			sms.setMessage(s.getMessage());
			sms.setName(s.getName());
			listSms.add(sms);
		}
		return listSms;
	}

	public static TypeSms parseTypeCallSincro(TypeSmsSINCRO typeSmsSincro) {
		switch (typeSmsSincro) {
		case SENT:
			return TypeSms.SENT;
		case INBOX:
			return TypeSms.INBOX;
		case DRAFT:
			return TypeSms.DRAFT;
		default:
			return TypeSms.ERROR;
		}
	}
}
