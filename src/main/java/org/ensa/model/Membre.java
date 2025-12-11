package org.ensa.model;

import java.time.LocalDate;
import java.util.Objects;

public class Membre implements Comparable<Membre> {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private LocalDate dateInscription;

    public Membre() {
    }

    public Membre(int id, String nom, String prenom, String email, LocalDate dateInscription) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.dateInscription = dateInscription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    @Override
    public int compareTo(Membre autre) {
        int compareNom = this.nom.compareTo(autre.nom);
        return compareNom != 0 ? compareNom : this.prenom.compareTo(autre.prenom);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Membre membre = (Membre) o;
        return Objects.equals(email, membre.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "Membre{" + "id=" + id + ", nom='" + nom + '\'' + ", prenom='" + prenom + '\'' + ", email='" + email + '\'' + ", dateInscription=" + dateInscription + '}';
    }
}
