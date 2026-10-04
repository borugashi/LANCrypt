package server;

import model.Message;

import java.io.IOException;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientSession implements Runnable{
    private final Socket socket;
    private final ChatServer server;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientSession(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run(){
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            Message message;
            while ((message = (Message) in.readObject()) != null){
                server.broadcast(message);
            }
        } catch (IOException | ClassNotFoundException error) {
            //client disconnected
        } finally {
            close();
        }
    }

    public void sendMessage(Message message){
        try {
            if (out != null){
                out.writeObject(message);
                out.flush();
            }
        } catch (IOException e) {
            System.err.println("Send error: " + e.getMessage());
        }
    }

    private void close(){
        server.removeClient(this);
        try {
            socket.close();
        } catch (IOException ignored) {}
    }
}
