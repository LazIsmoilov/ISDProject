package uts.isd.controller;

import uts.isd.model.User;
import uts.isd.model.dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserDAO userService;

    @Override
    public void init() {
        userService = new UserDAO(); // Initialize DAO
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // get input from form; trim for delete 'space'
        String fullName = request.getParameter("fullName").trim();
        String email = request.getParameter("email").trim();
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String phone = request.getParameter("phone");

        //  input validation
        if (fullName.isEmpty() || password.isEmpty()) {
            forwardWithError(request, response, "Username and password cannot be empty");
            return;
        }

//        check password length
        if (password.length() < 8) {
            forwardWithError(request, response, "Password must be at least 8 characters long");
            return;
        }

        //  Create a User object and give value for DAO
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setRole(role);

        try {
            // Check if the user already exists
            if (userService.emailExists(email)) {
                forwardWithError(request, response, "Email already registered");
                return;
            }

            userService.registerUser(user);

            //  Redirect to login page with success
            request.setAttribute("success", true);
            request.getRequestDispatcher("login.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            forwardWithError(request, response, "System error. Please try again later.");
        }
    }

    //    redirect to register if error
    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String errorMsg)
            throws ServletException, IOException {
        request.setAttribute("error", errorMsg);
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}