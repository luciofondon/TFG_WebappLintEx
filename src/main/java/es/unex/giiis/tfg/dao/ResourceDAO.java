package es.unex.giiis.tfg.dao;

import java.util.List;

import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.model.ResourceDTO;
import es.unex.giiis.tfg.model.ResourceDTO.TypeResource;

public interface ResourceDAO extends BaseDAO<ResourceDTO> {

	public List<ResourceDTO> findAllResourceByImeiAndTypeResource(String imei, TypeResource typeResource)
			throws DaoException;

	public void deleteAllResourceByImeiAndTypeResource(String imei, TypeResource typeResource) throws DaoException;

}
