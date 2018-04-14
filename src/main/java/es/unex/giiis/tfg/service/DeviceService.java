package es.unex.giiis.tfg.service;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.DeviceDTO;

public interface DeviceService extends BaseService<DeviceDTO> {

	public void addOrUpdateDevice(DeviceDTO device) throws ServiceException;

	public void updateConnection() throws ServiceException;

}
