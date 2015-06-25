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
import com.google.gwt.user.client.Window;
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
import com.google.gwt.libraryfinder.shared.LatLon;			//Y
import com.google.gwt.maps.client.MapWidget;				//Y
import com.google.gwt.maps.client.geom.LatLng;				//Y
import com.google.gwt.maps.client.control.LargeMapControl;	//Y
import com.google.gwt.maps.client.overlay.Marker;			//Y
import com.google.gwt.user.cellview.client.CellTable;		//Y
import com.google.gwt.user.cellview.client.Column;			//Y
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;	//Y
import com.google.gwt.user.cellview.client.TextColumn;		//Y
import com.google.gwt.cell.client.ButtonCell;				//Y

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LibraryFinder implements EntryPoint {
	LatLng latLngInVancouver = LatLng.newInstance(49.2827, -123.1207);				//Y
	private MapWidget libraryFinderMap = new MapWidget(latLngInVancouver, 11);		//Y
	CellTable<Library> libraryFinderTable = new CellTable<Library>();							//Y
	
	private List<Library> libraries = new ArrayList<Library>();
	
	private final LibraryServiceAsync libraryService = GWT.create(LibraryService.class);
	
	private VerticalPanel topPanel = new VerticalPanel();
	private VerticalPanel bottomPanel = new VerticalPanel();
	private Button loadDataButton = new Button("Load Data");
	
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
		
		displayMap();
		queryLibrariesFromServer();
		
		//Assemble top panel
		topPanel.add(libraryFinderMap); 
		topPanel.add(loadDataButton);
		
		//Assemble bottom panel
		bottomPanel.add(libraryFinderTable); 
		
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
		libraryService.retrieveLibraries(new AsyncCallback<List<Library>>() {

			//@Override
			public void onFailure(Throwable caught) {
				Window.alert("Querying libraries from server was not successful!");
			}

			//@Override
			public void onSuccess(List<Library> result) {
				Window.alert("Number of libraries found: " + result.size());
				libraries = result;
				
				populateMap();
				displayTable();
			}
			
		});
		
	}

	// REQUIRES: list of libraries
	// MODIFIES: nothing
	// EFFECTS: display, centralize, put icons on map
	private void displayMap() {	
		
		libraryFinderMap.setSize("1000px", "600px");
		libraryFinderMap.addControl(new LargeMapControl());
		
	}
	
	public void populateMap() {
		for (Library l: libraries) {
			LatLon latLon = l.getLatLon();
			LatLng latLng = LatLng.newInstance(latLon.getLat(), latLon.getLon());
			Marker marker = new Marker(latLng);	//final
			libraryFinderMap.addOverlay(marker);
		}
	}
	
	// REQUIRES: list of libraries
	// MODIFIES: nothing
	// EFFECTS: put in headings and library information into the table
	private void displayTable() {	//Y
//		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		TextColumn<Library> nameColumn = new TextColumn<Library>() {
			@Override
			public String getValue(Library object) {
				return object.getName();
			}
		};
		libraryFinderTable.addColumn(nameColumn, "Branch Name");
		TextColumn<Library> phoneColumn = new TextColumn<Library>() {
			@Override
			public String getValue(Library object) {
				return object.getPhone();
			}
		};
		libraryFinderTable.addColumn(phoneColumn, "Phone");
		TextColumn<Library> addressColumn = new TextColumn<Library>() {
			@Override
			public String getValue(Library object) {
				return object.getAddress();
			}
		};
		libraryFinderTable.addColumn(addressColumn, "Address");
		TextColumn<Library> cityColumn = new TextColumn<Library>() {
			@Override
			public String getValue(Library object) {
				return object.getCity();
			}
		};
		libraryFinderTable.addColumn(cityColumn, "City");
		TextColumn<Library> postalCodeColumn = new TextColumn<Library>() {
			@Override
			public String getValue(Library object) {
				return object.getPostalCode();
			}
		};
		libraryFinderTable.addColumn(postalCodeColumn, "Postal Code");
		ButtonCell favouriteButton = new ButtonCell();
		Column buttonColumn = new Column<Library, String>(favouriteButton) {
			@Override
			public String getValue(Library object) {
				return "+";
			}
		};
		libraryFinderTable.addColumn(buttonColumn, "Add To Favourite");
		libraryFinderTable.setVisibleRange(0, libraries.size());
		libraryFinderTable.setRowCount(libraries.size(), true);
		libraryFinderTable.setRowData(0,libraries);
	}

	//call LibraryServiceAsync class -> call LibraryServiceImpl
	//REQUIRES: nothing
	//MODIFIES: nothing
	//EFFECTS: call a method in LibraryServiceImpl that queries, parses 
	//			and stores a list of libraries
	private void loadLibraries() {
		libraryService.getLibraries(new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				Window.alert("Failed to load libraries!");
			}

			public void onSuccess(Void ignore) {
				Window.alert("Libraries have been loaded!");
				Window.Location.reload();
			}
		});
	}
	
	
}