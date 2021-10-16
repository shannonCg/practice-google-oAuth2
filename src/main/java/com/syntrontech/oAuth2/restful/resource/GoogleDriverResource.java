package com.syntrontech.oAuth2.restful.resource;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.syntrontech.oAuth2.google.driver.GoogleDriverService;
import com.syntrontech.oAuth2.google.oAuth2.to.OAuth2Token;

@Path("/driver")
public class GoogleDriverResource {

	@Autowired
	private GoogleDriverService googleDriverService;
	
	@POST
	@Path("/upload/single")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response singleUpload(OAuth2Token token) throws IOException{
		String result = googleDriverService.singleUpload(token.getTokenType(), token.getAccessToken());
		
		return Response.status(Response.Status.CREATED)
						.entity(result)
						.build();
	}
	
	@POST
	@Path("/upload/multipleChunk")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response multipleChunkUpload(OAuth2Token token) throws IOException{
		String result = googleDriverService.multipleChunkUpload(token.getTokenType(), token.getAccessToken());
		
		return Response.status(Response.Status.CREATED)
						.entity(result)
						.build();
	}
	
	@GET
	@Path("/search")
	public Response searchFiles(@QueryParam("tokenType")String tokenType,
				@QueryParam("accessToken")String accessToken) throws IOException{
		String result = googleDriverService.searchFile(tokenType, accessToken);
		
		return Response.status(Response.Status.OK)
						.entity(result)
						.build();
	}
	
	@GET
	@Path("/download")
	public Response downloadFiles(@QueryParam("tokenType")String tokenType,
				@QueryParam("accessToken")String accessToken) throws IOException{
		String result = googleDriverService.downloadFile(tokenType, accessToken);
		
		return Response.status(Response.Status.OK)
						.entity(result)
						.build();
	}
}
