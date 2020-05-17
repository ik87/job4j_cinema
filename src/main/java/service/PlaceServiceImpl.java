package service;

import com.google.gson.Gson;
import model.Account;
import model.Place;
import model.PlaceDTO;
import persistence.Store;
import persistence.DbStoreImpl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PlaceServiceImpl implements PlaceService {
    /**
     * Serves that works with DB
     */
    private final Store dbStore = DbStoreImpl.getInstance();

    /**
     * cache db reservedPlaces
     */
    private final Set<PlaceDTO> cacheDB = new LinkedHashSet<>();

    /**
     * Reserved places that have relations with session's id.
     * Save here before places will be paid
     */
    private final Map<String, Set<PlaceDTO>> reservedPlaces = new ConcurrentHashMap<>();

    private static final PlaceService INSTANCE = new PlaceServiceImpl();

    private PlaceServiceImpl() {
        cacheDB.addAll(getPlacesFromDb());
    }

    public static PlaceService getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean savePlacesToDb(Account account, String id) {
        boolean result = false;
/*        Collection<Place> reservedPlaces = placesDTO
                .stream()
                .map(this::toPlace)
                .collect(Collectors.toList());*/

        Set<Place> reserved = reservedPlaces.get(id)
                .stream().map(this::toPlace).collect(Collectors.toSet());

        if (dbStore.setPlace(reserved, account)) {
            cacheDB.clear();
            cacheDB.addAll(getPlacesFromDb());
            result = true;
        }

        return result;
    }

    @Override
    public void clear() {
        reservedPlaces.clear();
        cacheDB.clear();
        dbStore.clear();
        cacheDB.addAll(getPlacesFromDb());
    }

    @Override
    public String getJsonAllPlaces() {
        Map<String, Set<PlaceDTO>> map = new HashMap<>();
        map.put("reserved", getReservedPlaces());
        map.put("db_places", cacheDB);
        return new Gson().toJson(map);
    }

    @Override
    public String getJsonReservedPlaces(String id) {
        Set<PlaceDTO> reserved = reservedPlaces.get(id);
        return new Gson().toJson(reserved);
    }

    @Override
    public boolean addPlacesToCache(Set<PlaceDTO> reserved, String id) {

        boolean notIntersect = checkIntersectPlaces(reserved, id);

        //if reservedPlaces haven't been chosen earlier then map them with session
        if (notIntersect) {
            reserved = preparePlaces(reserved);
            reserved.forEach(x -> x.setState(PlaceDTO.RESERVED));
            this.reservedPlaces.put(id, reserved);
        }
        return notIntersect;
    }

    @Override
    public void removePlacesFromCache(String id) {
        reservedPlaces.remove(id);
    }


    private Set<PlaceDTO> getReservedPlaces() {
        return reservedPlaces.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

    }

    /**
     * Check chosen reserved Places with intersect with other reserved places
     * @param places put reservedPlaces
     * @return true if put reservedPlaces not intersect with exist reservedPlaces
     */
    private boolean checkIntersectPlaces(Set<PlaceDTO> places, String id) {

      return !places.isEmpty() ? !this.reservedPlaces
                .entrySet()
                .stream()
                .filter(x -> !id.equals(x.getKey()))
                .flatMap(x -> x.getValue().stream())
                .anyMatch(places::contains) : false;

    }

    /**
     * Add price for every chosen place
     * @param chosePlaces chose places
     * @return places with price
     */
    private Set<PlaceDTO> preparePlaces(Set<PlaceDTO> chosePlaces) {
        return cacheDB
                .stream()
                .filter(chosePlaces::contains)
                .map(this::copyPlaceDTO)
                .collect(Collectors.toSet());
    }

    private Collection<PlaceDTO> getPlacesFromDb() {
        Collection<Place> places = dbStore.getPlaces();
        return places
                .stream()
                .map(this::toPlaceDTO)
                .collect(Collectors.toList());
    }

    private PlaceDTO toPlaceDTO(Place place) {
        PlaceDTO placeDTO = new PlaceDTO();
        placeDTO.setPrice(place.getPrice());
        placeDTO.setState(place.getState());
        placeDTO.setPlace(place.getPlace());
        return placeDTO;
    }

    private PlaceDTO copyPlaceDTO(PlaceDTO place) {
        PlaceDTO placeDTO = new PlaceDTO();
        placeDTO.setPrice(place.getPrice());
        placeDTO.setState(place.getState());
        placeDTO.setPlace(place.getPlace());
        return placeDTO;
    }

    private Place toPlace(PlaceDTO placeDTO) {
        Place place = new Place();
        place.setPrice(placeDTO.getPrice());
        place.setState(placeDTO.getState());
        place.setPlace(placeDTO.getPlace());
        return place;
    }

}
