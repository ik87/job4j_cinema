package service;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PollServiceAsyncImpl implements PollService {
    private static final int POLLING_INTERVAL = 30;

    private final List<AsyncContext> context = new LinkedList<>();

    private static final PollServiceAsyncImpl INSTANCE = new PollServiceAsyncImpl();

    private PollServiceAsyncImpl() {

    }

    public static PollServiceAsyncImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public void printContext(String json) {
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

    @Override
    public void addContext(HttpServletRequest req, HttpServletResponse resp) {
        final AsyncContext asyncContext = req.startAsync(req, resp);
        asyncContext.setTimeout(POLLING_INTERVAL * 1000);
        context.add(asyncContext);
    }
}
