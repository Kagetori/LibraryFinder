package com.google.gwt.libraryfinder.server;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.libraryfinder.client.FavoriteService;
import com.google.gwt.libraryfinder.shared.Library;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FavoriteServiceImpl extends RemoteServiceServlet implements FavoriteService {
	
	private static final PersistenceManagerFactory PMF = 
			JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	
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
		PersistenceManager pm = PMF.getPersistenceManager();
		Favorite favorite = new Favorite(getUser(), favoriteLibrary);
		
		try {
			pm.makePersistent(favorite);
		} catch (Exception e) {
			System.out.println("There was an error while adding favorite library!");
		} finally {
			pm.close();
		}
	}

	//EFFECTS: returns current user
	private User getUser() {
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}

}
