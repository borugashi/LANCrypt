package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSession implements Runnable{
    private final Socket socket;
    private final ChatServer server;
    private PrintWriter out;
    private BufferedReader in;

    public ClientSession(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run(){
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null){
                server.broadcast(message);
            }
        } catch (IOException error) {
            //client disconnected
        } finally {
            close();
        }
    }

    public void sendMessage(String message){
        if (out != null){
            out.println(message);
        }
    }

    private void close(){
        server.removeClient(this);
        try {
            socket.close();
        } catch (IOException ignored) {}
    }
}
