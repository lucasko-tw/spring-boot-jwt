package com.test.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.test.model.Role;
import com.test.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SECRET = "secretkey";
	public static final long EXPIRATION_TIME = 864_000_000; // 10 days

	private UserService userService;

	public LoginFilter(String url, AuthenticationManager authManager, UserService userService) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
		this.userService = userService;
	}

	@Override
	public Authentication attemptAuthentication(javax.servlet.http.HttpServletRequest req, HttpServletResponse arg1)
			throws AuthenticationException, IOException, ServletException {

		String username = req.getParameter("username");
		String password = req.getParameter("password");

		if (username == null || password == null) {
			throw new ServletException("Please fill in username and password");
		}

		com.test.model.User user = userService.findByUsername(username);

		if (user == null) {
			throw new ServletException("User name not found.");
		}
		System.out.printf("username=%s \n", user.getUsername());
		System.out.printf("password=%s \n", user.getPassword());
		System.out.printf("role=%s \n", user.getRole());

		String pwd = user.getPassword();

		if (!password.equals(pwd)) {
			throw new ServletException("Invalid login. Please check your name and password.");
		}
		List<Role> roles = new ArrayList<Role>();
		roles.add(Role.valueOf(user.getRole()));

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, pwd, roles);

		return this.getAuthenticationManager().authenticate(token);

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		System.out.println(">>> successfulAuthentication");

		String username = ((User) auth.getPrincipal()).getUsername();
		String role = userService.findByUsername(username).getRole();

		String jwttoken = Jwts.builder().setSubject(username).claim("roles", role)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET)

				.compact();

		// res.addHeader(HEADER_STRING, TOKEN_PREFIX + jwttoken);

		JSONObject json = new JSONObject();
		json.put("jwttoken", jwttoken);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());

	}

}
