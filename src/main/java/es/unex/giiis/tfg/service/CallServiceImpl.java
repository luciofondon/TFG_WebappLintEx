package es.unex.giiis.tfg.service;

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

import es.unex.giiis.tfg.dao.CallDAO;
import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.CallDTO;
import es.unex.giiis.tfg.model.CallDTO.TypeCall;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.sincro.CallSINCRO;

@Service
@Transactional(readOnly = true)
public class CallServiceImpl extends BaseServiceImpl<CallDTO> implements CallService {

	final Logger LOGGER = Logger.getLogger("CallServiceImpl");

	@Autowired
	CallDAO dao;

	public CallDAO getDAO() {
		return this.dao;
	}

	@Transactional(readOnly = false)
	public void convertStringToAudioAndSave(CallSINCRO callSincro) throws ServiceException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SS");
		Calendar calendar = Calendar.getInstance();
		String nameAudioCall = sdf.format(calendar.getTime());

		try {
			byte[] decodedByte = Base64.decodeBase64(callSincro.getData());

			// Crear el fichero de audio
			FileOutputStream audioOutFile = new FileOutputStream(
					Protocol.PATH_BASE + "/WebApp5/src/main/webapp/resources/audio/call/" + nameAudioCall + ".mp4");
			audioOutFile.write(decodedByte);
			audioOutFile.flush();
			audioOutFile.close();

			// Crear llamada para guardar en BD
			CallDTO call = new CallDTO();
			call.setPathJava(
					Protocol.PATH_BASE + "/WebApp5/src/main/webapp/resources/audio/call/" + nameAudioCall + ".mp4");
			call.setPathWeb("./../resources/audio/call/" + nameAudioCall + ".mp4");
			call.setTypeCall(TypeCall.RECORD);
			call.setNameContact((callSincro.getNameContact()));
			call.setNameFile(nameAudioCall + ".mp4");
			call.setImei(callSincro.getImei());
			call.setDateAndroid((callSincro.getDateAndroid()));
			call.setDateSynchronize((calendar.getTime()));
			call.setPhoneNumber(callSincro.getPhoneNumber());
			this.dao.add(call, Protocol.TYPE_CALLDTO);

			this.LOGGER.info(
					"convertStringToAudioAndSave -> CallServiceImpl: Fichero de audio de la llamada guardado correctamente...");
		} catch (FileNotFoundException fnfe) {
			System.out.println("Audio Path not found" + fnfe);
			throw new ServiceException("Excepcion: No es valido el path...");
		} catch (IOException ioe) {
			throw new ServiceException("Excepcion: Crear el fichero de audio...");
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}

	}

	public List<CallDTO> findAllCallByImeiAndTypeCall(String imei, TypeCall typeCall) throws ServiceException {
		try {
			return this.dao.findAllCallByImeiAndTypeCall(imei, typeCall);
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}
	}

	@Transactional(readOnly = false)
	public void deleteAllCallByImeiAndTypeCall(String imei, TypeCall typeCall) throws ServiceException {
		try {
			this.dao.deleteAllCallByImeiAndTypeCall(imei, typeCall);
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}
	}

}