package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProdottoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String nome;
    private String descrizione;
    private double prezzo;
    private int quantita;
    private String imgPath;
    private String iva;          
    private long idCategoria; 
    private boolean visibile;

    // Costruttore vuoto (obbligatorio per i Java Bean)
    public ProdottoBean() {
        this.id = -1;
        this.nome = "";
        this.descrizione = "";
        this.prezzo = 0.0;
        this.quantita = 0;
        this.imgPath = "";
        this.iva = "22";
        this.idCategoria = -1;
        this.visibile = false;
    }

    // Getter e Setter
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public Double getPrezzo() { return prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    public int getQuantita() { return quantita; }
    public void setQuantita(int quantita) { this.quantita = quantita; }

    public String getImgPath() { return imgPath; }
    public void setImgPath(String imgPath) { this.imgPath = imgPath; }

    public String getIva() { return iva; }
    public void setIva(String iva) { this.iva = iva; }

    public long getIdCategoria() { return idCategoria; }
    public void setIdCategoria(long idCategoria) { this.idCategoria = idCategoria; }
    
    public boolean getVisibile() { return visibile;}
    public void setVisibile (boolean visibile) {this.visibile = visibile;}
    
    public double getPrezzoSenzaIva() {
    	double aliquota = Double.parseDouble(iva); 
        return (prezzo) / (1 + (aliquota / 100.0));    
    }
    
}