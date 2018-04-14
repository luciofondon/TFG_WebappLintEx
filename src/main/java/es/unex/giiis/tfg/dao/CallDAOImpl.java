package es.unex.giiis.tfg.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.model.CallDTO;
import es.unex.giiis.tfg.model.CallDTO.TypeCall;
import es.unex.giiis.tfg.protocol.Protocol;

@Repository
public class CallDAOImpl extends BaseDAOImpl<CallDTO> implements CallDAO {

	@SuppressWarnings("unchecked")
	public List<CallDTO> findAllCallByImeiAndTypeCall(String imei, TypeCall typeCall) throws DaoException {
		Query query;
		if (typeCall == TypeCall.RECORD) {
			query = getSessionFactory().getCurrentSession()
					.createQuery("FROM es.unex.giiis.tfg.model." + Protocol.TYPE_CALLDTO
							+ " WHERE IMEI = :IMEI AND TYPE_CALL = :TYPE_CALL ORDER BY DATE_ANDROID DESC");

			query.setParameter("TYPE_CALL", TypeCall.parseTypeCallToInteger(typeCall));

		} else {
			query = getSessionFactory().getCurrentSession()
					.createQuery("FROM es.unex.giiis.tfg.model." + Protocol.TYPE_CALLDTO
							+ " WHERE IMEI = :IMEI AND TYPE_CALL <> :TYPE_CALL ORDER BY DATE_ANDROID DESC");
			query.setParameter("TYPE_CALL", 3);

		}

		query.setParameter("IMEI", imei);
		List<CallDTO> list = (List<CallDTO>) query.list();
		return list;
	}

	public void deleteAllCallByImeiAndTypeCall(String imei, TypeCall typeCall) throws DaoException {
		List<CallDTO> entityList = findAllCallByImeiAndTypeCall(imei, typeCall);
		for (CallDTO entity : entityList) {
			delete(entity);
		}
	}

}
