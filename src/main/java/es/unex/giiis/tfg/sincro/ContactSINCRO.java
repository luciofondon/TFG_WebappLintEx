package es.unex.giiis.tfg.sincro;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.unex.giiis.tfg.model.ContactDTO;

public class ContactSINCRO extends BaseSINCRO {
	private String imei;

	private String email;

	private String name;

	private String phoneNumber;

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public static List<ContactDTO> parseSincroListContact(List<ContactSINCRO> list) {
		List<ContactDTO> listContact = new ArrayList<ContactDTO>();
		for (ContactSINCRO c : list) {
			ContactDTO contact = new ContactDTO();
			contact.setImei(c.getImei());
			contact.setName(c.getName());
			contact.setDateSynchronize(Calendar.getInstance().getTime());
			contact.setPhoneNumber(c.getPhoneNumber());
			contact.setEmail(c.getEmail());
			listContact.add(contact);
		}
		return listContact;
	}

}