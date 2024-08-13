package com.employee.model;

public class AuthenticationResponse {

	private final String token;

	public AuthenticationResponse(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

}
