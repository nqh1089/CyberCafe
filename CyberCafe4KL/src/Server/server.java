package Server;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class server {

    private static final int PORT = 12345;

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("SERVER ĐANG CHẠY TẠI CỔNG " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(new ClientHandler(clientSocket));
            }

        } catch (IOException e) {
            System.err.println("LỖI KHI CHẠY SERVER:");
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String clientAddress = socket.getInetAddress().getHostAddress();
        System.out.println("MÁY TRẠM KẾT NỐI TỪ " + clientAddress);

        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("NHẬN TỪ " + clientAddress + ": " + inputLine);
            }

        } catch (IOException e) {
            System.err.println("LỖI KHI ĐỌC DỮ LIỆU TỪ CLIENT " + clientAddress);
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                System.out.println("NGẮT KẾT NỐI VỚI " + clientAddress);
            } catch (IOException e) {
                System.err.println("KHÔNG THỂ ĐÓNG SOCKET");
            }
        }
    }
}