package model;

import java.io.Serializable;

public class CategoriaBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String nome;

    // Costruttore vuoto (Obbligatorio per i JavaBean)
    public CategoriaBean() {}

    // Costruttore 
    public CategoriaBean(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getter e Setter 
    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}
}