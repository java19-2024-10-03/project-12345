package telran.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExpiredPasswordFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (checkEndpoint(req.getMethod(), req.getServletPath()) && auth != null
				&& auth.getPrincipal() instanceof UserProfile) {
			UserProfile user = (UserProfile) auth.getPrincipal();
			if (!user.getPassIsNotExpired()) {
				resp.sendError(403, "Password expired!");
				return;
			}
		}
		chain.doFilter(req, resp);
	}

	private boolean checkEndpoint(String method, String servletPath) {

		return !(method.equals("PUT") && servletPath.matches("/account/password"));
	}

}