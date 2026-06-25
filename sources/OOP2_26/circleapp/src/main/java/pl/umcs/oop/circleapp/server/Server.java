package pl.umcs.oop.circleapp.server;

import javafx.scene.paint.Color;
import pl.umcs.oop.circleapp.Dot;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private final ServerSocket serverSocket;
    private CopyOnWriteArrayList<ClientThread> handlers = new CopyOnWriteArrayList<>();
    private Connection dbConnection;

    public Server(int port) throws IOException, SQLException {
        this.serverSocket = new ServerSocket(port);
        dbConnection = DriverManager.getConnection("jdbc:sqlite:circle.db");
        initDB();
    }

    private void initDB() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS dot(
                id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                x INTEGER NOT NULL,
                y INTEGER NOT NULL,
                color TEXT NOT NULL,
                radius INTEGER NOT NULL
                );
                """;
        Statement stmt = dbConnection.createStatement();
        stmt.execute(sql);
    }

    private void saveDot(Dot d) throws SQLException {
        String sql = "INSERT INTO dot(x, y, color, radius) VALUES (?, ?, ?, ?);";
        PreparedStatement stmt = dbConnection.prepareStatement(sql);
        stmt.setInt(1, (int) d.x());
        stmt.setInt(2, (int) d.y());
        stmt.setString(3, d.color().toString());
        stmt.setInt(4, (int) d.radius());
        stmt.executeUpdate();
    }

    private List<Dot> getSavedDots() throws SQLException {
        List<Dot> dots = new ArrayList<>();
        String sql = "SELECT * FROM dot;";
        Statement stmt = dbConnection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int x = rs.getInt("x");
            int y = rs.getInt("y");
            String color = rs.getString("color");
            int r = rs.getInt("radius");
            dots.add(new Dot((double)x, (double) y, Color.valueOf(color), (double) r));
        }
        return dots;
    }

    public void removeHandler(ClientThread ct) {
        handlers.remove(ct);
    }

    public void disconnectHandlers() {
        handlers.forEach(handler -> handler.send("disconnected"));
        handlers.clear();
    }

    public void broadcast(String message) throws SQLException {
        Dot dot = Dot.fromMessage(message);
        saveDot(dot);
        for (ClientThread ct : handlers) {
            ct.send(message);
        }
    }

    public void listen() throws IOException, SQLException {
        System.out.println("Server started");
        while (true) {
            System.out.println("Waiting for connection...");
            Socket socket = serverSocket.accept();
            ClientThread ct = new ClientThread(socket, this);
            Thread thread = new Thread(ct);
            thread.start();
            // wysłać wszystkie punkty z bazy danych do klienta
            for (Dot d : getSavedDots()) {
                ct.send(d.toMessage());
            }
            handlers.add(ct);
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        Server server = new Server(5000);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.disconnectHandlers();
        }));

        server.listen();
    }
}
