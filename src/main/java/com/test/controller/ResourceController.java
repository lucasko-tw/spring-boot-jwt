package com.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.model.User;
import com.test.service.UserService;

@SpringBootApplication
@RestController
@RequestMapping("/resources")
public class ResourceController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/user/username", method = RequestMethod.POST)
	public User findByUserName(@RequestParam String username) {
		// Notice that is different between @RequestParam & @RequestBody
		
		
		User user= userService.findByUsername(username);
		
		if (user == null)
		{
			return new User();  // return empty user
		}

		return user ;
	}

	@RequestMapping(value = "/user/update", method = RequestMethod.POST)
	public User updateUser(@RequestBody User user) {
		return userService.save(user);
	}
}
