package controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Account;
import model.PlaceDTO;
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
        asyncOperation.clear();
        asyncOperation.printAsyncContext();
    }

    void poll(HttpServletRequest req, HttpServletResponse resp) {
        final AsyncContext asyncContext = req.startAsync(req, resp);
        asyncContext.setTimeout(POLLING_INTERVAL * 1000);
        asyncOperation.addContext(asyncContext);
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
            String json = asyncOperation.getJsonPlaces();
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
                asyncOperation.removePlaces(session.getId());
                session.invalidate();
                asyncOperation.printAsyncContext();
            }
        }
    }

    void payment(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            synchronized (session) {
                Set<PlaceDTO> places = asyncOperation.getPlaces(session.getId());
                for (PlaceDTO place : places) {
                    System.out.println(place.getPrice());
                }
                String json = new Gson().toJson(places);
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

    void chosePlaces(HttpServletRequest req, HttpServletResponse resp) {
        String placesJson = req.getParameter("places");
        Type itemsSetType = new TypeToken<Set<PlaceDTO>>() {
        }.getType();
        Set<PlaceDTO> places = (new Gson()).fromJson(placesJson, itemsSetType);
        HttpSession session = getSession(req);
        boolean result = asyncOperation.addPlaces(session.getId(), places);
        asyncOperation.printAsyncContext();

        if (result) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            asyncOperation.removePlaces(session.getId());
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
                    Set<PlaceDTO> places = asyncOperation.getPlaces(session.getId());
                    if (asyncOperation.savePlaces(places, account)) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                }
                session.invalidate();
                asyncOperation.printAsyncContext();
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
