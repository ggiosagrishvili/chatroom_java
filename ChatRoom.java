import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ChatRoom {

    static ConcurrentHashMap<String, ClientHandler> clients =
            new ConcurrentHashMap<>();

    static int counter = 0;

    public static void main(String[] args) throws IOException {

        System.out.print("Enter port: ");
        int port = new java.util.Scanner(System.in).nextInt();

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("ChatRoom started on port " + port);

        while (true) {
            Socket socket = serverSocket.accept();
            counter++;

            String name = "User" + counter;
            ClientHandler handler = new ClientHandler(socket, name);

            clients.put(name, handler);
            handler.start();

            handler.send("WELCOME TO CHAT");
            broadcast(name + " joined. Users: " + clients.size());
        }
    }

    static void broadcast(String message) {
        for (ClientHandler c : clients.values()) {
            c.send(message);
        }
    }
}
