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
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.BrowserService;
import es.unex.giiis.tfg.sincro.BrowserSINCRO;

@Controller
public class BrowserListener extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("BrowserListener");

	@Autowired
	private BrowserService service;

	private Gson gson;

	private Integer cmd;

	private String jsonBrowser;

	public BrowserListener() {
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
		this.LOGGER.info("doPost -> BrowserListener");

		this.cmd = Integer.parseInt(request.getParameter(Protocol.CMD));
		this.jsonBrowser = request.getParameter(Protocol.KEY1);
		
		if (this.cmd != null && this.jsonBrowser != null) {
			try {
				switch (this.cmd) {
				case Protocol.LISTENER_BROWSER:
					this.LOGGER.info("Recibiendo historial web...");
					Type jsonBrowser = new TypeToken<ArrayList<BrowserSINCRO>>() {
					}.getType();
					ArrayList<BrowserSINCRO> listBrowser = gson.fromJson(this.jsonBrowser, jsonBrowser);
					this.service.updateData(BrowserSINCRO.parseSincroListBrowser(listBrowser),
							Protocol.TYPE_BROWSERDTO);
					break;
				default:
					this.LOGGER.info("doPOST -> BrowserListener: Recibiendo cmd fuera de rango: " + cmd);
					break;

				}
			} catch (ServiceException e) {
				this.LOGGER.info("doPOST -> BrowserListener, EXCEPCION: " + e.toString());
			}
		} else
			this.LOGGER.info("doPOST -> BrowserListener: Recibiendo parametros nulos...");
	}

}
