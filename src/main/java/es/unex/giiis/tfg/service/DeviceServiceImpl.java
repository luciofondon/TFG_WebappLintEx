package es.unex.giiis.tfg.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.unex.giiis.tfg.dao.DeviceDAO;
import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.DeviceDTO;
import es.unex.giiis.tfg.protocol.Protocol;

@Service
@Transactional(readOnly = true)
public class DeviceServiceImpl extends BaseServiceImpl<DeviceDTO> implements DeviceService {

	@Autowired
	DeviceDAO dao;

	public DeviceDAO getDAO() {
		return this.dao;
	}

	// Metodo encargado de actualizar o insertar el dispositivo (exista o no),
	// quedando sin actualizar datos que no se quieren que se actualicen
	@Transactional(readOnly = false)
	public void addOrUpdateDevice(DeviceDTO device) throws ServiceException {
		// Indiciamos la fecha de sincronizacion
		device.setLastConnection(Calendar.getInstance().getTime());
		List<DeviceDTO> listDevice;
		try {
			listDevice = this.dao.findAllByImei(device.getImei(), Protocol.TYPE_DEVICEDTO);
			if (listDevice != null && !listDevice.isEmpty()) {
				DeviceDTO deviceOld = listDevice.get(0);
				device.setContLatImage(deviceOld.getContLatImage());
				device.setAudioRecord(deviceOld.isAudioRecord());
				device.setVideoRecord(deviceOld.isVideoRecord());
				device.setStartBeaconPassive(deviceOld.isStartBeaconPassive());
				device.setStartBeaconNetwork(deviceOld.isStartBeaconNetwork());
				device.setStartBeaconGps(deviceOld.isStartBeaconGps());

				device.setLastSynchronization(Calendar.getInstance().getTime());

				this.dao.delete(deviceOld);

			}
			device.setLastSynchronization(Calendar.getInstance().getTime());
			this.dao.add(device, Protocol.TYPE_DEVICEDTO);

		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}
	}

	// Metodo encargado de hacer que se comuniquen los dispositivos para ver si
	// tienen conexion activa
	@Transactional(readOnly = false)
	public void updateConnection() throws ServiceException {
		try {
			List<DeviceDTO> list = this.dao.findAll(Protocol.TYPE_DEVICEDTO);
			this.dao.deleteAll(Protocol.TYPE_DEVICEDTO);
			for (DeviceDTO device : list) {
				// Cambiamos el estado de la conexion a falso,
				device.setDeviceState(false);
				device.setLastSynchronization(Calendar.getInstance().getTime());
				// Insertamos nuevamente el dispositivo para no perderlo
				this.dao.add(device, Protocol.TYPE_DEVICEDTO);
				// Mandamos la orden para que sincronice
				this.sendCmd(Protocol.SINCRONIZE, device.getIp());
			}
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}

	}
}
