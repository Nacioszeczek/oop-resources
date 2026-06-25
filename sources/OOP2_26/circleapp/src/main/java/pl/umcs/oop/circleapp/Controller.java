package pl.umcs.oop.circleapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import pl.umcs.oop.circleapp.client.ServerThread;

import java.io.IOException;
import java.net.Socket;

public class Controller {
    @FXML
    private TextField addressField;
    @FXML
    private TextField portField;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Slider radiusSlider;
    @FXML
    private Canvas canvas;

    private ServerThread serverThread;

    @FXML
    protected void onStartServerClicked() {
        // IGNORE
    }
    @FXML
    protected void onConnectClicked() {
        // 1. pobierz adres oraz port hosta
        String host = addressField.getText();
        int port = Integer.parseInt(portField.getText());
        // 2. połącz się z serwerem
        try {
            serverThread = new ServerThread(host, port);

            serverThread.setConsumer(dot -> Platform.runLater(() -> {
                canvas.getGraphicsContext2D().setFill(dot.color());
                canvas.getGraphicsContext2D().fillOval(
                        dot.x() - dot.radius(), dot.y() - dot.radius(),
                        dot.radius()*2, dot.radius()*2);
            }));
            serverThread.setDaemon(true);
            serverThread.start();
        } catch (IOException e) {
            System.out.println("Błąd połączenia z serwerem: " + e.getMessage());
        }

    }
    @FXML
    protected void onMouseClicked(MouseEvent mouseEvent) {
        // 1. pobrać pozycję myszki na canvas
        if (mouseEvent.getTarget() == canvas
                && mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED
                && mouseEvent.getButton() == MouseButton.PRIMARY) {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();
            // 2. pobrać kolor z colorPicker
            Color color = colorPicker.getValue();
            // 3. pobrać promień z radiusSlider
            double radius = radiusSlider.getValue();
            // 4. prześlij dane do serwera
            Dot dot = new Dot(x, y, color, radius);
            serverThread.send(dot.toMessage());

//            canvas.getGraphicsContext2D().setFill(color); // ustawia kolor następnej akcji
//            canvas.getGraphicsContext2D().fillOval(x - radius, y - radius, radius*2, radius*2);
        }
    }
}
