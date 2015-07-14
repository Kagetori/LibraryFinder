package com.google.gwt.libraryfinder.client;

import java.util.List;

import com.google.gwt.libraryfinder.shared.Library;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("favorite")
public interface FavoriteService extends RemoteService {
	
	public List<Library> getFavorites() throws NotLoggedInException;
	public void removeFavorite(Library favoriteLibrary);
	public void addFavorite(Library favoriteLibrary) throws NotLoggedInException;

}
