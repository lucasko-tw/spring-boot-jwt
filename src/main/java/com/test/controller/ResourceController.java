package com.test.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.model.User;
import com.test.service.UserService;

@SpringBootApplication
@RestController
// @RequestMapping("/resources")
public class ResourceController {

	@RequestMapping(value = "/admin/resources", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String adminResource() {

		return new JSONObject().put("msg", "It is admin resources").toString();
	}

	@RequestMapping(value = "/user/resources", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String userResource() {

		return new JSONObject().put("msg", "It is user resources").toString();
	}

	/*
	 * @RequestMapping(value = "/user/update", method = RequestMethod.POST) public
	 * User updateUser(@RequestBody User user) { return userService.save(user); }
	 */
}
