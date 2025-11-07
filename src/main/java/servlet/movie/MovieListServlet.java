package servlet.movie;


import dto.UserDTO;
import entity.Movie;
import service.MovieService;
import service.UserWatchedService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(name="MovieListServlet", urlPatterns={"/movies"})
public class MovieListServlet extends javax.servlet.http.HttpServlet {
    private MovieService movieService;
    private UserWatchedService watchedService;

    @Override
    public void init() {
        ServletContext sc = getServletContext();
        Map<String, Object> services = (Map<String, Object>) sc.getAttribute("services");
        this.movieService = (MovieService) services.get("movieService");
        this.watchedService = (UserWatchedService) services.get("userWatchedService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String genre = req.getParameter("genre");
        String director = req.getParameter("director");
        Double minRating = null;
        try {
            String s = req.getParameter("minRating");
            if (s != null && !s.isEmpty()) minRating = Double.parseDouble(s);
        } catch (NumberFormatException ignored) {}

        List<Movie> movies = movieService.search(genre, minRating, director);

        req.setAttribute("movies", movies);

        UserDTO user = (UserDTO) req.getSession().getAttribute("user");
        if (user != null) {
            Set<Long> watchedIds = new HashSet<>(watchedService.getWatchedMovieIds(user.getId()));
            req.setAttribute("watchedIds", watchedIds);
        }

        req.getRequestDispatcher("/WEB-INF/jsp/movies.jsp").forward(req, resp);
    }
}