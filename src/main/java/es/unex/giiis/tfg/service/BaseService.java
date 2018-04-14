package es.unex.giiis.tfg.service;

import java.util.List;

import es.unex.giiis.tfg.dao.BaseDAO;
import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.BaseDTO;

public interface BaseService<DTO extends BaseDTO> {

	public BaseDAO<DTO> getDAO();

	public void add(DTO item, String type) throws ServiceException;

	public List<DTO> findAllByImei(String imei, String type) throws ServiceException;

	public void updateData(List<DTO> list, String type) throws ServiceException;

	public void sendCmd(int protocolo, String ip) throws ServiceException;

	public void update(DTO item) throws ServiceException;

	public void delete(DTO item) throws ServiceException;

	public void deleteAll(String type) throws ServiceException;

	public void deleteAllByImei(String imei, String type) throws ServiceException;

	public List<DTO> findAll(String type) throws ServiceException;

}
