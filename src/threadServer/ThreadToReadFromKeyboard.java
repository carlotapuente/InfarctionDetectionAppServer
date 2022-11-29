/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package threadServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.InputOutput;

/**
 *
 * @author mariadefarges
 */
public class ThreadToReadFromKeyboard implements Runnable {

    @Override
    public void run() {
        ServerThreads st = new ServerThreads();
        String option = null;
        while (true) {
            
            System.out.println("Press X if you want to close the server ");
          option = InputOutput.get_String();

            if (option.equalsIgnoreCase("x")) {
                System.out.println("Admin password: \n");
                String password = InputOutput.get_String();
                if (password.equals("12345")) {
                    if (st.sendClientsAlive() == 0) {
                        System.out.println("There are no clients connected.\n The server has been closed successfully");
                        st.releaseResourcesServer(st.getServerSocket());
                        System.exit(0);
                    } else {
                        System.out.println("There are " + st.sendClientsAlive() + " clients still connected. \n Are you sure you want to close the server? Press X to accept");
                        option = InputOutput.get_String();
                        if (option.equalsIgnoreCase("x")){
                            System.out.println("The server has been closed successfully");
                            st.releaseResourcesServer(st.getServerSocket());
                            System.exit(0);
                        }
                    }
                }
            }
        }
    }
}
