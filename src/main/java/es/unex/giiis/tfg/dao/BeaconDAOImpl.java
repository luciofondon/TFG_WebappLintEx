package es.unex.giiis.tfg.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.model.BeaconDTO;
import es.unex.giiis.tfg.model.BeaconDTO.TypeBeacon;
import es.unex.giiis.tfg.protocol.Protocol;

@Repository
public class BeaconDAOImpl extends BaseDAOImpl<BeaconDTO> implements BeaconDAO {

	@SuppressWarnings("unchecked")
	public List<BeaconDTO> findAllBeaconByImeiAndTypeBeacon(String imei, TypeBeacon typeBeacon) throws DaoException {
		Query query = getSessionFactory().getCurrentSession().createQuery("FROM es.unex.giiis.tfg.model."
				+ Protocol.TYPE_BEACONDTO + " WHERE IMEI = :IMEI AND TYPE_BEACON = :TYPE_BEACON ORDER BY DATE_SYNCHRONIZE DESC");
		query.setParameter("IMEI", imei);
		query.setParameter("TYPE_BEACON", TypeBeacon.parseTypeBeaconToInteger(typeBeacon));
		List<BeaconDTO> list = (List<BeaconDTO>) query.list();
		return list;

	}

}
