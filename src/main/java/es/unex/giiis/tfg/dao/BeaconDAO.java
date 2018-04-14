package es.unex.giiis.tfg.dao;

import java.util.List;

import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.model.BeaconDTO;
import es.unex.giiis.tfg.model.BeaconDTO.TypeBeacon;

public interface BeaconDAO extends BaseDAO<BeaconDTO> {

	public List<BeaconDTO> findAllBeaconByImeiAndTypeBeacon(String imei, TypeBeacon typeBeacon) throws DaoException;

}
