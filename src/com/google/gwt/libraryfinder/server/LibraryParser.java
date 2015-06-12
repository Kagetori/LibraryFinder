package com.google.gwt.libraryfinder.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gwt.libraryfinder.shared.LatLon;
import com.google.gwt.libraryfinder.shared.Library;

public class LibraryParser {

	// make a http request to get data in a string that can be made into a JSONObject
	public String makeHTTPRequest() throws MalformedURLException, IOException {
        String httpRequest = "http://catalogue.data.gov.bc.ca/api/action/datastore_search?resource_id=b6a28bfb-580b-4662-9f00-f7189d52fbe6";
		URL url = new URL(httpRequest);
        HttpURLConnection client = (HttpURLConnection) url.openConnection();
        InputStream in = client.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String returnString = br.readLine();
        client.disconnect();
        //System.out.println(returnString);
        return returnString;
    }
	
	//REQUIRES: valid string that can be made into a JSONObject
    //MODIFIES: nothing
    //EFFECTS: parses the string from the URL to create a new library, adds it to a list
	//			of libraries, and return that list
	public List<Library> parse(String returnString) throws JSONException {
		System.out.println(returnString);
		int num = 0;
		String s = "";
		List<Library> libraries = new ArrayList<Library>();
		JSONObject object = new JSONObject(returnString);
		JSONObject result = object.getJSONObject("result");
		System.out.println("parsed result");
		JSONArray records = result.getJSONArray("records");
		System.out.println("parsed records");
		int size = records.length();
		for (int i=0; i < size; i++) {
			JSONObject library = records.getJSONObject(i);
			String city = library.getString("City");
			System.out.println(city);
			String name = library.getString("Branch or Service Point Name");
			System.out.println(name);
			String phone = library.getString("Phone");
			System.out.println(phone);
			String postalCode = library.getString("Postal Code");
			System.out.println("parsed postalCode");
			String address = library.getString("Physical Address");
			System.out.println("parsed address");
			Double lat = library.getDouble("Latitude");
			System.out.println("parsed lat");
			Double lon = library.getDouble("Longitude");
			System.out.println("parsed lon");
			LatLon latLon = new LatLon(lat, lon);
			Library newLibrary = new Library(name, city, address, postalCode, phone, latLon);
			num++;
			s = Integer.toString(num);
			System.out.println(s + "libraries created");
			libraries.add(newLibrary);
			System.out.println(s + "libraries added to library list");
		}
		
		return libraries;
	}
}
