package es.unex.giiis.tfg.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.model.ImageDTO;
import es.unex.giiis.tfg.model.ImageDTO.TypeImage;
import es.unex.giiis.tfg.protocol.Protocol;

@Repository
public class ImageDAOImpl extends BaseDAOImpl<ImageDTO> implements ImageDAO {

	@SuppressWarnings("unchecked")
	public List<ImageDTO> findAllImageByImeiAndTypeImage(String imei, TypeImage typeImage) throws DaoException {
		Query query;
		if (typeImage == TypeImage.GALLERY)
			query = getSessionFactory().getCurrentSession()
					.createQuery("FROM es.unex.giiis.tfg.model." + Protocol.TYPE_IMAGEDTO
							+ " WHERE IMEI = :IMEI AND TYPE_IMAGE = :TYPE_IMAGE ORDER BY DATE_ANDROID DESC");
		else
			query = getSessionFactory().getCurrentSession()
					.createQuery("FROM es.unex.giiis.tfg.model." + Protocol.TYPE_IMAGEDTO
							+ " WHERE IMEI = :IMEI AND TYPE_IMAGE = :TYPE_IMAGE ORDER BY DATE_SYNCHRONIZE DESC");
		query.setParameter("IMEI", imei);
		query.setParameter("TYPE_IMAGE", TypeImage.parseTypeImageToInteger(typeImage));
		List<ImageDTO> list = (List<ImageDTO>) query.list();
		return list;
	}

	public void deleteAllImageByImeiAndTypeImage(String imei, TypeImage typeImage) throws DaoException {
		List<ImageDTO> entityList = findAllImageByImeiAndTypeImage(imei, typeImage);
		for (ImageDTO entity : entityList) {
			delete(entity);
		}
	}

}
