package service;

import model.Account;
import model.PlaceDTO;

import java.util.Collection;

public interface PlaceService {
    Collection<PlaceDTO> getPlaces();

    boolean setPlace(Collection<PlaceDTO> places, Account account);

    void clear();
}
