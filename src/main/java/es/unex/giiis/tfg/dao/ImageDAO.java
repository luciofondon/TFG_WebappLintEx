package es.unex.giiis.tfg.dao;

import java.util.List;

import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.model.ImageDTO;
import es.unex.giiis.tfg.model.ImageDTO.TypeImage;

public interface ImageDAO extends BaseDAO<ImageDTO> {

	public List<ImageDTO> findAllImageByImeiAndTypeImage(String imei, TypeImage typeImage) throws DaoException;

	public void deleteAllImageByImeiAndTypeImage(String imei, TypeImage typeImage) throws DaoException;

}
