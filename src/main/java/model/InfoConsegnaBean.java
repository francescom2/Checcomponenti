package model;

import java.io.Serializable;

public class InfoConsegnaBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String citta;
    private int cap;
    private String via;
    private String altro;
    private String destinatario;
    private long idUtente;

    public InfoConsegnaBean() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta; }

    public int getCap() { return cap; }
    public void setCap(int cap) { this.cap = cap; }

    public String getVia() { return via; }
    public void setVia(String via) { this.via = via; }

    public String getAltro() { return altro; }
    public void setAltro(String altro) { this.altro = altro; }

    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }

    public long getIdUtente() { return idUtente; }
    public void setIdUtente(long idUtente) { this.idUtente = idUtente; }
}