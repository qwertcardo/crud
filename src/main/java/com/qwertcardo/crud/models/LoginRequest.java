package com.qwertcardo.crud.models;

public class LoginRequest {

	private String principal;
	
	private String credential;

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}
	
}
