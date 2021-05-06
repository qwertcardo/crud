package com.qwertcardo.crud.configurations;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.qwertcardo.crud.models.User;
import com.qwertcardo.crud.repositories.UserRepository;
import com.qwertcardo.crud.services.TokenService;

public class AuthenticationTokenFilter extends OncePerRequestFilter {

	private TokenService service;
	
	private UserRepository repository;
	
	public AuthenticationTokenFilter(TokenService serv, UserRepository repo) {
		this.service = serv;
		this.repository = repo;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(service.isTokenValid(getToken(request))) {
			authenticate(getToken(request));
		}
		filterChain.doFilter(request, response);
		
	}
	
	private String getToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		} else {
			return token.substring(7, token.length());
		}		
	}
	
	private void authenticate(String token) {
		Long id = service.extractId(token);
		User user = repository.findById(id).orElseThrow(
				() -> new AuthenticationCredentialsNotFoundException("Invalid Credentials"));
		
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());			
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
