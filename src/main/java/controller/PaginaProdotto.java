package controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ProdottoBean;
import model.ProdottoDAO;

@WebServlet("/PaginaProdotto")
public class PaginaProdotto extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAO prodottoDAO = new ProdottoDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                // Recupera il singolo prodotto dal DB tramite la chiave primaria
                ProdottoBean prodotto = prodottoDAO.doRetrieveByKey(id);
                
                if (prodotto != null) {
                    request.setAttribute("prodotto", prodotto);
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/paginaProdotto.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
            }
        }
        
        // Se l'ID non è valido o il prodotto non esiste, torna al catalogo
        response.sendRedirect(request.getContextPath() + "/catalogo");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}