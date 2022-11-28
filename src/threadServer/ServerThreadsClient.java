/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadServer;

/**
 *
 * @author carlo
 */
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.*;
import jdbc.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ServerThreadsClient implements Runnable {

    //int byteRead;
    Socket socket;
    String line;
    JDBCManager jdbcManager = new JDBCManager();
    JDBCFileManager fileManager = new JDBCFileManager(jdbcManager);
    JDBCPatientManager patientManager = new JDBCPatientManager(jdbcManager);
    //fileManager.setPatientManager(patientManager);

    public ServerThreadsClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
            try {
                while (true) {

                    int opcion = 0;
                    opcion = inputStream.read();
                    System.out.println(opcion);

                    switch (opcion) {
                        case 1:
                    try {
                            sendPatient();
                        } catch (IOException | SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                        case 2:
                            sendPatientsFileNames();
                            break;
                        case 3:
                            receiveAndSafeSignal();
                            break;
                        case 4:
                    try {
                            register();
                        } catch (IOException | SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                        case 5:
                    try {
                            login();

                        } catch (IOException | SQLException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException ex) {
                            Logger.getLogger(ServerThreadsClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                        case 6:
                            releaseResourcesClient(inputStream, socket);
                            break;
                        case 7:
                            try {
                            sendPatientsFullNameAndBitalino();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;

                        case 8:
                            sendCheck();
                            break;
                    }

                }

            } catch (SocketException ex) {
                System.out.println("client closed");
            } catch (SQLException ex) {
                Logger.getLogger(ServerThreadsClient.class.getName()).log(Level.SEVERE, null, ex);
            }

        }//catch(IOException | SQLException e){
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register() throws IOException, SQLException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String name = bufferedReader.readLine();
        System.out.println(name);
        String surname = bufferedReader.readLine();
        System.out.println(surname);
        String gender = bufferedReader.readLine();
        System.out.println(gender);

        Date birthDate = Date.valueOf(bufferedReader.readLine());
        System.out.println(birthDate);
        String bloodType = bufferedReader.readLine();
        String email = bufferedReader.readLine();
        String password = bufferedReader.readLine();

        System.out.println(password);
        String symptoms = bufferedReader.readLine();
        System.out.println(symptoms);
        String bitalino = bufferedReader.readLine();
        System.out.println(bitalino);

        Patient patient = new Patient(name, surname, gender, birthDate, bloodType, email, password, symptoms, bitalino);
        System.out.println(patient);
        patientManager.addPatient(patient);
        System.out.println(patient);
    }

    public void login() throws IOException, SQLException, NoSuchAlgorithmException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String email = bufferedReader.readLine();
        String password = bufferedReader.readLine();
        System.out.println(" " + email + " " + password);
        int patientId = patientManager.getPatientId(email, password);
        System.out.println("Patient id: " + patientId);
        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(patientId);
        /*
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println(patientId);
         */
    }

    public void sendPatientsFileNames() throws IOException, SQLException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        int patientId = bufferedReader.read();
        System.out.println("pId for fileNames: " + patientId);
        String fileNames = fileManager.getPatientsFileNamesById(patientId);
        //System.out.println(fileNames);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println(fileNames);
    }

    public void receiveAndSafeSignal() throws IOException, SQLException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        int patientId = Integer.parseInt(bufferedReader.readLine());
        String formattedDateTime = bufferedReader.readLine();
        String line;
        //String path = "files\\patient" + patientId + "_" + formattedDateTime + ".txt";
        File file = new File("files\\patient" + patientId + "_" + formattedDateTime + ".txt");
        System.out.println("filepath:" + file.getName());
        PrintWriter printWriter = new PrintWriter(new FileWriter(file), true);
        //while ((line = bufferedReader.readLine()) != null) { // NO SALE DEL WHILE ????
        for (int i = 0; i < 11; i++) {
            line = bufferedReader.readLine();
            System.out.println(line);
            printWriter.println(line);
        }
        fileManager.addFile(file.getName(), patientId);
        System.out.println("fuera while");
        printWriter.close();
        //releaseResourcesClient(bufferedReader, socket);
    }

    public void sendPatient() throws IOException, SQLException {
        //Patient patient;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        int patientId = bufferedReader.read();
        System.out.println(patientId);
        Patient patient = patientManager.searchPatientById(patientId);
        String patientSend = patient.toString2();
        System.out.println(patientSend);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println(patientSend);
    }

    public void sendPatientsFullNameAndBitalino() throws IOException, SQLException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        int patientId = bufferedReader.read();
        System.out.println("PatId bitalino: " + patientId);
        String fullName = patientManager.getPatientsFullNameById(patientId);
        String bitalino = patientManager.getPatientsBitalino(patientId);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println(fullName);
        printWriter.println(bitalino);
    }

    private static void releaseResourcesClient(InputStream inputStream, Socket socket) {
        try {
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThreads.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThreads.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void releaseResourcesClient(BufferedReader bufferedReader, Socket socket) {
        try {
            bufferedReader.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThreads.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThreads.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendCheck() throws SQLException, IOException {
        BufferedReader bufferedReader;
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String email = bufferedReader.readLine();
        String checkemail = patientManager.checkEmail(email);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println(checkemail);
    }
}
