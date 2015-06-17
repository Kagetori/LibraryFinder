package com.google.gwt.libraryfinder.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import javax.jdo.Extent;
import javax.jdo.FetchGroup;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.gwt.libraryfinder.client.LibraryService;
import com.google.gwt.libraryfinder.shared.LatLon;
import com.google.gwt.libraryfinder.shared.Library;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LibraryServiceImpl extends RemoteServiceServlet implements LibraryService {

	private List<Library> libraries = new ArrayList<Library>();
	private LibraryParser libraryParser = new LibraryParser();
	
	private static final PersistenceManagerFactory PMF = 
			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public void getLibraries() {
		String jsonString = queryLibrariesFromRemote(); //might take in URL string
		parseLibraries(jsonString);
		storeLibraries();
	}

	//REQUIRES: valid Http URL string
	//MODIFIES: nothing
	//EFFECTS: return a JSON string
	private String queryLibrariesFromRemote() {
		String returnString = "";
		try {
			returnString = libraryParser.makeHTTPRequest();
		} catch (IOException e) {
			System.out.println("Invalid httpRequest");
			e.printStackTrace();
		}
		return returnString;
	}

	//REQUIRES: valid JSON string
	//MODIFIES: this
	//EFFECTS: parse string and add all libraries to the list of libraries
	private void parseLibraries(String s) {
		try {
			libraries = libraryParser.parse(s);
		} catch (JSONException e) {
			System.out.println("JSONException caught");
			e.printStackTrace();
		}
		
		System.out.println("Parsed libraries: " + libraries.size());
	}

	//REQUIRES: a list of libraries
	//MODIFIES: nothing
	//EFFECTS: store a list of libraries to web server using PersistenceManager
	private void storeLibraries() {
	
		PersistenceManager pm = PMF.getPersistenceManager();
		
		try {
			pm.makePersistentAll(libraries);
		} catch (Exception e) {
			System.out.println("There was an error storing the libraries!");
		}
		finally {
			pm.close();
		}

	}

	@Override
	public List<Library> retrieveLibraries() {
		
		PersistenceManager pm = PMF.getPersistenceManager();
		List<Library> listLibraries = new ArrayList<Library>();
		
		pm.getFetchPlan().setGroup(FetchGroup.ALL);
		Extent<Library> extent = pm.getExtent(Library.class, false);
		for (Library l: extent) {
			listLibraries.add(l);
		}
		
		extent.closeAll();
		
		System.out.println("Retrieved libraries: " + listLibraries.size());
		
		return listLibraries;
	}

}
