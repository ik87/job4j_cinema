package listeners;

import model.Place;

import javax.servlet.http.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SessionActivityListener implements HttpSessionListener, HttpSessionAttributeListener {
    private static final Map<HttpSession, Set<Place>> CHOSE_PLACES = new ConcurrentHashMap<>();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        //System.out.println("session id: " + se.getSession().getId() + " was created");
        CHOSE_PLACES.put(se.getSession(), new LinkedHashSet<>());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        //System.out.println("session id: " + se.getSession().getId() + " was destroy");
        CHOSE_PLACES.remove(se.getSession());
    }

    public static Set<Place> getChosePlaces() {
        return CHOSE_PLACES
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        if ("place".equals(event.getName())) {
            var places = CHOSE_PLACES.get(event.getSession());
            places.add((Place) event.getValue());
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {

    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        attributeAdded(event);
    }
}
