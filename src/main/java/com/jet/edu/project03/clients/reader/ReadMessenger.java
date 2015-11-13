package com.jet.edu.project03.clients.reader;

import com.jet.edu.project03.clients.ConsoleHelper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static com.jet.edu.project03.clients.UtilitiesMessaging.sendMessage;
import static com.jet.edu.project03.clients.UtilitiesMessaging.takeMessage;

/**
 * New thrad for reading messages from server
 */

public class ReadMessenger extends Thread {
    private final int localPort;
    private String host;
    private int port;
    private long id;

    /**
     * Constructor for inlalling ip addres and port
     * @param host IP address
     */
    public ReadMessenger(String host, int port, int localPort) {
        this.host = host;
        this.port = port;
        this.localPort = localPort;
    }

    /**
     * All time listen port and write to console all messages from server
     */
    @Override
    public void run() {
        startLocalServerForSynchronizationLocalClients();
        startReadingMessageFromServer();
    }

    private void startReadingMessageFromServer() {
        try (Socket socket = new Socket(host, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            sendMessage(writer, "READER USER_ID");
            sendMessage(writer, String.valueOf(id));
            System.out.println("id send to server");
            System.out.println(id);

            if ((takeMessage(reader).equals("OK"))) {
                writeToLocalConsole(reader);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToLocalConsole(BufferedReader reader) {
        while (true) {
            try {
                ConsoleHelper.writeMessage(takeMessage(reader));
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void startLocalServerForSynchronizationLocalClients() {
        try(ServerSocket readerServerSocket = new ServerSocket(localPort);
            Socket socket = readerServerSocket.accept()) {
            id = Long.parseLong(takeMessage(new BufferedReader(new InputStreamReader(socket.getInputStream()))));
            System.out.println("id get");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
