package listeners;

import com.google.gson.Gson;
import controllers.HallServlet;
import model.Place;

import javax.servlet.http.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SessionActivityListener implements HttpSessionListener, HttpSessionAttributeListener {
    private static final Map<String, Set<Place>> CHOSE_PLACES = new ConcurrentHashMap<>();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("session id: " + se.getSession().getId() + " was created");
        CHOSE_PLACES.put(se.getSession().getId(), new LinkedHashSet<>());
        print();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("session id: " + se.getSession().getId() + " was destroy");
        var places = CHOSE_PLACES.get(se.getSession().getId());
        places.forEach(x->x.setState(Place.FREE));
        String json = new Gson().toJson(places);
        HallServlet.printAsyncContext(json);
        print();
        CHOSE_PLACES.remove(se.getSession().getId());
    }

    public static Set<Place> getChosePlaces() {
        print();
        return CHOSE_PLACES
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        System.out.println("attribute for session id: " + event.getSession().getId() + " was add");
        if ("place".equals(event.getName())) {
            var places = (Set<Place>) event.getValue();
            CHOSE_PLACES.put(event.getSession().getId(), places);
            print();
        }

    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        System.out.println("attribute for session id: " + event.getSession().getId() + " was removed");
        print();
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        attributeAdded(event);
    }

    private static void print() {
        for(var cp : CHOSE_PLACES.entrySet()) {
            System.out.println("Key: " + cp.getKey() + " Values: ");
            cp.getValue().forEach(System.out::println);
        }
    }
}
