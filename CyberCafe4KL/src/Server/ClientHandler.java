package Server;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {

    private Socket socket;
    private static final String SERVER_IP = "26.228.105.146";
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("from client: " + line);
                // TODO: xử lý chuỗi và cập nhật trạng thái máy trong DB
            }
        } catch (IOException e) {
            System.out.println("Client client has disconnected.");
        }
    }
}
