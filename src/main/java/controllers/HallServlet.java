package controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Place;
import service.AsyncOperation;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.BiConsumer;


@WebServlet(urlPatterns = {"/serv"}, asyncSupported = true)
public class HallServlet extends HttpServlet {
    private static final int POLLING_INTERVAL = 30;
    private static final int SESSION_INACTIVE_INTERVAL = 50;


    private final AsyncOperation asyncOperation = AsyncOperation.getInstance();
    private Map<String, BiConsumer<HttpServletRequest, HttpServletResponse>> road = new HashMap<>();

    @Override
    public void init() throws ServletException {
        road.put("poll", this::poll);
        road.put("new", this::newPage);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        road.get(action).accept(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String placesJson = req.getParameter("places");

        Type itemsSetType = new TypeToken<Set<Place>>() {
        }.getType();
        Set<Place> places = (new Gson()).fromJson(placesJson, itemsSetType);

        places.forEach(x -> x.setState(Place.RESERVED));
        HttpSession session = getSession(req);

        boolean result = asyncOperation.addPlaces(session.getId(), places);
        asyncOperation.printAsyncContext();

        String json = new Gson().toJson("busy");

        if (result) {
            json = new Gson().toJson(places);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(json);
        printWriter.flush();
    }

    void poll(HttpServletRequest req, HttpServletResponse resp) {
        final AsyncContext asyncContext = req.startAsync(req, resp);
        asyncContext.setTimeout(POLLING_INTERVAL * 1000);
        asyncOperation.addContext(asyncContext);
    }

    void newPage(HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter printWriter = resp.getWriter()) {
            String json = asyncOperation.getJsonPlaces();
            printWriter.write(json);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
