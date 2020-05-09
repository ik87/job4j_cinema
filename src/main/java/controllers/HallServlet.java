package controllers;

import com.google.gson.Gson;
import listeners.SessionActivityListener;
import model.Place;
import service.PlaceService;
import service.PlaceServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HallServlet extends HttpServlet {
    //private final PlaceService placeService = PlaceServiceImpl.getInstance();
    //private final Set<Place> places = new ConcurrentSkipListSet<>(placeService.getPlaces());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
/*        HttpSession session = req.getSession();

        synchronized (session) {
            session.setMaxInactiveInterval(20);
        }
        Set<Place> choosePlaces = SessionActivityListener.getChosePlaces();

        //Set<Place> combinePlaces = new ConcurrentSkipListSet<>(places);
        //combinePlaces.addAll(choosePlaces);

        Set<Place> combinePlaces = Stream
                .concat(places.stream(), choosePlaces.stream())
                .collect(Collectors.toSet());

        String json = new Gson().toJson(combinePlaces);
        SessionActivityListener.getChosePlaces();*/
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        //resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String places = req.getParameter("places");
        //System.out.println(places);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
