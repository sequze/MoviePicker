package servlet;


import dto.UserDTO;
import entity.Movie;
import service.UserWatchedService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name="WatchedListServlet", urlPatterns={"/user/watched"})
public class WatchedListServlet extends HttpServlet {
    private UserWatchedService userWatchedService;

    @Override
    public void init() {
        ServletContext sc = getServletContext();
        Map<String, Object> services = (Map<String, Object>) sc.getAttribute("services");
        this.userWatchedService = (UserWatchedService) services.get("userWatchedService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDTO u = (UserDTO) req.getSession().getAttribute("user");
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        List<Movie> watched = userWatchedService.getWatchedMovies(u.getId());
        req.setAttribute("movies", watched);
        req.getRequestDispatcher("/WEB-INF/jsp/movies.jsp").forward(req, resp);
    }
}