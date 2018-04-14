package es.unex.giiis.tfg.sincro;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.unex.giiis.tfg.model.BrowserDTO;
import es.unex.giiis.tfg.model.BrowserDTO.TypeBrowser;

public class BrowserSINCRO extends BaseSINCRO {
	public static enum TypeBrowserSincro {
		HISTORY, BOOKMARK, ERROR
	};

	private Long id;

	private String imei;

	private Date dateAndroid;

	private String title;

	private TypeBrowserSincro typeBrowser;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public TypeBrowserSincro getTypeBrowser() {
		return typeBrowser;
	}

	public void setTypeBrowser(TypeBrowserSincro typeBrowser) {
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
		return "BrowserSINCRO [id=" + id + ", imei=" + imei + ", dateAndroid=" + dateAndroid + ", title=" + title
				+ ", typeBrowser=" + typeBrowser + ", url=" + url + "]\n";
	}

	public static List<BrowserDTO> parseSincroListBrowser(List<BrowserSINCRO> list) {
		List<BrowserDTO> listBrowser = new ArrayList<BrowserDTO>();

		for (BrowserSINCRO b : list) {
			BrowserDTO browser = new BrowserDTO();
			browser.setImei(b.getImei());
			browser.setTitle(b.getTitle());
			browser.setId(b.getId());
			browser.setDateSynchronize(Calendar.getInstance().getTime());
			browser.setDateAndroid((b.getDateAndroid()));
			browser.setUrl(b.getUrl());
			browser.setTypeBrowser(parseTypeBrowserSincro(b.getTypeBrowser()));

			listBrowser.add(browser);
		}
		return listBrowser;
	}

	public static TypeBrowser parseTypeBrowserSincro(TypeBrowserSincro typeBrowserSincro) {
		switch (typeBrowserSincro) {
		case BOOKMARK:
			return TypeBrowser.BOOKMARK;
		case HISTORY:
			return TypeBrowser.HISTORY;

		default:
			return TypeBrowser.ERROR;
		}
	}

}
