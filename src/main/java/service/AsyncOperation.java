package service;

import com.google.gson.Gson;
import model.Account;
import model.Place;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AsyncOperation {
    private final List<AsyncContext> context = new LinkedList<>();
    private final Map<String, Set<Place>> places = new ConcurrentHashMap<>();
    private final PlaceService placeService = PlaceServiceImpl.getInstance();
    private final Set<Place> cacheDB;

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

    public boolean addPlaces(String id, Set<Place> places) {
        //checking chosen places in other sessions
        boolean result = !places.isEmpty() ? !this.places
                .values()
                .stream()
                .flatMap(Set::stream)
                .anyMatch(places::contains) : false;

        //if places haven't been chosen earlier then map them with session
        if (result) {
            var val = this.places.get(id);
            if (val != null) {
                val.addAll(places);
            } else {
                this.places.put(id, places);
            }
        }
        return result;
    }

    public void bindAccountAndPlaces(Set<Place> places, Account account) {
        places.forEach(x -> {
                    x.setState(Place.RESERVED);
                    x.setAccount(account);
                }
        );
    }

    public boolean savePlaces(Set<Place> places) {
        boolean result = false;
        if(placeService.setPlace(places)) {
           result = cacheDB.addAll(placeService.getPlaces());
        }
        return result;
    }

    private Set<Place> chosePlaces() {
        return places.values().stream()
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet());

    }

    public String getJsonPlaces() {
        Map<String, Set<Place>> map = new HashMap<>();
        map.put("reserved", chosePlaces());
        map.put("db_places", cacheDB);
        return new Gson().toJson(map);
    }

    public Set<Place> getPlaces(String id) {
        Set<Place> preparePlaces = new HashSet<>(cacheDB);
        preparePlaces.retainAll(places.get(id));
        return preparePlaces;
    }
}
