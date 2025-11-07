package servlet.movie;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.MovieDTO;
import entity.Movie;
import service.MovieService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "RandomMovieServlet", urlPatterns = {"/movies/random"})
public class RandomMovieServlet extends HttpServlet {
    private MovieService movieService;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        ServletContext sc = getServletContext();
        Map<String, Object> services = (Map<String, Object>) sc.getAttribute("services");
        this.movieService = (MovieService) services.get("movieService");
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String genre = req.getParameter("genre");
        String director = req.getParameter("director");
        Double minRating = null;

        try {
            String s = req.getParameter("minRating");
            if (s != null && !s.isEmpty()) {
                minRating = Double.parseDouble(s);
            }
        } catch (NumberFormatException ignored) {
        }

        Optional<Movie> randomMovie = movieService.random(genre, minRating, director);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        if (randomMovie.isPresent()) {
            Movie movie = randomMovie.get();
            Optional<MovieDTO> fullMovie = movieService.getById(movie.getId());

            if (fullMovie.isPresent()) {
                MovieDTO m = fullMovie.get();
                result.put("found", true);
                result.put("id", m.getId());
                result.put("title", m.getName());
                result.put("genre", m.getGenre() != null ? m.getGenre() : "");
                result.put("director", m.getDirector() != null ? m.getDirector() : "");
                result.put("rating", m.getRating());
                result.put("posterUrl", m.getPosterUrl());
            } else {
                result.put("found", false);
            }
        } else {
            result.put("found", false);
        }

        resp.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
