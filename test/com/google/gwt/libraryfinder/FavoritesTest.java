package com.google.gwt.libraryfinder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gwt.libraryfinder.shared.LatLon;
import com.google.gwt.libraryfinder.shared.Library;

public class FavoritesTest {
	
	LatLon ll1 = new LatLon(49.274931, -123.070318);
	Library l1 = new Library("Britannia", "Vancouver", "1661 Napier", "V5L 4X4", "(604) 665-2222", ll1);
	LatLon ll2 = new LatLon(54.014086, -124.008737);
	Library l2 = new Library("Vanderhoof", "Vanderhoof", "230 Stewart Drive", "V0J 3A0", "(250) 567-4060", ll2);
	LatLon ll3 = new LatLon(49.329034, -123.16538);
	Library l3 = new Library("West Vancouver", "West Vancouver", "1950 Marine Drive", "V7V 1J8", "(604) 925-7400", ll3);
	LatLon ll4 = new LatLon(50.117127, -122.956295);
	Library l4 = new Library("Whistler", "Whistler", "4329 Main Street", "V0N 1B4", "(604) 935-8433", ll4);
	
	List<Library> libraries = new ArrayList<Library>();
	
	@Before
	public void addLibraries() {
		libraries.clear();
		libraries.add(l1);
		libraries.add(l2);
		libraries.add(l2);
		libraries.add(l3);
	}
	
	@Test
	public void testHasDuplicate() {
		assertTrue(hasDuplicate(l2));
		assertTrue(hasDuplicate(l3));
		assertFalse(hasDuplicate(l4));
	}
	
	public boolean hasDuplicate(Library library) {
		final String libraryName = library.getName();
		Boolean hasDuplicate = false;

		for(Library l: libraries) {
			if(l.getName().equals(libraryName)) {
				hasDuplicate = true;
			}
		}
		return hasDuplicate;
	}
}
