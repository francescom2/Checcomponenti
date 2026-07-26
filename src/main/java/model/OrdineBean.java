package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrdineBean {
    private long id;
    private long idUtente;
    private long infoConsegna;
    private Timestamp dataOrdine;
    
    // Dati calcolati o associati da JOIN
    private String indirizzoConsegnaFormatted; 
    private List<OrderItemBean> items = new ArrayList<>();

    public OrdineBean() {}

    // Getter e Setter
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getIdUtente() { return idUtente; }
    public void setIdUtente(long idUtente) { this.idUtente = idUtente; }

    public long getInfoConsegna() { return infoConsegna; }
    public void setInfoConsegna(long infoConsegna) { this.infoConsegna = infoConsegna; }

    public Timestamp getDataOrdine() { return dataOrdine; }
    public void setDataOrdine(Timestamp dataOrdine) { this.dataOrdine = dataOrdine; }

    public String getIndirizzoConsegnaFormatted() { return indirizzoConsegnaFormatted; }
    public void setIndirizzoConsegnaFormatted(String indirizzoConsegnaFormatted) { 
        this.indirizzoConsegnaFormatted = indirizzoConsegnaFormatted; 
    }

    public List<OrderItemBean> getItems() { return items; }
    public void setItems(List<OrderItemBean> items) { this.items = items; }

    // Calcola il totale globale dell'ordine sommando tutti gli OrderItem
    public double getTotaleOrdine() {
        double totale = 0;
        for (OrderItemBean item : items) {
            totale += item.getPrezzo() *item.getQuantita();
        }
        return totale;
    }
}