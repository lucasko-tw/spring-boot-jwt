package com.test.controller;

import java.util.Date;

import javax.servlet.ServletException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.model.User;
import com.test.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class LoginController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/signup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String registerUser(@RequestBody User user) {

		JSONObject json = new JSONObject();
		if (userService.findByUsername(user.getUsername()) != null) {
			json.put("msg", "username already exists.");
			json.put("status", false);
			return json.toString();
		} 
		
		userService.save(user);
	

		if (userService.findByUsername(user.getUsername()) != null) {
			json.put("msg", "Account successfully sign up ");
			json.put("status", true);
		} 
		else {
			json.put("msg", "failed to sign up");
			json.put("status", true);
		}

	
	

		return json.toString();
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String login(@RequestParam String username, @RequestParam String password) throws ServletException {

		String jwtToken = "";

		if (username == null || password == null) {
			throw new ServletException("Please fill in username and password");
		}

		User user = userService.findByUsername(username);

		if (user == null) {
			throw new ServletException("User name not found.");
		}

		String pwd = user.getPassword();

		if (!password.equals(pwd)) {
			throw new ServletException("Invalid login. Please check your name and password.");
		}

		jwtToken = Jwts.builder().setSubject(username).claim("roles", "USER").setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS256, "secretkey").compact();

		return jwtToken;
	}
}
