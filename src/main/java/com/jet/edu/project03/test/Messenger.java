package com.jet.edu.project03.test;

import java.io.*;
import java.net.Socket;

public class Messenger extends Thread {
    private String host;
    private int port;
    private Filter filterName = new FilterName();

    public Messenger(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try (Socket clientSocket = new Socket(host, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            String str;
            while (!(str = ConsoleHelper.readString()).equals("exit")) {
                writer.write(str);
                writer.newLine();
                writer.flush();

                String s = readStringFromServer(reader);
                System.out.println(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readStringFromServer(BufferedReader reader) throws IOException {
        while (true) {
            String result;
            if (reader.ready() && (result = reader.readLine()) != null) {
                return result;
            }
        }
    }

    private boolean firstConnection(BufferedReader reader, BufferedWriter writer) {
        ConsoleHelper.writeMessage("Введите имя");
        readUserName();
        ConsoleHelper.writeMessage("Введите имя");

        return false;
    }

    private String readUserName() {
        String name = null;
        while (!filterName.filter(name)) {
            name = ConsoleHelper.readString();
        }
        return name;
    }
}