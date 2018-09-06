package com.github.vedeshkin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by vedeshkin on 06.09.2018.
 */
public class Client {

    private static final int SERVER_PORT = 8089;
    private static final String HOST = "localhost";
    private static final String STOP_STRING = "bye";
    private Scanner in;
    private PrintWriter out;
    private static Client instance;

    private Client(){}

    public static void main(String[] args) {
        Client client = Client.getInstance();
        client.init();

    }

    private static Client getInstance() {
        if(instance == null) instance = new Client();
        return instance;
    }

    private void init(){

        Socket clientSocket;
        try {
            clientSocket = new Socket(HOST,SERVER_PORT);
            in = new Scanner(clientSocket.getInputStream());
            out = new PrintWriter(clientSocket.getOutputStream());

        }catch (IOException e){
            e.printStackTrace();
        }

        Thread t1 = new Thread(()->{

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
        Thread t2 = new Thread(()->{

            while (true) {
                String message = in.nextLine();
               if(!message.equals(STOP_STRING)){
                   System.out.println("Server: " + message);
               }else System.exit(0);
            }
        });
        t1.start();
        t2.start();
    }


}
