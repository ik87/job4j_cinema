package service;

import com.google.gson.Gson;
import model.Account;
import model.PlaceDTO;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AsyncOperation {
    private final List<AsyncContext> context = new LinkedList<>();
    private final Map<String, Set<PlaceDTO>> places = new ConcurrentHashMap<>();
    private final PlaceService placeService = PlaceServiceImpl.getInstance();
    private final Set<PlaceDTO> cacheDB;

    private AsyncOperation() {
        cacheDB = new LinkedHashSet<>(placeService.getPlaces());
    }

    private static final AsyncOperation INSTANCE = new AsyncOperation();

    public static AsyncOperation getInstance() {
        return INSTANCE;
    }

    public void printAsyncContext() {
        String json = getJsonPlaces();
        List<AsyncContext> asyncContexts = new ArrayList<>(context);
        context.clear();
        for (AsyncContext asyncContext : asyncContexts) {
            try {
                HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                try (PrintWriter printWriter = response.getWriter()) {
                    printWriter.write(json);
                    printWriter.flush();
                }
                asyncContext.complete();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void addContext(AsyncContext cont) {
        context.add(cont);
    }

    public void removePlaces(String id) {
        places.remove(id);
    }

    public boolean addPlaces(String id, Set<PlaceDTO> places) {

        boolean notIntersect = checkIntersectPlaces(places);

        //if places haven't been chosen earlier then map them with session
        if (notIntersect) {
            places = preparePlaces(places);
            places.forEach(x -> x.setState(PlaceDTO.RESERVED));
            var val = this.places.get(id);
            if (val != null) {
                val.addAll(places);
            } else {
                this.places.put(id, places);
            }
        }
        return notIntersect;
    }

    public boolean savePlaces(Set<PlaceDTO> places, Account account) {
        boolean result = false;
        if (placeService.setPlace(places, account)) {
            //update cache places
            cacheDB.clear();
            cacheDB.addAll(placeService.getPlaces());
            result = true;
        }
        return result;
    }

    private Set<PlaceDTO> getChosePlaces() {
        return places.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

    }

    public String getJsonPlaces() {
        Map<String, Set<PlaceDTO>> map = new HashMap<>();
        map.put("reserved", getChosePlaces());
        map.put("db_places", cacheDB);
        return new Gson().toJson(map);
    }

    public Set<PlaceDTO> getPlaces(String id) {
        //Set<PlaceDTO> preparePlaces = new HashSet<>(cacheDB);
        //preparePlaces.retainAll(places.get(id));
        // return preparePlaces;
        return places.get(id);
    }

    /**
     * Checking chosen places in other sessions
     *
     * @param places put places
     * @return true if put places not intersect with exist places
     */
    boolean checkIntersectPlaces(Set<PlaceDTO> places) {

        return !places.isEmpty() ? !this.places
                .values()
                .stream()
                .flatMap(Set::stream)
                .anyMatch(places::contains) : false;
    }

    Set<PlaceDTO> preparePlaces(Set<PlaceDTO> places) {

        return cacheDB
                .stream()
                .filter(places::contains)
                .map(x -> {
                    PlaceDTO placeDTO = new PlaceDTO();
                    placeDTO.setState(x.getState());
                    placeDTO.setPlace(x.getPlace());
                    placeDTO.setPrice(x.getPrice());
                    return placeDTO;
                })
                .collect(Collectors.toSet());
    }



    public void clear() {
        placeService.clear();
        cacheDB.clear();
        places.clear();
        cacheDB.addAll(placeService.getPlaces());
    }
}
