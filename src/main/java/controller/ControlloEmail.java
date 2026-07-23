package controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.UtenteDAO;

@WebServlet("/controlloEmail")
public class ControlloEmail extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDAO utenteDAO = new UtenteDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        boolean esiste = false;

        if (email != null && !email.trim().isEmpty()) {
            try {
                esiste = utenteDAO.doCheckEmail(email.trim());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Restituisce un JSON chiaro al client
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"exists\": " + esiste + "}");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}