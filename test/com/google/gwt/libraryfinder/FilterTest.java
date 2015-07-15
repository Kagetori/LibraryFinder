package com.google.gwt.libraryfinder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.gwt.libraryfinder.shared.LatLon;
import com.google.gwt.libraryfinder.shared.Library;

public class FilterTest {
	
	private List<Library> libraries = new ArrayList<Library>();
	

	LatLon ll1 = new LatLon(49.274931, -123.070318);
	Library l1 = new Library("Britannia", "Vancouver", "1661 Napier", "V5L 4X4", "(604) 665-2222", ll1);
	LatLon ll2 = new LatLon(54.014086, -124.008737);
	Library l2 = new Library("Vanderhoof", "Vanderhoof", "230 Stewart Drive", "V0J 3A0", "(250) 567-4060", ll2);
	LatLon ll3 = new LatLon(49.329034, -123.16538);
	Library l3 = new Library("West Vancouver", "West Vancouver", "1950 Marine Drive", "V7V 1J8", "(604) 925-7400", ll3);
	LatLon ll4 = new LatLon(50.117127, -122.956295);
	Library l4 = new Library("Whistler", "Whistler", "4329 Main Street", "V0N 1B4", "(604) 935-8433", ll4);
	
	public void addLibraries(){
		libraries.add(l1);
		libraries.add(l2);
		libraries.add(l3);
		libraries.add(l4);
	}
	
	private ArrayList<String> getCityNames() {
		ArrayList<String> cityNames = new ArrayList<String>();
		for (Library l: libraries) {
			if (!duplicateCity(cityNames, l.getCity())){
				cityNames.add(l.getCity());
			}
		}
		Collections.sort(cityNames);
		return cityNames;
	}
	
	private boolean duplicateCity(List<String> cities, String city){
		boolean duplicated = false;
		for (String c: cities) {
			if (c.equals(city)) {
				duplicated = true;
			}
		}
		return duplicated;
	}
	
	@Test
	public void duplicateCityTest(){
		List<String> cityNames = new ArrayList<String>();
		cityNames.add("Vancouver");
		cityNames.add("Vanderhoof");
		cityNames.add("West Vancouver");
		cityNames.add("Whistler");
		assertTrue(duplicateCity(cityNames, "Vancouver"));
	}
	
	@Test
	public void getCityNamesTest(){
		addLibraries();
		ArrayList<String> cityNames = getCityNames();
		assertEquals(cityNames.size(), 4);
	}
	
}
