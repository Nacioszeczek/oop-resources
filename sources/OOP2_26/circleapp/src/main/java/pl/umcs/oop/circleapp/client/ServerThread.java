package pl.umcs.oop.circleapp.client;

import pl.umcs.oop.circleapp.Dot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

public class ServerThread extends Thread {
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    private Consumer<Dot> consumer;

    public void setConsumer(Consumer<Dot> consumer) {
        this.consumer = consumer;
    }

    public ServerThread(String address, int port) throws IOException {
        socket = new Socket(address, port);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        System.out.println("Połączono z serwerem");
        try {
            String message;
            while ((message = reader.readLine()) != null)
                if (message.equals("disconnected")) {
                    System.out.println("Server disconnected!");
                } else {
                    Dot dot = Dot.fromMessage(message);
                    consumer.accept(dot);
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        writer.println(message);
    }
}
