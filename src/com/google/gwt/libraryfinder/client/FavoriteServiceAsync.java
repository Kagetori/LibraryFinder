package com.google.gwt.libraryfinder.client;

import java.util.List;

import com.google.gwt.libraryfinder.shared.Library;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FavoriteServiceAsync {

	void getFavorites(AsyncCallback<List<Library>> asyncCallback);
	void removeFavorite(Library favoriteLibrary, AsyncCallback<Void> asyncCallback);
	void addFavorite(Library favoriteLibrary, AsyncCallback<Void> asyncCallback);

}
