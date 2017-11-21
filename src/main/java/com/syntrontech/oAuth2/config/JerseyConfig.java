package com.syntrontech.oAuth2.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.syntrontech.autoTool.exception.handler.ServerExceptionMapper;
import com.syntrontech.oAuth2.restful.resource.GoogleDriverResource;
import com.syntrontech.oAuth2.restful.resource.OAuth2Resource;

/*
 * register the Endpoints in Jersey
 */

@Component
@ApplicationPath("/google")
public class JerseyConfig extends ResourceConfig{
	
	public JerseyConfig(){
		registerResource();
		registerProvider();
	}
	
	private void registerResource(){
		register(OAuth2Resource.class);
		register(GoogleDriverResource.class);
	}
	
	private void registerProvider(){
		register(new ServerExceptionMapper());
	}
		
}
