package es.unex.giiis.tfg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.unex.giiis.tfg.dao.ContactDAO;
import es.unex.giiis.tfg.model.ContactDTO;

@Service
@Transactional(readOnly = true)
public class ContactServiceImpl extends BaseServiceImpl<ContactDTO> implements ContactService {

	@Autowired
	ContactDAO dao;

	public ContactDAO getDAO() {
		return this.dao;
	}

}
