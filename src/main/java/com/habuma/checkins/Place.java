package com.habuma.checkins;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
public class Place {

	@GraphId
	private Long nodeId;
	
	private String name;
	private String facebookId;
	private String foursquareId;
	private float latitude;	
	private float longitude;

	// TODO: Does this *have* to be named "wkt" ???
	@Indexed(indexName = "PlaceLocation", indexType = IndexType.POINT)
	private String wkt;
	
	
	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFacebookId() {
		return facebookId;
	}
	
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	
	public String getFoursquareId() {
		return foursquareId;
	}
	
	public void setFoursquareId(String foursquareId) {
		this.foursquareId = foursquareId;
	}
	
	public void setLocation(float latitude, float longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.wkt = String.format("POINT( %.6f %.6f )",longitude, latitude);;
	}
	
	public float getLatitude() {
		return latitude;
	}
	
	public float getLongitude() {
		return longitude;
	}
}
