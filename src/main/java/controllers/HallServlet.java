package controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Account;
import model.PlaceDTO;
import service.*;

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

/**
 * @author Kosoloapov Ilya (d_dexter@mial.ru)
 *
 * @version 1.0
 */
@WebServlet(urlPatterns = {"/serv"}, asyncSupported = true)
public class HallServlet extends HttpServlet {
    /**
     * After 50 second inactive session will be remove,
     * and early reserved session place's will be also remove,
     * see SessionActivityListener
     */
    private static final int SESSION_LIVES = 50;

    private final PlaceService placeService = PlaceServiceImpl.getInstance();
    private final PollService pollService = PollServiceAsyncImpl.getInstance();
    private final Map<String, BiConsumer<HttpServletRequest, HttpServletResponse>> road = new HashMap<>();

    @Override
    public void init() throws ServletException {
        road.put("poll", this::poll);
        road.put("new", this::newPage);
        road.put("close", this::closePage);
        road.put("chose", this::chosePlaces);
        road.put("payment", this::payment);
        road.put("buy", this::buy);
        road.put("clear", this::clear);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        road.get(action).accept(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        road.get(action).accept(req, resp);
    }

    void clear(HttpServletRequest req, HttpServletResponse resp) {
        placeService.clear();
        String json = placeService.getJsonAllPlaces();
        pollService.printContext(json);
    }

    void poll(HttpServletRequest req, HttpServletResponse resp) {
        pollService.addContext(req, resp);
    }

    void newPage(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter printWriter = resp.getWriter()) {
            String json = placeService.getJsonAllPlaces();
            printWriter.write(json);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void closePage(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            synchronized (session) {
                session.invalidate();
                String json = placeService.getJsonAllPlaces();
                pollService.printContext(json);
            }
        }
    }


    void chosePlaces(HttpServletRequest req, HttpServletResponse resp) {
        String placesJson = req.getParameter("places");
        Type itemsSetType = new TypeToken<Set<PlaceDTO>>() {
        }.getType();
        Set<PlaceDTO> places = (new Gson()).fromJson(placesJson, itemsSetType);
        HttpSession session = getSession(req);
        synchronized (session) {
            boolean result = placeService.addPlacesToCache(places, session.getId());
            String json = placeService.getJsonAllPlaces();
            pollService.printContext(json);

            if (result) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                session.invalidate();
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    void payment(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            synchronized (session) {
                String json = placeService.getJsonReservedPlaces(session.getId());
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                try (PrintWriter printWriter = resp.getWriter()) {
                    printWriter.write(json);
                    printWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    void buy(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            synchronized (session) {
                String accountJson = req.getParameter("account");
                Account account = (new Gson()).fromJson(accountJson, Account.class);
                String name = account.getName();
                String phone = account.getPhone();

                if (name.length() < 3 && phone.length() < 11) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                } else {
                    if (placeService.savePlacesToDb(account, session.getId())) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                }
                session.invalidate();
                String json = placeService.getJsonAllPlaces();
                pollService.printContext(json);
            }
        }
    }


    private HttpSession getSession(HttpServletRequest req) {
        HttpSession session = req.getSession();
        synchronized (session) {
            session.setMaxInactiveInterval(SESSION_LIVES);
        }
        return session;
    }


}
