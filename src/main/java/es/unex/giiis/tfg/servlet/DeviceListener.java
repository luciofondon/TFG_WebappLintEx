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
import es.unex.giiis.tfg.model.DeviceDTO;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.DeviceService;

public class DeviceListener extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("DeviceListener");

	@Autowired
	private DeviceService deviceService;

	private Gson gson;

	private Integer cmd;

	private String jsonDevice;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}

	public DeviceListener() {
		super();
		this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.LOGGER.info("doPOST -> DeviceListener");

		this.cmd = Integer.parseInt(request.getParameter(Protocol.CMD));
		this.jsonDevice = request.getParameter(Protocol.KEY1);

		if (this.cmd != null && this.jsonDevice != null) {
			try {
				switch (this.cmd) {
				case Protocol.LISTENER_SINCRONIZE:
					this.LOGGER.info("Sincronizando dispositivo...");
					DeviceDTO device = gson.fromJson(jsonDevice, DeviceDTO.class);
					this.deviceService.addOrUpdateDevice(device);
					break;

				default:
					this.LOGGER.info("doPOST -> DeviceListener: Recibiendo cmd fuera de rango: " + cmd);
				}
			} catch (ServiceException e) {
				this.LOGGER.info("doPOST -> DeviceListener, EXCEPCION: " + e.toString());
			}
		} else
			this.LOGGER.info("doPOST -> DeviceListener: Recibiendo parametros nulos...");

	}

}
