package es.unex.giiis.tfg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.unex.giiis.tfg.dao.UserDAO;
import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.UserDTO;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl extends BaseServiceImpl<UserDTO> implements UserService {

	@Autowired
	UserDAO dao;

	public UserDAO getDAO() {
		return this.dao;
	}

	public boolean validate(String nick, String password) throws ServiceException {
		try {
			return this.dao.validate(nick, password);
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}
	}

	public UserDTO findByNick(String nick) throws ServiceException {
		try {
			return this.dao.findByNick(nick);
		} catch (DaoException e) {
			throw new ServiceException(e.toString());
		}
	}

}
