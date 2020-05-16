package model;

import java.util.Objects;

public class PlaceDTO {
    public static final int FREE = 1;
    public static final int RESERVED = 2;

    private String place;
    private int state;
    private float price;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlaceDTO placeDTO = (PlaceDTO) o;
        return Objects.equals(place, placeDTO.place);
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
