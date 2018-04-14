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
import es.unex.giiis.tfg.model.ResourceDTO;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.ResourceService;
import es.unex.giiis.tfg.sincro.ResourceSINCRO;

public class ResourceListener extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("ResourceListener");

	@Autowired
	private ResourceService service;

	private Gson gson;

	private Integer cmd;

	private String jsonResource;

	public ResourceListener() {
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
		this.LOGGER.info("doPOST -> ResourceListener");

		this.cmd = Integer.parseInt(request.getParameter(Protocol.CMD));
		this.jsonResource = request.getParameter(Protocol.KEY1);
		
		if (this.jsonResource != null && this.cmd != null) {
			try {
				switch (this.cmd) {
				case Protocol.LISTENER_TAKE_RESOURCE:
					this.LOGGER.info("Recibiendo fichero...");
					ResourceSINCRO resourceSincro = gson.fromJson(jsonResource, ResourceSINCRO.class);
					this.service.convertStringToResourceAndSave(resourceSincro);
					break;

				case Protocol.LISTENER_READ_RESOURCE:
					this.LOGGER.info("Recibiendo paths de Android...");
					Type typeJson = new TypeToken<ArrayList<ResourceSINCRO>>() {
					}.getType();
					ArrayList<ResourceSINCRO> listResourceSincro = gson.fromJson(jsonResource, typeJson);
					this.service.updateData(ResourceSINCRO.parseSincroListResource(listResourceSincro),
							Protocol.TYPE_RESOURCEDTO);
					break;
				default:
					this.LOGGER.info("doPOST -> ResourceListener: Recibiendo cmd fuera de rango: " + cmd);
					break;
				}
			} catch (ServiceException e) {
				this.LOGGER.info("doPOST -> ResourceListener, EXCEPCION: " + e.toString());
			}
		} else
			this.LOGGER.info("doPOST -> ResourceListener: Recibiendo parametros nulos.");

	}

}
