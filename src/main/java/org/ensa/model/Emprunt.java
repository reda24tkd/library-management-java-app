package org.ensa.model;

import java.time.LocalDate;

public class Emprunt {
    private int id;
    private Livre livre;
    private Membre membre;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourReel;

    public Emprunt() {}

    public Emprunt(int id, Livre livre, Membre membre, LocalDate dateEmprunt, LocalDate dateRetourPrevue, LocalDate dateRetourReel) {
        this.id = id;
        this.livre = livre;
        this.membre = membre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourReel = dateRetourReel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public void setDateRetourPrevue(LocalDate dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public LocalDate getDateRetourReel() {
        return dateRetourReel;
    }

    public void setDateRetourReel(LocalDate dateRetourReel) {
        this.dateRetourReel = dateRetourReel;
    }

    @Override
    public String toString() {
        return "Emprunt{" +
                "id=" + id +
                ", livre=" + livre +
                ", membre=" + membre +
                ", dateEmprunt=" + dateEmprunt +
                ", dateRetourPrevue=" + dateRetourPrevue +
                ", dateRetourReel=" + dateRetourReel +
                '}';
    }
}
