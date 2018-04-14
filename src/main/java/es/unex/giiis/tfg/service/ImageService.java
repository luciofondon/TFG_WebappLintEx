package es.unex.giiis.tfg.service;

import java.util.List;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.ImageDTO;
import es.unex.giiis.tfg.model.ImageDTO.TypeImage;
import es.unex.giiis.tfg.sincro.ImageSINCRO;

public interface ImageService extends BaseService<ImageDTO> {

	public void convertStringToImageAndSave(ImageSINCRO imagenSincro) throws ServiceException;

	public List<ImageDTO> findAllImageByImeiAndTypeImage(String imei, TypeImage typeImage) throws ServiceException;

	public void deleteAllImageByImeiAndTypeImage(String imei, TypeImage typeImage) throws ServiceException;

}
