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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThreads {

    private static final List<Thread> threadList = new ArrayList();
    private static ServerSocket serverSocket;
    private static Socket socket;

    public static void main(String args[]) throws IOException {

        
        serverSocket = new ServerSocket(9000);
        new Thread(new ThreadToReadFromKeyboard()).start();  
        

        try{
        while (true) {
            Socket socket = serverSocket.accept();
            Thread t = new Thread(new ServerThreadsClient(socket));
            t.start();
            threadList.add(t);
        }
        }catch(SocketException e){
            System.out.println("Good bye!");
        }
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
