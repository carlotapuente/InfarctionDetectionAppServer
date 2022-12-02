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


public class ServerThreadsClient implements Runnable {

    Socket socket;
    JDBCManager jdbcManager = new JDBCManager();
    JDBCFileManager fileManager = new JDBCFileManager(jdbcManager);
    JDBCPatientManager patientManager = new JDBCPatientManager(jdbcManager);

    public ServerThreadsClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
            try {
                while (true) {

                    int opcion = 0;
                    opcion = inputStream.read();

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
                        case 9:
                            sendFiles();
                            break;
                    }

                }

            } catch (SocketException ex) {
                System.out.println("client closed");
            } catch (SQLException ex) {
                Logger.getLogger(ServerThreadsClient.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register() throws IOException, SQLException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String name = bufferedReader.readLine();
        String surname = bufferedReader.readLine();
        String gender = bufferedReader.readLine();

        Date birthDate = Date.valueOf(bufferedReader.readLine());
        String bloodType = bufferedReader.readLine();
        String email = bufferedReader.readLine();
        String password = bufferedReader.readLine();
        String symptoms = bufferedReader.readLine();
        String bitalino = bufferedReader.readLine();

        Patient patient = new Patient(name, surname, gender, birthDate, bloodType, email, password, symptoms, bitalino);
        patientManager.addPatient(patient);
    }

    public void login() throws IOException, SQLException, NoSuchAlgorithmException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String email = bufferedReader.readLine();
        String password = bufferedReader.readLine();
        int patientId = patientManager.getPatientId(email, password);
        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(patientId);
    }

    public void sendPatientsFileNames() throws IOException, SQLException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        int patientId = bufferedReader.read();
        String fileNames = fileManager.getPatientsFileNamesById(patientId);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println(fileNames);
        printWriter.println("stop");
    }

    public void sendFiles() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String fileName = bufferedReader.readLine();
        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader("files\\"+file));
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        String line;
        while ((line = br.readLine()) != null) { // reads the file and sends it to the client

            printWriter.println(line);

        }
        printWriter.println("stop");
        br.close();
    }

    public void receiveAndSafeSignal() throws IOException, SQLException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        int patientId = Integer.parseInt(bufferedReader.readLine());
        String formattedDateTime = bufferedReader.readLine();
        String line;
        File file = new File("files\\patient" + patientId + "_" + formattedDateTime + ".txt");
        PrintWriter printWriter = new PrintWriter(new FileWriter(file), true);
        printWriter.println("Patient id: "+patientId);
        printWriter.println("Date: "+formattedDateTime);
        
        while (!((line = bufferedReader.readLine()).equals("stop"))) {

            printWriter.println(line);
        }
        fileManager.addFile(file.getName(), patientId);
        printWriter.close();
    }

    public void sendPatient() throws IOException, SQLException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        int patientId = bufferedReader.read();
        Patient patient = patientManager.searchPatientById(patientId);
        String patientSend = patient.toString2();
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println(patientSend);
    }

    public void sendPatientsFullNameAndBitalino() throws IOException, SQLException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        int patientId = bufferedReader.read();
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

    private void sendCheck() throws SQLException, IOException {
        BufferedReader bufferedReader;
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String email = bufferedReader.readLine();
        String checkemail = patientManager.checkEmail(email);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println(checkemail);
    }
}
