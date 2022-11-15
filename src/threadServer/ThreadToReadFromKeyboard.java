/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package threadServer;

import java.net.Socket;
import ui.InputOutput;

/**
 *
 * @author mariadefarges
 */
public class ThreadToReadFromKeyboard implements Runnable {
    Socket socket;

    public ThreadToReadFromKeyboard(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Boolean clientsConnected = true;
        String option = null;
        while (true) {
            System.out.println("1. Close the server.");
            option = InputOutput.get_String();

            if (option.equals("1")) {
                System.out.println("Admin password: \n");
                String password = InputOutput.get_String();
                if (password.equals("12345")) {
                    if (clientsConnected) {
                        System.out.println("There are clients still connected. \n Are you sure you want to close the server? Y/N");
                        option = InputOutput.get_String();
                        if (option.equals("Y")) {
                            System.exit(0);
                        }

                    }
                }
            }
        }
    }
}
