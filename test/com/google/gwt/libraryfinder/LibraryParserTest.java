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
		}
		assertFalse(returnString == "");
	}
		
	@Test
	public void testParse() { 
		List<Library> testLibraries = new ArrayList<Library>();
		String testJsonString = "{\"result\":{\"records\":[{\"City\":\"Alert Bay\", \"Latitude\""
				+ ":\"50.577201\", \"Phone\":\"(250)974-5721\", \"Branch or Service Point Name\":"
				+ "\"Alert Bay\", \"Physical Address\":\"118 Fir Street\", \"Longitude\":\"-126.90657\", "
				+ "\"Postal Code\":\"V0N 1A0\"},{\"City\":\"Richmond\", \"Latitude\":\"50.577201\", "
				+ "\"Phone\":\"(250)974-5721\", \"Branch or Service Point Name\":\"Alert Bay\", "
				+ "\"Physical Address\":\"118 Fir Street\", \"Longitude\":\"-126.90657\", \"Postal Code\":"
				+ "\"V0N 1A0\"}]}}";

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
