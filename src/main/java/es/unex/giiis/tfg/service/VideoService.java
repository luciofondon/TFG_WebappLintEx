package es.unex.giiis.tfg.service;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.VideoDTO;
import es.unex.giiis.tfg.sincro.VideoSINCRO;

public interface VideoService extends BaseService<VideoDTO> {

	public void convertStringToVideoAndSave(VideoSINCRO videoSincro) throws ServiceException;
}
