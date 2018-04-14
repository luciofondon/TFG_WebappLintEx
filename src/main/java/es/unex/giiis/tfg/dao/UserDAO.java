package es.unex.giiis.tfg.dao;

import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.model.UserDTO;

public interface UserDAO extends BaseDAO<UserDTO> {

	public boolean validate(String nick, String password) throws DaoException;

	public UserDTO findByNick(String nick) throws DaoException;

}
