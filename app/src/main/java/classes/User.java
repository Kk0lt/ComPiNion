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
    int character_id;
    @SerializedName("limite")
    int limite;
    private String token;
    @SerializedName("experience")
    int experience;
    @SerializedName("merite")
    int merite;
    @SerializedName("jours")
    int jours;

    String companionImage;

    private boolean isExtended;

    public User(){

    }

    public User( String nom, String prenom, String email, String password, String pseudo, int companion_id, int limite) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.pseudo = pseudo;
        this.character_id = companion_id;
        this.limite = limite;
    }

    public User(int id, String nom, String prenom, String email, String password, String pseudo, int companion_id, int limite) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.pseudo = pseudo;
        this.character_id = companion_id;
        this.limite = limite;
    }
    public User(int id, String nom, String prenom, String email, String password, String pseudo, int merit, int jours, int companion_id, int limite) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.pseudo = pseudo;
        this.merite = merit;
        this.jours = jours;
        this.character_id = companion_id;
        this.limite = limite;
    }

    public User(int id, String nom, String prenom, String email, String password, String pseudo, int merit, int jours, int companion_id, int limite, String companionImage) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.pseudo = pseudo;
        this.merite = merit;
        this.jours = jours;
        this.character_id = companion_id;
        this.limite = limite;
        this.companionImage = companionImage;
    }
    public User(int id, String nom, String prenom, String email, String password, String pseudo, int merit, int jours, int companion_id, int limite, Boolean extension) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.pseudo = pseudo;
        this.merite = merit;
        this.jours = jours;
        this.character_id = companion_id;
        this.limite = limite;
        this.isExtended = extension;
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

    public int getMerite() {
        return merite;
    }

    public void setMerite(int merite) {
        this.merite = merite;
    }

    public int getJours() {
        return jours;
    }

    public void setJours(int jours) {
        this.jours = jours;
    }

    public boolean isExtended() {
        return isExtended;
    }

    public void setExtended(boolean extended) {
        isExtended = extended;
    }
    public int getCompanion_id() {
        return character_id;
    }

    public void setCompanion_id(int companionId) {
        this.character_id = companionId;
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

    public int getCharacter_id() {
        return character_id;
    }

    public void setCharacter_id(int character_id) {
        this.character_id = character_id;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getCompanionImage() {
        return "http://172.16.87.61/img/" + companionImage;
    }

    public String getCompanionPNG() {
        return  companionImage;
    }
    public void setCompanionImage(String companionImage) {
        this.companionImage = companionImage;
    }
}
