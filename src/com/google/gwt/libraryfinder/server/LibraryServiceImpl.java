package com.google.gwt.libraryfinder.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.libraryfinder.client.LibraryService;
import com.google.gwt.libraryfinder.shared.Library;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LibraryServiceImpl extends RemoteServiceServlet implements LibraryService {

	private List<Library> libraries = new ArrayList<Library>();

	public void getLibraries() {
		String jsonString = queryLibrariesFromRemote(); //might take in URL string
		parseLibraries(jsonString);
		storeLibraries();
	}

	//REQUIRES: maybe URL string
	//MODIFIES: nothing
	//EFFECTS: return a JSON string
	private String queryLibrariesFromRemote() {
		// TODO Auto-generated method stub
		return null;
	}

	//REQUIRES: valid JSON string
	//MODIFIES: this
	//EFFECTS: add all libraries to the list of libraries
	private void parseLibraries(String s) {
		// TODO Auto-generated method stub

	}

	//REQUIRES: a list of libraries
	//MODIFIES: nothing
	//EFFECTS: store a list of libraries to web server using PersistenceManager
	private void storeLibraries() {
		// TODO Auto-generated method stub
		// TODO pop-up window (with OK button) informing user libraries are loaded

	}

}
