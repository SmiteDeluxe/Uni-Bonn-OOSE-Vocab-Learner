package application;

import animatefx.animation.Bounce;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerLoading extends AnchorPane implements Initializable {
    @FXML
    HBox circles;
    @FXML
    Circle circle1,circle2,circle3;
    @FXML
    AnchorPane load;

    public ControllerLoading() {
        FXMLLoader loading = new FXMLLoader(getClass().getResource("/loading.fxml"));
        loading.setRoot(this);
        loading.setController(this);
        try {
            loading.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        AnchorPane.setTopAnchor(circles, 220.0);
        AnchorPane.setLeftAnchor(circles, 70.0);
        new Bounce(circle1).setCycleCount(100).setSpeed(0.8).setDelay(new Duration(100)).play();
        new Bounce(circle2).setCycleCount(100).setSpeed(0.8).setDelay(new Duration(200)).play();
        new Bounce(circle3).setCycleCount(100).setSpeed(0.8).setDelay(new Duration(300)).play();
    }
    public void changeSize(int h, int w, double distTop, double distLeft) {
        load.prefHeight(h);
        load.prefWidth(w);
        AnchorPane.setTopAnchor(circles, distTop);
        AnchorPane.setLeftAnchor(circles, distLeft);
    }
}