package es.unex.giiis.tfg.service;

import java.util.List;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.ResourceDTO;
import es.unex.giiis.tfg.model.ResourceDTO.TypeResource;
import es.unex.giiis.tfg.sincro.ResourceSINCRO;

public interface ResourceService extends BaseService<ResourceDTO> {

	public void convertStringToResourceAndSave(ResourceSINCRO resourceSincro) throws ServiceException;

	public List<ResourceDTO> findAllResourceByImeiAndTypeResource(String imei, TypeResource typeResource)
			throws ServiceException;

	
	public void deleteAllResourceByImeiAndTypeResource(String imei, TypeResource typeResource) throws ServiceException;

}
