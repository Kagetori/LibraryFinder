package com.google.gwt.libraryfinder.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.libraryfinder.client.FavoriteService;
import com.google.gwt.libraryfinder.client.NotLoggedInException;
import com.google.gwt.libraryfinder.shared.Library;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FavoriteServiceImpl extends RemoteServiceServlet implements FavoriteService {

	private static final PersistenceManagerFactory PMF = 
			JDOHelper.getPersistenceManagerFactory("transactions-optional");


	//REQUIRES: nothing
	//MODIFIES: nothing
	//EFFECTS: returns list of favorite libraries from server
	@Override
	public List<Library> getFavorites() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = PMF.getPersistenceManager();
		List<Library> favoriteLibraries = new ArrayList<Library>();

		try {
			Query q = pm.newQuery(Favorite.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<Favorite> favorites = (List<Favorite>) q.execute(getUser());

			for(Favorite f: favorites) {
				Library l = f.getLibrary();
				favoriteLibraries.add(l);
			}
		} finally {
			pm.close();
		}
		return favoriteLibraries;
	}

	//REQUIRES: valid library in favorites
	//MODIFIES: list of favorites stored on server
	//EFFECTS: removes specified library from list of favorites on server
	@Override
	public void removeFavorite(Library favoriteLibrary) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = PMF.getPersistenceManager();
		String deleteName = favoriteLibrary.getName();

		try {
			Query q = pm.newQuery(Favorite.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<Favorite> favorites = (List<Favorite>) q.execute(getUser());

			for(Favorite f: favorites) {
				Library l = f.getLibrary();
				if(l.getName().equals(deleteName)) {
					pm.deletePersistent(f);
				}
			}
		} finally {
			pm.close();
		}
	}

	//REQUIRES: valid library
	//MODIFIES: list of favorite libraries stored on server
	//EFFECTS: adds specified library to list of favorites on server
	@Override
	public void addFavorite(Library favoriteLibrary) throws NotLoggedInException {
		checkLoggedIn();
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

	//REQUIRES: nothing
	//MODIFIES: nothing
	//EFFECTS: throws NotLoggedInException if user is not logged in
	private void checkLoggedIn() throws NotLoggedInException {
		if (getUser() == null) {
			throw new NotLoggedInException("Not logged in.");
		}
	}

	//EFFECTS: returns current user
	private User getUser() {
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}
}
