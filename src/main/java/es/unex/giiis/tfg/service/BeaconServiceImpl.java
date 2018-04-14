package es.unex.giiis.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.unex.giiis.tfg.dao.BeaconDAO;
import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.BeaconDTO;
import es.unex.giiis.tfg.model.BeaconDTO.TypeBeacon;

@Service
@Transactional(readOnly = true)
public class BeaconServiceImpl extends BaseServiceImpl<BeaconDTO> implements BeaconService {

	@Autowired
	BeaconDAO dao;

	public BeaconDAO getDAO() {
		return this.dao;
	}

	public List<BeaconDTO> findAllBeaconByImeiAndTypeBeacon(String imei, TypeBeacon typeBeacon)
			throws ServiceException {
		try {
			return this.dao.findAllBeaconByImeiAndTypeBeacon(imei, typeBeacon);
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}
	}

}
