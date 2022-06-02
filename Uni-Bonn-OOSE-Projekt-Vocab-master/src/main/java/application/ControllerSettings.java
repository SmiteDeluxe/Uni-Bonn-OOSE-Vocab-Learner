package application;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class for the Controller of the Settings Window
 */
public class ControllerSettings extends AnchorPane implements Initializable {
    public ControllerSettings() {
        FXMLLoader goSett = new FXMLLoader(getClass().getResource("/Settings.fxml"));
        goSett.setRoot(this);
        goSett.setController(this);
        try {
            goSett.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    ComboBox<String> langDD;
    @FXML
    Label listSetts1;
    @FXML
    Button apply;

    /**
     * Init the default Settings and Tools to edit them
     */
    public void initialize(URL location, ResourceBundle resources) {
        LocalizationManager.Init();
        setLang();
        langDD.getItems().addAll("English", "German");

        //Set selected Language to the selected item in dropdown
        try {
            Gson g = new Gson();
            Settings s = g.fromJson(new FileReader("src/main/resources/Settings.json"), Settings.class);
            String lang = s.getLang().toLowerCase();
            char c = lang.charAt(0);
            c = Character.toUpperCase(c);
            langDD.getSelectionModel().select(lang.replaceFirst(String.valueOf(lang.charAt(0)), String.valueOf(c)));
        } catch (Exception e){
            e.printStackTrace();
        }

        buttonFeedback(apply);
    }

    /**
     * Apply changes and reload Scene
     */
    public void apply() {
        try {
            LocalizationManager.setLanguage(LocalizationManager.SupportedLanguage.valueOf(langDD.getSelectionModel().getSelectedItem().toUpperCase()));

            //refresh Main Window
            new SettingsChanger();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the Strings for Language of Program
     */
    public void setLang() {
        listSetts1.setText(LocalizationManager.get("listSetts1"));
        apply.setText(LocalizationManager.get("apply"));
    }
    public void buttonFeedback(Button b) {
        b.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                b.setStyle("-fx-font-size: 15; -fx-padding: 4 0 4 0");
            } else {
                b.setStyle("");
            }
        });
    }
}
