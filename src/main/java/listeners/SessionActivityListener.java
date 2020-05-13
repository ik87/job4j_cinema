package listeners;

import service.AsyncOperation;

import javax.servlet.http.*;

public class SessionActivityListener implements HttpSessionListener {

    private final AsyncOperation asyncOperation = AsyncOperation.getInstance();

    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        asyncOperation.removePlaces(se.getSession().getId());
        asyncOperation.printAsyncContext();
    }
}
