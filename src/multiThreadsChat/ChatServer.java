package multiThreadsChat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    public static final int PORT = 5000;
    private static final String SERVER_IP = "172.20.10.8";

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        List<PrintWriter> listWriters = new ArrayList<PrintWriter>();

        try {
            //1. 서버 소켓 생성
            serverSocket = new ServerSocket();

            //2. 바인딩
            String hostAdd = InetAddress.getLocalHost().getHostAddress();

            serverSocket.bind(new InetSocketAddress(hostAdd, PORT));
            System.out.println("연결 기다림 - " + hostAdd + ":" + PORT);

            //3. 요청대기
            while (true) {
                Socket socket = serverSocket.accept();
                new ChatServerProcessThread(socket, listWriters).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
