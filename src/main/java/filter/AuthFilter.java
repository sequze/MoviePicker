package filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.UserDTO;
import entity.Role;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // no-op
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) req;
        HttpServletResponse w = (HttpServletResponse) res;

        String path = r.getRequestURI();
        UserDTO user = (UserDTO) r.getSession().getAttribute("user");

        boolean isAdminArea = path.startsWith(r.getContextPath() + "/api/admin");
        boolean needLogin = path.startsWith(r.getContextPath() + "/movies/random") || path.startsWith(r.getContextPath() + "/movies/watched") || path.startsWith(r.getContextPath() + "/user/watched");
        if (needLogin && user == null) {
            w.sendRedirect(r.getContextPath() + "/login");
            return;
        }
        if (isAdminArea && (user == null || user.getRole() != Role.admin)) {
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            res.getWriter().write(mapper.writeValueAsString(
                    Map.of("error", "Admin access required")
            ));
            ((HttpServletResponse) res).setStatus(401);
            return;
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {}
}