package pl.umcs.oop.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        // do komunikacji z serwerem służy obiekt klasy Socket
        try (Socket socket = new Socket("localhost", 12345)) {
            System.out.println("Połączono z serwerem");
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);  // do wysyłania wiadomości
            Scanner consoleIn = new Scanner(System.in); // System.in to InputStream z konsoli
            Receiver receiver = new Receiver(socket);
            receiver.start();

            String input = consoleIn.nextLine();
            while (!input.equals("exit")) {     // ustaliliśmy, że słowo exit kończy działanie
                out.println(input);             // wysyłanie wczytanej linii
                input = consoleIn.nextLine();   // czytamy kolejne wejscie z konsoli
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
