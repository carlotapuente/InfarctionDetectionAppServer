/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc;

import java.sql.*;

/**
 *
 * @author mariadefarges
 */
public class JDBCManager {

    private Connection c = null;

    public JDBCManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:./db/ServerApp.db");
            c.createStatement().execute("PRAGMA foreign_keys=ON");
            System.out.println("Database connection opened.");
            try {
                this.createTables();
            } catch (SQLException e) {

                System.out.println("The tables have been already created");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Libraries not loaded");
        }
    }

    public void disconnect() {
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return c;
    }

    public void createTables() throws SQLException {
        // Create Tables
        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE patients "
                + "(patientId INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "	name TEXT NOT NULL,"
                + " surname TEXT NOT NULL,"
                + "	gender TEXT NOT NULL,"
                + "	birthDate DATE NOT NULL,"
                + " bloodType TEXT NOT NULL,"
                + "	email TEXT NOT NULL,"
                + " password TEXT NOT NULL,"
                + "	symptoms TEXT NOT NULL,"
                + "	bitalino TEXT NOT NULL)";

        stmt.executeUpdate(sql);
        sql = "CREATE TABLE files "
                + "(fileId INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "	name TEXT NOT NULL,"
                + "	patientId INTEGER REFERENCES patients(patientId) ON DELETE CASCADE )";

        stmt.executeUpdate(sql);
        System.out.println("Tables created");
    }
    
    public static void main(String[] args) throws SQLException {
        JDBCManager jdbc = new JDBCManager();
        JDBCFileManager jdbcf = new JDBCFileManager(jdbc);
        jdbcf.addFile("whatever", 1);
        
        System.out.println("break!");
    }
}
