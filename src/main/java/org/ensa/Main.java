package org.ensa;

import org.ensa.database.DatabaseConnection;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello and welcome!");
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.getConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
