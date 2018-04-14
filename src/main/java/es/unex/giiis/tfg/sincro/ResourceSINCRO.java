package es.unex.giiis.tfg.sincro;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.unex.giiis.tfg.model.ResourceDTO;
import es.unex.giiis.tfg.model.ResourceDTO.TypeResource;

public class ResourceSINCRO extends BaseSINCRO {
	public static enum TypeResourceSINCRO {
		WEB, ANDROID, ERROR
	};

	private String imei;

	private String data;

	private String name;

	private String pathAndroid;

	private TypeResourceSINCRO typeResource;

	private Long size;

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

	public TypeResourceSINCRO getTypeResource() {
		return typeResource;
	}

	public void setTypeResource(TypeResourceSINCRO typeResource) {
		this.typeResource = typeResource;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public static List<ResourceDTO> parseSincroListResource(List<ResourceSINCRO> list) {
		List<ResourceDTO> listResource = new ArrayList<ResourceDTO>();
		for (ResourceSINCRO r : list) {
			ResourceDTO resource = new ResourceDTO();
			resource.setImei(r.getImei());
			resource.setPathAndroid(r.getPathAndroid());
			resource.setTypeResource(parseTypeResourceSincro(r.getTypeResource()));
			resource.setDateSynchronize(Calendar.getInstance().getTime());
			String[] token = r.getPathAndroid().split("/");
			resource.setName(token[token.length - 1]);
			resource.setSize(r.getSize());
			listResource.add(resource);
		}
		return listResource;

	}

	public static TypeResource parseTypeResourceSincro(TypeResourceSINCRO typeResourceSincro) {
		switch (typeResourceSincro) {
		case ANDROID:
			return TypeResource.ANDROID;
		case WEB:
			return TypeResource.WEB;
		case ERROR:
			return TypeResource.ERROR;
		default:
			return TypeResource.ERROR;

		}
	}

}
