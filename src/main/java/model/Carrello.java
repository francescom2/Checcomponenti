package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Carrello implements Serializable {
    private static final long serialVersionUID = 1L;

    // Classe interna per rappresentare la singola riga del carrello (prodotto + quantità)
    public static class ItemCarrello implements Serializable {
        private static final long serialVersionUID = 1L;
        private ProdottoBean prodotto;
        private int quantita;

        public ItemCarrello(ProdottoBean prodotto, int quantita) {
            this.prodotto = prodotto;
            this.quantita = quantita;
        }

        public ProdottoBean getProdotto() { return prodotto; }
        public void setProdotto(ProdottoBean prodotto) { this.prodotto = prodotto; }
        
        public int getQuantita() { return quantita; }
        public void setQuantita(int quantita) { this.quantita = quantita; }

        public double getTotaleParziale() {
            return prodotto.getPrezzo() * quantita;
        }
    }

    private List<ItemCarrello> items;

    public Carrello() {
        items = new ArrayList<>();
    }

    public List<ItemCarrello> getItems() {
        return items;
    }

    // Aggiunge un prodotto o ne incrementa la quantità se esiste già
    public void addProdotto(ProdottoBean prodotto) {
        for (ItemCarrello item : items) {
            if (item.getProdotto().getId() == prodotto.getId()) { 
                item.setQuantita(item.getQuantita() + 1);
                return;
            }
        }
        items.add(new ItemCarrello(prodotto, 1));
    }

    // Rimuove un elemento dal carrello
    public void removeProdotto(int idProdotto) {
        items.removeIf(item -> item.getProdotto().getId() == idProdotto);
    }

    // Aggiorna la quantità di un prodotto
    public void setQuantita(int idProdotto, int quantita) {
        if (quantita <= 0) {
            removeProdotto(idProdotto);
            return;
        }
        for (ItemCarrello item : items) {
            if (item.getProdotto().getId() == idProdotto) {
                item.setQuantita(quantita);
                return;
            }
        }
    }

    // Calcola il totale complessivo del carrello
    public double getTotale() {
        double totale = 0;
        for (ItemCarrello item : items) {
            totale += item.getTotaleParziale();
        }
        return totale;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void svuota() {
        items.clear();
    }
}