package es.unex.giiis.tfg.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.unex.giiis.tfg.protocol.Protocol;

@WebFilter(urlPatterns = { "/secured/*" })
public class LoginFilter implements Filter {

	public LoginFilter() {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = ((HttpServletRequest) request).getSession(false);

		boolean loggedIn = (session != null) && (session.getAttribute(Protocol.USER_NAME_LOGIN) != null);

		// URL que esta requiriendo el cliente (en minuscula)
		String urlStr = req.getRequestURL().toString().toLowerCase();
	//	boolean noProteger = noProteger(urlStr);
		// System.out.println(urlStr + " - desprotegido=[" + noProteger + "]");

		// Si no requiere protección continuo normalmente.
		if (noProteger(urlStr)) {
			chain.doFilter(request, response);
			return;
		}

		// El usuario no está logueado
		if (!loggedIn) {
			res.sendRedirect(req.getContextPath() + "/Login.xhtml");
			return;
		}

		// El recurso requiere protección, pero el usuario ya está logueado.
		chain.doFilter(request, response);

	}

	// Recursos que no requieren proteccion.
	private boolean noProteger(String urlStr) {
		if (urlStr.endsWith("login.xhtml"))
			return true;
		if (urlStr.endsWith("logo_uex.png"))
			return true;
		if (urlStr.endsWith("login.css"))
			return true;
		if (urlStr.endsWith("fondo_login.png"))
			return true;
		if (urlStr.indexOf("/javax.faces.resource/") != -1)
			return true;
		return false;
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
