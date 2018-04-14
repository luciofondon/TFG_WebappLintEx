package es.unex.giiis.tfg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.unex.giiis.tfg.dao.SmsDAO;
import es.unex.giiis.tfg.model.SmsDTO;

@Service
@Transactional(readOnly = true)
public class SmsServiceImpl extends BaseServiceImpl<SmsDTO> implements SmsService {

	@Autowired
	SmsDAO dao;

	public SmsDAO getDAO() {
		return this.dao;
	}

}
