package es.unex.giiis.tfg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.unex.giiis.tfg.dao.BrowserDAO;
import es.unex.giiis.tfg.model.BrowserDTO;

@Service
@Transactional(readOnly = true)
public class BrowserServiceImpl extends BaseServiceImpl<BrowserDTO> implements BrowserService {

	@Autowired
	BrowserDAO dao;

	public BrowserDAO getDAO() {
		return this.dao;
	}

}