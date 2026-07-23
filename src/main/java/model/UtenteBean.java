package model;

import java.io.Serializable;

public class UtenteBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String username;
    private String email;
    private String imgPath;
    private boolean isAdmin;
    private String password;

    public UtenteBean() {
        this.imgPath = "img/iconaUtente.png";
        this.isAdmin = false;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getImgPath() { return imgPath; }
    public void setImgPath(String imgPath) { this.imgPath = imgPath; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}