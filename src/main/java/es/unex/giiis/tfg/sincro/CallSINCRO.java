package es.unex.giiis.tfg.sincro;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.unex.giiis.tfg.model.CallDTO;
import es.unex.giiis.tfg.model.CallDTO.TypeCall;

public class CallSINCRO extends BaseSINCRO {
	public static enum TypeCallSINCRO {
		INCOMING, OUTGOING, MISSED, RECORD, ERROR
	};

	private String imei;

	private String data;

	private Date dateAndroid;

	private int durationHour;

	private int durationMinute;

	private int durationSecond;

	private String nameContact;

	private String phoneNumber;

	private TypeCallSINCRO typeCall;

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Date getDateAndroid() {
		return dateAndroid;
	}

	public void setDateAndroid(Date dateAndroid) {
		this.dateAndroid = dateAndroid;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public TypeCallSINCRO getTypeCall() {
		return typeCall;
	}

	public void setTypeCall(TypeCallSINCRO typeCall) {
		this.typeCall = typeCall;
	}

	@Override
	public String toString() {
		return "CallSINCRO [imei=" + imei + ", dateAndroid=" + dateAndroid + ", durationHour=" + durationHour
				+ ", durationMinute=" + durationMinute + ", durationSecond=" + durationSecond + ", nameContact="
				+ nameContact + ", phoneNumber=" + phoneNumber + ", typeCall=" + typeCall + "]\n";
	}

	public static List<CallDTO> parseSincroListCall(List<CallSINCRO> list) {
		List<CallDTO> listCall = new ArrayList<CallDTO>();
		for (CallSINCRO c : list) {
			System.out.println(c);
			CallDTO call = new CallDTO();
			call.setDateAndroid(c.getDateAndroid());
			call.setDateSynchronize((Calendar.getInstance().getTime()));
			call.setDurationHour(c.getDurationHour());
			call.setDurationMinute(c.getDurationMinute());
			call.setDurationSecond(c.getDurationSecond());
			call.setImei(c.getImei());
			call.setPhoneNumber(c.getPhoneNumber());
			call.setTypeCall(parseTypeCallSincro(c.getTypeCall()));
			call.setNameContact((c.getNameContact()));
			listCall.add(call);
		}
		return listCall;
	}

	public static CallDTO parseSincroCall(CallSINCRO callSincro) {
		CallDTO call = new CallDTO();
		call.setDateAndroid(callSincro.getDateAndroid());
		call.setDateSynchronize((Calendar.getInstance().getTime()));
		call.setDurationHour(callSincro.getDurationHour());
		call.setDurationMinute(callSincro.getDurationMinute());
		call.setDurationSecond(callSincro.getDurationSecond());
		call.setImei(callSincro.getImei());
		call.setPhoneNumber(callSincro.getPhoneNumber());
		call.setTypeCall(parseTypeCallSincro(callSincro.getTypeCall()));
		call.setNameContact((callSincro.getNameContact()));
		return call;
	}

	static public TypeCall parseTypeCallSincro(TypeCallSINCRO typeCallSincro) {
		switch (typeCallSincro) {
		case INCOMING:
			return TypeCall.INCOMING;
		case OUTGOING:
			return TypeCall.OUTGOING;
		case MISSED:
			return TypeCall.MISSED;
		case RECORD:
			return TypeCall.RECORD;
		default:
			return TypeCall.ERROR;
		}
	}

}
