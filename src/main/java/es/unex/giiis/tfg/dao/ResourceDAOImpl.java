package es.unex.giiis.tfg.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.model.ResourceDTO;
import es.unex.giiis.tfg.model.ResourceDTO.TypeResource;
import es.unex.giiis.tfg.protocol.Protocol;

@Repository
public class ResourceDAOImpl extends BaseDAOImpl<ResourceDTO> implements ResourceDAO {

	@SuppressWarnings("unchecked")
	public List<ResourceDTO> findAllResourceByImeiAndTypeResource(String imei, TypeResource typeResource)
			throws DaoException {
		Query query;
		if (typeResource == TypeResource.ANDROID)
			query = getSessionFactory().getCurrentSession()
					.createQuery("FROM es.unex.giiis.tfg.model." + Protocol.TYPE_RESOURCEDTO
							+ " WHERE IMEI = :IMEI AND TYPE_RESOURCE = :TYPE_RESOURCE ORDER BY PATH_ANDROID ASC");
		else
			query = getSessionFactory().getCurrentSession()
					.createQuery("FROM es.unex.giiis.tfg.model." + Protocol.TYPE_RESOURCEDTO
							+ " WHERE IMEI = :IMEI AND TYPE_RESOURCE = :TYPE_RESOURCE ORDER BY DATE_SYNCHRONIZE DESC");
		query.setParameter("IMEI", imei);
		query.setParameter("TYPE_RESOURCE", TypeResource.parseTypeResourceToInteger(typeResource));
		List<ResourceDTO> list = (List<ResourceDTO>) query.list();
		return list;
	}

	public void deleteAllResourceByImeiAndTypeResource(String imei, TypeResource typeResource) throws DaoException {
		List<ResourceDTO> entityList = findAllResourceByImeiAndTypeResource(imei, typeResource);
		for (ResourceDTO entity : entityList) {
			delete(entity);
		}
	}

}
