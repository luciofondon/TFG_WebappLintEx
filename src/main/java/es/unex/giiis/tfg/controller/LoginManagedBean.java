package es.unex.giiis.tfg.controller;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.unex.giiis.tfg.exception.ServiceException;
import es.unex.giiis.tfg.model.UserDTO;
import es.unex.giiis.tfg.protocol.Protocol;
import es.unex.giiis.tfg.service.UserService;

@Controller
@ManagedBean
@SessionScoped
public class LoginManagedBean extends BaseManagedBean implements Serializable {
	final Logger LOGGER = Logger.getLogger("LoginManagedBean");

	private static final long serialVersionUID = 1L;

	private String password;

	private String nick;

	private boolean logged;

	@Autowired
	UserService service;

	// Usuario logeado
	private UserDTO user;

	// Tema que se esta visualizando
	private String theme;

	// Posibles temas a elegir
	private Map<String, String> themes;

	@PostConstruct
	public void init() {
		this.logged = false;
		// Valor por defecto
		this.theme = "dark-hive";
		this.themes = new TreeMap<String, String>();
		this.themes.put("Afterdark", "afterdark");
		this.themes.put("Afternoon", "afternoon");
		this.themes.put("Afterwork", "afterwork");
		this.themes.put("Aristo", "aristo");
		this.themes.put("Black-Tie", "black-tie");
		this.themes.put("Blitzer", "blitzer");
		this.themes.put("Bluesky", "bluesky");
		this.themes.put("Bootstrap", "bootstrap");
		this.themes.put("Casablanca", "casablanca");
		this.themes.put("Cupertino", "cupertino");
		this.themes.put("Cruze", "cruze");
		this.themes.put("Dark-Hive", "dark-hive");
		this.themes.put("Dot-Luv", "dot-luv");
		this.themes.put("Eggplant", "eggplant");
		this.themes.put("Excite-Bike", "excite-bike");
		this.themes.put("Flick", "flick");
		this.themes.put("Glass-X", "glass-x");
		this.themes.put("Home", "home");
		this.themes.put("Hot-Sneaks", "hot-sneaks");
		this.themes.put("Humanity", "humanity");
		this.themes.put("Le-Frog", "le-frog");
		this.themes.put("Midnight", "midnight");
		this.themes.put("Mint-Choc", "mint-choc");
		this.themes.put("Overcast", "overcast");
		this.themes.put("Pepper-Grinder", "pepper-grinder");
		this.themes.put("Redmond", "redmond");
		this.themes.put("Rocket", "rocket");
		this.themes.put("Sam", "sam");
		this.themes.put("Smoothness", "smoothness");
		this.themes.put("South-Street", "south-street");
		this.themes.put("Start", "start");
		this.themes.put("Sunny", "sunny");
		this.themes.put("Swanky-Purse", "swanky-purse");
		this.themes.put("Trontastic", "trontastic");
		this.themes.put("UI-Darkness", "ui-darkness");
		this.themes.put("UI-Lightness", "ui-lightness");
		this.themes.put("Vader", "vader");

	}

	public String getIP() {
		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			this.LOGGER.info("getIP -> LoginManagedBean, EXCEPCION: Obtener direccion IP...");
		}
		return ip.getHostAddress();

	}

	public void doLogin() {
		this.LOGGER.info("Intentado acceder con Usuario: " + nick + " y Contrasenia: " + password);
		RequestContext context = RequestContext.getCurrentInstance();
		try {
			if (this.service.validate(nick, password)) {
				this.logged = true;
				this.user = this.service.findByNick(nick);
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(Protocol.USER_NAME_LOGIN,
						nick);
				this.mostrarMensajeInformacion("Bienvenid@", nick + ".");
			} else {
				logged = false;
				this.mostrarMensajeAlerta("ERROR. ", "Credenciales no válidas.");
			}
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("logeado", logged);
			context.addCallbackParam("estaLogeado", logged);
			if (logged)
				context.addCallbackParam("view", "secured/Device.xhtml");

		} catch (ServiceException e) {
			this.LOGGER.info("doLogin -> LoginManagedBean, EXCEPCION: " + e.toString());
		}

	}

	public void save() {
		if (this.user != null && this.user.getName() != "" && this.user.getLastName() != "" && this.user.getNick() != ""
				&& this.user.getPassword() != "") {
			try {

				this.service.update(this.user);
				Thread.sleep(Protocol.TIME_SLEEP);
			} catch (ServiceException e) {
				this.LOGGER.info("save -> LoginManagedBean, EXCEPCION: " + e.toString());
			} catch (InterruptedException e) {
				this.LOGGER.info("save -> LoginManagedBean, EXCEPCION: Sleep durante la sincronizacion...");
			}
		} else
			this.mostrarMensajeAlerta("Campos no válidos.", "");
	}

	public void doLogout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();
		this.logged = false;
		this.user = null;
	}

	public void reset() {
		this.password = "";
		this.nick = "";
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String userName) {
		this.nick = userName;
	}

	// http://jmagm.blogspot.com.es/2013/02/login-y-control-de-acceso-basico-con.html
	public void logout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();
		this.logged = false;
	}

	public boolean isLoggedIn() {
		return this.logged;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public String getTheme() {
		if (this.user != null && this.logged)
			return this.user.getTheme();
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public Map<String, String> getThemes() {
		return this.themes;
	}

	public void setThemes(Map<String, String> themes) {
		this.themes = themes;
	}

}
