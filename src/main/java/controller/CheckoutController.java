package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Carrello;
import model.InfoConsegnaBean;
import model.InfoConsegnaDAO;
import model.OrdineDAO;
import model.UtenteBean;

@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private InfoConsegnaDAO infoDAO = new InfoConsegnaDAO();
    private OrdineDAO ordineDAO = new OrdineDAO();

    // Mostra la pagina di checkout con il riepilogo
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verifica se l'utente è loggato
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utente") : null;
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Verifica se il carrello esiste e non è vuoto
        Carrello carrello = (Carrello) session.getAttribute("carrello");
        if (carrello == null || carrello.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/carrello");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/jsp/checkout.jsp").forward(request, response);
    }

    // Processa la conferma dell'ordine
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utente") : null;
        Carrello carrello = (session != null) ? (Carrello) session.getAttribute("carrello") : null;

        if (utente == null || carrello == null || carrello.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/catalogo");
            return;
        }

        String destinatario = request.getParameter("destinatario");
        String via = request.getParameter("via");
        String citta = request.getParameter("citta");
        String capStr = request.getParameter("cap");
        String altro = request.getParameter("altro");

        try {
            int cap = Integer.parseInt(capStr);

            // 1. Salva Indirizzo di Consegna
            InfoConsegnaBean info = new InfoConsegnaBean();
            info.setDestinatario(destinatario);
            info.setVia(via);
            info.setCitta(citta);
            info.setCap(cap);
            info.setAltro(altro);
            info.setIdUtente(utente.getId());

            long idInfo = infoDAO.doSave(info);

            // 2. Salva Ordine e OrderItems
            long idOrdine = ordineDAO.doSaveOrder(utente.getId(), idInfo, carrello.getItems());

            // 3. Svuota il carrello in sessione
            session.removeAttribute("carrello");

            // Redirect alla pagina di conferma
            request.setAttribute("idOrdine", idOrdine);
            request.getRequestDispatcher("/WEB-INF/jsp/confermaOrdine.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Errore durante l'elaborazione dell'ordine. Riprova.");
            request.getRequestDispatcher("/WEB-INF/jsp/checkout.jsp").forward(request, response);
        }
    }
}