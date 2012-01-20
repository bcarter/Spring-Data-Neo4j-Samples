package com.habuma.checkins;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.helpers.collection.ClosableIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PlacesExplorationTest {
	
	private static final double MK_ENTRANCE_LATITUDE = -81.581198;
	private static final double MK_ENTRANCE_LONGITUDE = 28.416191;

	private static final String[] NEAR_MK_ENTRANCE = {
		"Entrance to the Magic Kingdom",
		"Walt Disney World Railroad",
		"Main Street Chamber of Commerce",
		"Tony's Town Square Restaurant",
		"Town Square Theater",
		"City Hall",
		"Ferryboat Landing",
		"Boat Launch",
		"Magic Kingdom Firehouse",
		"Monorail Station",
		"Harmony Barbershop",
		"Main Street Bakery",
		"Plaza Ice Cream Parlor",
		"Casey's Corner",
		"The Plaza Restaurant"
		};
	
	@Autowired
	private PlaceRepository placesRepository;	
	
	@Test
	public void loadPlaces() throws Exception {
		InputStream is = null;
		
		try {
			ClassPathResource placesResource = new ClassPathResource("DisneyWorldPlaces.txt", PlacesExplorationTest.class);
			is = placesResource.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			while (reader.ready()) {
				String line = reader.readLine();
				String[] split = line.split("\\t");
				String name = split[0];
				float lat = Float.parseFloat(split[1]);
				float lon = Float.parseFloat(split[2]);
				String foursquareId = split.length > 3 ? split[3] : null;
				String facebookId = split.length > 4 ? split[4] : null;
				Place place = new Place();
				place.setName(name);
				place.setLocation(lat, lon);
				place.setFoursquareId(foursquareId);
				place.setFacebookId(facebookId);
				placesRepository.save(place);
			}
			
			long totalPlaces = placesRepository.count();
			assertEquals(170, totalPlaces);
			
			ClosableIterable<Place> matches = placesRepository.findWithinDistance("PlaceLocation", 	MK_ENTRANCE_LATITUDE, MK_ENTRANCE_LONGITUDE, 0.2); // 0.2 = approx. 1/8 mile
			ArrayList<Place> placeList = toArray(matches);
			assertEquals(NEAR_MK_ENTRANCE.length, placeList.size());
			// TODO: Assert placeList entries
		} finally {
			placesRepository.deleteAll();
			is.close();
		}
		
	}

	private static ArrayList<Place> toArray(ClosableIterable<Place> matches) {
		ArrayList<Place> placeList = new ArrayList<Place>();
		for (Place place : matches) {
			placeList.add(place);
		}
		return placeList;
	}

	@Test
	@Ignore
	public void extracted() throws Exception{
		Thread.sleep(5000);
		ClosableIterable<Place> matches = placesRepository.findWithinDistance("PlaceLocation", -81.58472, 28.420262, 0.499999);
		System.out.println(matches);
		int i=0;
		for (Place place : matches) {
			System.out.println(place.getName());
			i++;
		}
		System.out.println("FOUND " + i);
	}

}
