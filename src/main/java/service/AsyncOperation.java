package service;

import com.google.gson.Gson;
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

    private static AsyncOperation INSTANCE = new AsyncOperation();

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
        boolean result = !this.places
                .values()
                .stream()
                .flatMap(x -> x.stream())
                .anyMatch(x -> places.contains(x));
        if (result) {
            this.places.put(id, places);
        }
        return result;
    }

    private Set<Place> combinerPlaces() {
        Set<Place> sessionPlaces =
                places.entrySet()
                        .stream()
                        // .filter(x-> !Objects.equals(id, x.getKey()))
                        .flatMap(x -> x.getValue().stream())
                        .collect(Collectors.toSet());
        sessionPlaces.addAll(cacheDB);
        return sessionPlaces;
    }

    private String getJsonPlaces() {
        return new Gson().toJson(combinerPlaces());
    }
}
