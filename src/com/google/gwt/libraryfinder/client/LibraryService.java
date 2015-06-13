package com.google.gwt.libraryfinder.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("library")
public interface LibraryService extends RemoteService {
	public void getLibraries();
}
