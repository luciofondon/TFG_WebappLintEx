package es.unex.giiis.tfg.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "IMAGE")
public class ImageDTO extends BaseDTO {
	public static enum TypeImage {
		CAMERA, GALLERY, ERROR;

		public static Integer parseTypeImageToInteger(TypeImage typeImage) {
			switch (typeImage) {
			case CAMERA:
				return 0;
			case GALLERY:
				return 1;
			default:
				return 2;

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

	@Column(name = "HEIGHT")
	private Integer height;

	@Column(name = "LATITUDE")
	private Double latitude;

	@Column(name = "LONGITUDE")
	private Double longitude;

	@Column(name = "NAME")
	private String name;

	@Column(name = "PATH_JAVA")
	private String pathJava;

	@Column(name = "PATH_WEB")
	private String pathWeb;

	@Column(name = "SIZE")
	private Long size;

	@Column(name = "TYPE_IMAGE")
	private TypeImage typeImage;

	@Column(name = "WIDTH")
	private Integer width;

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

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public TypeImage getTypeImage() {
		return typeImage;
	}

	public void setTypeImage(TypeImage typeImage) {
		this.typeImage = typeImage;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	@Override
	public String toString() {
		return "ImageDTO [id=" + id + ", imei=" + imei + ", dateAndroid=" + dateAndroid + ", dateSynchronize="
				+ dateSynchronize + ", height=" + height + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", name=" + name + ", pathJava=" + pathJava + ", pathWeb=" + pathWeb + ", size=" + size
				+ ", typeImage=" + typeImage + ", width=" + width + "]";
	}

}
