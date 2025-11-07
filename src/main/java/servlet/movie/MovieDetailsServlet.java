package servlet.movie;


import dto.MovieDTO;
import dto.UserDTO;
import service.MovieService;
import service.UserWatchedService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@WebServlet(name="MovieDetailsServlet", urlPatterns="/movies/view")
public class MovieDetailsServlet extends javax.servlet.http.HttpServlet {
    private MovieService movieService;
    private UserWatchedService userWatchedService;

    @Override
    public void init() {
        ServletContext sc = getServletContext();
        Map<String, Object> services = (Map<String, Object>) sc.getAttribute("services");
        this.movieService = (MovieService) services.get("movieService");
        this.userWatchedService = (UserWatchedService) services.get("userWatchedService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (Exception e) {
            resp.sendError(400);
            return;
        }
        Optional<MovieDTO> movie = movieService.getById(id);
        if (!movie.isPresent()) {
            resp.sendError(404);
            return;
        }
        req.setAttribute("movie", movie.get());

        UserDTO user = (UserDTO) req.getSession().getAttribute("user");
        boolean isWatched = false;
        if (user != null) {
            isWatched = userWatchedService.isWatched(user.getId(), id);
        }
        req.setAttribute("isWatched", isWatched);

        req.getRequestDispatcher("/WEB-INF/jsp/movie.jsp").forward(req, resp);
    }
}