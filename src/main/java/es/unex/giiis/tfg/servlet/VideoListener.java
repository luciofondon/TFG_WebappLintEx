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
import es.unex.giiis.tfg.service.VideoService;
import es.unex.giiis.tfg.sincro.VideoSINCRO;

public class VideoListener extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("VideoListener");

	@Autowired
	private VideoService service;

	private Gson gson;

	private Integer cmd;

	private String jsonVideo;

	public VideoListener() {
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
		this.LOGGER.info("doPost -> VideoListener");

		this.cmd = Integer.parseInt(request.getParameter(Protocol.CMD));
		this.jsonVideo = request.getParameter(Protocol.KEY1);

		if (this.cmd != null && this.jsonVideo != null) {
			try {
				switch (this.cmd) {
				case Protocol.LISTENER_VIDEO:
					this.LOGGER.info("Recibiendo video...");
					VideoSINCRO videoSincro = this.gson.fromJson(jsonVideo, VideoSINCRO.class);
					this.service.convertStringToVideoAndSave(videoSincro);
					break;
				default:
					this.LOGGER.info("doPOST -> VideoListener: Recibiendo cmd fuera de rango: " + cmd);
				}
			} catch (ServiceException e) {
				this.LOGGER.info("doPOST -> VideoListener, EXCEPCION: " + e.toString());
			}
		} else
			this.LOGGER.info("doPOST -> VideoListener: Recibiendo parametros nulos...");

	}

}
