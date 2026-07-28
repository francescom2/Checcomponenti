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
import java.io.File;
import java.nio.file.Paths;
import javax.servlet.annotation.MultipartConfig;

@WebServlet("/GestioneProdottiAdmin")
// Per il carinamento dei filw
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB max per file
    maxRequestSize = 1024 * 1024 * 50     // 50MB max richiesta
)
public class GestioneProdottiAdmin extends HttpServlet {

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
            String descrizione = request.getParameter("descrizione");
            String existingImgPath = request.getParameter("existingImgPath");

         // Gestione Upload immagine prodotto
            Part filePart = request.getPart("immagine");
            
            // Fallback: se non c'è una vecchia immagine, usa l'immagine di default del sistema
            String imgPath = (existingImgPath != null && !existingImgPath.trim().isEmpty()) 
                             ? existingImgPath 
                             : "img/imgNonTrovata.png";

            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

                String uploadPath = System.getProperty("user.home") + File.separator + "checomponenti_uploads";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                filePart.write(uploadPath + File.separator + uniqueFileName);
                imgPath = "uploads/" + uniqueFileName;
            }
            ProdottoBean prodotto = new ProdottoBean();
            prodotto.setNome(nome);
            prodotto.setIdCategoria(idCategoria);
            prodotto.setPrezzo(prezzo);
            prodotto.setIva(iva);
            prodotto.setQuantita(quantita);
            prodotto.setDescrizione(descrizione);
            prodotto.setImgPath(imgPath);

            if ("update".equalsIgnoreCase(action)) {
                long id = Long.parseLong(request.getParameter("id"));
                prodotto.setId(id);
                prodottoDAO.doUpdate(prodotto);
            } else {
                prodotto.setVisibile(true); // Nuovo prodotto visibile di default
                prodottoDAO.doSave(prodotto);
            }

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