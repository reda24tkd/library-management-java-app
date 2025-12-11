package org.ensa.model;

import java.util.Objects;

public class Livre implements Comparable<Livre> {
    private int id;
    private String titre;
    private String auteur;
    private String isbn;
    private int anneePublication;
    private boolean disponible;

    public Livre() {
    }

    public Livre(int id, String titre, String auteur, String isbn, int anneePublication, boolean disponible) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.anneePublication = anneePublication;
        this.disponible = disponible;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public int getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public int compareTo(Livre autre) {
        return this.titre.compareTo(autre.titre);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livre livre = (Livre) o;
        return Objects.equals(isbn, livre.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return "Livre{" + "id=" + id + ", titre='" + titre + '\'' + ", auteur='" + auteur + '\'' + ", isbn='" + isbn + '\'' + ", anneePublication=" + anneePublication + ", disponible=" + disponible + '}';
    }
}
