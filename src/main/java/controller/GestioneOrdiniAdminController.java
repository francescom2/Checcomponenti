package controller;

import model.OrdineBean;
import model.OrdineDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/GestioneOrdiniAdmin")
public class GestioneOrdiniAdminController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {


        String dataInizio = request.getParameter("dataInizio");
        String dataFine = request.getParameter("dataFine");
        String idUtente = request.getParameter("idUtente");

        try {
            OrdineDAO ordineDAO = new OrdineDAO();
            List<OrdineBean> ordini;

            // Se l'admin ha applicato dei filtri
            if ((dataInizio != null && !dataInizio.isEmpty()) || 
                (dataFine != null && !dataFine.isEmpty()) || 
                (idUtente != null && !idUtente.isEmpty())) {
                
                ordini = ordineDAO.doRetrieveByFilter(dataInizio, dataFine, idUtente);
            } else {
                // Altrimenti mostra tutti gli ordini
                ordini = ordineDAO.doRetrieveAll();
            }

            request.setAttribute("ordini", ordini);
            request.getRequestDispatcher("/WEB-INF/jsp/admin/gestioneOrdiniAdmin.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(500, "Errore durante il recupero degli ordini.");
        }
    }
}