package service;

import model.PlaceDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class PlaceServiceTest {
    @Test
    public void whenGetAllPlacesTheGet() {
        PlaceService placeService = PlaceServiceImpl.getInstance();
        List<PlaceDTO> places = new ArrayList<>();
        places.addAll(placeService.getPlaces());
        assertThat(places.size(), is(9));
    }

}