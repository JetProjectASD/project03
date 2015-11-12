package com.jet.edu.project03.client;

import java.io.*;
import java.net.Socket;

public class ServerConnector {

    private String hostname;
    private int port;
    private ServerSender serverSender;
    private Socket clientSocket;

    /**
     * Constructor
     * @param hostname IP address
     */
    public ServerConnector(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        serverSender = new ServerSender();
    }

    /**
     * install connection with server
     */
    public void connectToSever(String name) throws IOException {
        if (clientSocket == null) {
            clientSocket = new Socket(hostname, port);
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            while (true) {
                if ("TRUE".equals(serverSender.sendMessage(reader, writer, name))) {
                    ServerListener serverListener = new ServerListener(reader);
                    Thread listener = new Thread(serverListener);
                    listener.start();
                    return;
                }
            }
        }

    }


}