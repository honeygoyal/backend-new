package com.egatetutor.backend.model.responsemodel;

import com.egatetutor.backend.model.UserInfo;

import java.io.Serializable;

public class JwtResponse implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = -1428603846781953145L;
	private final String jwttoken;
	private UserInfo user;
	

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}
	


	public JwtResponse(String jwttoken, UserInfo user) {
		this.jwttoken = jwttoken;
		this.user=user;
	}

	public String getToken() {
		return this.jwttoken;
	}
}