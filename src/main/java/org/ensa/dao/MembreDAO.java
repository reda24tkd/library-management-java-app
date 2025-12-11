package org.ensa.dao;

import org.ensa.database.DatabaseConnection;
import org.ensa.model.Membre;
import org.ensa.model.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MembreDAO implements DAO<Membre> {

    @Override
    public Result<Membre> create(Membre membre) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO membres (nom, prenom, email, dateInscription) VALUES (?, ?, ?, ?)",
                 Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setString(3, membre.getEmail());
            stmt.setDate(4, Date.valueOf(membre.getDateInscription()));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) membre.setId(rs.getInt(1));
            return new Result<>(membre, "Créé", true);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    @Override
    public Result<Membre> findById(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM membres WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Result<>(new Membre(rs.getInt("id"), rs.getString("nom"),
                    rs.getString("prenom"), rs.getString("email"),
                    rs.getDate("dateInscription").toLocalDate()), "Trouvé", true);
            }
            return new Result<>(null, "Non trouvé", false);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    @Override
    public Result<List<Membre>> findAll() {
        List<Membre> membres = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM membres")) {
            while (rs.next()) {
                membres.add(new Membre(rs.getInt("id"), rs.getString("nom"),
                    rs.getString("prenom"), rs.getString("email"),
                    rs.getDate("dateInscription").toLocalDate()));
            }
            return new Result<>(membres, "OK", true);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    @Override
    public Result<Membre> update(Membre membre) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "UPDATE membres SET nom = ?, prenom = ?, email = ?, dateInscription = ? WHERE id = ?")) {
            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setString(3, membre.getEmail());
            stmt.setDate(4, Date.valueOf(membre.getDateInscription()));
            stmt.setInt(5, membre.getId());
            return stmt.executeUpdate() > 0 ?
                new Result<>(membre, "Mis à jour", true) :
                new Result<>(null, "Non trouvé", false);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    @Override
    public Result<Boolean> delete(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM membres WHERE id = ?")) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0 ?
                new Result<>(true, "Supprimé", true) :
                new Result<>(false, "Non trouvé", false);
        } catch (SQLException e) {
            return new Result<>(false, e.getMessage(), false);
        }
    }

    public Result<Set<Membre>> findAllAsSet() {
        Set<Membre> membres = new HashSet<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM membres")) {
            while (rs.next()) {
                membres.add(new Membre(rs.getInt("id"), rs.getString("nom"),
                    rs.getString("prenom"), rs.getString("email"),
                    rs.getDate("dateInscription").toLocalDate()));
            }
            return new Result<>(membres, "OK", true);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    public Result<Membre> findByEmail(String email) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM membres WHERE email = ?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Result<>(new Membre(rs.getInt("id"), rs.getString("nom"),
                    rs.getString("prenom"), rs.getString("email"),
                    rs.getDate("dateInscription").toLocalDate()), "Trouvé", true);
            }
            return new Result<>(null, "Non trouvé", false);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    public Result<Set<Membre>> findMembresActifs() {
        Set<Membre> membres = new HashSet<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT DISTINCT m.* FROM membres m JOIN emprunts e ON m.id = e.membre_id WHERE e.dateRetourReel IS NULL")) {
            while (rs.next()) {
                membres.add(new Membre(rs.getInt("id"), rs.getString("nom"),
                    rs.getString("prenom"), rs.getString("email"),
                    rs.getDate("dateInscription").toLocalDate()));
            }
            return new Result<>(membres, "OK", true);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }
}
