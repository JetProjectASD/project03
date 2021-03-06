package com.jet.edu.project03.server;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.jet.edu.project03.server.ServerApp.*;

/**
 * Class which save history
 */
public class ChatHistoryPrinter implements Runnable {
    private Logger logger = Logger.getLogger(ChatHistoryPrinter.class.getName());
    private String fileName;
    private String encoding;
    private final static int MAX_BUFFER_SIZE = 9;

    /**
     * Constructor which set file name and encoding
     * @param fileName
     * @param encoding
     */
    public ChatHistoryPrinter(String fileName, String encoding) {
        this.fileName = fileName;
        this.encoding = encoding;

    }

    /**
     * Record buffer in file if buffers size more then max buffer size
     */
    @Override
    public void run() {
        if (lastTenChatMessages.size() > MAX_BUFFER_SIZE) {
            synchronized (lastTenChatMessages) {
                sendBuffer();
            }
        }
    }

    private void sendBuffer() {
        StringBuilder builder = new StringBuilder();
        for(String string : lastTenChatMessages) {
            builder.append(string).append(System.lineSeparator());
        }
        lastTenChatMessages.clear();
        print(builder.toString());
    }

    /**
     * Print in file
     * @param message
     */
    public void print(String message) {
        try(OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(fileName, true), encoding)) {
            streamWriter.write(message);
            streamWriter.flush();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Cant record buffer messages to file", e);
        }
    }

    /**
     * Get first 100 messeges from history if user setd - HIST
     */
    public String getHistoryMessages() throws IOException {
        StringBuilder history = new StringBuilder();
        List<String> messagesList;
        synchronized (lastTenChatMessages) {
            messagesList = FileUtils.readLines(new File(fileName), encoding);
        }
        Collections.reverse(messagesList);
        for(String string : messagesList) {
            history.append(string).append(System.lineSeparator());
        }
        return history.toString();
    }
}
