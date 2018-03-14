package com.test.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.test.model.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

import static java.util.Collections.emptyList;

public class JwtFilter extends GenericFilterBean {

	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SECRET = "secretkey";
	public static final long EXPIRATION_TIME = 864_000_000; // 10 days

	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
			throws IOException, ServletException {

		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;
		final String authHeader = request.getHeader("authorization");

		if ("OPTIONS".equals(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);

			chain.doFilter(req, res);
		} else {

			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				throw new ServletException("Missing or invalid Authorization header");
			}

			final String token = authHeader.substring(7);
			//System.out.println("token is:"+	token);
			Claims claims;
			String user;
			String role;
			try {
				claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
				request.setAttribute("claims", claims);

				user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
						.getSubject();

				role = (String) claims.get("roles");

			} catch (final SignatureException e) {
				throw new ServletException("Invalid token");
			}
			

			List<Role> roles = new ArrayList<Role>();
			//System.out.println("role is:"+	role);
			//System.out.println("role value of:"+	Role.valueOf(role));
			
			roles.add(Role.valueOf(role));
			
			
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, roles);
			SecurityContextHolder.getContext().setAuthentication(auth);

			chain.doFilter(req, res);
		}
	}
}
