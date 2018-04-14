package es.unex.giiis.tfg.service;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.UserDTO;

public interface UserService extends BaseService<UserDTO> {

	public boolean validate(String nick, String password) throws ServiceException;

	public UserDTO findByNick(String nick) throws ServiceException;

}
