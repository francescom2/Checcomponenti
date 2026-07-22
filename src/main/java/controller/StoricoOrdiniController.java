package controller;

import model.OrdineBean;
import model.OrdineDAO;
import model.UtenteBean;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/StoricoOrdini")
public class StoricoOrdiniController extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utente") : null;

        // Se l'utente non è loggato, reindirizza al login
        if (utente == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Nessun DataSource da passare!
            OrdineDAO ordineDAO = new OrdineDAO(); 
            List<OrdineBean> ordini = ordineDAO.doRetrieveByUtente(utente.getId());
            
            request.setAttribute("ordini", ordini);
            request.getRequestDispatcher("/WEB-INF/jsp/storicoOrdini.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(500, "Errore durante il recupero dello storico ordini");
        }
    }
}