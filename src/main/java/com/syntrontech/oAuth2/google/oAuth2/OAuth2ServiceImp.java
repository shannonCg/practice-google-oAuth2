package com.syntrontech.oAuth2.google.oAuth2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntrontech.oAuth2.google.oAuth2.to.OAuth2Token;

@Service
public class OAuth2ServiceImp implements OAuth2Service {
	
	private static final Logger logger = LoggerFactory.getLogger(OAuth2ServiceImp.class);
	
	private final static String GRANT_GOOGLE_AOTH2_URI = "https://accounts.google.com/o/oauth2/v2/auth";
	
	private final static String REDIRECT_URI = "http://localhost:8080/google/oAuth2/login";
	
	private final static String CLIENT_ID = "159240870861-faqnscee5la559jqtlvl3q333oe5f3di.apps.googleusercontent.com";
	
	private final static String GOOGLE_DRIVE_SCOPE = "https://www.googleapis.com/auth/drive";
	
	private final static String EXCHANGE_ACCESS_TOKEN_URI = "https://www.googleapis.com/oauth2/v4/token";
	
	private final static String CLIENT_SECRET = "vr30YmXsiDGsG0mR-WVrJ8i7";
	
	@Override
	public String login() {
		try {
			Response response = ClientBuilder.newClient()
											 .target(GRANT_GOOGLE_AOTH2_URI)
											 .queryParam("client_id", CLIENT_ID)
											 .queryParam("redirect_uri", URLEncoder.encode(REDIRECT_URI, "utf-8"))
											 .queryParam("access_type", "offline")
											 .queryParam("scope", URLEncoder.encode(GOOGLE_DRIVE_SCOPE, "utf-8"))
											 .queryParam("response_type", "code")
											 .request(MediaType.TEXT_PLAIN_TYPE)
											 .get();
			
			String entity = response.readEntity(String.class);
			
			if (response.getStatus() != 200) {
				int status = response.getStatus();
				String errorMessage = "request to google oAuth2 successfully, but get response status[" + status
						+ "], and the error code is [" + entity + "]";
				
				logger.error(errorMessage);
			}
			return entity;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public OAuth2Token getTokenFromAuthorizationCode(String code, String error) throws OAuth2Exception {
		if("access_denied".equals(error)){
			String message = "the user denied the google oAuth2 consent";
			logger.debug(message);
			throw new OAuth2Exception(message);
		}
		
		Form form = new Form();
		form.param("code", code);
		form.param("client_id", CLIENT_ID);
		form.param("client_secret", CLIENT_SECRET);
		form.param("redirect_uri", REDIRECT_URI);
		form.param("grant_type", "authorization_code");
		Response response = ClientBuilder.newClient()
										.target(EXCHANGE_ACCESS_TOKEN_URI)
										.request(MediaType.APPLICATION_FORM_URLENCODED)
										.post(Entity.form(form));
			
		String entity = response.readEntity(String.class);
		
		if (response.getStatus() != 200) {
			int status = response.getStatus();
			String errorMessage = "request to google oAuth2 successfully, but get response status[" + status
					+ "], and the error code is [" + entity + "]";
			
			logger.error(errorMessage);
		}
		System.out.println(entity);
		ObjectMapper objMapper = new ObjectMapper();
		try {
			return objMapper.readValue(entity, OAuth2Token.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public OAuth2Token getTokenFromRefreshToke(String refreshToken) {
		Form form = new Form();
		form.param("refresh_token", refreshToken);
		form.param("client_id", CLIENT_ID);
		form.param("client_secret", CLIENT_SECRET);
		form.param("grant_type", "refresh_token");
		Response response = ClientBuilder.newClient()
										.target(EXCHANGE_ACCESS_TOKEN_URI)
										.request(MediaType.APPLICATION_FORM_URLENCODED)
										.post(Entity.form(form));
			
		String entity = response.readEntity(String.class);
		
		if (response.getStatus() != 200) {
			int status = response.getStatus();
			String errorMessage = "request to google oAuth2 successfully, but get response status[" + status
					+ "], and the error code is [" + entity + "]";
			
			logger.error(errorMessage);
		}
		System.out.println(entity);
		ObjectMapper objMapper = new ObjectMapper();
		try {
			return objMapper.readValue(entity, OAuth2Token.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
