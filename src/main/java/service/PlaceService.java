package service;

import model.Place;

import java.util.Collection;

public interface PlaceService {
    Collection<Place> getPlaces();
    Place setPlace(Place place);
}
