package pl.umcs.oop.circleapp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class ClientThread implements Runnable {
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final Server server;

    public ClientThread(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.server = server;
    }

    private void close() throws IOException {
        socket.close();
        server.removeHandler(this);
    }

    public void send(String message) {
        writer.println(message);
    }

    @Override
    public void run() {
        System.out.println("Client connected");
        String message;
        try {
            while ((message = reader.readLine()) != null)
                server.broadcast(message);
            close();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("CLient disconnected");
    }
}
