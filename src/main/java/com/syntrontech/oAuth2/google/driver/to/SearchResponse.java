package com.syntrontech.oAuth2.google.driver.to;

import java.util.List;

public class SearchResponse {
	
	private String kind;
	
	private String nextPageToken;
	
	private boolean incompleteSearch;
	
	private List<GoogleDriveFile> files;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getNextPageToken() {
		return nextPageToken;
	}

	public void setNextPageToken(String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}

	public boolean isIncompleteSearch() {
		return incompleteSearch;
	}

	public void setIncompleteSearch(boolean incompleteSearch) {
		this.incompleteSearch = incompleteSearch;
	}

	public List<GoogleDriveFile> getFiles() {
		return files;
	}

	public void setFiles(List<GoogleDriveFile> files) {
		this.files = files;
	}
	
	
}
