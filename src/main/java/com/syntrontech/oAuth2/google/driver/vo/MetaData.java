package com.syntrontech.oAuth2.google.driver.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MetaData {
	
	private String name;
	
	private String createdTime;
	
	private String modifiedTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Long createdTime) {
		this.createdTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date(createdTime));
	}

	public String getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Long modifiedTime) {
		this.modifiedTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date(modifiedTime));
	}
	
}
