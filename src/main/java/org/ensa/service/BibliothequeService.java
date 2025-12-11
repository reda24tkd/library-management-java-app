package org.ensa.service;

import org.ensa.dao.EmpruntDAO;
import org.ensa.dao.LivreDAO;
import org.ensa.model.Emprunt;
import org.ensa.model.Livre;
import org.ensa.model.Membre;
import org.ensa.model.Result;

import java.util.*;

public class BibliothequeService {
    private final LivreDAO livreDAO = new LivreDAO();
    private final EmpruntDAO empruntDAO = new EmpruntDAO();

    public Result<SortedSet<Livre>> getLivresTries() {
        Result<List<Livre>> result = livreDAO.findAll();
        if (!result.isSuccess()) {
            return new Result<>(null, result.getMessage(), false);
        }
        TreeSet<Livre> livresTries = new TreeSet<>(result.getData());
        return new Result<>(livresTries, "OK", true);
    }

    public Result<NavigableSet<Livre>> getLivresEntreAnnees(int anneeDebut, int anneeFin) {
        Result<List<Livre>> result = livreDAO.findAll();
        if (!result.isSuccess()) {
            return new Result<>(null, result.getMessage(), false);
        }
        TreeSet<Livre> livresParAnnee = new TreeSet<>(Comparator.comparingInt(Livre::getAnneePublication));
        for (Livre livre : result.getData()) {
            if (livre.getAnneePublication() >= anneeDebut && livre.getAnneePublication() <= anneeFin) {
                livresParAnnee.add(livre);
            }
        }
        return new Result<>(livresParAnnee, "OK", true);
    }

    public Result<Map<Membre, Integer>> getNombreEmpruntsParMembre() {
        Result<List<Emprunt>> result = empruntDAO.findAll();
        if (!result.isSuccess()) {
            return new Result<>(null, result.getMessage(), false);
        }
        Map<Membre, Integer> stats = new HashMap<>();
        for (Emprunt emprunt : result.getData()) {
            stats.put(emprunt.getMembre(), stats.getOrDefault(emprunt.getMembre(), 0) + 1);
        }
        return new Result<>(stats, "OK", true);
    }

    public Result<Map<String, List<Livre>>> getLivresParAuteur() {
        Result<List<Livre>> result = livreDAO.findAll();
        if (!result.isSuccess()) {
            return new Result<>(null, result.getMessage(), false);
        }
        Map<String, List<Livre>> livresParAuteur = new HashMap<>();
        for (Livre livre : result.getData()) {
            livresParAuteur.computeIfAbsent(livre.getAuteur(), k -> new ArrayList<>()).add(livre);
        }
        return new Result<>(livresParAuteur, "OK", true);
    }

    public Result<Map<Integer, Long>> getStatistiquesParAnnee() {
        Result<List<Livre>> result = livreDAO.findAll();
        if (!result.isSuccess()) {
            return new Result<>(null, result.getMessage(), false);
        }
        Map<Integer, Long> stats = new HashMap<>();
        for (Livre livre : result.getData()) {
            stats.put(livre.getAnneePublication(), stats.getOrDefault(livre.getAnneePublication(), 0L) + 1);
        }
        return new Result<>(stats, "OK", true);
    }
}
