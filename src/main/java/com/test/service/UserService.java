package com.test.service;

import com.test.model.User;

public interface UserService {
	User save(User user);

	User findByUsername(String username);
}
