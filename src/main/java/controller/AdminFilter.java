package controller;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.UtenteBean; 

// Protegge tutto dentro la cartella admin
@WebFilter(urlPatterns = {"/GestioneProdottiAdmin", "/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inizializzazione 
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utente") : null;

        // se l'utente esiste ed è un amministratore
        boolean isAdmin = (utente != null) && (utente.isAdmin());

        if (isAdmin) {
        	// utente autorizzato
            chain.doFilter(request, response);
        } else {
            // utente non autorizzato
            res.sendRedirect(req.getContextPath() + "/login.jsp?error=unauthorized");
        }
    }

    @Override
    public void destroy() {
    }
}