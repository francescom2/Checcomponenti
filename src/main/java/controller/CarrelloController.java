package controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Carrello;
import model.ProdottoBean;
import model.ProdottoDAO;

@WebServlet("/carrello")
public class CarrelloController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAO prodottoDAO = new ProdottoDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Carrello carrello = (Carrello) session.getAttribute("carrello");
        
        // Se il carrello non esiste ancora in sessione, lo creiamo
        if (carrello == null) {
            carrello = new Carrello();
            session.setAttribute("carrello", carrello);
        }

        String action = request.getParameter("action");

        if (action != null) {
            try {
                if (action.equalsIgnoreCase("add")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    ProdottoBean prodotto = prodottoDAO.doRetrieveByKey(id);
                    if (prodotto != null) {
                        carrello.addProdotto(prodotto);
                    }
                } else if (action.equalsIgnoreCase("remove")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    carrello.removeProdotto(id);
                } else if (action.equalsIgnoreCase("update")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    int qty = Integer.parseInt(request.getParameter("quantita"));
                    carrello.setQuantita(id, qty);
                } else if (action.equalsIgnoreCase("clear")) {
                    carrello.svuota();
                }
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
            }
            
            // 
            String isAjax = request.getParameter("ajax");
            if ("true".equals(isAjax)) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                // Restituiamo una risposta JSON confermando l'operazione
                response.getWriter().write("{\"status\":\"success\"}");
                return;
            }

            // Comportamento classico se non è una chiamata AJAX (fallback)
            response.sendRedirect(request.getContextPath() + "/carrello");
            return;
        }
        // Mostra la JSP del carrello
        request.getRequestDispatcher("/WEB-INF/jsp/carrello.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}