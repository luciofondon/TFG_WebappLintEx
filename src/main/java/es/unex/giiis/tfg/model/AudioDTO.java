package es.unex.giiis.tfg.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AUDIO")
public class AudioDTO extends BaseDTO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "IMEI")
	private String imei;

	@Column(name = "DATE_SYNCHRONIZE")
	private Date dateSynchronize;

	@Column(name = "NAME")
	private String name;

	@Column(name = "PATH_JAVA")
	private String pathJava;

	@Column(name = "PATH_WEB")
	private String pathWeb;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public String toString() {
		return "AudioDTO [id=" + id + ", imei=" + imei + ", dateSynchronize=" + dateSynchronize + ", name=" + name
				+ ", pathJava=" + pathJava + ", pathWeb=" + pathWeb + "]";
	}

}
