package com.github.vedeshkin;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private static final int SERVER_PORT = 8089;
    private Scanner in;
    private PrintWriter out;
    private static Server instance = null;
    private static final String STOP_STRING = "bye";

    private Server() {
    }

    public static void main(String[] args) {
        Server s = Server.getInstance();
        s.init();

    }

    static Server getInstance() {
        if (instance == null) instance = new Server();
        return instance;
    }

    private void init() {

        Socket s = null;
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            s = serverSocket.accept();
            in = new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread input = new Thread(() -> {

            while (true) {
                String message = in.nextLine();
                if(!message.equals(STOP_STRING)){
                    System.out.println("Client: " + message);
                }else System.exit(0);
            }
        });
        Thread output = new Thread(() -> {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {

                while (true) {
                    String message = br.readLine();
                    if (!message.equals(STOP_STRING)) {

                        out.println(message);
                        out.flush();
                    }else {
                        out.println(STOP_STRING);
                        out.flush();
                        System.exit(0);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        input.start();
        output.start();

    }
}
