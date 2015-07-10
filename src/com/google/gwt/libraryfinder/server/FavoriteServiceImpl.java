package com.google.gwt.libraryfinder.server;

import java.util.List;

import com.google.gwt.libraryfinder.client.FavoriteService;
import com.google.gwt.libraryfinder.shared.Library;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FavoriteServiceImpl extends RemoteServiceServlet implements FavoriteService {
	
	
	//REQUIRES: nothing
	//MODIFIES: nothing
	//EFFECTS: returns list of favorite libraries from server
	@Override
	public List<Library> getFavorites() {
		// TODO Auto-generated method stub
		return null;
	}

	//REQUIRES: valid library in favorites
	//MODIFIES: list of favorite libraries stored on server
	//EFFECTS: removes specified library from list of favorite libraries on server
	@Override
	public void removeFavorite(Library favoriteLibrary) {
		// TODO Auto-generated method stub
		
	}

	//REQUIRES: valid library
	//MODIFIES: list of favorite libraries stored on server
	//EFFECTS: adds specified library to list of favorite libraries on server
	@Override
	public void addFavorite(Library favoriteLibrary) {
		// TODO Auto-generated method stub
		
	}

}
