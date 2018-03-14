package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.test.config.JwtFilter;

@SpringBootApplication
public class WebApplication {
 
	/*
	@Bean
	public org.springframework.boot.web.servlet.FilterRegistrationBean jwtFilter() {
		final org.springframework.boot.web.servlet.FilterRegistrationBean registrationBean = new org.springframework.boot.web.servlet.FilterRegistrationBean();
		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/resources/*"); 

		return registrationBean;
	}
*/
	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
}
