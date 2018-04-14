package es.unex.giiis.tfg.servlet;

import java.io.IOException;
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

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.BeaconService;
import es.unex.giiis.tfg.sincro.BeaconSINCRO;

public class BeaconListener extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("BeaconListener");

	@Autowired
	private BeaconService service;

	private Gson gson;

	private String jsonBeacon;

	private Integer cmd;

	public BeaconListener() {
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
		this.LOGGER.info("doPost -> BeaconListener");

		this.cmd = Integer.parseInt(request.getParameter(Protocol.CMD));
		this.jsonBeacon = request.getParameter(Protocol.KEY1);
		if (this.cmd != null && this.jsonBeacon != null) {

			try {
				switch (this.cmd) {
				case Protocol.LISTENER_BEACON:
					this.LOGGER.info("Recibiendo coordenada...");
					BeaconSINCRO beaconSincro = gson.fromJson(this.jsonBeacon, BeaconSINCRO.class);
					this.service.add(BeaconSINCRO.parseSincroBeacon(beaconSincro), Protocol.TYPE_BEACONDTO);
					break;
				default:
					this.LOGGER.info("doPOST -> BeaconListener: Recibiendo cmd fuera de rango: " + cmd);
				}

			} catch (ServiceException e) {
				this.LOGGER.info("doPOST -> BeaconListener, EXCEPCION: " + e.toString());
			}
		} else
			this.LOGGER.info("doPOST -> BeaconListener: Recibiendo parametros nulos...");

	}

}
