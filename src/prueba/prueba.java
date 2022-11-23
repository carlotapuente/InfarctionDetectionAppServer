/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba;

/**
 *
 * @author carlo
 */

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import jdbc.*;
public class prueba {
    public static void main(String[] args) throws SQLException {
        JDBCManager jdbcManager = new JDBCManager();
    JDBCFileManager fileManager = new JDBCFileManager(jdbcManager);

    //PrintWriter printWriter = new PrintWriter(new FileWriter(file), true);
    fileManager.addFile("patient2_23-11-2022.17-05-01.txt", 1);
        
    }
}
