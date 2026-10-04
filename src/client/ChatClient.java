package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.io.OutputStreamWriter;

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
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            Thread recieverThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String incomingMessage;
                        while ((incomingMessage = in.readLine()) != null) {
                            System.out.println(incomingMessage);
                        }
                    } catch (IOException error) {
                        System.out.println("Server connection lost.");
                    }
                }
            });
            recieverThread.start();

            Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
            out.println("[SYSTEM]" + username + " joined to chat");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equalsIgnoreCase("/exit")) {
                    out.println("[SYSTEM] " + username + " left the chat");
                    break;
                }
                if (!line.trim().isEmpty()) {
                    out.println("[" + username + "]: " + line);
                }
            }

            socket.close();
        } catch (IOException error) {
            System.err.println("Failed to connect: " + error.getMessage());
        }
    }
}
