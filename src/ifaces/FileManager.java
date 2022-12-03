/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ifaces;

import java.io.File;
import java.sql.*;

/**
 *
 * @author carlo
 */
public interface FileManager {
    
    public void addFile(String path, int patientId) throws SQLException;
    
    public File getFileByName(String name) throws SQLException;
    
    public String getPatientsFileNamesById(int patientId) throws SQLException;
  
}
