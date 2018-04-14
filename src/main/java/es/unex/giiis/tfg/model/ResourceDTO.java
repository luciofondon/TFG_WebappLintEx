package es.unex.giiis.tfg.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RESOURCE")
public class ResourceDTO extends BaseDTO {
	public static enum TypeResource {
		WEB, ANDROID, ERROR;

		public static Integer parseTypeResourceToInteger(TypeResource typeResource) {
			switch (typeResource) {
			case WEB:
				return 0;
			case ANDROID:
				return 1;
			default:
				return 2;
			}
		}
	};

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "IMEI")
	private String imei;

	@Column(name = "DATE_SYNCHRONIZE")
	private Date dateSynchronize;

	@Column(name = "NAME")
	private String name;

	@Column(name = "PATH_ANDROID")
	private String pathAndroid;

	@Column(name = "PATH_JAVA")
	private String pathJava;

	@Column(name = "PATH_WEB")
	private String pathWeb;

	@Column(name = "SIZE")
	private Long size;

	@Column(name = "TYPE_RESOURCE")
	private TypeResource typeResource;

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

	public String getPathAndroid() {
		return pathAndroid;
	}

	public void setPathAndroid(String pathAndroid) {
		this.pathAndroid = pathAndroid;
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

	public TypeResource getTypeResource() {
		return typeResource;
	}

	public void setTypeResource(TypeResource typeResource) {
		this.typeResource = typeResource;
	}

	@Override
	public String toString() {
		return "ResourceDTO [id=" + id + ", imei=" + imei + ", dateSynchronize=" + dateSynchronize + ", name=" + name
				+ ", pathAndroid=" + pathAndroid + ", pathJava=" + pathJava + ", pathWeb=" + pathWeb + ", size=" + size
				+ ", typeResource=" + typeResource + "]";
	}

}
