package com.qwertcardo.crud.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.qwertcardo.crud.models.User;
import com.qwertcardo.crud.services.UserService;

@Controller
@RequestMapping(value = "/users")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<User> create(@RequestBody User user) {
		User newUser = service.save(user);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(newUser.getId()).toUri();
		return ResponseEntity.created(uri).body(newUser);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<User> update(@PathVariable long id, @RequestBody User user) {
		try {
			return ResponseEntity.ok(service.update(id, user));			
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<User> delete(@PathVariable long id) {
		try {
			service.delete(id);
			return ResponseEntity.ok().build();
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().build();
		}
		
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> getById(@PathVariable long id) {
		try {
			return ResponseEntity.ok(service.getById(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<User>> getUserList() {
		return ResponseEntity.ok(service.getUserList());
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Page<User>> getUserPage(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		return ResponseEntity.ok(service.getUserPage(pageable));
	}

}
