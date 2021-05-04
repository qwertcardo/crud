package com.qwertcardo.crud.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.qwertcardo.crud.models.User;
import com.qwertcardo.crud.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javassist.NotFoundException;

@Service
public class TokenService {

	private final Long expiration = 3600000L;
	private final String secret = "dEoJN4BsaNQFESIZFnZELuM64z2xddxs";
	
	@Autowired
	private UserRepository repository;
	
	public String generate(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		Date date = new Date();
		
		return Jwts.builder()
					.setSubject(user.getId().toString())
					.setIssuedAt(date)
					.setExpiration(new Date(date.getTime() + expiration))
					.signWith(SignatureAlgorithm.HS256, secret).compact();				
	}
	
	public boolean isTokenValid(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public Long extractId(String token) {
		Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return Long.parseLong(body.getSubject());
	}
	
	public User getUser(String token) throws NotFoundException {
		return repository.findById(extractId(token)).orElseThrow(
					() -> new NotFoundException("Has no Active User with this id."));
	}
	
}
