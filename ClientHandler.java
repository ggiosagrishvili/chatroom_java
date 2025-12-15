import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {

    Socket socket;
    String name;
    DataInputStream in;
    DataOutputStream out;

    public ClientHandler(Socket socket, String name) throws IOException {
        this.socket = socket;
        this.name = name;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void send(String msg) {
        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
            System.out.println("Send error");
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = in.readUTF();
                if (msg.startsWith("/name ")) {
                    String newName = msg.substring(6);

                    ChatRoom.clients.remove(name);
                    name = newName;
                    ChatRoom.clients.put(name, this);

                    send("Your name is now " + name);
                }
                else if (msg.startsWith("/pm ")) {
                    String[] parts = msg.split(" ", 3);
                    if (parts.length == 3 &&
                            ChatRoom.clients.containsKey(parts[1])) {
                        ChatRoom.clients.get(parts[1])
                                .send("(PM from " + name + "): " + parts[2]);
                    }
                }
                else if (msg.equals("/exit")) {
                    break;
                }
                else {
                    ChatRoom.broadcast(name + ": " + msg);
                }
            }
        } catch (IOException e) {
            System.out.println(name + " disconnected");
        }

        ChatRoom.clients.remove(name);
        ChatRoom.broadcast(name + " left. Users: " + ChatRoom.clients.size());

        try {
            socket.close();
        } catch (IOException ignored) {}
    }
}
