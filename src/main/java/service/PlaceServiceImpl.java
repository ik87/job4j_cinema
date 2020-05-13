package service;

import model.Place;
import persistence.DbStore;
import persistence.DbStoreImpl;

import java.util.Collection;

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
        return dbStore.getPlaces();
    }

    @Override
    public boolean setPlace(Collection<Place> places) {
        return  dbStore.setPlace(places);
    }
}
