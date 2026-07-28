package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ProdottoBean;
import model.ProdottoDAO;

@WebServlet("/RicercaProdotti")
public class RicercaProdottiController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String query = request.getParameter("q");
        
        // Impostiamo l'header HTTP per indicare che rispondiamo in JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Se la stringa è troppo corta, restituiamo un array JSON vuoto
        if (query == null || query.trim().length() < 2) {
            response.getWriter().write("[]");
            return;
        }

        ProdottoDAO dao = new ProdottoDAO();
        try {
            List<ProdottoBean> risultati = dao.doRetrieveByNome(query.trim());

            // Costruzione manuale della stringa JSON 
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < risultati.size(); i++) {
                ProdottoBean p = risultati.get(i);
                
                json.append("{")
                    .append("\"id\":").append(p.getId()).append(",")
                    .append("\"nome\":\"").append(p.getNome().replace("\"", "\\\"")).append("\",")
                    .append("\"prezzo\":").append(p.getPrezzo()).append(",")
                    .append("\"imgPath\":\"").append(p.getImgPath() != null ? p.getImgPath() : "img/imgNonTrovata.png").append("\"")
                    .append("}");

                if (i < risultati.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");

            // Inviamo il JSON al client
            response.getWriter().write(json.toString());

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("[]");
        }
    }
}