package service;

import model.Place;

import java.util.Collection;

public interface PlaceService {
    Collection<Place> getPlaces();

    boolean setPlace(Collection<Place> places);
}
