package service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Some operations for maintains polling
 *
 * @author Kosolapov Ilya (d_dexter@mail.ru)
 * @version 1.0
 */
public interface PollService {
    /**
     * Makes response for  clients that waiting in context
     * release contexts requests
     * @param json json that will be send to client(s)
     */
    void printContext(String json);

    /**
     * New incoming clients catch to context and await there until printContext will be call
     * @param req request
     * @param resp response
     */
    void addContext(HttpServletRequest req, HttpServletResponse resp);
}
