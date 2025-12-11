package org.ensa.dao;

import org.ensa.database.DatabaseConnection;
import org.ensa.model.Emprunt;
import org.ensa.model.Livre;
import org.ensa.model.Membre;
import org.ensa.model.Result;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class EmpruntDAO implements DAO<Emprunt> {

    @Override
    public Result<Emprunt> create(Emprunt emprunt) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO emprunts (livre_id, membre_id, dateEmprunt, dateRetourPrevue, dateRetourReel) VALUES (?, ?, ?, ?, ?)",
                 Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, emprunt.getLivre().getId());
            stmt.setInt(2, emprunt.getMembre().getId());
            stmt.setDate(3, Date.valueOf(emprunt.getDateEmprunt()));
            stmt.setDate(4, Date.valueOf(emprunt.getDateRetourPrevue()));
            stmt.setDate(5, emprunt.getDateRetourReel() != null ? Date.valueOf(emprunt.getDateRetourReel()) : null);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) emprunt.setId(rs.getInt(1));
            return new Result<>(emprunt, "Créé", true);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    @Override
    public Result<Emprunt> findById(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT e.*, l.*, m.* FROM emprunts e JOIN livres l ON e.livre_id = l.id JOIN membres m ON e.membre_id = m.id WHERE e.id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Result<>(mapEmprunt(rs), "Trouvé", true);
            }
            return new Result<>(null, "Non trouvé", false);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    @Override
    public Result<List<Emprunt>> findAll() {
        List<Emprunt> emprunts = new LinkedList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT e.*, l.*, m.* FROM emprunts e JOIN livres l ON e.livre_id = l.id JOIN membres m ON e.membre_id = m.id")) {
            while (rs.next()) {
                emprunts.add(mapEmprunt(rs));
            }
            return new Result<>(emprunts, "OK", true);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    @Override
    public Result<Emprunt> update(Emprunt emprunt) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "UPDATE emprunts SET livre_id = ?, membre_id = ?, dateEmprunt = ?, dateRetourPrevue = ?, dateRetourReel = ? WHERE id = ?")) {
            stmt.setInt(1, emprunt.getLivre().getId());
            stmt.setInt(2, emprunt.getMembre().getId());
            stmt.setDate(3, Date.valueOf(emprunt.getDateEmprunt()));
            stmt.setDate(4, Date.valueOf(emprunt.getDateRetourPrevue()));
            stmt.setDate(5, emprunt.getDateRetourReel() != null ? Date.valueOf(emprunt.getDateRetourReel()) : null);
            stmt.setInt(6, emprunt.getId());
            return stmt.executeUpdate() > 0 ?
                new Result<>(emprunt, "Mis à jour", true) :
                new Result<>(null, "Non trouvé", false);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    @Override
    public Result<Boolean> delete(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM emprunts WHERE id = ?")) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0 ?
                new Result<>(true, "Supprimé", true) :
                new Result<>(false, "Non trouvé", false);
        } catch (SQLException e) {
            return new Result<>(false, e.getMessage(), false);
        }
    }

    public Result<List<Emprunt>> findByMembre(int idMembre) {
        List<Emprunt> emprunts = new LinkedList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT e.*, l.*, m.* FROM emprunts e JOIN livres l ON e.livre_id = l.id JOIN membres m ON e.membre_id = m.id WHERE m.id = ?")) {
            stmt.setInt(1, idMembre);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                emprunts.add(mapEmprunt(rs));
            }
            return new Result<>(emprunts, "OK", true);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    public Result<List<Emprunt>> findByLivre(int idLivre) {
        List<Emprunt> emprunts = new LinkedList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT e.*, l.*, m.* FROM emprunts e JOIN livres l ON e.livre_id = l.id JOIN membres m ON e.membre_id = m.id WHERE l.id = ?")) {
            stmt.setInt(1, idLivre);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                emprunts.add(mapEmprunt(rs));
            }
            return new Result<>(emprunts, "OK", true);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    public Result<List<Emprunt>> findEmpruntsEnCours() {
        List<Emprunt> emprunts = new LinkedList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT e.*, l.*, m.* FROM emprunts e JOIN livres l ON e.livre_id = l.id JOIN membres m ON e.membre_id = m.id WHERE e.dateRetourReel IS NULL")) {
            while (rs.next()) {
                emprunts.add(mapEmprunt(rs));
            }
            return new Result<>(emprunts, "OK", true);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    private Emprunt mapEmprunt(ResultSet rs) throws SQLException {
        Livre livre = new Livre(rs.getInt("l.id"), rs.getString("l.titre"),
            rs.getString("l.auteur"), rs.getString("l.isbn"),
            rs.getInt("l.anneePublication"), rs.getBoolean("l.disponible"));
        Membre membre = new Membre(rs.getInt("m.id"), rs.getString("m.nom"),
            rs.getString("m.prenom"), rs.getString("m.email"),
            rs.getDate("m.dateInscription").toLocalDate());
        return new Emprunt(rs.getInt("e.id"), livre, membre,
            rs.getDate("e.dateEmprunt").toLocalDate(),
            rs.getDate("e.dateRetourPrevue").toLocalDate(),
            rs.getDate("e.dateRetourReel") != null ? rs.getDate("e.dateRetourReel").toLocalDate() : null);
    }
}
