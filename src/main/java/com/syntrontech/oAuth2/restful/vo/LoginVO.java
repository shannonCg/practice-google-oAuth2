package com.syntrontech.oAuth2.restful.vo;

import javax.ws.rs.QueryParam;

public class LoginVO {
	
	@QueryParam("code")
	private String code;
	
	@QueryParam("error")
	private String error;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
}
