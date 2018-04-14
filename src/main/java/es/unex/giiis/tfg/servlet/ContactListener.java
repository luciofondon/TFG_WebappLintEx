package es.unex.giiis.tfg.servlet;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.ContactService;
import es.unex.giiis.tfg.sincro.ContactSINCRO;

public class ContactListener extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("ContactListener");

	@Autowired
	private ContactService service;

	private Gson gson;

	private Integer cmd;

	private String jsonContact;

	public ContactListener() {
		super();
		this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.LOGGER.info("doPOST -> ContactListener");

		this.cmd = Integer.parseInt(request.getParameter(Protocol.CMD));
		this.jsonContact = request.getParameter(Protocol.KEY1);

		if (this.cmd != null && this.jsonContact != null) {
			try {
				switch (this.cmd) {
				case Protocol.LISTENER_CONTACT:
					this.LOGGER.info("Recibiendo contactos...");
					Type typeJsonContact = new TypeToken<ArrayList<ContactSINCRO>>() {
					}.getType();
					ArrayList<ContactSINCRO> listContact = gson.fromJson(this.jsonContact, typeJsonContact);

					this.service.updateData(ContactSINCRO.parseSincroListContact(listContact),
							Protocol.TYPE_CONTACTDTO);

					break;
				default:
					this.LOGGER.info("doPOST -> ContactListener: Recibiendo cmd fuera de rango: " + cmd);
					break;
				}
			} catch (ServiceException e) {
				this.LOGGER.info("doPOST -> ContactListener, EXCEPCION: " + e.toString());
			}
		} else
			this.LOGGER.info("doPOST -> ContactListener: Recibiendo parametros nulos...");

	}

}
