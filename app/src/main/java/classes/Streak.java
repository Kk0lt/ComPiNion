package classes;

import java.sql.Date;

public class Streak {

    public Date getDateDeb() {
        return dateDeb;
    }

    public void setDateDeb(Date dateDeb) {
        this.dateDeb = dateDeb;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public int getJours() {
        return jours;
    }

    public void setJours(int jours) {
        this.jours = jours;
    }

    Date dateDeb, dateFin;

    public Streak(Date dateDeb, Date dateFin, int jours, int id) {
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
        this.jours = jours;
        this.id = id;
    }

    int jours;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;
}
