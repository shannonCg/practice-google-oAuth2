package com.syntrontech.oAuth2.google.oAuth2;

import com.syntrontech.autoTool.exception.server.ServerException;

public class OAuth2Exception extends ServerException{

	private static final long serialVersionUID = 1675690009808685144L;

	private String errorCode = "GOOGLE_OAUTH2_SERVER_ERROR";
	
	private String errorMessage = "google oAuth2 server error =>";
	
	public OAuth2Exception(String errorMessage){
		this.errorMessage = this.errorMessage + errorMessage;
	}
	
	@Override
	public String getErrorCode() {
		return errorCode;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
}
