package es.unex.giiis.tfg.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.unex.giiis.tfg.dao.ResourceDAO;
import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.ResourceDTO;
import es.unex.giiis.tfg.model.ResourceDTO.TypeResource;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.sincro.ResourceSINCRO;
import es.unex.giiis.tfg.sincro.ResourceSINCRO.TypeResourceSINCRO;

@Service
@Transactional(readOnly = true)
public class ResourceServiceImpl extends BaseServiceImpl<ResourceDTO> implements ResourceService {

	final Logger LOGGER = Logger.getLogger("ResourceServiceImpl");

	@Autowired
	ResourceDAO dao;

	public ResourceDAO getDAO() {
		return this.dao;
	}

	@Transactional(readOnly = false)
	public void convertStringToResourceAndSave(ResourceSINCRO resourceSincro) throws ServiceException {
		Calendar calendar = Calendar.getInstance();
		if (resourceSincro.getTypeResource() != TypeResourceSINCRO.ERROR) {

			try {
				byte[] decoded = Base64.decodeBase64(resourceSincro.getData());

				// Crear el fichero de audio
				FileOutputStream fileOutputStream = new FileOutputStream(
						Protocol.PATH_BASE + "/WebApp5/src/main/webapp/resources/storage/" + resourceSincro.getName());
				fileOutputStream.write(decoded);
				fileOutputStream.flush();
				fileOutputStream.close();

				// Crear recurso para guardar en BD
				ResourceDTO resource = new ResourceDTO();
				resource.setPathAndroid(resourceSincro.getPathAndroid());
				resource.setPathJava(
						Protocol.PATH_BASE + "/WebApp5/src/main/webapp/resources/storage/" + resourceSincro.getName());
				resource.setPathWeb("./../resources/storage/" + resource.getName());
				resource.setImei(resourceSincro.getImei());
				resource.setName(resourceSincro.getName());
				resource.setTypeResource(ResourceSINCRO.parseTypeResourceSincro(resourceSincro.getTypeResource()));
				resource.setDateSynchronize(calendar.getTime());
				resource.setSize(resourceSincro.getSize());
				this.dao.add(resource, Protocol.TYPE_DEVICEDTO);

				this.LOGGER
						.info("convertStringToResourceAndSave -> ResourceServiceImpl: Fichero guardado correctamente.");
			} catch (FileNotFoundException fileNotFoundException) {
				throw new ServiceException("convertStringToResourceAndSave -> ResourceServiceImpl: Path no valido.");
			} catch (IOException ioe) {
				throw new ServiceException(
						"convertStringToResourceAndSave -> ResourceServiceImpl: Excepcion mientras se almacenaba el fichero.");
			} catch (DaoException e) {
				throw new ServiceException(e.toString());
			}
		}

	}

	public List<ResourceDTO> findAllResourceByImeiAndTypeResource(String imei, TypeResource typeResource)
			throws ServiceException {
		try {
			return this.dao.findAllResourceByImeiAndTypeResource(imei, typeResource);
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}
	}

	@Transactional(readOnly = false)
	public void deleteAllResourceByImeiAndTypeResource(String imei, TypeResource typeResource) throws ServiceException {
		try {
			this.dao.deleteAllResourceByImeiAndTypeResource(imei, typeResource);
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}

	}

}
