package es.unex.giiis.tfg.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BROWSER")
public class BrowserDTO extends BaseDTO {
	public static enum TypeBrowser {
		HISTORY, BOOKMARK, ERROR
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

	@Column(name = "TITLE")
	private String title;

	@Column(name = "TYPE_BROWSER")
	private TypeBrowser typeBrowser;

	@Column(name = "URL")
	private String url;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public TypeBrowser getTypeBrowser() {
		return typeBrowser;
	}

	public void setTypeBrowser(TypeBrowser typeBrowser) {
		this.typeBrowser = typeBrowser;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "BrowserDTO [id=" + id + ", imei=" + imei + ", dateAndroid=" + dateAndroid + ", dateSynchronize="
				+ dateSynchronize + ", title=" + title + ", typeBrowser=" + typeBrowser + ", url=" + url + "]\n";
	}

}
