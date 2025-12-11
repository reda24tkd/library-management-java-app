package org.ensa.dao;

import org.ensa.database.DatabaseConnection;
import org.ensa.model.Livre;
import org.ensa.model.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO implements DAO<Livre> {
    @Override
    public Result<Livre> create(Livre livre) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement
                     ("INSERT INTO livres (titre, auteur, isbn, anneePublication, disponible) VALUES (?, ?, ?, ?, ?)",
                             Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setString(3, livre.getIsbn());
            stmt.setInt(4, livre.getAnneePublication());
            stmt.setBoolean(5, livre.isDisponible());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) livre.setId(rs.getInt(1));
            return new Result<>(livre, "Créé", true);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    @Override
    public Result<Livre> findById(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM livres WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Result<>
                        (new Livre(rs.getInt("id"), rs.getString("titre"), rs.getString("auteur"), rs.getString("isbn"), rs.getInt("anneePublication"), rs.getBoolean("disponible")), "Trouvé", true);
            }
            return new Result<>(null, "Non trouvé", false);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    @Override
    public Result<List<Livre>> findAll() {
        List<Livre> livres = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM livres")) {
            while (rs.next()) {
                livres.add(new Livre(rs.getInt("id"), rs.getString("titre"), rs.getString("auteur"), rs.getString("isbn"), rs.getInt("anneePublication"), rs.getBoolean("disponible")));
            }
            return new Result<>(livres, "OK", true);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    @Override
    public Result<Livre> update(Livre livre) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("UPDATE livres SET titre = ?, auteur = ?, isbn = ?, anneePublication = ?, disponible = ? WHERE id = ?")) {
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setString(3, livre.getIsbn());
            stmt.setInt(4, livre.getAnneePublication());
            stmt.setBoolean(5, livre.isDisponible());
            stmt.setInt(6, livre.getId());
            return stmt.executeUpdate() > 0 ? new Result<>(livre, "Mis à jour", true) : new Result<>(null, "Non trouvé", false);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    @Override
    public Result<Boolean> delete(int id) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM livres WHERE id = ?")) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0 ? new Result<>(true, "Supprimé", true) : new Result<>(false, "Non trouvé", false);
        } catch (SQLException e) {
            return new Result<>(false, e.getMessage(), false);
        }
    }

    public Result<List<Livre>> findByAuteur(String auteur) {
        List<Livre> livres = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM livres WHERE auteur = ?")) {
            stmt.setString(1, auteur);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                livres.add(new Livre(rs.getInt("id"), rs.getString("titre"), rs.getString("auteur"), rs.getString("isbn"), rs.getInt("anneePublication"), rs.getBoolean("disponible")));
            }
            return new Result<>(livres, "OK", true);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }

    public Result<List<Livre>> findDisponibles() {
        List<Livre> livres = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM livres WHERE disponible = true")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                livres.add(new Livre(rs.getInt("id"), rs.getString("titre"), rs.getString("auteur"), rs.getString("isbn"), rs.getInt("anneePublication"), rs.getBoolean("disponible")));
            }
            return new Result<>(livres, "OK", true);
        } catch (SQLException e) {
            return new Result<>(null, e.getMessage(), false);
        }
    }
}
