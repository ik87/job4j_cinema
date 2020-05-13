package model;

import java.util.Objects;

public class Place {
    public static final int FREE = 1;
    public static final int RESERVED = 2;
    public static final int TAKEN = 3;

    private String place;
    private int state;
    private Account account;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Place place1 = (Place) o;
        return Objects.equals(place, place1.place);
    }

    @Override
    public int hashCode() {
        return Objects.hash(place);
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Place{" +
                "place='" + place + '\'' +
                ", state=" + state +
                ", account=" + account +
                '}';
    }
}
