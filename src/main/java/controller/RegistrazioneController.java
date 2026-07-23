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

@WebServlet("/registrazione")
public class RegistrazioneController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDAO utenteDAO = new UtenteDAO();

    // Regex per validazione server-side
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_-]{3,20}$";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/registrazione.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validazione Server-Side
        if (username == null || !username.matches(USERNAME_REGEX)) {
            request.setAttribute("error", "Username non valido (da 3 a 20 caratteri alfanumerici).");
            request.getRequestDispatcher("/WEB-INF/jsp/registrazione.jsp").forward(request, response);
            return;
        }

        if (email == null || !email.matches(EMAIL_REGEX)) {
            request.setAttribute("error", "Indirizzo e-mail non valido.");
            request.getRequestDispatcher("/WEB-INF/jsp/registrazione.jsp").forward(request, response);
            return;
        }

        if (password == null || password.length() < 6) {
            request.setAttribute("error", "La password deve contenere almeno 6 caratteri.");
            request.getRequestDispatcher("/WEB-INF/jsp/registrazione.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Le password inserite non coincidono.");
            request.getRequestDispatcher("/WEB-INF/jsp/registrazione.jsp").forward(request, response);
            return;
        }

        try {
            // Verifica duplicato email
            if (utenteDAO.doCheckEmail(email)) {
                request.setAttribute("error", "Email già registrata. Scegli un'altra email o effettua il login.");
                request.getRequestDispatcher("/WEB-INF/jsp/registrazione.jsp").forward(request, response);
                return;
            }

            // Creazione nuovo utente
            UtenteBean utente = new UtenteBean();
            utente.setUsername(username);
            utente.setEmail(email);
            utente.setPassword(password); // Verrà cifrata con SHA-512 dentro utenteDAO.doSave
            utente.setAdmin(false);

            utenteDAO.doSave(utente);

            // Recupera l'utente appena creato per effettuare subito l'autologin
            UtenteBean utenteCreato = utenteDAO.doRetrieveByEmailAndPassword(email, password);

            HttpSession session = request.getSession();
            session.setAttribute("utente", utenteCreato);

            // Redirect alla homepage o al catalogo
            response.sendRedirect(request.getContextPath() + "/catalogo");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Errore durante la registrazione. Riprova più tardi.");
            request.getRequestDispatcher("/WEB-INF/jsp/registrazione.jsp").forward(request, response);
        }
    }
}