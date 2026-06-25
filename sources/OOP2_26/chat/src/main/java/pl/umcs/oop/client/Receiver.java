package pl.umcs.oop.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Receiver extends Thread {
    private Socket socket;
    public Receiver(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());  // do odczytywania wiadomości z serwera
            while (in.hasNextLine()) {
                String message = in.nextLine();                 // odczyt wiadomości z serwera
                System.out.println(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("Rozłączono z serwerem");
        }
    }
}
