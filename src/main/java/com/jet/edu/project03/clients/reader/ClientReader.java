package com.jet.edu.project03.clients.reader;

public class ClientReader {

    public static void main(String[] args) {
        int port = 40001;
        ReadMessenger messenger = new ReadMessenger("127.0.0.1", port);
        messenger.start();
    }
}