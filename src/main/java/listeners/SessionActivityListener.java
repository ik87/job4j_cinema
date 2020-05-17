package listeners;

import service.*;

import javax.servlet.http.*;

public class SessionActivityListener implements HttpSessionListener {

    private final PollService pollService = PollServiceAsyncImpl.getInstance();
    private final PlaceService placeService = PlaceServiceImpl.getInstance();

    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        placeService.removePlacesFromCache(se.getSession().getId());
        String json = placeService.getJsonAllPlaces();
        pollService.printContext(json);
    }
}
