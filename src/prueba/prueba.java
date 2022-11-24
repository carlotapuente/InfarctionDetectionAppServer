/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba;

/**
 *
 * @author carlo
 */

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import jdbc.*;
public class prueba {
    public static void main(String[] args) throws SQLException {
        JDBCManager jdbcManager = new JDBCManager();
    JDBCFileManager fileManager = new JDBCFileManager(jdbcManager);

    //PrintWriter printWriter = new PrintWriter(new FileWriter(file), true);
    //fileManager.addFile("patient2_23-11-2022.17-05-01.txt", 1);
      //fileManager.addFile("socorro", 2);
        //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        int patientId = 2;
        //System.out.println(patientId);
        String fileNames = fileManager.getPatientsFileNamesById(patientId);
        System.out.println("files:" + fileNames);
        //PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        //printWriter.println(fileNames);
    }
    
}
