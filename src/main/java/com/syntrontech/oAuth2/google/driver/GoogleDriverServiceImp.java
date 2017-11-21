package com.syntrontech.oAuth2.google.driver;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;
import org.springframework.stereotype.Service;

import com.syntrontech.oAuth2.google.driver.to.GoogleDriveFile;
import com.syntrontech.oAuth2.google.driver.to.SearchResponse;
import com.syntrontech.oAuth2.google.driver.vo.MetaData;

@Service
public class GoogleDriverServiceImp implements GoogleDriverService {

	private final static String filePath = "/home/shannon/Documents/Push server 介接說明書.pdf";

//	private final static String filePath = "/home/shannon/Documents/Java经典编程300例.pdf";
	
//	private final static String filePath = "/home/shannon/Documents/test10GFile.txt";

	protected Path getPath(String path) {
		return Paths.get(path);
	}

	protected BasicFileAttributes getFileAttr(Path path) throws IOException {
		return Files.readAttributes(path, BasicFileAttributes.class);
	}

	protected String getFileMime(Path path) throws IOException {
		return Files.probeContentType(path);
	}
	
	@Override
	public String singleUpload(String tokenType, String accessToken) throws IOException {
		Path file1 = getPath(filePath);
		BasicFileAttributes file1Attr = null;
		try {
			file1Attr = getFileAttr(file1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		MetaData data = new MetaData();
		data.setName(file1.getFileName().toString());

		String mime = getFileMime(file1);

		//upload file metadata
		String metaUri = "https://www.googleapis.com/upload/drive/v3/files";
		Response metaResponse = ClientBuilder.newClient()
											.target(metaUri)
											.queryParam("uploadType", "resumable")
											.request(MediaType.APPLICATION_JSON)
											.header("Authorization", tokenType + " " + accessToken)
											.header("X-Upload-Content-Type", mime)
											.header("X-Upload-Content-Length", file1Attr.size())
											.post(Entity.entity(data, MediaType.APPLICATION_JSON));

		String metaEntity = metaResponse.readEntity(String.class);
		System.out.println("First Response: " + metaEntity);
		if (metaResponse.getStatus() != 200) {
			int status = metaResponse.getStatus();
			String errorMessage = "request to google oAuth2 successfully, but get response status[" + status
					+ "], and the error code is [" + metaEntity + "]";
			System.out.println(errorMessage);
			return errorMessage;
		}

		Response uploadResponse = null;
		try (FileInputStream fileInputStream = new FileInputStream(file1.toFile());
				BufferedInputStream inputStream = new BufferedInputStream(fileInputStream,1024)) {
		//upload file data
		String uploadUri = metaResponse.getHeaderString("Location");
		uploadResponse = ClientBuilder.newClient()
									  .property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED")
									  .target(uploadUri)
									  .request()
//									  .header("Content-Length", file1Attr.size())
									  .put(Entity.entity(inputStream, mime));
		}
		String uploadEntity = uploadResponse.readEntity(String.class);
		System.out.println("Second Response: " + uploadEntity);
		return uploadEntity;
	}

	@Override
	public String multipleChunkUpload(String tokenType, String accessToken) throws IOException {
		Path file1 = getPath(filePath);
		BasicFileAttributes file1Attr = null;
		try {
			file1Attr = getFileAttr(file1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		MetaData data = new MetaData();
		data.setName(file1.getFileName().toString());
		
		String mime = getFileMime(file1);
		
		//upload file metadata
		String metaUri = "https://www.googleapis.com/upload/drive/v3/files";
		Response metaResponse = ClientBuilder.newClient()
										 	.target(metaUri)
										 	.queryParam("uploadType", "resumable")
										 	.request(MediaType.APPLICATION_JSON)
										 	.header("Authorization", tokenType + " " + accessToken)
										 	.header("X-Upload-Content-Type", mime)
										 	.header("X-Upload-Content-Length", file1Attr.size())
										 	.post(Entity.entity(data, MediaType.APPLICATION_JSON));
		
		String metaEntity = metaResponse.readEntity(String.class);
		System.out.println("First Response: " + metaEntity);
		if (metaResponse.getStatus() != 200) {
			int status = metaResponse.getStatus();
			String errorMessage = "request to google oAuth2 successfully, but get response status[" + status
					+ "], and the error code is [" + metaEntity + "]";
			return errorMessage;
		}
		
		int containerSize = 100*256*1024;
		if(file1Attr.size() < containerSize){
			containerSize = (int)file1Attr.size();
		}
		String uploadUri = metaResponse.getHeaderString("Location");
		Response uploadResponse = null;
		byte[] container = new byte[containerSize];
		int startIndex = 0;
		int endIndex = containerSize -1;
		String result = null;
		do{
			System.out.println("startIndex:"+startIndex+", endIndex:"+endIndex);
			try(FileInputStream fileInputStream = new FileInputStream(file1.toFile());
				BufferedInputStream inputStream = new BufferedInputStream(fileInputStream)){
				inputStream.skip(startIndex);
				inputStream.read(container);
			}
			//upload file data
			uploadResponse = ClientBuilder.newClient()
											.target(uploadUri)
		 									.request()
		 									.header("Content-Length",containerSize)
		 									.header("Content-Range", "bytes "+startIndex+"-"+endIndex+"/"+file1Attr.size())
		 									.put(Entity.entity(container, mime));
			String uploadEntity = uploadResponse.readEntity(String.class);
			System.out.println("Second Response: "+uploadEntity);
			result = uploadEntity;
			if(uploadResponse.getStatus() == 308){
				System.out.println("upload range: "+uploadResponse.getHeaderString("Range"));
				String range = uploadResponse.getHeaderString("Range");
				startIndex = Integer.valueOf(range.split("-")[1]);
				if((startIndex+containerSize) > file1Attr.size()){
					containerSize = (int)file1Attr.size() - startIndex;
					container = new byte[containerSize];
				}
				endIndex = startIndex + containerSize -1;
			}
			if(uploadResponse.getStatus() == 401){
				System.out.println("required access token");
				break;
			}
				
		}while(uploadResponse.getStatus() != 200);
		
		return result;
	}

	@Override
	public String searchFile(String tokenType, String accessToken) {
		String searchUri = "https://www.googleapis.com/drive/v3/files";
		String queryString = "trashed = false and 'me' in owners";
		String encodeString = null;
		try {
			encodeString = URLEncoder.encode(queryString, "utf-8");
			encodeString = encodeString.replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		Response searchResponse = ClientBuilder.newClient()
												.target(searchUri)
												.queryParam("q", encodeString)
												.request()
												.header("Authorization", tokenType + " " + accessToken)
												.get();
		
		String searchEntity = searchResponse.readEntity(String.class);
		if (searchResponse.getStatus() != 200) {
			int status = searchResponse.getStatus();
			String errorMessage = "request to google oAuth2 successfully, but get response status[" + status
					+ "], and the error code is [" + searchResponse + "]";
			return errorMessage;
		}
		return searchEntity;
	}

	@Override
	public String downloadFile(String tokenType, String accessToken) throws IOException {
		//search file api
		String searchUri = "https://www.googleapis.com/drive/v3/files";
		String queryString = "trashed = false and name contains 'Java'";
//		String queryString = "trashed = false and name contains 'Push'";
		String encodeString = null;
		try {
			encodeString = URLEncoder.encode(queryString, "utf-8");
			encodeString = encodeString.replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		Response searchResponse = ClientBuilder.newClient()
												.target(searchUri)
												.queryParam("q", encodeString)
												.request()
												.header("Authorization", tokenType + " " + accessToken)
												.get();
		
		if (searchResponse.getStatus() != 200) {
			int status = searchResponse.getStatus();
			String errorMessage = "request to google oAuth2 successfully, but get response status[" + status
					+ "], and the error code is [" + searchResponse.readEntity(String.class) + "]";
			return errorMessage;
		}
		SearchResponse search = searchResponse.readEntity(SearchResponse.class);
		
		//download file
		GoogleDriveFile file = search.getFiles().get(0);
		Response downloadResponse = ClientBuilder.newClient()
												.target(searchUri)
												.path(file.getId())
												.queryParam("alt", "media")
												.request()
												.header("Authorization", tokenType + " " + accessToken)
												.get();
		if (downloadResponse.getStatus() != 200) {
			int status = downloadResponse.getStatus();
			String errorMessage = "request to google oAuth2 successfully, but get response status[" + status
					+ "], and the error code is [" + searchResponse.readEntity(String.class) + "]";
			return errorMessage;
		}
		
		String filePath = "/home/shannon/"+file.getName();
		try(InputStream inputStream = downloadResponse.readEntity(InputStream.class);
			OutputStream outputStream = new FileOutputStream(filePath)){
			byte[] buffer = new byte[1024*1024];
			int byteRead;
			while((byteRead = inputStream.read(buffer)) != -1){
				outputStream.write(buffer, 0, byteRead);
			}
		}
		
		return null;
	}

}
