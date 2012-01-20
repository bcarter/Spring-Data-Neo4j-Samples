package com.habuma.checkins;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.SpatialRepository;

public interface PlaceRepository extends GraphRepository<Place>, SpatialRepository<Place> {

}
