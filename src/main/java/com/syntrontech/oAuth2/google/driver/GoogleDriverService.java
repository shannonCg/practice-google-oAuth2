package com.syntrontech.oAuth2.google.driver;

import java.io.IOException;

public interface GoogleDriverService {
	
	String singleUpload(String tokenType, String accessToken)throws IOException;
	
	String multipleChunkUpload(String tokenType, String accessToken)throws IOException;
	
	String searchFile(String tokenType, String accessToken);
	
	String downloadFile(String tokenType, String accessToken) throws IOException;
	
}
