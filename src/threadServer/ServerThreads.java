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
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.InputOutput;

public class ServerThreads {

    private static List<Thread> threadList = new ArrayList();
    private static ServerSocket serverSocket;
    private static Socket socket;

    public static void main(String args[]) throws IOException {
        //Create a service that is waiting in port 9000

        serverSocket = new ServerSocket(9000);
        Socket socket1 = serverSocket.accept();
        new Thread(new ThreadToReadFromKeyboard(socket1)).start();  
        
        String option;

        while (true) {
            //Thie executes when we have a client
            Socket socket = serverSocket.accept();
            Thread t = new Thread(new ServerThreadsClient(socket));
            t.start();
            threadList.add(t);
        

            System.out.println("1. Close the server.");
            option = InputOutput.get_String();

            /*if (option.equals("1")) {
                System.out.println("Admin password: \n");
                String password = InputOutput.get_String();
                if (password.equals("12345")) {
                    if (sendClientsAlive() == 0) {
                        System.out.println("There are no clients connected.\n The server has been closed successfully");
                        releaseResourcesServer(getServerSocket());
                        //System.exit(0);
                    } else {
                        System.out.println("There are " + sendClientsAlive() + " clients still connected. \n Are you sure you want to close the server? Y/N");
                        option = InputOutput.get_String();
                        if (option.equals("Y")) {
                            System.out.println("The server has been closed successfully");
                            releaseResourcesServer(getServerSocket(), socket);

                            //System.exit(0);
                        }
                    }
                }
            }*/
        }
            
        

        /*finally {
            releaseResourcesServer(serverSocket);
        }*/
    }

    
    public static ServerSocket getServerSocket() {
        return serverSocket;
    }
    
    public static Socket getSocket() {
        return socket;
    }

    public static int sendClientsAlive() {
        int contador = 0;

        for (int i = 0; i < threadList.size(); i++) {
            if (threadList.get(i).isAlive()) {
                contador++;
            }
        }
        return contador;
    }

    public static void releaseResourcesServer(ServerSocket serverSocket, Socket socket) {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThreads.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThreads.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
        public static void releaseResourcesServer(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThreads.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
