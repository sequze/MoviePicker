package servlet.admin_api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Movie;
import service.MovieService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "AdminMoviesApiServlet", urlPatterns = {"/api/admin/movies/*"})
public class AdminMoviesApiServlet extends HttpServlet {
    private transient MovieService movieService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        ServletContext sc = getServletContext();
        Map<String, Object> services = (Map<String, Object>) sc.getAttribute("services");
        this.movieService = (MovieService) services.get("movieService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String pathInfo = req.getPathInfo(); // may be null or like /{id}
        if (pathInfo == null || pathInfo.equals("/")) {
            List<Movie> movies = movieService.getAll();
            mapper.writeValue(resp.getWriter(), movies);
            return;
        }
        Long id = parseIdFromPath(pathInfo);
        if (id == null) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid id in path");
            return;
        }
        Optional<Movie> movieOpt = movieService.getEntityById(id);
        if (!movieOpt.isPresent()) {
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Movie not found");
            return;
        }
        mapper.writeValue(resp.getWriter(), movieOpt.get());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try {
            JsonNode root = mapper.readTree(req.getReader());
            Movie movie = jsonToMovie(root, null);
            Movie saved = movieService.createOrUpdate(movie);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            mapper.writeValue(resp.getWriter(), saved);
        } catch (IllegalArgumentException e) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String pathInfo = req.getPathInfo();
        Long id = parseIdFromPath(pathInfo);
        if (id == null) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid id in path");
            return;
        }
        Optional<Movie> existingOpt = movieService.getEntityById(id);
        if (!existingOpt.isPresent()) {
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Movie not found");
            return;
        }
        Movie existing = existingOpt.get();
        try {
            JsonNode root = mapper.readTree(req.getReader());
            Movie patched = jsonToMovie(root, existing);
            patched.setId(id);
            Movie saved = movieService.createOrUpdate(patched);
            mapper.writeValue(resp.getWriter(), saved);
        } catch (IllegalArgumentException e) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        Long id = parseIdFromPath(pathInfo);
        if (id == null) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid id in path");
            return;
        }
        boolean ok = movieService.delete(id);
        if (!ok) {
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Movie not found");
            return;
        }
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private Long parseIdFromPath(String pathInfo) {
        if (pathInfo == null || pathInfo.isEmpty() || "/".equals(pathInfo)) return null;
        String s = pathInfo.startsWith("/") ? pathInfo.substring(1) : pathInfo;
        int slash = s.indexOf('/');
        if (slash >= 0) s = s.substring(0, slash);
        try { return Long.parseLong(s); } catch (NumberFormatException e) { return null; }
    }

    private void sendError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        mapper.writeValue(resp.getWriter(), Map.of("error", message));
    }

    private Movie jsonToMovie(JsonNode root, Movie base) {
        Movie m = base == null ? new Movie() : copy(base);
        if (root.has("name") && !root.get("name").isNull()) m.setName(root.get("name").asText());
        else if (base == null) throw new IllegalArgumentException("name cannot be null");
        if (root.has("description")) m.setDescription(root.get("description").isNull() ? null : root.get("description").asText());
        if (root.has("director_id") && !root.get("director_id").isNull()) m.setDirectorId(root.get("director_id").asLong());
        else if (base == null) throw new IllegalArgumentException("directorId cannot be null");
        if (root.has("genre_id") && !root.get("genre_id").isNull()) m.setGenreId(root.get("genre_id").asLong());
        else if (base == null) throw new IllegalArgumentException("genreId cannot be null");
        if (root.has("rating") && !root.get("rating").isNull()) m.setRating(root.get("rating").asDouble());
        else if (base == null) throw new IllegalArgumentException("rating cannot be null");
        // posterUrl теперь необязательный параметр (можно загрузить через отдельный эндпоинт)
        if (root.has("poster_url")) m.setPosterUrl(root.get("poster_url").isNull() ? null : root.get("poster_url").asText());
        if (root.has("year") && !root.get("year").isNull()) m.setYear(root.get("year").asInt());
        else if (base == null) throw new IllegalArgumentException("year cannot be null");
        return m;
    }

    private Movie copy(Movie src) {
        Movie m = new Movie();
        m.setId(src.getId());
        m.setName(src.getName());
        m.setDescription(src.getDescription());
        m.setDirectorId(src.getDirectorId());
        m.setGenreId(src.getGenreId());
        m.setRating(src.getRating());
        m.setPosterUrl(src.getPosterUrl());
        m.setYear(src.getYear());
        return m;
    }
}

