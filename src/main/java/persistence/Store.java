package persistence;

import model.Account;
import model.Place;
import java.util.Collection;

/**
 * @author Kosolapov Ilya (d_dexter@mail.ru)
 * @version 1.0
 */
public interface Store {
    /**
     * Set new state for place (join account)
     *
     * @param places collection places
     * @return true if success
     */
    boolean setPlace(Collection<Place> places, Account account);


    /**
     * get all places
     *
     * @return all exist places
     */
    Collection<Place> getPlaces();

    void clear();
}
