package servlet.admin_api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Director;
import service.DirectorService;

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

@WebServlet(name = "AdminDirectorsApiServlet", urlPatterns = "/api/admin/directors/*")
public class AdminDirectorsApiServlet extends HttpServlet {
    private DirectorService directorService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        ServletContext sc = getServletContext();
        Map<String, Object> services = (Map<String, Object>) sc.getAttribute("services");
        this.directorService = (DirectorService) services.get("directorService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || "/".equals(pathInfo)) {
            List<Director> all = directorService.getAllDirectors();
            mapper.writeValue(resp.getWriter(), all);
            return;
        }
        Long id = parseIdFromPath(pathInfo);
        if (id == null) { sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid id in path"); return; }
        Optional<Director> d = directorService.getDirectorById(id);
        if (!d.isPresent()) { sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Director not found"); return; }
        mapper.writeValue(resp.getWriter(), d.get());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        JsonNode root = mapper.readTree(req.getReader());
        if (root == null || !root.hasNonNull("name")) { sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "name is required"); return; }
        String name = root.get("name").asText();
        Director dir = directorService.createDirector(name);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        mapper.writeValue(resp.getWriter(), dir);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        Long id = parseIdFromPath(req.getPathInfo());
        if (id == null) { sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid id in path"); return; }
        if (!directorService.getDirectorById(id).isPresent()) { sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Director not found"); return; }
        JsonNode root = mapper.readTree(req.getReader());
        if (root == null || !root.has("name")) { sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "name is required"); return; }
        String name = root.get("name").isNull() ? null : root.get("name").asText();
        if (name == null || name.isEmpty()) { sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "name cannot be null/blank"); return; }
        directorService.updateDirector(id, name);
        mapper.writeValue(resp.getWriter(), new Director(id, name));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = parseIdFromPath(req.getPathInfo());
        if (id == null) { sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid id in path"); return; }
        directorService.deleteDirector(id);
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
}

