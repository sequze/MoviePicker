package servlet;

import dto.UserDTO;
import service.UserService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet(name="RegisterServlet", urlPatterns={"/register"})
public class RegisterServlet extends javax.servlet.http.HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        ServletContext sc = getServletContext();
        Map<String, Object> services = (Map<String, Object>) sc.getAttribute("services");
        this.userService = (UserService) services.get("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            UserDTO u = userService.register(login, email, password);
            req.getSession().setAttribute("user", u);
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (IllegalArgumentException iae) {
            req.setAttribute("error", iae.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
        }
    }
}