package es.unex.giiis.tfg.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CONTACT")
public class ContactDTO extends BaseDTO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "IMEI")
	private String imei;

	@Column(name = "DATE_SYNCHRONIZE")
	private Date dateSynchronize;

	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;

	@Column(name = "NAME")
	private String name;

	@Column(name = "EMAIL")
	private String email;

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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "ContactDTO [id=" + id + ", imei=" + imei + ", dateSynchronize=" + dateSynchronize + ", phoneNumber="
				+ phoneNumber + ", name=" + name + ", email=" + email + "]";
	}

}