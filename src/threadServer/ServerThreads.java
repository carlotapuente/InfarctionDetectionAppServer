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

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThreads {

    public static void main(String args[]) throws IOException {
        //Create a service that is waiting in port 9000
        //while (true) {
                /*if (option.equals("1")) {
                    if (clientsConnected) {
                        System.out.println("There are clients still connected. \n Are you sure you want to close the server? Y/N");
                        option = InputOutput.get_String();
                        if (option.equals("Y")) {
                            System.out.println("Password: \n");
                            String password = InputOutput.get_String();
                            if (password.equals("12345")) {
                                releaseResourcesServer(serverSocket);
                                System.exit(0);
                            } else {
                                System.out.println("Incorrect password.");
                            }
                        }
                    } else {
                        System.out.println("Password: \n");
                        String password = InputOutput.get_String();
                        if (password.equals("12345")) {
                            releaseResourcesServer(serverSocket);
                            System.exit(0);
                        } else {
                            System.out.println("Incorrect password.");
                        }
                    }
                }*/
                
                /*ServerSocket serverSocket = new ServerSocket(9000);
                Socket socket = serverSocket.accept();
                new Thread(new ServerThreadsClient(socket)).start();*/
                /*if (!socket.isClosed()) {
                    clientsConnected = true;
                }*/

           // }
        ServerSocket serverSocket = new ServerSocket(9000);
        try {
            while (true) {
                //Thie executes when we have a client
                Socket socket = serverSocket.accept();
                new Thread(new ServerThreadsClient(socket)).start();
            }
        } finally {
            releaseResourcesServer(serverSocket);
        }
    }

    

    private static void releaseResourcesServer(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThreads.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    }

