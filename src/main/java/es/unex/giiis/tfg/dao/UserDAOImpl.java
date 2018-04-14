package es.unex.giiis.tfg.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.unex.giiis.tfg.exception.DaoException;
import es.unex.giiis.tfg.model.UserDTO;
import es.unex.giiis.tfg.protocol.Protocol;

@Repository
public class UserDAOImpl extends BaseDAOImpl<UserDTO> implements UserDAO {

	@SuppressWarnings("unchecked")
	public boolean validate(String nick, String password) throws DaoException {
		if (nick != null && nick != "" && password != null && password != "") {
			Query query = getSessionFactory().getCurrentSession().createQuery("FROM es.unex.giiis.tfg.model."
					+ Protocol.TYPE_USERDTO + " where nick = :NICK AND password = :PASSWORD");
			query.setParameter("NICK", nick);
			query.setParameter("PASSWORD", password);

			List<UserDTO> list = (List<UserDTO>) query.list();
			if (list != null && list.size() > 0) {
				System.out.println(list);
				return true;

			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public UserDTO findByNick(String nick) throws DaoException {
		UserDTO user = null;
		if (nick != null && nick != "") {
			Query query = getSessionFactory().getCurrentSession()
					.createQuery("FROM es.unex.giiis.tfg.model." + Protocol.TYPE_USERDTO + " where nick = :NICK");
			query.setParameter("NICK", nick);

			List<UserDTO> list = (List<UserDTO>) query.list();
			if (list != null && list.size() > 0) {
				user = list.get(0);

			}
		}
		return user;
	}

}
