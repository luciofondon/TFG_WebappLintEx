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
import es.unex.giiis.tfg.service.AudioService;
import es.unex.giiis.tfg.sincro.AudioSINCRO;

public class AudioListener extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("AudioListener");

	@Autowired
	private AudioService service;

	private Gson gson;

	private Integer cmd;

	private String jsonAudio;

	public AudioListener() {
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
		this.LOGGER.info("doPost -> AudioListener");
		
		this.cmd = Integer.parseInt(request.getParameter(Protocol.CMD));
		this.jsonAudio = request.getParameter(Protocol.KEY1);

		if (this.cmd != null && this.jsonAudio != null) {
			try {
				switch (this.cmd) {
				case Protocol.LISTENER_AUDIO:
					this.LOGGER.info("Recibiendo audio...");
					AudioSINCRO audioSincro = gson.fromJson(jsonAudio, AudioSINCRO.class);
					this.service.convertStringToAudioAndSave(audioSincro);
					break;
				default:
					this.LOGGER.info("doPOST -> AudioListener: Recibiendo cmd fuera de rango: " + cmd);
					break;
				}
			} catch (ServiceException e) {
				this.LOGGER.info("doPOST -> AudioListener, EXCEPCION: " + e.toString());
			}
		} else
			this.LOGGER.info("doPOST -> AudioListener: Recibiendo parametros nulos...");

	}

}
