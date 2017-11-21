package com.syntrontech.oAuth2.restful.resource;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.syntrontech.oAuth2.google.oAuth2.OAuth2Exception;
import com.syntrontech.oAuth2.google.oAuth2.OAuth2Service;
import com.syntrontech.oAuth2.google.oAuth2.to.OAuth2Token;
import com.syntrontech.oAuth2.restful.vo.LoginVO;

@Path("/oAuth2")
public class OAuth2Resource {
	
	@Autowired
	private OAuth2Service oAuth2Service;
	
	@GET
	public Response loginGoogleOAuth2() {
		
		String response = oAuth2Service.login();
		
		return Response.status(Response.Status.OK)
						.entity(response)
						.build();
	}
	
	@GET
	@Path("/login")
	public Response redirectOAuth2(@BeanParam LoginVO loginVO) throws OAuth2Exception{
		
		OAuth2Token token = oAuth2Service.getTokenFromAuthorizationCode(loginVO.getCode(), loginVO.getError());
		
		return Response.status(Response.Status.CREATED)
						.type(MediaType.APPLICATION_JSON)
						.entity(token)
						.build();
	}
	
	@GET
	@Path("/refreshToken")
	public Response refreshToken(@QueryParam("refreshToken") String refreshToken){
		
		OAuth2Token token = oAuth2Service.getTokenFromRefreshToke(refreshToken);
		
		return Response.status(Response.Status.CREATED)
						.type(MediaType.APPLICATION_JSON)
						.entity(token)
						.build();
	}
}
