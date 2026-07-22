package model;

public class OrderItemBean {
    private long id;
    private String nome;
    private long idProdotto;
    private long idOrdine;
    private double prezzo;
    private int quantita;
    private String iva; // Corrisponde al tipo ENUM('4','10','22')

    public OrderItemBean() {}

    // Getter e Setter
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public long getIdProdotto() { return idProdotto; }
    public void setIdProdotto(long idProdotto) { this.idProdotto = idProdotto; }

    public long getIdOrdine() { return idOrdine; }
    public void setIdOrdine(long idOrdine) { this.idOrdine = idOrdine; }

    public double getPrezzo() { return prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    public int getQuantita() { return quantita; }
    public void setQuantita(int quantita) { this.quantita = quantita; }

    public String getIva() { return iva; }
    public void setIva(String iva) { this.iva = iva; }

    // Calcolo subtotale singola riga d'ordine (comprensivo di IVA)
    public double getSubtotaleConIva() {
        double aliquota = Double.parseDouble(iva);
        return (prezzo * quantita) * (1 + aliquota / 100.0);
    }
}