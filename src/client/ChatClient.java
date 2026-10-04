package client;

import model.Message;
import model.SystemMessage;
import model.TextMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class ChatClient {
    private final String host;
    private final int port;
    private final String username;

    public ChatClient(String host, int port, String username) {
        this.host = host;
        this.port = port;
        this.username = username;
    }

    public void start(){
        try {
            Socket socket = new Socket(host, port);
            System.out.println("Successfully connected to server!");

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Thread receiverThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Message incomingMessage;
                        while ((incomingMessage = (Message) in.readObject()) != null) {
                            System.out.println(incomingMessage.toString());
                        }
                    } catch (IOException | ClassNotFoundException error) {
                        System.out.println("Server connection lost.");
                    }
                }
            });
            receiverThread.start();

            Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
            out.writeObject(new SystemMessage(username + " joined the chat"));
            out.flush();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equalsIgnoreCase("/exit")) {
                    out.writeObject(new SystemMessage(username + " left the chat"));
                    out.flush();
                    break;
                }
                if (!line.trim().isEmpty()) {
                    // Отправляем обычный текст
                    out.writeObject(new TextMessage(username, line));
                    out.flush();
                }
            }

            socket.close();
        } catch (IOException error) {
            System.err.println("Failed to connect: " + error.getMessage());
        }
    }
}