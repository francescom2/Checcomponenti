package controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.UtenteBean;
import model.UtenteDAO;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDAO utenteDAO = new UtenteDAO();

    // Mostra la pagina di login
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Se l'utente è già loggato, reindirizzalo al catalogo
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("utente") != null) {
            response.sendRedirect(request.getContextPath() + "/catalogo");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    // Elabora le credenziali inviate
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Inserisci sia l'email che la password.");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }

        try {
            // Verifica le credenziali tramite UtenteDAO (con hashing SHA-512)
            UtenteBean utente = utenteDAO.doRetrieveByEmailAndPassword(email.trim(), password);

            if (utente != null) {
                // Credenziali corrette: salvo l'utente in sessione
                HttpSession session = request.getSession();
                session.setAttribute("utente", utente);

                // Se l'utente è admin, reindirizza all'area riservata admin, altrimenti al catalogo
                if (utente.isAdmin()) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/catalogo");
                }
            } else {
                // Credenziali errate
                request.setAttribute("error", "Email o password non corrette.");
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Errore del server durante l'accesso. Riprova più tardi.");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }
}