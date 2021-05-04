package com.qwertcardo.crud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qwertcardo.crud.models.LoginRequest;
import com.qwertcardo.crud.models.LoginResponse;
import com.qwertcardo.crud.services.TokenService;

import javassist.NotFoundException;

@Controller
@RequestMapping(value = "/login")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private TokenService service;
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		UsernamePasswordAuthenticationToken data = new UsernamePasswordAuthenticationToken(request.getPrincipal(), request.getCredential());
		LoginResponse response = new LoginResponse();
		try {
			Authentication authentication = manager.authenticate(data);
			String token = service.generate(authentication);
			response.setToken(token);
			response.setUser(service.getUser(token));
			return ResponseEntity.ok(response);
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		} catch (NotFoundException e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
