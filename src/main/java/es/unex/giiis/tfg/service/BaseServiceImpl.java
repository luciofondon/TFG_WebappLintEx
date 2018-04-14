package es.unex.giiis.tfg.service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.unex.giiis.tfg.dao.BaseDAO;
import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.BaseDTO;
import es.unex.giiis.tfg.protocol.Protocol;

@Service
@Transactional(readOnly = true)
public abstract class BaseServiceImpl<DTO extends BaseDTO> implements BaseService<DTO> {

	@Transactional(readOnly = false)
	public void add(DTO item, String type) throws ServiceException {
		BaseDAO<DTO> dao = this.getDAO();
		try {
			dao.add(item, type);
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}
	}

	@Transactional(readOnly = false)
	public void update(DTO item) throws ServiceException {
		BaseDAO<DTO> dao = this.getDAO();
		try {
			dao.update(item);
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}
	}

	@Transactional(readOnly = false)
	public void delete(DTO item) throws ServiceException {
		try {
			BaseDAO<DTO> dao = this.getDAO();
			dao.delete(item);
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}
	}

	public List<DTO> findAll(String type) throws ServiceException {
		try {
			BaseDAO<DTO> dao = this.getDAO();
			return dao.findAll(type);
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}
	}

	public List<DTO> findAllByImei(String imei, String type) throws ServiceException {
		BaseDAO<DTO> dao = this.getDAO();
		try {
			return dao.findAllByImei(imei, type);
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}
	}

	public void sendCmd(int protocolo, String ip) {
		try {
			DatagramSocket ds = new DatagramSocket();
			byte[] cmd = (protocolo + "").getBytes();
			DatagramPacket dp = new DatagramPacket(cmd, cmd.length, InetAddress.getByName(ip), Protocol.PUERTO);
			ds.send(dp);
			ds.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transactional(readOnly = false)
	public void deleteAll(String type) throws ServiceException {
		BaseDAO<DTO> dao = this.getDAO();
		try {
			dao.deleteAll(type);
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}

	}

	@Transactional(readOnly = false)
	public void deleteAllByImei(String imei, String type) throws ServiceException {
		BaseDAO<DTO> dao = this.getDAO();
		try {
			dao.deleteAllByImei(imei, type);
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}
	}

	@Transactional(readOnly = false)
	public void updateData(List<DTO> list, String type) throws ServiceException {
		BaseDAO<DTO> dao = this.getDAO();
		try {

			dao.deleteAll(type);
			for (DTO dto : list) {
				add(dto, type);
			}
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}
	}

}
