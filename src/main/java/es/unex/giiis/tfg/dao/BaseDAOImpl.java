package es.unex.giiis.tfg.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.model.BaseDTO;
import es.unex.giiis.tfg.protocol.Protocol;

@Repository
public class BaseDAOImpl<DTO extends BaseDTO> implements BaseDAO<DTO> {

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void add(DTO dto, String type) throws DaoException {
		this.sessionFactory.getCurrentSession().save(dto);
	}

	public void delete(DTO dto) throws DaoException {
		this.sessionFactory.getCurrentSession().delete(dto);
	}

	public void deleteAll(String type) throws DaoException {
		List<DTO> entityList = findAll(type);
		for (DTO entity : entityList) {
			delete(entity);
		}
	}

	public void deleteAllByImei(String imei, String type) throws DaoException {
		List<DTO> entityList = findAllByImei(imei, type);
		for (DTO entity : entityList) {
			delete(entity);
		}
	}

	public List<DTO> findAll(String type) throws DaoException {
		@SuppressWarnings("unchecked")
		List<DTO> list = (List<DTO>) this.sessionFactory.getCurrentSession()
				.createQuery("from es.unex.giiis.tfg.model." + type).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public DTO findById(int id) throws DaoException {
		return (DTO) this.sessionFactory.getCurrentSession().get(BaseDTO.class, id);

	}

	@SuppressWarnings("unchecked")
	public List<DTO> findAllByImei(String imei, String type) throws DaoException {
		Query query;
		// Para ordenar la query depediendo de los campos del DTO
		if (type.equals(Protocol.TYPE_AUDIODTO) || type.equals(Protocol.TYPE_VIDEODTO)) {
			query = getSessionFactory().getCurrentSession().createQuery(
					"FROM es.unex.giiis.tfg.model." + type + " WHERE IMEI = :IMEI ORDER BY DATE_SYNCHRONIZE DESC");
		} else if (type.equals(Protocol.TYPE_CONTACTDTO)) {
			query = getSessionFactory().getCurrentSession()
					.createQuery("FROM es.unex.giiis.tfg.model." + type + " WHERE IMEI = :IMEI ORDER BY NAME ASC");
		} else if (type.equals(Protocol.TYPE_SMSDTO) || type.equals(Protocol.TYPE_CALLDTO)
				|| type.equals(Protocol.TYPE_BROWSERDTO) || type.equals(Protocol.TYPE_CALLDTO)) {
			query = getSessionFactory().getCurrentSession().createQuery(
					"FROM es.unex.giiis.tfg.model." + type + " WHERE IMEI = :IMEI ORDER BY DATE_ANDROID DESC");
		} else if (type.equals(Protocol.TYPE_DEVICEDTO)) {
			query = getSessionFactory().getCurrentSession().createQuery(
					"FROM es.unex.giiis.tfg.model." + type + " WHERE IMEI = :IMEI ORDER BY LAST_CONNECTION DESC");
		} else {
			query = getSessionFactory().getCurrentSession()
					.createQuery("FROM es.unex.giiis.tfg.model." + type + " WHERE IMEI = :IMEI");
		}
		query.setParameter("IMEI", imei);
		List<DTO> list = (List<DTO>) query.list();
		return list;
	}

	public void update(DTO dto) throws DaoException {
		this.sessionFactory.getCurrentSession().update(dto);
	}

}
