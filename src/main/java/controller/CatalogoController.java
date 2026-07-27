package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.CategoriaBean;
import model.CategoriaDAO;
import model.ProdottoBean;
import model.ProdottoDAO;

@WebServlet(urlPatterns = {"/catalogo", ""})
public class CatalogoController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // UTF-8 per evitare ? per i caratteri esotici
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        ProdottoDAO dao = new ProdottoDAO();
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        
        try {
            // 1. Prelevare la lista dei prodotti dal DB
            Collection<ProdottoBean> prodotti = dao.doRetrieveAll(true);
            List<CategoriaBean> categorie = categoriaDAO.doRetrieveAll();
            
            // 2. Spostare la lista dentro la request con il nome prodotti
            request.setAttribute("prodotti", prodotti);
            request.setAttribute("categorie", categorie);

        } catch (SQLException e) {
            System.err.println("Errore nel recupero prodotti: " + e.getMessage());
            request.setAttribute("error", "Impossibile caricare il catalogo in questo momento.");
        }

        // 3. Inoltro richiesta alla pagina JSP (View)
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/catalogo.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}