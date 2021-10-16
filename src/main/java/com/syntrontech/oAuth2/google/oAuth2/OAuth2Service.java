package com.syntrontech.oAuth2.google.oAuth2;

import com.syntrontech.oAuth2.google.oAuth2.to.OAuth2Token;

public interface OAuth2Service {
	
	public String login();
	
	public OAuth2Token getTokenFromAuthorizationCode(String code, String error) throws OAuth2Exception;
	
	public OAuth2Token getTokenFromRefreshToke(String refreshToken);
}
