package com.google.gwt.libraryfinder.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.libraryfinder.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.libraryfinder.client.NotLoggedInException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
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
	
	private List<Library> libraries = new ArrayList<Library>();
	
	// for library service
	private final LibraryServiceAsync libraryService = GWT.create(LibraryService.class);
	
	private LatLng latLngInVancouver = LatLng.newInstance(49.2827, -123.1207);				//Y
	private MapWidget libraryFinderMap = new MapWidget(latLngInVancouver, 11);		//Y
	private Label libraryFinderTableTitle = new Label("Table of Libraries");
	private CellTable<Library> libraryFinderTable = new CellTable<Library>();							//Y
	
	private Label userEmail = new Label("User Email");
	//private Button logoutButton = new Button("Logout");
	private Button socialLink = new Button("Facebook"); //this is just a placeholder
	
	private Label filterTitle = new Label("Search by City");
	private ListBox libraryFinderFilter = new ListBox();
	//private ListBox filterMenu = new ListBox();	//this is a placeholder
	private Label favoritesTitle = new Label("Favorites");
	private FlexTable favoritesTable = new FlexTable();
	//private CellTable<Library> favoritesTable = new CellTable<Library>();
	
	private Button loadDataButton = new Button("Load Data");
	private Button clearDataButton = new Button("Clear Data");
	
	private HorizontalPanel buttonsPanel = new HorizontalPanel();
	private HorizontalPanel mapPanel = new HorizontalPanel();
	private VerticalPanel mapSidePanel = new VerticalPanel();
	private VerticalPanel mainPanel = new VerticalPanel();
	
	// for login/logout page
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Please log in to your Google Account to access the LibraryFinder application.");
	private Anchor loginButton = new Anchor("Log In");
	private Anchor logoutButton = new Anchor("Log Out");
	
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
		//Assemble login panel
		loginPanel.add(loginLabel);
		loginPanel.add(loginButton);
		
		//Associate panel with html page
		RootPanel.get().add(loginPanel);
		
		//Link login button to Google Account Sign-In Page 
		loginButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loginButton.setHref(loginInfo.getLoginUrl());
				//Window.Location.assign("https://accounts.google.com");
			}
		});
	}

	//REQUIRES: nothing
	//MODIFIES: view
	//EFFECTS: add widgets onto panels and organize panels on webpage
	private void loadLibraryFinder() {
		
		displayMap();
		queryLibrariesFromServer();
		
		//Assemble top panel
		//TODO: make panel for email, logout, facebook
		
		buttonsPanel.addStyleName("buttonToRight");
		
		buttonsPanel.add(userEmail);
		buttonsPanel.add(logoutButton);
		
		//TODO: insert facebook(?) icon
		//on click calls shareOnSocialMedia
		buttonsPanel.add(socialLink); //just a placeholder
		
		//TODO: make panel for filter drop-down menu 
		displayFilterMenu();
		
		//TODO: make favorites table
		loadFavoritesTable();
		

		mapSidePanel.add(filterTitle);
		mapSidePanel.add(libraryFinderFilter);
		mapSidePanel.add(favoritesTitle);
		mapSidePanel.add(favoritesTable);
		
		mapPanel.add(libraryFinderMap);
		mapPanel.add(mapSidePanel);
		
		mainPanel.add(buttonsPanel);
		mainPanel.add(mapPanel); 
		
		//TODO: can only see these if admin~!
		mainPanel.add(loadDataButton);
		mainPanel.add(clearDataButton);
		
		//Assemble bottom part of panel
		mainPanel.add(libraryFinderTableTitle);
		mainPanel.add(libraryFinderTable); 
		
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
		logoutButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				logoutButton.setHref(loginInfo.getLogoutUrl());
				//Window.Location.assign("https://accounts.google.com/ServiceLogin?sacu=1&service=mail#identifier");
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
	
	public void populateMap(List<Library> libraries) {
		libraryFinderMap.clearOverlays();
		LatLon centralizeLatLon = libraries.get(0).getLatLon();
		LatLng centralizeLatLng = LatLng.newInstance(centralizeLatLon.getLat(), centralizeLatLon.getLon());
		libraryFinderMap.setCenter(centralizeLatLng);
		for (Library l: libraries) {
			LatLon latLon = l.getLatLon();
			LatLng latLng = LatLng.newInstance(latLon.getLat(), latLon.getLon());
			Marker marker = new Marker(latLng);	//final
			//TODO: call getLibraryFromLatLng(LatLng latLng)
			//TODO: implement method that shows a box with library info when marker is clicked
			//TODO: add favourite button to box
			libraryFinderMap.addOverlay(marker);
		}
	}
	
	// REQUIRES: LatLng from marker
	// MODIFIES: nothing
	// EFFECTS: return a library from searching through global library with latlng
	private Library getLibraryFromLatLng(LatLng latLng) {
		//TODO:
		return null;
	}

	// REQUIRES: list of libraries (from field) and a city name
	// MODIFIES: nothing
	// EFFECTS: returns a list of libraries filtered by city
	private List<Library> filterLibraries(String city) {
		if (city.equals("Show All Libraries")) {
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
	
	//EFFECTS: refreshes the map according to the filter option
	//this method is call when user select city from filter
	private void refreshMap(int index) {
		String city = libraryFinderFilter.getItemText(index);
		List<Library> filteredLibraries = filterLibraries(city);
		populateMap(filteredLibraries);
	}
	
	// REQUIRES: list of libraries
	// MODIFIES: nothing
	// EFFECTS: displays the drop-down filter menu
	private void displayFilterMenu() {
		libraryFinderFilter.clear();
		List<String> cityNames = getCityNames();
		libraryFinderFilter.addItem("Show All Libraries");
		for (String c: cityNames) {
			libraryFinderFilter.addItem(c);
		}
		libraryFinderFilter.setVisibleItemCount(1);
		// TODO: make the drop down scrollable?
		libraryFinderFilter.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				refreshMap(libraryFinderFilter.getSelectedIndex());
			}
		});
	}
	
	// EFFECTS: return a list of city names from the global libraries field with no duplication
	// TODO: write tests
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
	
	// EFFECTS: returns true if city is found in cities, otherwise return false
	// TODO: write tests
	private boolean duplicateCity(List<String> cities, String city){
		boolean duplicated = false;
		for (String c: cities) {
			if (c.equals(city)) {
				duplicated = true;
			}
		}
		return duplicated;
	}

	// REQUIRES: nothing
	// MODIFIES: nothing
	// EFFECTS: gets list of favorites and display them on success 
	//(calls displayFavoritesTable)
	private void loadFavoritesTable() {
		// TODO Auto-generated method stub
		//make Async Callback to get list of libraries from server
		
		//placeholder
		favoritesTable.setText(0, 0, "Name");
		favoritesTable.setText(0, 1, "Remove");
		favoritesTable.setText(1, 0, "Library Name");
		favoritesTable.setText(1, 1, "Button");

	}

	// REQUIRES: list of libraries
	// MODIFIES: nothing
	// EFFECTS: displays the favorites table
	private void displayFavoritesTable(List<Library> favoriteLibraries) {
		// TODO Auto-generated method stub
		for (Library l: favoriteLibraries) {
			displayFavorite(l);
		}
	}

	// REQUIRES: library
	// MODIFIES: nothing
	// EFFECTS: displays the library
	private void displayFavorite(Library favoriteLibrary) {
		// TODO Auto-generated method stub
		//remember to add the "remove" button
		
	}
	
	// REQUIRES: nothing
	// MODIFIES: nothing
	// EFFECTS: checks server for duplicates and calls addFavorite
	//note: wait for Yuki. Also, may make into checkDuplicate helper method.
	//Will need to somehow figure out which library we are looking at from table/box in map
	private void addFavorite() {
		// TODO Auto-generated method stub
		//makes Async Callback (get favorites)
		
	}
	
	// REQUIRES: library
	// MODIFIES: nothing
	// EFFECTS: adds the library to the server and calls displayFavorite
	private void addFavorite(Library favoriteLibrary) {
		// TODO Auto-generated method stub
		//makes Async Callback (add favorite)
	}
	
	// REQUIRES: library
	// MODIFIES: nothing
	// EFFECTS: removes the library from server
	private void removeFavorite(Library favoriteLibrary) {
		// TODO Auto-generated method stub
		//makes Async Callback (remove favorite)
		
	}
	
	// REQUIRES: library
	// MODIFIES: nothing
	// EFFECTS: removes the library from the local table
	private void undisplayFavorite(Library favoriteLibrary) {
		// TODO Auto-generated method stub
		
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
			//TODO: link button to add favorite functionality (on click)
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
	
	//REQUIRES: nothing
	//MODIFIES: nothing
	//EFFECTS: shows either a new page or pop-up window so that user can share Library Finder on social media	
	public void shareOnSocialMedia()  {
		// TODO: 
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