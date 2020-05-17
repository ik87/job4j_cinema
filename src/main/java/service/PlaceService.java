package service;

import model.Account;
import model.PlaceDTO;

import java.util.Set;

/**
 * Serves places
 *
 * @author Kosolapov Ilya(d_dexter@mail.ru)
 * @version 1.0
 */
public interface PlaceService {
    /**
     * Save places and relation account to DB
     *
     * @param account account
     * @param id relation session's id
     * @return true if successful
     */
    boolean savePlacesToDb(Account account, String id);

    /**
     * Reset table, remove accounts,
     * clear reserved places cache
     * reduce to initial version
     */
    void clear();

    /**
     * Get places in json form.
     *
     * @return json string that consists of  db_places and reserved Place array
     */
    String getJsonAllPlaces();

    /**
     * Get places by id base on session id
     *
     * @param id HttpSession::getId()
     * @return list reserved Places that have relation with session id
     */
    String getJsonReservedPlaces(String id);

    /**
     * Add places to reserved cache
     *
     * @param places reserved places that have relation with session id
     * @param id     HttpSession::getId()
     * @return true if successful
     */
    boolean addPlacesToCache(Set<PlaceDTO> places, String id);

    /**
     * remove places that have relation with session id
     *
     * @param id relation with session's id
     */
    void removePlacesFromCache(String id);
}
