package com.google.gwt.libraryfinder.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.libraryfinder.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.libraryfinder.shared.Library;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LibraryFinder implements EntryPoint {
	
	private List<Library> libraries = new ArrayList<Library>();
	
	private final LibraryServiceAsync libraryService = GWT
			.create(LibraryService.class);
	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {
		//TODO: layout of web page 
		loadLibraryFinder();
	}

	//REQUIRES: nothing
	//MODIFIES: view
	//EFFECTS: add widgets onto panels and organize panels on webpage
	private void loadLibraryFinder() {
		// TODO load webpage and widgets
		// TODO add map widget to "the" panel
		// TODO add table widget to panel
		// TODO link load data button to parser 
			// on click 
		loadLibraries();
		
		//TODO query library data from server 
		queryLibrariesFromServer();
		
		//TODO make map
		displayMap();
		
		//TODO make table
		displayTable();
		
	}
	
	// REQUIRES: nothing
	// MODIFIES: this
	// EFFECTS: get libraries from server and add to the list of libraries
	private void queryLibrariesFromServer() {
		// TODO Auto-generated method stub
		
	}

	// REQUIRES: list of libraries
	// MODIFIES: nothing
	// EFFECTS: display, centralize, put icons on map
	private void displayMap() {
		// TODO Auto-generated method stub
		
	}
	
	// REQUIRES: list of libraries
	// MODIFIES: nothing
	// EFFECTS: put in headings and library information into the table
	private void displayTable() {
		// TODO Auto-generated method stub
		
	}

	//call LibraryServiceAsync class -> call LibraryServiceImpl
	//REQUIRES: nothing
	//MODIFIES: nothing
	//EFFECTS: call a method in LibraryServiceImpl that queries, parses 
	//			and stores a list of libraries
	private void loadLibraries() {
		// TODO Auto-generated method stub
		
	}
	
	
}