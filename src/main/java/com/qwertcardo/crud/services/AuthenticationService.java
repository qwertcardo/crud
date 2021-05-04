package com.qwertcardo.crud.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.qwertcardo.crud.models.User;
import com.qwertcardo.crud.repositories.UserRepository;

@Service
public class AuthenticationService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = Optional.ofNullable(repository.findByEmail(username)).orElse(Optional.ofNullable(repository.findByCpf(username)).orElse(repository.findByLogin(username)));
		if(user != null) {
			return user;
		} else {
			throw new UsernameNotFoundException("Credential Invalid");
		}
	}
		
}
