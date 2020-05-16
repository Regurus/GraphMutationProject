package Controllers;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Main {
    @FXML
    private TextField bar_a;
    @FXML
    private TextField bar_n;
    @FXML
    private TextField bar_m;

    @FXML
    private void toggleBarabasi(){
        bar_a.setDisable(!bar_a.isDisabled());
    }
}
