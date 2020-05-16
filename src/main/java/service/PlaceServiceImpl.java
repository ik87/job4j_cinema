package service;

import model.Account;
import model.Place;
import model.PlaceDTO;
import persistence.DbStore;
import persistence.DbStoreImpl;

import java.util.Collection;
import java.util.stream.Collectors;

public class PlaceServiceImpl implements PlaceService {
    private final DbStore dbStore = DbStoreImpl.getInstance();
    private static final PlaceService INSTANCE = new PlaceServiceImpl();

    private PlaceServiceImpl() {
    }

    public static PlaceService getInstance() {
        return INSTANCE;
    }

    @Override
    public Collection<PlaceDTO> getPlaces() {
        Collection<Place> places = dbStore.getPlaces();
        return places
                .stream()
                .map(this::toPlaceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean setPlace(Collection<PlaceDTO> placesDTO, Account account) {
        Collection<Place> places = placesDTO
                .stream()
                .map(x -> this.toPlace(x, account))
                .collect(Collectors.toList());
        return dbStore.setPlace(places);
    }

    private PlaceDTO toPlaceDTO(Place place) {
        PlaceDTO placeDTO = new PlaceDTO();
        placeDTO.setPrice(place.getPrice());
        placeDTO.setState(place.getState());
        placeDTO.setPlace(place.getPlace());
        return placeDTO;
    }

    private Place toPlace(PlaceDTO placeDTO, Account account) {
        Place place = new Place();
        place.setAccount(account);
        place.setPrice(placeDTO.getPrice());
        place.setState(placeDTO.getState());
        place.setPlace(placeDTO.getPlace());
        return place;
    }

    @Override
    public void clear() {
        dbStore.clear();
    }
}
