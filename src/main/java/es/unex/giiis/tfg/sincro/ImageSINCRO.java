package es.unex.giiis.tfg.sincro;

import java.util.Date;

import es.unex.giiis.tfg.model.BaseDTO;
import es.unex.giiis.tfg.model.ImageDTO.TypeImage;

public class ImageSINCRO extends BaseDTO {
	public static enum TypeImageSINCRO {
		CAMERA, GALLERY, ERROR
	};

	private String imei;

	private String data;

	private Date dateAndroid;

	private Integer height;

	private Double latitude;

	private Double longitude;

	private String name;

	private Long size;

	private TypeImageSINCRO typeImage;

	private Integer width;

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

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public TypeImageSINCRO getTypeImage() {
		return typeImage;
	}

	public void setTypeImage(TypeImageSINCRO typeImage) {
		this.typeImage = typeImage;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public static TypeImage parseTypeImageSincro(TypeImageSINCRO typeImageSincro) {
		switch (typeImageSincro) {
		case CAMERA:
			return TypeImage.CAMERA;
		case GALLERY:
			return TypeImage.GALLERY;
		case ERROR:
			return TypeImage.ERROR;
		default:
			return TypeImage.ERROR;

		}
	}

	@Override
	public String toString() {
		return "ImageSINCRO [imei=" + imei + ", dateAndroid=" + dateAndroid + ", height=" + height + ", latitude="
				+ latitude + ", longitude=" + longitude + ", name=" + name + ", size=" + size + ", typeImage="
				+ typeImage + ", width=" + width + "]";
	}

}
