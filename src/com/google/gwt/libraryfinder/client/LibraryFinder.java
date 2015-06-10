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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.libraryfinder.shared.Library;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LibraryFinder implements EntryPoint {
	
	private List<Library> libraries = new ArrayList<Library>();
	
	private final LibraryServiceAsync libraryService = GWT
			.create(LibraryService.class);
	
	private VerticalPanel topPanel = new VerticalPanel();
	private VerticalPanel bottomPanel = new VerticalPanel();
	private Button loadDataButton = new Button("Load Data");
	
	//Placeholder widgets for map and table 
	private DatePicker libraryFinderMap = new DatePicker();
	private FlexTable libraryFinderTable = new FlexTable();
	
	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {
		loadLibraryFinder();
	}

	//REQUIRES: nothing
	//MODIFIES: view
	//EFFECTS: add widgets onto panels and organize panels on webpage
	private void loadLibraryFinder() {
		
		//TODO query library data from server 
		queryLibrariesFromServer();
		
		//TODO make map
		displayMap();
		
		//TODO make table
		displayTable();
		//Make fake table (delete when you get actual table)
		libraryFinderTable.setText(0, 0, "Symbol");
		libraryFinderTable.setText(0, 1, "Price");
		libraryFinderTable.setText(1, 0, "Random");
		libraryFinderTable.setText(1, 1, "Test");
		
		//Assemble top panel
		topPanel.add(libraryFinderMap); //adds map widget
		topPanel.add(loadDataButton);
		
		//Assemble bottom panel
		bottomPanel.add(libraryFinderTable); //adds table widget
		
		//Associate panels with html page
		RootPanel.get("libraryFinderMap").add(topPanel);
		RootPanel.get("libraryFinderTable").add(bottomPanel);
		
		//Link load data button to parser 
		loadDataButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadLibraries();
			}
		});
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