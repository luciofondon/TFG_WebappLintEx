package es.unex.giiis.tfg.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.unex.giiis.tfg.dao.ImageDAO;
import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.ImageDTO;
import es.unex.giiis.tfg.model.ImageDTO.TypeImage;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.sincro.ImageSINCRO;
import es.unex.giiis.tfg.sincro.ImageSINCRO.TypeImageSINCRO;

@Service
@Transactional(readOnly = true)
public class ImageServiceImpl extends BaseServiceImpl<ImageDTO> implements ImageService {

	final Logger LOGGER = Logger.getLogger("ImageServiceImpl");

	@Autowired
	ImageDAO dao;

	public ImageDAO getDAO() {
		return this.dao;
	}

	@Transactional(readOnly = false)
	public void convertStringToImageAndSave(ImageSINCRO imageSincro) throws ServiceException {
		if (imageSincro != null) {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SS");
			try {
				byte[] decodeByte = Base64.decodeBase64(imageSincro.getData());

				// Crear imagen para guardar en BD
				ImageDTO image = new ImageDTO();
				File file = null;
				String nameImage = "";
				if (imageSincro.getTypeImage() == TypeImageSINCRO.CAMERA) {
					nameImage = sdf.format(calendar.getTime());
					file = new File(Protocol.PATH_BASE + "/WebApp5/src/main/webapp/resources/image/camera");
					image.setPathWeb("./../resources/image/camera/" + nameImage + ".jpg");
					image.setPathJava(Protocol.PATH_BASE + "/WebApp5/src/main/webapp/resources/image/camera/"
							+ nameImage + ".jpg");
					image.setName(nameImage);
				} else if (imageSincro.getTypeImage() == TypeImageSINCRO.GALLERY) {
					file = new File(Protocol.PATH_BASE + "/WebApp5/src/main/webapp/resources/image/gallery");
					image.setPathWeb("./../resources/image/gallery/" + imageSincro.getName() + ".jpg");
					image.setPathJava(Protocol.PATH_BASE + "/WebApp5/src/main/webapp/resources/image/gallery/"
							+ imageSincro.getName() + ".jpg");
					image.setName(imageSincro.getName());
					nameImage = imageSincro.getName();

				}
				// Crear el fichero de imagen
				FileOutputStream imageOutFile = new FileOutputStream(file + "/" + nameImage + ".jpg");
				imageOutFile.write(decodeByte);
				imageOutFile.flush();
				imageOutFile.close();

				image.setTypeImage(ImageSINCRO.parseTypeImageSincro(imageSincro.getTypeImage()));
				image.setImei(imageSincro.getImei());
				image.setDateSynchronize(calendar.getTime());
				image.setSize(imageSincro.getSize());
				image.setLatitude(imageSincro.getLatitude());
				image.setLongitude(imageSincro.getLongitude());
				image.setWidth(imageSincro.getWidth());
				image.setHeight(imageSincro.getHeight());
				image.setDateAndroid(imageSincro.getDateAndroid());
				this.dao.add(image, Protocol.TYPE_IMAGEDTO);

				this.LOGGER.info(
						"convertStringToImageAndSave -> ImageServiceImpl: Fichero de audio de la llamada guardado correctamente...");
			} catch (FileNotFoundException fnfe) {
				throw new ServiceException("Excepcion: No es valido el path...");
			} catch (IOException ioe) {
				throw new ServiceException("Excepcion: Crear el fichero de imagen...");
			} catch (DaoException e) {
				throw new ServiceException(e.toString());
			}
		}

	}

	public List<ImageDTO> findAllImageByImeiAndTypeImage(String imei, TypeImage typeImage) throws ServiceException {
		try {
			return this.dao.findAllImageByImeiAndTypeImage(imei, typeImage);
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}

	}

	@Transactional(readOnly = false)
	public void deleteAllImageByImeiAndTypeImage(String imei, TypeImage typeImage) throws ServiceException {
		try {
			this.dao.deleteAllImageByImeiAndTypeImage(imei, typeImage);
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}
	}

}
