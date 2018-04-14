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

import es.unex.giiis.tfg.dao.VideoDAO;
import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.VideoDTO;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.sincro.VideoSINCRO;

@Service
@Transactional(readOnly = true)
public class VideoServiceImpl extends BaseServiceImpl<VideoDTO> implements VideoService {

	final Logger LOGGER = Logger.getLogger("VideoServiceImpl");

	@Autowired
	VideoDAO dao;

	public VideoDAO getDAO() {
		return this.dao;
	}

	@Transactional(readOnly = false)
	public void convertStringToVideoAndSave(VideoSINCRO videoSincro) throws ServiceException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SS");
		Calendar calendar = Calendar.getInstance();
		String nameVideo = sdf.format(calendar.getTime());

		try {
			// Decode String using Base64 Class
			byte[] decodedBytesAudio = Base64.decodeBase64(videoSincro.getData());

			// Crear el fichero de video
			FileOutputStream audioOutFile = new FileOutputStream(
					"/Users/luciofondon/Desktop/T.F.G./workspace/workspace_web/WebApp5/src/main/webapp/resources/video/"
							+ nameVideo + ".mp4");
			audioOutFile.write(decodedBytesAudio);
			audioOutFile.flush();
			audioOutFile.close();
			
			// Parametros del nuevo audio para insertarlo en BD
			VideoDTO video = new VideoDTO();
			video.setName(nameVideo);
			video.setPathWeb("./../resources/video/" + nameVideo + ".mp4");
			video.setPathJava(
					"/Users/luciofondon/Desktop/T.F.G./workspace/workspace_web/WebApp5/src/main/webapp/resources/video/"
							+ nameVideo + ".mp4");
			video.setDateSynchronize(calendar.getTime());
			video.setImei(videoSincro.getImei());
			this.dao.add(video, Protocol.TYPE_VIDEODTO);
			
			this.LOGGER.info("convertStringToVideoAndSave -> VideoServiceImpl: Fichero guardado correctamente.");
		} catch (FileNotFoundException fnfe) {
			throw new ServiceException("convertStringToVideoAndSave -> VideoServiceImpl: Path no valido.");
		} catch (IOException ioe) {
			throw new ServiceException(
					"convertStringToVideoAndSave -> VideoServiceImpl: Excepcion mientras se almacenaba el audio.");
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}
	}

}
