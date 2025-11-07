package servlet;


import dto.UserDTO;
import service.UserWatchedService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet(name="WatchedToggleServlet", urlPatterns={"/movies/watched"})
public class WatchedToggleServlet extends HttpServlet {
    private UserWatchedService userWatchedService;

    @Override
    public void init() {
        ServletContext sc = getServletContext();
        Map<String, Object> services = (Map<String, Object>) sc.getAttribute("services");
        this.userWatchedService = (UserWatchedService) services.get("userWatchedService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDTO user = (UserDTO) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String movieIdStr = req.getParameter("movieId");
        String action = req.getParameter("action"); // "add" | "remove"
        if (movieIdStr == null || action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Long movieId;
        try {
            movieId = Long.parseLong(movieIdStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if ("add".equals(action)) {
            userWatchedService.markWatched(user.getId(), movieId);
        } else if ("remove".equals(action)) {
            userWatchedService.unmarkWatched(user.getId(), movieId);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String referer = req.getHeader("Referer");
        if (referer != null) resp.sendRedirect(referer);
        else resp.sendRedirect(req.getContextPath() + "/movies");
    }
}