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
import es.unex.giiis.tfg.service.ImageService;
import es.unex.giiis.tfg.sincro.ImageSINCRO;

public class ImageListener extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("ImageListener");

	@Autowired
	private ImageService service;

	private Gson gson;

	private Integer cmd;

	private String jsonImage;

	public ImageListener() {
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
		this.LOGGER.info("doPost -> ImageListener");
		
		this.cmd = Integer.parseInt(request.getParameter(Protocol.CMD));
		this.jsonImage = request.getParameter(Protocol.KEY1);

		if (this.cmd != null && this.jsonImage != null) {
			try {
				switch (this.cmd) {
				case Protocol.LISTENER_IMAGE_GALLERY:
				case Protocol.LISTENER_IMAGE_CAMERA:
					this.LOGGER.info("Recibiendo imagen...");
					ImageSINCRO imageSincro = gson.fromJson(this.jsonImage, ImageSINCRO.class);
					this.service.convertStringToImageAndSave(imageSincro);
					break;
				default:
					this.LOGGER.info("doPOST -> ImageListener: Recibiendo cmd fuera de rango: " + cmd);
					break;
				}
			} catch (ServiceException e) {
				this.LOGGER.info("doPOST -> ImageListener, EXCEPCION: " + e.toString());
			}
		} else
			this.LOGGER.info("doPOST -> ImageListener: Recibiendo parametros nulos...");
	}

}
