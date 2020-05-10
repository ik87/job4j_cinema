package persistence;

import model.Account;
import model.Place;

import java.sql.SQLException;
import java.util.Collection;

/**
 * @author Kosolapov Ilya (d_dexter@mail.ru)
 * @version 1.0
 */
public interface DbStore {
    /**
     * Set new state for place (join account)
     * @param place place
     * @return id place
     */
    boolean setPlace(Place place);

/*    *//**
     * Add new account;
     * @param account account
     * @return id account
     *//*
    Long addAccount(Account account);*/
    /**
     * get all places
     * @return all exist places
     */
    Collection<Place> getPlaces();

}
