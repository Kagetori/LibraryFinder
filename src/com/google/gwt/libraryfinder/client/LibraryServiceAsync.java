package com.google.gwt.libraryfinder.client;

import java.util.List;

import com.google.gwt.libraryfinder.shared.Library;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LibraryServiceAsync {
	public void getLibraries(AsyncCallback<Void> async);
	public void retrieveLibraries(AsyncCallback<List<Library>> asyncCallback);
	public void deleteLibraries(AsyncCallback<Void> asyncCallback);
}
