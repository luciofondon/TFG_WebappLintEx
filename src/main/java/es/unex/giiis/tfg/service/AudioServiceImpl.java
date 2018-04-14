package es.unex.giiis.tfg.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.unex.giiis.tfg.dao.AudioDAO;
import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.AudioDTO;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.sincro.AudioSINCRO;

@Service
@Transactional(readOnly = true)
public class AudioServiceImpl extends BaseServiceImpl<AudioDTO> implements AudioService {

	final Logger LOGGER = Logger.getLogger("AudioServiceImpl");

	@Autowired
	AudioDAO dao;

	public AudioDAO getDAO() {
		return this.dao;
	}

	@Transactional(readOnly = false)
	public void convertStringToAudioAndSave(AudioSINCRO audioSincro) throws ServiceException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SS");
		Calendar calendar = Calendar.getInstance();
		String nameAudio = sdf.format(calendar.getTime());

		try {
			byte[] decodedBytesAudio = Base64.decodeBase64(audioSincro.getData());

			// Crear el fichero de audio
			FileOutputStream audioOutFile = new FileOutputStream(
					Protocol.PATH_BASE + "/WebApp5/src/main/webapp/resources/audio/" + nameAudio + ".mp4");
			audioOutFile.write(decodedBytesAudio);
			audioOutFile.flush();
			audioOutFile.close();

			// Parametros del nuevo audio para insertarlo en BD
			AudioDTO audio = new AudioDTO();
			audio.setName(nameAudio);
			audio.setPathWeb("./../resources/audio/" + nameAudio + ".mp4");
			audio.setPathJava(Protocol.PATH_BASE + "/WebApp5/src/main/webapp/resources/audio/" + nameAudio + ".mp4");
			audio.setImei(audioSincro.getImei());
			audio.setDateSynchronize(calendar.getTime());
			this.dao.add(audio, Protocol.TYPE_AUDIODTO);

			this.LOGGER.info("convertStringToAudioAndSave -> AudioServiceImpl: Fichero guardado correctamente.");
		} catch (FileNotFoundException fileNotFoundException) {
			throw new ServiceException("convertStringToAudioAndSave -> AudioServiceImpl: Path no valido.");
		} catch (IOException ioe) {
			throw new ServiceException(
					"convertStringToAudioAndSave -> AudioServiceImpl: Excepcion mientras se almacenaba el audio.");
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}

	}

}
