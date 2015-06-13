package com.google.gwt.libraryfinder.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.google.gwt.libraryfinder.client.LibraryService;
import com.google.gwt.libraryfinder.shared.LatLon;
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
		
		LatLon ll1 = new LatLon(49.274931, -123.070318);
		LatLon ll2 = new LatLon(49.281272, -123.099827);
		Library l1 = new Library("Britannia", "Vancouver", "1661 Napier", "V5L 4X4", "(604) 665-2222", ll1);
		Library l2 = new Library("Carnegie", "Vancouver", "401 Main Street", "V6A 2T7", "(604) 665-3010", ll2);
		libraries.add(l1);
		libraries.add(l2);

		PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
		PersistenceManager pm = PMF.getPersistenceManager();
		
		try {
			//pm.makePersistentAll(libraries);
			pm.makePersistent(l1);
		} catch (Exception e) {
			System.out.println("There was an error storing the libraries!");
		}
		finally {
			pm.close();
		}
		
		// TODO pop-up window (with OK button) informing user libraries are loaded

	}

}
