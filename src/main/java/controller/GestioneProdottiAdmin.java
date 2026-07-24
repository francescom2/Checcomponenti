package controller;

import model.ProdottoBean;
import model.ProdottoDAO;
import model.CategoriaBean;
import model.CategoriaDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/GestioneProdottiAdmin")
public class GestioneProdottiAdmin extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ProdottoDAO prodottoDAO = new ProdottoDAO();
    private CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        try {
            // Se è stata richiesta un'azione su un prodotto specifico
            if (idParam != null && !idParam.isEmpty()) {
                long id = Long.parseLong(idParam);

                if ("hide".equalsIgnoreCase(action)) {
                    // Nascondi
                    prodottoDAO.setVisibilita(id, false);
                    response.sendRedirect(request.getContextPath() + "/GestioneProdottiAdmin?msg=hidden");
                    return;

                } else if ("show".equalsIgnoreCase(action)) {
                    // Mostra
                    prodottoDAO.setVisibilita(id, true);
                    response.sendRedirect(request.getContextPath() + "/GestioneProdottiAdmin?msg=shown");
                    return;

                } else if ("delete".equalsIgnoreCase(action)) {
                    // Elimina dal database
                    try {
                        prodottoDAO.doDelete(id);
                        response.sendRedirect(request.getContextPath() + "/GestioneProdottiAdmin?msg=deleted");
                        return;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        // Se fallisce per vincolo di Foreign Key (prodotto già in un ordine passato)
                        response.sendRedirect(request.getContextPath() + "/GestioneProdottiAdmin?error=fk_constraint");
                        return;
                    }
                }
            }

            // Carica i dati aggiornati per la view
            List<ProdottoBean> prodotti = prodottoDAO.doRetrieveAll();
            List<CategoriaBean> categorie = categoriaDAO.doRetrieveAll();

            request.setAttribute("prodotti", prodotti);
            request.setAttribute("categorie", categorie);

            request.getRequestDispatcher("/WEB-INF/jsp/admin/crud.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(500, "Errore durante la gestione del catalogo prodotti.");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            String nome = request.getParameter("nome");
            long idCategoria = Long.parseLong(request.getParameter("idCategoria"));
            double prezzo = Double.parseDouble(request.getParameter("prezzo"));
            String iva = request.getParameter("iva");
            int quantita = Integer.parseInt(request.getParameter("quantita"));
            String imgPath = request.getParameter("imgPath");
            String descrizione = request.getParameter("descrizione");
            
            ProdottoBean prodotto = new ProdottoBean();
            prodotto.setNome(nome);
            prodotto.setIdCategoria(idCategoria);
            prodotto.setPrezzo(prezzo);
            prodotto.setIva(iva);
            prodotto.setQuantita(quantita);
            prodotto.setDescrizione(descrizione);
                        
	        // Se l'admin lascia il campo vuoto
	        if (imgPath == null || imgPath.trim().isEmpty()) {
	            imgPath = "img/imgNonTrovata.png";
	        } else {
	            prodotto.setImgPath(imgPath);

	        }
	            
            if ("update".equalsIgnoreCase(action)) {
                long id = Long.parseLong(request.getParameter("id"));
                prodotto.setId(id);
                prodottoDAO.doUpdate(prodotto);
            } else {
                prodotto.setVisibile(true); // Nuovo prodotto visibile di default
                prodottoDAO.doSave(prodotto);
            }

            // Redirect al pannello con messaggio di successo
            response.sendRedirect(request.getContextPath() + "/GestioneProdottiAdmin?msg=success");

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendError(400, "Dati del form non validi.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(500, "Errore salvataggio prodotto nel database.");
        }
    }
}