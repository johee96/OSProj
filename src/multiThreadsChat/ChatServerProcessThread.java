package multiThreadsChat;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ChatServerProcessThread extends Thread {

    private String name = null;
    private Socket socket = null;
    List<PrintWriter> listWriters = null;

    public ChatServerProcessThread(Socket socket, List<PrintWriter> listWriters) {
        this.socket = socket;
        this.listWriters = listWriters;
    }

    @Override
    public void run() {
        super.run();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            while (true) {
                String request = bufferedReader.readLine();
                if (request == null) {
                    consoleLog("Client로부터 연결 끊김");
                    doQuit(printWriter);
                    break;
                }

                String[] tokens = request.split(":");
                if("join".equals(tokens[0])) {
                    doJoin(tokens[1], printWriter);
                }
                else if("message".equals(tokens[0])) {
                    doMessage(tokens[1]);
                }
                else if("quit".equals(tokens[0])) {
                    doQuit(printWriter);
                }
            }

        } catch (IOException e) {
            consoleLog(this.name + "님이 채팅방을 나갔습니다.");
        }
    }

    private void doQuit(PrintWriter printWriter) {
        removeWriter(printWriter);
        String data = this.name + "님이 채팅방을 나갔습니다.";
        broadcast(data);
    }

    private void removeWriter(PrintWriter writer) {
        synchronized (listWriters) {
            listWriters.remove(writer);
        }
    }

    private void doMessage(String data) {
        broadcast(this.name + ":" + data);
    }

    private void doJoin(String name, PrintWriter printWriter) {
        this.name = name;
        String data = this.name + "님이 채팅방을 입장하샸습니다.";
        broadcast(data);

        //writer pool에 저장
        addWriter(printWriter);
    }

    private void addWriter(PrintWriter printWriter) {
        synchronized (listWriters) {
            listWriters.add(printWriter);
        }
    }

    private void broadcast(String data) {
        synchronized (listWriters) {
            for (PrintWriter printWriter : listWriters) {
                printWriter.println(data);
                printWriter.flush();
            }
        }

    }

    private void consoleLog(String log) {
        System.out.println(log);
    }
}