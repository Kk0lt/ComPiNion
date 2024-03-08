package classes;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    int id;
    @SerializedName("nom")
    String nom;
    @SerializedName("prenom")
    String prenom;
    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;
    @SerializedName("pseudo")
    String pseudo;
    @SerializedName("character_id")
    int companion_id;
    @SerializedName("limite")
    int limite;
    private String token;


    public User(){

    }
    public User( String nom, String prenom, String email, String password, String pseudo, int companion_id, int limite) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.pseudo = pseudo;
        this.companion_id = companion_id;
        this.limite = limite;
    }

    public User(int id, String nom, String prenom, String email, String password, String pseudo, int companion_id, int limite) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.pseudo = pseudo;
        this.companion_id = companion_id;
        this.limite = limite;
    }
    public int getId() { return id; }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getCompanion_id() {
        return companion_id;
    }

    public void setCompanion_id(int companion_id) {
        this.companion_id = companion_id;
    }

    public int getLimite() {
        return limite;
    }

    public void setLimite(int limite) {
        this.limite = limite;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
