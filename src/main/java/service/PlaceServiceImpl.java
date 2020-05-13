package service;

import model.Place;
import persistence.DbStore;
import persistence.DbStoreImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlaceServiceImpl implements PlaceService {
    private final DbStore dbStore = DbStoreImpl.getInstance();
    private static final PlaceService INSTANCE = new PlaceServiceImpl();

    private PlaceServiceImpl() {
    }

    public static PlaceService getInstance() {
        return INSTANCE;
    }

    @Override
    public Collection<Place> getPlaces() {
    //    List<Place> places = new ArrayList<>(dbStore.getPlaces());
  //      places.forEach(System.out::println);
        return dbStore.getPlaces();
    }

    @Override
    public boolean setPlace(Place place) {
        return  dbStore.setPlace(place);
    }
}
