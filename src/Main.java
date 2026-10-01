import client.ChatClient;
import server.ChatServer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("|||  LANCrypt v0.1a  |||");
        System.out.println("1. Run server");
        System.out.println("2. Run client");
        System.out.print("Press '1' or '2' key: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                new ChatServer(8888).start();
                break;
            case "2":
                System.out.println("Server IP (leave it empty for localhost): ");
                String host = scanner.nextLine().trim();
                if (host.isEmpty()) host = "127.0.0.1";

                System.out.println("Enter your name: ");
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) name = "Anonim";

                new ChatClient(host, 8888, name).start();
                break;
            default:
                System.out.println("Incorrect input. Run program again.");
                break;
        }
    }
}
