package es.unex.giiis.tfg.service;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.AudioDTO;
import es.unex.giiis.tfg.sincro.AudioSINCRO;

public interface AudioService extends BaseService<AudioDTO> {

	public void convertStringToAudioAndSave(AudioSINCRO audioSincro) throws ServiceException;
}
