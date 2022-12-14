/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc;

import ifaces.PatientManager;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pojos.Patient;

/**
 *
 * @author mariadefarges
 */
public class JDBCPatientManager implements PatientManager {

    private final JDBCManager manager;

    public JDBCPatientManager(JDBCManager m) {
        this.manager = m;
    }

    @Override
    public void addPatient(Patient p) throws SQLException {
        String sql = "INSERT INTO patients (name, surname, gender, birthDate, bloodType, email,password, symptoms, bitalino) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement prep = manager.getConnection().prepareStatement(sql);
        prep.setString(1, p.getName());
        prep.setString(2, p.getSurname());
        prep.setString(3, p.getGender());
        prep.setDate(4, p.getBirthDate());
        prep.setString(5, p.getBloodType());
        prep.setString(6, p.getEmail());
        prep.setString(7, p.getPassword());
        prep.setString(8, p.getSymptoms());
        prep.setString(9, p.getBitalino());
        prep.executeUpdate();
        prep.close();
    }

    @Override
    public int getPatientId(String email, String password) throws SQLException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] hash = md.digest();
        String pw = new String(hash, 0, hash.length);
        int patientId = 0;
        String sql = "SELECT patientId FROM patients WHERE email = ? AND password = ?";
        PreparedStatement prep = manager.getConnection().prepareStatement(sql);
        prep.setString(1, email);
        prep.setString(2, pw);
        ResultSet rs = prep.executeQuery();
        while (rs.next()) {
            patientId = rs.getInt("patientId");
        }
        rs.close();
        prep.close();
        return patientId;
    }

    @Override
    public Patient searchPatientById(int patientId) throws SQLException {
        Patient p = null;
        String sql = "SELECT * FROM patients WHERE patientId= ?";
        PreparedStatement prep = manager.getConnection().prepareStatement(sql);
        prep.setInt(1, patientId);
        ResultSet rs = prep.executeQuery();
        while (rs.next()) {
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            String gender = rs.getString("gender");
            Date birthDate = rs.getDate("birthDate");
            String bloodType = rs.getString("bloodType");
            String email = rs.getString("email");
            String password = rs.getString("password");
            String symptoms = rs.getString("symptoms");
            String bitalino = rs.getString("bitalino");

            p = new Patient(patientId, name, surname, gender, birthDate, bloodType, email, password, symptoms, bitalino);
        }
        prep.close();
        rs.close();
        return p;
    }

    @Override
    public List<Patient> searchPatientbyName(String name) throws SQLException {
        Patient p = null;
        String sql = "SELECT * FROM patients WHERE name = ?";
        PreparedStatement prep = manager.getConnection().prepareStatement(sql);
        prep.setString(1, name);
        ResultSet rs = prep.executeQuery();
        List<Patient> patients = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("patientId");
            String surname = rs.getString("surname");
            String gender = rs.getString("gender");
            Date birthDate = rs.getDate("birthDate");
            String bloodType = rs.getString("bloodType");
            String email = rs.getString("email");
            String password = rs.getString("password");
            String symptoms = rs.getString("symptoms");
            String bitalino = rs.getString("bitalino");

            p = new Patient(id, name, surname, gender, birthDate, bloodType, email, password, symptoms, bitalino);
            patients.add(p);
        }
        prep.close();
        rs.close();
        return patients;
    }

    @Override
    public List<Patient> searchPatientbySurname(String surname) throws SQLException {
        Patient p = null;
        String sql = "SELECT * FROM patients WHERE surname = ?";
        PreparedStatement prep = manager.getConnection().prepareStatement(sql);
        prep.setString(1, surname);
        ResultSet rs = prep.executeQuery();
        List<Patient> patients = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("patientId");
            String name = rs.getString("name");
            String gender = rs.getString("gender");
            Date birthDate = rs.getDate("birthDate");
            String bloodType = rs.getString("bloodType");
            String email = rs.getString("email");
            String password = rs.getString("password");
            String symptoms = rs.getString("symptoms");
            String bitalino = rs.getString("bitalino");

            p = new Patient(id, name, surname, gender, birthDate, bloodType, email, password, symptoms, bitalino);
            patients.add(p);
        }
        prep.close();
        rs.close();
        return patients;
    }

    @Override
    public String getPatientsFullNameById(int patientId) throws SQLException {
        String sql = "SELECT name, surname FROM patients WHERE patientId = ?";
        PreparedStatement prep = manager.getConnection().prepareStatement(sql);
        prep.setInt(1, patientId);
        ResultSet rs = prep.executeQuery();
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        String fullName = name + " " + surname;
        prep.close();
        rs.close();
        return fullName;
    }

    @Override
    public String getPatientsBitalino(int patientId) throws SQLException {
        String sql = "SELECT bitalino FROM patients WHERE patientId = ?";
        PreparedStatement prep = manager.getConnection().prepareStatement(sql);
        prep.setInt(1, patientId);
        ResultSet rs = prep.executeQuery();
        String bitalino = rs.getString("bitalino");
        prep.close();
        rs.close();
        return bitalino;
    }

    @Override
    public String checkEmail(String email) throws SQLException {
        String checkemail = "";
        String sql = "SELECT email FROM patients WHERE email = ?";
        PreparedStatement prep = manager.getConnection().prepareStatement(sql);
        prep.setString(1, email);
        ResultSet rs = prep.executeQuery();

        if (rs.next()) {
            checkemail = rs.getString("email");
        }
        prep.close();
        rs.close();
        return checkemail;
    }

    @Override
    public Patient checkPassword(String email, String password) throws SQLException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] hash = md.digest();
            Patient p = null;
            String sql = "SELECT * FROM patients WHERE email = ? AND password = ?";
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);
            prep.setString(1, email);
            prep.setBytes(2, hash);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                p = new Patient(rs.getInt("patientId"), rs.getString("name"),
                        rs.getString("surname"), rs.getString("gender"),
                        rs.getDate("birthDate"), rs.getString("bloodType"),
                        rs.getString("email"), rs.getString("password"), rs.getString("symptoms"),
                        rs.getString("bitalino"));
            }
            prep.close();
            rs.close();
            return p;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
