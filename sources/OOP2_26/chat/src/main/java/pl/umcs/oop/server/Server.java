package pl.umcs.oop.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private int port;
    private List<ClientThread> clients;
    public Server(int port) {
        this.port = port;
        // CopyOnWriteArrayList -- implementacja listy bezpieczna dla wielowątkowości
        this.clients = new CopyOnWriteArrayList<>();
    }

    public void listen() {
        // ServerSocket służy do przyjmowania połączeń klientów
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serwer nasłuchuje na porcie " + port);

            while (true) {
                System.out.println("Oczekiwanie na połączenie...");
                Socket clientSocket = serverSocket.accept();        // ten Socket służy do komunikacji z klientem
                System.out.println("Połączono: " + clientSocket);
                ClientThread ct = new ClientThread(clientSocket, clients);   // tworzymy wątek dla klienta
                clients.add(ct);
                ct.start();                                         // i delegujemy komunikację  z klientem do niego
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server(12345);
        server.listen();
    }
}
