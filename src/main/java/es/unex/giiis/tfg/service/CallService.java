package es.unex.giiis.tfg.service;

import java.util.List;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.CallDTO;
import es.unex.giiis.tfg.model.CallDTO.TypeCall;
import es.unex.giiis.tfg.sincro.CallSINCRO;

public interface CallService extends BaseService<CallDTO> {

	public void convertStringToAudioAndSave(CallSINCRO callSincro) throws ServiceException;

	public List<CallDTO> findAllCallByImeiAndTypeCall(String imei, TypeCall typeCall) throws ServiceException;

	public void deleteAllCallByImeiAndTypeCall(String imei, TypeCall typeCall) throws ServiceException;

}
