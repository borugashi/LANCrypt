package server;

import model.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private final int port;
    private final List<ClientSession> clients = new ArrayList<>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void start(){
        System.out.println("Server running on port " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)){
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Somebody connected: " + socket.getInetAddress());
                ClientSession session = new ClientSession(socket, this);
                clients.add(session);

                new Thread(session).start();
            }
        } catch (IOException error){
            System.err.println("Server error: " + error.getMessage());
        }
    }

    public synchronized void broadcast(Message message){
        for (ClientSession client : clients){
            client.sendMessage(message);
        }
    }

    public synchronized void removeClient(ClientSession session){
        clients.remove(session);
    }
}
