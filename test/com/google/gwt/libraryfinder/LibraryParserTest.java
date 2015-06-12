package com.google.gwt.libraryfinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.junit.Test;

import com.google.gwt.libraryfinder.server.LibraryParser;
import com.google.gwt.libraryfinder.shared.Library;

import static org.junit.Assert.*;

public class LibraryParserTest {

	LibraryParser libraryParser = new LibraryParser();
	
	@Test
	public void testHTTPRequest() {	
		String returnString= "";
		try {
			returnString = libraryParser.makeHTTPRequest();
		} catch (IOException e) {
			System.out.println("IOException caught");
			e.printStackTrace();
		}
		assertFalse(returnString == "");
	}
		
	@Test
	public void testParse() { 
		List<Library> testLibraries = new ArrayList<Library>();
//		String result = "result";
//		String records = "records";
//		String city = "City";
//		String alertBay = "Alert Bay";
//		String latitude = "Latitude";
//		String lat = "50.577201";
//		String phone = "Phone";
//		String ph = "(250)974-5721";
//		String name = "Branch or Service Point Name";
//		String address = "Physical Address";
//		String add = "118 Fir Street";
//		String longitude = "Longitude";
//		String lon = "-126.90657";
//		String postalCode = "Postal Code";
//		String pc = "V0N 1A0";
		String testJsonString = "{\"result\":{\"records\":[{\"City\":\"Alert Bay\", \"Latitude\":\"50.577201\", \"Phone\":\"(250)974-5721\", \"Branch or Service Point Name\":\"Alert Bay\", \"Physical Address\":\"118 Fir Street\", \"Longitude\":\"-126.90657\", \"Postal Code\":\"V0N 1A0\"},{\"City\":\"Richmond\", \"Latitude\":\"50.577201\", \"Phone\":\"(250)974-5721\", \"Branch or Service Point Name\":\"Alert Bay\", \"Physical Address\":\"118 Fir Street\", \"Longitude\":\"-126.90657\", \"Postal Code\":\"V0N 1A0\"}]}}";
		//System.out.println(testJsonString);
		try {
			testLibraries = libraryParser.parse(testJsonString);
		} catch (JSONException e) {
			System.out.println("JSONException caught");
			e.printStackTrace();
		}
		assertFalse(testLibraries.size() == 0);
		assertEquals(testLibraries.size(), 2);
	}

}
