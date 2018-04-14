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
import es.unex.giiis.tfg.service.CallService;
import es.unex.giiis.tfg.sincro.CallSINCRO;

public class CallListener extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final Logger LOGGER = Logger.getLogger("CallListener");

	@Autowired
	private CallService service;

	private Gson gson;

	private Integer cmd;

	private String jsonCall;

	public CallListener() {
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
		this.LOGGER.info("doPost -> CallListener");

		this.cmd = Integer.parseInt(request.getParameter(Protocol.CMD));
		this.jsonCall = request.getParameter(Protocol.KEY1);
		
		if (this.cmd != null && this.jsonCall != null) {
			try {
				switch (this.cmd) {
				case Protocol.LISTENER_CALL_RECORD:
					this.LOGGER.info("Recibiendo llamada grabada...");
					Type jsonRecordCall = new TypeToken<CallSINCRO>() {
					}.getType();
					CallSINCRO callSincro = gson.fromJson(this.jsonCall, jsonRecordCall);
					this.service.convertStringToAudioAndSave(callSincro);
					break;
				case Protocol.LISTENER_CALL:
					this.LOGGER.info("Recibiendo historial de llamadas...");
					Type jsonCall = new TypeToken<ArrayList<CallSINCRO>>() {
					}.getType();
					ArrayList<CallSINCRO> listCall = gson.fromJson(this.jsonCall, jsonCall);
					this.service.updateData(CallSINCRO.parseSincroListCall(listCall), Protocol.TYPE_CALLDTO);
					break;
				default:
					this.LOGGER.info("doPOST -> CallListener: Recibiendo cmd fuera de rango: " + cmd);
					break;

				}
			} catch (ServiceException e) {
				this.LOGGER.info("doPOST -> CallListener, EXCEPCION: " + e.toString());
			}
		} else
			this.LOGGER.info("doPOST -> CallListener: Recibiendo parametros nulos...");
	}

}
