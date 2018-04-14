package es.unex.giiis.tfg.dao;

import java.util.List;

import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.model.CallDTO;
import es.unex.giiis.tfg.model.CallDTO.TypeCall;

public interface CallDAO extends BaseDAO<CallDTO> {

	public List<CallDTO> findAllCallByImeiAndTypeCall(String imei, TypeCall typeCall) throws DaoException;

	public void deleteAllCallByImeiAndTypeCall(String imei, TypeCall typeCall) throws DaoException;

}
