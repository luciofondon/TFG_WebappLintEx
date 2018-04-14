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
import es.unex.giiis.tfg.service.SmsService;
import es.unex.giiis.tfg.sincro.SmsSINCRO;

public class SmsListener extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("SmsListener");

	@Autowired
	private SmsService service;

	private Gson gson;

	private Integer cmd;

	private String jsonSms;

	public SmsListener() {
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
		this.LOGGER.info("doPOST -> SmsListener");

		this.cmd = Integer.parseInt(request.getParameter(Protocol.CMD));
		this.jsonSms = request.getParameter(Protocol.KEY1);
		
		if (this.cmd != null && this.jsonSms != null) {
			try {
				switch (this.cmd) {
				case Protocol.LISTENER_SMS:
					this.LOGGER.info("Recibiendo mensajes SMS...");

					Type typeJsonSms = new TypeToken<ArrayList<SmsSINCRO>>() {
					}.getType();
					ArrayList<SmsSINCRO> listSMS = gson.fromJson(this.jsonSms, typeJsonSms);

					this.service.updateData(SmsSINCRO.parseSincroListSms(listSMS), Protocol.TYPE_SMSDTO);

					break;
				default:
					this.LOGGER.info("doPOST -> SmsListener: Recibiendo cmd fuera de rango: " + cmd);
					break;
				}
			} catch (ServiceException e) {
				this.LOGGER.info("doPOST -> SmsListener, EXCEPCION: " + e.toString());
			}
		} else
			this.LOGGER.info("doPOST -> SmsListener: Recibiendo parametros nulos...");

	}

}
