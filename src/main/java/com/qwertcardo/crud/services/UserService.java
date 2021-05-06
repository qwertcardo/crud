package com.qwertcardo.crud.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.qwertcardo.crud.models.Product;
import com.qwertcardo.crud.models.User;
import com.qwertcardo.crud.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User save(User user) {
		User newUser = user;
		List<Product> productsList = new ArrayList<Product>();
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setProducts(productsList);
		return repository.saveAndFlush(user);
	}
	
	public User update(long id, User user) {
		User userData = getById(id);
		userData.setName(user.getName());
		userData.setEmail(user.getEmail());
		userData.setLogin(user.getLogin());
		userData.setPassword((user.getPassword() != null && !user.getPassword().isEmpty())? passwordEncoder.encode(user.getPassword()) : userData.getPassword());
		userData.setBirthDate(Optional.ofNullable(user.getBirthDate()).orElse(userData.getBirthDate()));
		
		return repository.saveAndFlush(userData);
	}
	
	public void delete(long id) {
		repository.deleteById(id);
	}
	
	public User getById(long id) {
		Optional<User> user = repository.findById(id);
		return user.orElseThrow(
				() -> new IllegalArgumentException("There is no User with id: " + id));
	}
	
	public List<User> getUserList() {
		return repository.findAll();
	}
	
	public Page<User> getUserPage(Pageable pageable) {
		return repository.findAll(pageable);
	}
}
