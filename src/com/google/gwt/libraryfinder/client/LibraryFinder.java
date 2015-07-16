package com.google.gwt.libraryfinder.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.libraryfinder.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.libraryfinder.client.NotLoggedInException;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.libraryfinder.shared.Library;
import com.google.gwt.libraryfinder.shared.LatLon;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LibraryFinder implements EntryPoint {
	
	private final LibraryServiceAsync libraryService = GWT.create(LibraryService.class);
	private final FavoriteServiceAsync favoriteService = GWT.create(FavoriteService.class);
	
	private List<Library> libraries = new ArrayList<Library>();
	private List<Library> favoriteLibraries = new ArrayList<Library>();
	
	private LatLon VancouverLatLon = new LatLon(49.2827, -123.1207);
	private LatLng latLngInVancouver = LatLng.newInstance(49.2827, -123.1207);
	private int defaultZoom = 11;
	private MapWidget libraryFinderMap = new MapWidget(latLngInVancouver, defaultZoom);
	private InfoWindow info = null;
	private Label libraryFinderTableTitle = new Label("Table of Libraries");
	private CellTable<Library> libraryFinderTable = new CellTable<Library>();
	
	private Label filterTitle = new Label("Search Map by City");
	private ListBox libraryFinderFilter = new ListBox();
	private Label favoritesTitle = new Label("Favorite Libraries");
	private FlexTable favoritesTable = new FlexTable();
	
	private Button loadDataButton = new Button("Load Data");
	private Button clearDataButton = new Button("Clear Data");
	
	private HorizontalPanel buttonsPanel = new HorizontalPanel();
	private HorizontalPanel mapPanel = new HorizontalPanel();
	private VerticalPanel mapSidePanel = new VerticalPanel();
	private ScrollPanel scrollPanel = new ScrollPanel();
	private HorizontalPanel dataButtonsPanel = new HorizontalPanel();
	private ScrollPanel libraryTablePanel = new ScrollPanel();
	private VerticalPanel mainPanel = new VerticalPanel();
	
	// for login/logout page
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Please log in to your Google Account to access the LibraryFinder application.");
	private Button loginButton = new Button("Log In");
	private Button logoutButton = new Button("Log Out");
	
	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {
		// Check login status using login service.
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			
			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				if (loginInfo.isLoggedIn()) {
					loadLibraryFinder();
				} else {
					loadLogin();
				}
			}
		});
	}

	//REQUIRES: nothing
	//MODIFIES: nothing
	//EFFECTS: assembles login page
	private void loadLogin() {	
		loginButton.getElement().setId("loginButton");		
		loginLabel.setStyleName("loginLabel");
		
		//Assemble login panel
		loginPanel.add(loginLabel);
		loginPanel.add(loginButton);
		//loginPanel.add(new HTMLPanel("<div class=\"g-signin2\" data-onsuccess=\"onSignIn\" data-theme=\"dark\"></div>"));
		
		//Set background image
		loginPanel.setStyleName("backgroundImage");
		loginPanel.getElement().getStyle().setBackgroundImage("war/images/shutterstock_87330377.jpg");
		
		//Associate panel with html page
		RootPanel.get("libraryFinder").add(loginPanel);
		
		//Link login button to Google Account Sign-In Page 
		loginButton.addClickHandler( new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.Location.assign(loginInfo.getLoginUrl());
			}	
		});
	}

	//REQUIRES: nothing
	//MODIFIES: view
	//EFFECTS: add widgets onto panels and organize panels on webpage
	private void loadLibraryFinder() {
		
		displayMap();
		displayTable();
		queryLibrariesFromServer();
		
		//Assemble panel for email, logout, facebook
		buttonsPanel.addStyleName("buttonToRight");
		
		buttonsPanel.add(new Label(loginInfo.getEmailAddress()));
		buttonsPanel.add(logoutButton);
		buttonsPanel.add(new HTMLPanel("<div class=\"fb-share-button\" data-href=\"http://1-dot-libraryfinder-1000.appspot.com\" data-layout=\"button\"></div>"));
		buttonsPanel.add(new HTMLPanel("<g:plus action=\"share\" annotation=\"none\"></g:plus>"));
		
		makeFavoritesTable();

		loadFavoritesTable();

		mapSidePanel.addStyleName("mapSidePanel");
		mapSidePanel.getElement().getStyle().setDisplay(Display.BLOCK);
		filterTitle.addStyleName("sidebarLabel");
		favoritesTitle.addStyleName("sidebarLabel");
		libraryFinderFilter.setWidth("90%");
		
		scrollPanel.add(favoritesTable);
		scrollPanel.setSize("220px", "450px");
		
		mapSidePanel.add(filterTitle);
		mapSidePanel.add(libraryFinderFilter);
		mapSidePanel.add(favoritesTitle);
		mapSidePanel.add(scrollPanel);
		
		mapPanel.add(libraryFinderMap);
		mapPanel.add(mapSidePanel);
		
		mainPanel.add(buttonsPanel);
		mainPanel.add(mapPanel); 
		
		//Set main page background style name
		mainPanel.setStyleName("mainBackground");
		
		// only admins can see these buttons
		if (loginInfo.getEmailAddress() == "phoebeyu7@gmail.com" || loginInfo.getEmailAddress() == "yukiwongky@gmail.com" || loginInfo.getEmailAddress() == "meng.tian401@gmail.com") {
			
			dataButtonsPanel.add(loadDataButton);
			dataButtonsPanel.add(clearDataButton);
			mainPanel.add(dataButtonsPanel);
		}
		
		//Assemble bottom part of panel
		libraryFinderTableTitle.setStyleName("tableTitle");
		mainPanel.add(libraryFinderTableTitle); 
		
		libraryTablePanel.add(libraryFinderTable);
		libraryTablePanel.setSize("1150px", "600px");
		libraryFinderTable.setWidth("100%");

		mainPanel.add(libraryTablePanel); 
		
		//Associate panels with html page
		RootPanel.get("libraryFinder").add(mainPanel);
		
		//Link load data button to parser 
		loadDataButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
					loadLibraries();
			}
		});
		
		clearDataButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
					clearLibraries();
			}
		});
		
		//Link logout button to Google Account Sign-In Page 
		logoutButton.setStyleName("logoutButton");
		logoutButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.Location.assign(loginInfo.getLogoutUrl());
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
				
				populateMap(libraries);
				populateTable(libraries);
				displayFilterMenu();
			}
			
		});
		
	}

	// REQUIRES: nothing
	// MODIFIES: nothing
	// EFFECTS: display map, set size and add control
	private void displayMap() {	
		libraryFinderMap.setSize("1000px", "600px");
		libraryFinderMap.addControl(new LargeMapControl());
	}
	
	// REQUIRES: List<Library>
	// MODIFIES: nothing
	// EFFECTS: center map to the first library of the list
	//          and add markers into map based on the lat lon of the libraries
	public void populateMap(List<Library> libraries) {
		libraryFinderMap.clearOverlays();
		LatLon centralizeLatLon = libraries.get(0).getLatLon();
		if (this.libraries.size() == libraries.size()){
			centralizeLatLon = VancouverLatLon;
		}
		LatLng centralizeLatLng = LatLng.newInstance(centralizeLatLon.getLat(), centralizeLatLon.getLon());
		libraryFinderMap.setCenter(centralizeLatLng, defaultZoom);
		for (Library l: libraries) {
			Marker marker = constructMarker(l);
			libraryFinderMap.addOverlay(marker);
		}
	}
	
	// REQUIRES: Library
	// MODIFIES: nothing
	// EFFECTS: returns a marker at the lat lon of a given library
	//          infoWindow shows when marker is clicked
	public Marker constructMarker(final Library l) {
		LatLon latLon = l.getLatLon();
		LatLng latLng = LatLng.newInstance(latLon.getLat(), latLon.getLon());
		final Marker marker = new Marker(latLng);
		marker.addMarkerClickHandler(new MarkerClickHandler() {
			@Override
			public void onClick(MarkerClickEvent event) {
				if (info != null) {
					info.close();
				}
				info=libraryFinderMap.getInfoWindow();
				info.open(marker, getInfoWindowContent(l));
			}
		});
		return marker;
	}
	
	// REQUIRES: Library
	// MODIFIES: nothing
	// EFFECTS: returns infoWindowContent with information string and favorite button
	public InfoWindowContent getInfoWindowContent(final Library library) {
		final String infoWindowString = getInfoWindowString(library);
		Button favoriteButton = new Button("Add to favorite");
		favoriteButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				addFavoriteLibrary(library);
			}
		});
		HTMLPanel panel = new HTMLPanel(infoWindowString + "<div id='favoriteButton'></div>");
		panel.add(favoriteButton, "favoriteButton");
		final InfoWindowContent content = new InfoWindowContent(panel);
		return content;
	}
	
	// REQUIRES: Library
	// MODIFIES: nothing
	// EFFECTS: returns an string of library information with proper format
	public String getInfoWindowString(Library l) {
		String contentString = "<b>" + l.getName() + " library</b>" + "<br />" + l.getAddress() + "<br />" + l.getCity() + ", BC  " + l.getPostalCode() + "<br />" + l.getPhone() + "<br />" + "<br />";
		return contentString;
	}

	// REQUIRES: city name
	// MODIFIES: nothing
	// EFFECTS: returns a list of libraries filtered by city
	private List<Library> filterLibraries(String city) {
		if (city.equals("Show All")) {
			return libraries;
		} else {
			List<Library> filteredLibraries = new ArrayList<Library>();
			for (Library l: libraries) {
				if (l.getCity().equals(city)) {
					filteredLibraries.add(l);
				}
			}
			return filteredLibraries;
		}
	}
	
	// REQUIRES: index of filter menu
	// MODIFIES: nothing
	// EFFECTS: refreshes the map and table according to the filter option
	private void refresh(int index) {
		String city = libraryFinderFilter.getItemText(index);
		List<Library> filteredLibraries = filterLibraries(city);
		populateMap(filteredLibraries);
		populateTable(filteredLibraries);
	}
	
	// REQUIRES: nothing
	// MODIFIES: nothing
	// EFFECTS: displays the drop-down filter menu filled with BC city names
	private void displayFilterMenu() {
		libraryFinderFilter.clear();
		libraryFinderFilter.addItem("Show All");
		List<String> cityNames = getCityNames();
		for (String c: cityNames) {
			libraryFinderFilter.addItem(c);
		}
		libraryFinderFilter.setVisibleItemCount(1);
		libraryFinderFilter.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				refresh(libraryFinderFilter.getSelectedIndex());
			}
		});
	}
	
	// REQUIRES: nothing
	// MODIFIES: nothing
	// EFFECTS: return a list of city names from the global libraries field with no duplication
	private List<String> getCityNames() {
		List<String> cityNames = new ArrayList<String>();
		for (Library l: libraries) {
			if (!duplicateCity(cityNames, l.getCity())){
				cityNames.add(l.getCity());
			}
		}
		Collections.sort(cityNames);
		return cityNames;
	}
	
	// REQUIRES: List<city name>, city name
	// MODIFIES: nothing
	// EFFECTS: returns true if city is found in list of city, otherwise return false
	private boolean duplicateCity(List<String> cities, String city){
		boolean duplicated = false;
		for (String c: cities) {
			if (c.equals(city)) {
				duplicated = true;
			}
		}
		return duplicated;
	}

	//REQUIRES: nothing
	//MODIFIES: view
	//EFFECTS: makes a favorites table and adds style elements
	private void makeFavoritesTable() {
		//TODO: make pretty
		favoritesTable.getRowFormatter().addStyleName(0, "favoritesTableHeader");
		favoritesTable.getColumnFormatter().setWidth(0, "170px");
		favoritesTable.setText(0, 0, "Name");
		favoritesTable.setText(0, 1, "Remove");
		

	}

	// REQUIRES: nothing
	// MODIFIES: nothing
	// EFFECTS: gets list of favorites from server and display them on success 
	private void loadFavoritesTable() {
		favoriteService.getFavorites(new AsyncCallback<List<Library>>() {

			//@Override
			public void onFailure(Throwable caught) {
				Window.alert("Failed to get favorite libraries from server!");
			}

			//@Override
			public void onSuccess(List<Library> favoriteLibraries) {
				displayFavoritesTable(favoriteLibraries);
			}
			
		});
		
	}

	// REQUIRES: list of libraries
	// MODIFIES: nothing
	// EFFECTS: displays the favorites table
	private void displayFavoritesTable(List<Library> favoriteLibraries) {
		for (Library l: favoriteLibraries) {
			displayFavorite(l);
		}
	}

	// REQUIRES: library
	// MODIFIES: list of favorite libraries
	// EFFECTS: displays the library
	private void displayFavorite(final Library favoriteLibrary) {
		int row = favoritesTable.getRowCount();
		favoriteLibraries.add(favoriteLibrary);
		String libraryName = favoriteLibrary.getName();
		favoritesTable.getCellFormatter().addStyleName(row, 1, "removeButtonColumn");
		
		Label libraryLabel = new Label(libraryName);
		libraryLabel.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				goToLibraryOnMap(favoriteLibrary);
			}
		});
		favoritesTable.setWidget(row, 0, libraryLabel);
		
		//add the "remove" button
		Button removeFavoriteButton = new Button("X");
		removeFavoriteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				//int rowIndex = favoritesTable.getCellForEvent(event).getRowIndex();
				removeFavorite(favoriteLibrary);
			}
		});
		
		favoritesTable.setWidget(row, 1, removeFavoriteButton);
	}
	
	//REQUIRES: valid library
	//MODIFIES: nothing
	//EFFECTS: returns a window alert if library is already in favorites, adds library if it is not
	private void addFavoriteLibrary(Library library) {
		if(hasDuplicate(library)){
			Window.alert("Library is already in favorites!");
		} else {
			addFavorite(library);
		}
	}
	
	// REQUIRES: nothing
	// MODIFIES: nothing
	// EFFECTS: checks server for duplicates and calls addFavorite
	//note: wait for Yuki. Also, may make into checkDuplicate helper method.
	//Will need to somehow figure out which library we are looking at from table/box in map
	private boolean hasDuplicate(Library library) {
		final String libraryName = library.getName();
		Boolean hasDuplicate = false;

		for(Library l: favoriteLibraries) {
			if(l.getName().equals(libraryName)) {
				hasDuplicate = true;
			}
		}
		return hasDuplicate;
	}
	
	// REQUIRES: library
	// MODIFIES: nothing
	// EFFECTS: adds the library to the server and calls displayFavorite
	private void addFavorite(final Library favoriteLibrary) {
		favoriteService.addFavorite(favoriteLibrary, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Failed to add library to favorites");
			}

			@Override
			public void onSuccess(Void result) {
				displayFavorite(favoriteLibrary);
			}
		});
	}
	
	// REQUIRES: library
	// MODIFIES: nothing
	// EFFECTS: removes the library from server
	private void removeFavorite(final Library favoriteLibrary) {
		favoriteService.removeFavorite(favoriteLibrary, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Failed to remove library from favorites");
			}

			@Override
			public void onSuccess(Void result) {
				undisplayFavorite(favoriteLibrary);
			}
		});
	}
	
	// REQUIRES: library
	// MODIFIES: list of favorite libraries
	// EFFECTS: removes the library from the local table
	private void undisplayFavorite(Library favoriteLibrary) {
		int removedIndex = favoriteLibraries.indexOf(favoriteLibrary);
		favoriteLibraries.remove(removedIndex);
		favoritesTable.removeRow(removedIndex+1);
	}

	// REQUIRES: nothing
	// MODIFIES: nothing
	// EFFECTS: put in headings into the table
	private void displayTable() {
		Column<Library, String> nameColumn = new Column<Library, String>(new ClickableTextCell()) {
			@Override
			public String getValue(Library object) {
				return object.getName();
			}
		};
		nameColumn.setFieldUpdater(new FieldUpdater<Library, String>(){
			@Override
			public void update(int index, Library object, String value) {
				goToLibraryOnMap(object);
			}
		});
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
		Column<Library, String> buttonColumn = new Column<Library, String>(favouriteButton) {
			@Override
			public String getValue(Library object) {
				return "+";
			}
		};
		buttonColumn.setFieldUpdater(new FieldUpdater<Library, String>() {
			@Override
			public void update(int index, Library object, String value) {
				addFavoriteLibrary(object);
			}
		});
		libraryFinderTable.addColumn(buttonColumn, "Add To Favourite");
		libraryFinderTable.setColumnWidth(nameColumn, 215, Unit.PX);
		libraryFinderTable.setColumnWidth(phoneColumn, 117, Unit.PX);
		libraryFinderTable.setColumnWidth(addressColumn, 302, Unit.PX);
		libraryFinderTable.setColumnWidth(cityColumn, 136, Unit.PX);
		libraryFinderTable.setColumnWidth(postalCodeColumn, 90, Unit.PX);
		libraryFinderTable.setColumnWidth(buttonColumn, 117, Unit.PX);
	}
	
	// REQUIRES: libraries
	// MODIFIES: void
	// EFFECTS: fill library finder table with library information
	public void populateTable(List<Library> libraries){
		sortLibraryByName(libraries);
		sortLibraryByCity(libraries);
		libraryFinderTable.setVisibleRange(0, libraries.size());
		libraryFinderTable.setRowCount(libraries.size(), true);
		libraryFinderTable.setRowData(0,libraries);
	}
	
	// REQUIRES: libraries
	// MODIFIES: libraries
	// EFFECTS: sort libraries by cities
	public void sortLibraryByCity(List<Library> libraries){
		Collections.sort(libraries, new Comparator<Library>() {
			@Override
			public int compare(Library a, Library b) {
				return a.getCity().compareTo(b.getCity());
			}
		});
	}
	
	// REQUIRES: libraries
	// MODIFIES: libraries
	// EFFECTS: sort libraries by branch names
	public void sortLibraryByName(List<Library> libraries){
		Collections.sort(libraries, new Comparator<Library>() {
			@Override
			public int compare(Library a, Library b) {
				return a.getName().compareTo(b.getName());
			}
		});
	}
	
	//REQUIRES: a library
	//MODIFIES: nothing
	//EFFECTS: takes a library, makes it into a list, call populateMap
	public void goToLibraryOnMap(Library l) {
		List<Library> oneLibrary = new ArrayList<Library>();
		oneLibrary.add(l);
		populateMap(oneLibrary);
		libraryFinderMap.getElement().setAttribute("tabindex", "0");
		Timer timer = new Timer(){

			@Override
			public void run() {
				libraryFinderMap.getElement().focus();
			}
		};
		timer.schedule(50);
	}

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
	
	//REQUIRES: nothing
	//MODIFIES: nothing
	//EFFECTS: clears all saved libraries in server
	private void clearLibraries() {
		libraryService.deleteLibraries(new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				Window.alert("Failed to clear libraries!");
			}

			public void onSuccess(Void ignore) {
				Window.alert("All saved libraries have been deleted!");
			}
		});
	}
	
	//REQUIRES: a throwable error
	//MODIFIES: nothing
	//EFFECTS: display a pop-up window with error message; if it's a NotLOggedInException replace url of current page with logout url
	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}
}