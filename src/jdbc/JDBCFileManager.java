/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc;

import ifaces.*;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author carlo
 */
public class JDBCFileManager implements FileManager {

    private JDBCManager manager;
    private PatientManager patientManager;

    public JDBCFileManager(JDBCManager m) {
        this.manager = m;
    }

    public void setPatientManager(PatientManager patientManager) {
        this.patientManager = patientManager;
    }

    @Override
    public void addFile(String path, int patientId) throws SQLException {
        String sql = "INSERT INTO files (name, patientId) VALUES (?,?)";
        PreparedStatement prep = manager.getConnection().prepareStatement(sql);
        prep.setString(1, path);
        prep.setInt(2, patientId);
        prep.executeUpdate();
        prep.close();
    }

    @Override
    public File getFileByName(String name) throws SQLException {
        String sql = "SELECT * FROM files WHERE name = ?";
        PreparedStatement prep = manager.getConnection().prepareStatement(sql);
        prep.setString(1, name);
        File file = new File(name);
        return file;
    }

    @Override
    public String getPatientsFileNamesById(int patientId) throws SQLException {
        String sql = "SELECT * FROM files WHERE patientId = ?";
        PreparedStatement prep = manager.getConnection().prepareStatement(sql);
        prep.setInt(1, patientId);
        String fileNames = "";
        ResultSet rs = prep.executeQuery();

        while (rs.next()) {
            String name = rs.getString("name");
            fileNames = fileNames + "\n" + name;

        }
        rs.close();
        prep.close();
        return fileNames;
    }
}
