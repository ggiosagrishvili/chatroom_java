import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatUser {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Socket socket = null;
        while (socket == null) {
            try {
                System.out.print("Enter address: ");
                String host = scanner.nextLine();

                System.out.print("Enter port: ");
                int port = Integer.parseInt(scanner.nextLine());

                socket = new Socket(host, port);
                System.out.println("Connected to server");

            } catch (Exception e) {
                System.out.println("Connection failed, try again");
            }
        }

        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    while (true) {
                        System.out.println(in.readUTF());
                    }
                } catch (Exception e) {
                    System.out.println("Server disconnected");
                }
            }).start();
            while (true) {
                String msg = scanner.nextLine();
                out.writeUTF(msg);
                out.flush();

                if (msg.equals("/exit")) break;
            }

            socket.close();

        } catch (Exception ignored) {}
    }
}
