package service;

import model.Place;

import java.util.Collection;

public class PlaceServiceImpl implements PlaceService {
    private static final PlaceService INSTANCE = new PlaceServiceImpl();

    private PlaceServiceImpl() {
    }

    public static PlaceService getInstance() {
        return INSTANCE;
    }

    @Override
    public Collection<Place> getPlaces() {
        return null;
    }

    @Override
    public Place setPlace(Place place) {
        return null;
    }
}
