package es.unex.giiis.tfg.sincro;

public class VideoSINCRO extends BaseSINCRO {

	private String data;

	private String imei;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	@Override
	public String toString() {
		return "VideoSINCRO [imei=" + imei + "]";
	}

}
