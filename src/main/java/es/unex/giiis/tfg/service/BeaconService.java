package es.unex.giiis.tfg.service;

import java.util.List;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.BeaconDTO;
import es.unex.giiis.tfg.model.BeaconDTO.TypeBeacon;

public interface BeaconService extends BaseService<BeaconDTO> {

	public List<BeaconDTO> findAllBeaconByImeiAndTypeBeacon(String imei, TypeBeacon typeBeacon)
			throws ServiceException;

}
