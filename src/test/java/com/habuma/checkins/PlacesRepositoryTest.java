package com.habuma.checkins;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PlacesRepositoryTest {
	
	private static final double ONE_EIGHTH_MILE = 0.201168;
	private static final double MK_ENTRANCE_LATITUDE = 28.416191;
	private static final double MK_ENTRANCE_LONGITUDE = -81.581198;
	private static final double EPCOT_ENTRANCE_LATITUDE = 28.37626;
	private static final double EPCOT_ENTRANCE_LONGITUDE = -81.549406;
	private static final double STUDIOS_ENTRANCE_LATITUDE = 28.358658;
	private static final double STUDIOS_ENTRANCE_LONGITUDE = -81.558434;
	private static final double AK_ENTRANCE_LATITUDE = 28.355154;
	private static final double AK_ENTRANCE_LONGITUDE = -81.590091;

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
	
	private static final String[] NEAR_EPCOT_ENTRANCE = {
		"Entrance to Epcot",
		"Monorail Station",
		"Bus Station",
		"Spaceship Earth",
		"Test Track",
		"Coral Reef Restaurant",
		"The Seas with Nemo & Friends",
		"Universe of Energy: Ellen's Energy Adventure",
		"Turtle Talk with Crush"
	};
	
	private static final String[] NEAR_STUDIOS_ENTRANCE = {
		"Entrance to Disney's Hollywood Studios",
		"Beauty and the Beast - Live on Stage",
		"Disney's Hollywood Studios Ferry Terminal",
		"Hollywood & Vine",
		"Play 'N Dine at Hollywood & Vine",
		"50's Prime Time Cafe",
		"Fairfax Fare",
		"Toluca Legs Turkey Co.",
		"Starring Rolls Cafe",
		"Catalina Eddie's",
		"Rosie's All-American Cafe"
	};
	
	private static final String[] NEAR_AK_ENTRANCE = {
		"Entrance to Disney's Animal Kingdom",
		"Garden Gate Gifts",
		"Rainforest Cafe",
		"Outpost",
		"The Oasis Exhibits",
		"Restaurantosaurus",
		"Camp Soft Serve",
		"The Dino Institute Shop"
	};
	
	private PlaceRepository placesRepository;
	
	@Autowired
	public void setPlacesRepository(PlaceRepository placesRepository) {
		if (placesRepository.count() == 0) {
			try {
				loadPlacesData(placesRepository);
			} catch (IOException e) {}
		}
		this.placesRepository = placesRepository;
	}
	
	@Test
	public void testMagicKingdomLocations() throws Exception {		
		assertPlacesMatch(NEAR_MK_ENTRANCE, placesRepository.findWithinDistance("PlaceLocation", 	MK_ENTRANCE_LONGITUDE, MK_ENTRANCE_LATITUDE, ONE_EIGHTH_MILE));
	}

	@Test
	public void testEpcotPlaces() throws Exception {		
		assertPlacesMatch(NEAR_EPCOT_ENTRANCE, placesRepository.findWithinDistance("PlaceLocation", 	EPCOT_ENTRANCE_LONGITUDE, EPCOT_ENTRANCE_LATITUDE, ONE_EIGHTH_MILE));
	}

	@Test
	public void testDisneyStudiosPlaces() throws Exception {		
		assertPlacesMatch(NEAR_STUDIOS_ENTRANCE, placesRepository.findWithinDistance("PlaceLocation", 	STUDIOS_ENTRANCE_LONGITUDE, STUDIOS_ENTRANCE_LATITUDE, ONE_EIGHTH_MILE));
	}

	@Test
	public void testAnimalKingdomPlaces() throws Exception {		
		assertPlacesMatch(NEAR_AK_ENTRANCE, placesRepository.findWithinDistance("PlaceLocation", 	AK_ENTRANCE_LONGITUDE, AK_ENTRANCE_LATITUDE, ONE_EIGHTH_MILE));
	}

	private void assertPlacesMatch(String[] placeNames, Iterable<Place> places) {
		int i=0;
		for (Place place:places) {
			assertEquals(placeNames[i++], place.getName());
		}
		assertEquals(placeNames.length, i);
	}
	
	private void loadPlacesData(PlaceRepository placesRepository) throws IOException {
		InputStream is = null;
		try {
			ClassPathResource placesResource = new ClassPathResource("DisneyWorldPlaces.txt", PlacesRepositoryTest.class);
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
			assertEquals(221, totalPlaces);
		} finally {
			is.close();
		}
	}
}
