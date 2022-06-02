package application;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the AddVoc.fxml pane
 * Gets the input from the user and converts it into an element which can be added to the API
 */
public class ControllerVocabAdd extends AnchorPane implements Initializable {
    @FXML
    private Label TopLabel, Lower1Label, Lower2Label, Lower3Label;
    @FXML
    private TextField AnswerTextField, QuestionTextField, LanguageTextField;
    @FXML
    private Button ConfirmButton, CancelButton;

    static final int START_PHASE = 0; // is currently ignored anyway. According to the APi 0, acc. to requirements 1

    public void initialize(URL url, ResourceBundle resourceBundle) {
        //execute language selection
        LocalizationManager.Init();
        setLang();

        buttonFeedback(ConfirmButton);
        buttonFeedback(CancelButton);
    }

    /**
     * Returns the new generated vocabulary and adds it to the VocList as well as communicate it to the API
     */
    public void parseNewVoc () {
        if (AnswerTextField.getText() != null && QuestionTextField.getText() != null && LanguageTextField.getText() != null) {
            Vocab newVoc = new Vocab(AnswerTextField.getText(), QuestionTextField.getText(), LanguageTextField.getText(), START_PHASE);
            ControllerLoading l = new ControllerLoading();
            l.changeSize(410,700,180.0,250.0);
            Variables.getC().table.setCenter(l);
            ControllerVocList call = Variables.getC();
            new Thread(() -> {
                try {
                    APICalls api = new APICalls();
                    api.postToVoc(newVoc);
                    api.getUsersVocab();
                    call.getData();
                    call.VocTableList.setItems(call.list);
                    Platform.runLater(() -> Variables.getC().table.setCenter(Variables.getC().VocTableList));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        AnswerTextField.clear();
        QuestionTextField.clear();
        LanguageTextField.clear();
    }
    //TODO check if entered voc is not empty and is not already in API

    /**
     * Closes the AddVoc stage -> returns to Vocab5 without changing any content
     */
    public void closeAddStage (){
        Stage Stage = (Stage) CancelButton.getScene().getWindow();
        Stage.close();
    }

    /**
     * Method for setting the language
     */
    public void setLang(){
        AnswerTextField.setPromptText(LocalizationManager.get("answerAdd"));
        QuestionTextField.setPromptText(LocalizationManager.get("questionAdd"));
        LanguageTextField.setPromptText(LocalizationManager.get("languageAdd"));
        ConfirmButton.setText(LocalizationManager.get("confirm"));
        CancelButton.setText(LocalizationManager.get("cancel"));
        TopLabel.setText(LocalizationManager.get("addLabel"));
    }
    public void buttonFeedback(Button b) {
        b.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                b.setStyle("-fx-font-size: 15; -fx-padding: 4 0 5 0");
            } else {
                b.setStyle("");
            }
        });
    }

}

