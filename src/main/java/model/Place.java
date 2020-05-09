package model;

import java.util.Objects;

public class Place {
    int placeId;
    int place;
    int state;
    Account account;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Place place1 = (Place) o;
        return place == place1.place;
    }

    @Override
    public int hashCode() {
        return Objects.hash(place);
    }
}
