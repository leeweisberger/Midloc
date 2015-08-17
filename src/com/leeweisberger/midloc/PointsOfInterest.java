package com.leeweisberger.midloc;

public enum PointsOfInterest {
	AMUSEMENT_PARK("Amusement Park","amusement_park"),
	AQUARIUM("Aquarium","aquarium"),
	ART_GALLERY("Art Gallery", "art_gallery"),
	BARS("Bar", "bar"),
	BOWLING_ALLEY("Bowling Alley","bowling_alley"),
	CASINO("Casino","casino"),
	MOVIE_THEATER("Movie Theater", "movie_theater"),
	NIGHT_CLUB("Night Club","night_club"),
	RESTAURANTS("Restaurant","restaurant"),
	SHOPPING_MALL("Shopping Mall", "shopping_mall"),
	SPA("Spa", "spa"),
	STADIUM("Stadium", "stadium"),
	ZOO("Zoo","zoo");
	
	private String view;
	private String api;
	
	private PointsOfInterest(String view, String api) {
		this.view=view;
		this.api=api;
	}

	public String getViewPOI() {
		return view;
	}

	public String getApiPOI() {
		return api;
	}
	

}
