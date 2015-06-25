package com.google.gwt.libraryfinder.client;

import java.util.List;

import com.google.gwt.libraryfinder.shared.Library;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("library")
public interface LibraryService extends RemoteService {
	public void getLibraries();
	public List<Library> retrieveLibraries();
	public void deleteLibraries();
}
