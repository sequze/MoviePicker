package servlet.admin_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Movie;
import service.MovieService;
import util.CloudinaryUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "MovieImageUploadServlet", urlPatterns = {"/api/admin/movies/image/*"})
@MultipartConfig(
        maxFileSize = 10 * 1024 * 1024,      // 10 MB
        maxRequestSize = 15 * 1024 * 1024    // 15 MB
)
public class MovieImageUploadServlet extends HttpServlet {
    private transient MovieService movieService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        ServletContext sc = getServletContext();
        Map<String, Object> services = (Map<String, Object>) sc.getAttribute("services");
        this.movieService = (MovieService) services.get("movieService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        // получаем ID фильма
        String pathInfo = req.getPathInfo();
        Long movieId = parseMovieIdFromPath(pathInfo);

        if (movieId == null) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid movie id in path");
            return;
        }

        Optional<Movie> movieOpt = movieService.getEntityById(movieId);
        if (!movieOpt.isPresent()) {
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Movie not found");
            return;
        }

        Part filePart = req.getPart("image");
        if (filePart == null || filePart.getSize() == 0) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "No image file provided");
            return;
        }

        try {
            InputStream inputStream = filePart.getInputStream();
            byte[] imageBytes = inputStream.readAllBytes();
            inputStream.close();

            String imageUrl = CloudinaryUtil.uploadImage(imageBytes);

            if (imageUrl == null || imageUrl.isEmpty()) {
                sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to upload image to Cloudinary");
                return;
            }

            Movie movie = movieOpt.get();
            movie.setPosterUrl(imageUrl);
            Movie updatedMovie = movieService.createOrUpdate(movie);

            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), Map.of(
                "message", "Image uploaded successfully",
                "posterUrl", imageUrl,
                "movie", updatedMovie
            ));

        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error uploading image: " + e.getMessage());
        }
    }

    private Long parseMovieIdFromPath(String pathInfo) {
        if (pathInfo == null || pathInfo.isEmpty()) return null;
        String idStr = pathInfo.substring(1);
        if (idStr.isEmpty()) return null;
        try {
            return Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void sendError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        mapper.writeValue(resp.getWriter(), Map.of("error", message));
    }
}

