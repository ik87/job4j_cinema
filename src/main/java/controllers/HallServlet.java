package controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import listeners.SessionActivityListener;
import model.Place;
import service.PlaceService;
import service.PlaceServiceImpl;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet(urlPatterns = {"/serv"}, asyncSupported=true)
public class HallServlet extends HttpServlet {
    private static final int POLLING_INTERVAL = 30;
    private static final int SESSION_INACTIVE_INTERVAL = 50;

    private final PlaceService placeService = PlaceServiceImpl.getInstance();
    private final Set<Place> places = new ConcurrentSkipListSet<>(placeService.getPlaces());
    private List<AsyncContext> contexts = new LinkedList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = getSession(req);
        System.out.println("I am here");
        final AsyncContext asyncContext = req.startAsync(req, resp);
        asyncContext.setTimeout(POLLING_INTERVAL * 1000);
        contexts.add(asyncContext);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<AsyncContext> asyncContexts = new ArrayList<>(this.contexts);
        this.contexts.clear();

        String placesJson = req.getParameter("places");
        Type itemsSetType = new TypeToken<Set<Place>>() {
        }.getType();
        Set<Place> places = (new Gson()).fromJson(placesJson, itemsSetType);
        HttpSession session = getSession(req);
        session.setAttribute("place", places);

        Set<Place> choosePlaces = SessionActivityListener.getChosePlaces();

        Set<Place> combinePlaces = Stream
                .concat(places.stream(), choosePlaces.stream())
                .collect(Collectors.toSet());

        String json = new Gson().toJson(combinePlaces);

        for (AsyncContext asyncContext : asyncContexts) {
            try {
                HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                try(PrintWriter printWriter = response.getWriter()) {
                    printWriter.write(json);
                    printWriter.flush();
                }
                asyncContext.complete();
            } catch (Exception ex) {

            }
        }
    }

    private HttpSession getSession(HttpServletRequest req) {
        HttpSession session = req.getSession();

        synchronized (session) {
            session.setMaxInactiveInterval(SESSION_INACTIVE_INTERVAL);
        }
        return session;
    }



}
