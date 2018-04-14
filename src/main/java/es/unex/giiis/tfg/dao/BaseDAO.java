package es.unex.giiis.tfg.dao;

import java.util.List;

import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.model.BaseDTO;

public interface BaseDAO<DTO extends BaseDTO> {

	public void add(DTO dto, String type) throws DaoException;

	public void delete(DTO dto) throws DaoException;

	public void deleteAll(String type) throws DaoException;
	
	public void deleteAllByImei(String imei, String type) throws DaoException;

	public List<DTO> findAll(String type) throws DaoException;

	public DTO findById(int id) throws DaoException;

	public List<DTO> findAllByImei(String imei, String type) throws DaoException;

	public void update(DTO cto) throws DaoException;

}
