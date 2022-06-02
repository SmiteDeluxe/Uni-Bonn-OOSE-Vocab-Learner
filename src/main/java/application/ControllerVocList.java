package application;
;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import static application.Main.primarStage;


/**
 * Generates the Vocabulary list and implements the functions search, edit, delete and add
 * @author ***
 */
public class ControllerVocList extends AnchorPane implements Initializable {
    public VBox VBoxMain, VBoxMenu;
    @FXML
    public HBox HBoxMenuFrame, HBoxButtonBar;
    @FXML
    public TextField SearchBarTextField;
    @FXML
    public Button AddButton, DeleteButton, SearchButton, ResetButton;
    @FXML
    public ToggleButton EditEnableToggle;
    @FXML
    public TableView<VocabSelection> VocTableList;
    @FXML
    BorderPane table;

    public ObservableList<VocabSelection> list;

    APICalls api = new APICalls();
    Timer timer = new Timer();

    private final ControllerMainScene controllerMainScene;

    public ControllerVocList(ControllerMainScene controllerMainScene) {
        this.controllerMainScene = controllerMainScene;

        FXMLLoader goList = new FXMLLoader(getClass().getResource("/VocList.fxml"));
        goList.setRoot(this);
        goList.setController(this);
        try {
            goList.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the controller class.
     * This method is automatically called after the login is finished(the corresponding fxml file: VocList.fxml is loaded)
     * and the pane as well as its features are loaded
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //execute language selection
        LocalizationManager.Init();
        setLang();

        //Button feedback
        buttonFeedback(AddButton);
        buttonFeedback(EditEnableToggle);
        buttonFeedback(DeleteButton);
        buttonFeedback(SearchButton);
        buttonFeedback(ResetButton);


        //ID-Column stuff
        TableColumn<VocabSelection, Integer> idColumn = new TableColumn(LocalizationManager.get("id"));
        idColumn.setMinWidth(40);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        //Answer-Column stuff
        TableColumn<VocabSelection, String> answerColumn = new TableColumn(LocalizationManager.get("answer"));
        answerColumn.setMinWidth(80);
        answerColumn.setCellValueFactory(
                new PropertyValueFactory<>("answer"));
        answerColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        answerColumn.setOnEditCommit(event -> {
            (event.getTableView().getItems().get(
                    event.getTablePosition().getRow())
            ).setAnswer(event.getNewValue());
            //System.out.println(event.getNewValue()+event.getRowValue());
            Vocab tmp = event.getRowValue();
            editOnAPI(tmp);
        });

        //Question-Column initialization
        TableColumn<VocabSelection, String> questionColumn = new TableColumn(LocalizationManager.get("question"));
        questionColumn.setMinWidth(80);
        questionColumn.setCellValueFactory(
                new PropertyValueFactory<>("question"));
        questionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        questionColumn.setOnEditCommit(event -> {
            (event.getTableView().getItems().get(
                    event.getTablePosition().getRow())
            ).setQuestion(event.getNewValue());
            Vocab tmp = event.getRowValue();
            editOnAPI(tmp);
        });

        //Language-Column initialization
        TableColumn<VocabSelection, String> languageColumn = new TableColumn<>(LocalizationManager.get("language"));
        languageColumn.setMinWidth(80);
        languageColumn.setCellValueFactory(
                new PropertyValueFactory<>("language"));
        languageColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        languageColumn.setOnEditCommit(event -> {
            (event.getTableView().getItems().get(
                    event.getTablePosition().getRow())
            ).setLanguage(event.getNewValue());
            Vocab tmp = event.getRowValue();
            editOnAPI(tmp);
        });

        //Phase-Column initialization
        TableColumn<VocabSelection, Integer> phaseColumn = new TableColumn(LocalizationManager.get("phase"));
        phaseColumn.setMinWidth(20);
        phaseColumn.setCellValueFactory(new PropertyValueFactory<>("phase"));

        //Select-Column initialization
        TableColumn<VocabSelection, CheckBox> selectColumn = new TableColumn(LocalizationManager.get("select"));
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("selectVocab"));

        //Set Data to list and add Columns
        VocTableList.getColumns().addAll(idColumn, answerColumn, questionColumn, languageColumn, phaseColumn, selectColumn);
        VocTableList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //System.out.println("1" + list);
        getData();
        //System.out.println("2" + list);
        VocTableList.setItems(list);

        //Some default settings
        boolean testing = false;  //set all True FOR TESTING
        VocTableList.setEditable(testing);
        idColumn.setVisible(testing);
        selectColumn.setEditable(testing);
        selectColumn.setVisible(testing);
        AddButton.setVisible(testing);
        DeleteButton.setVisible(testing);
        EditEnableToggle.setText(LocalizationManager.get("enable"));

        /**
         * Toggle-function for enable and disable the edit-function
         */

        final ToggleGroup editGroup = new ToggleGroup();

        EditEnableToggle.setToggleGroup(editGroup);
        EditEnableToggle.setSelected(false);

        editGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                VocTableList.setEditable(false);
                selectColumn.setEditable(false); //Makes selectColumn not editable
                selectColumn.setVisible(false); //Makes selectColumn invisible
                DeleteButton.setVisible(false);
                AddButton.setVisible(false);
                //System.out.println("IF");
                EditEnableToggle.setText(LocalizationManager.get("enable"));

            } else {
                VocTableList.setEditable(true);
                selectColumn.setEditable(true); //Makes selectColumn editable
                selectColumn.setVisible(true); //Makes selectColumn visible
                DeleteButton.setVisible(true);
                AddButton.setVisible(true);
                //System.out.println("ELse");
                EditEnableToggle.setText(LocalizationManager.get("disable"));
            }
        });

        /**
         * Timer for automatic refresh after two minutes and at launch
         */
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override

            public void run() {
                getData();
                VocTableList.setItems(list);
                //System.out.println("Refresh");

                //Cancel timer when application is closed
                primarStage.setOnCloseRequest(event -> timer.cancel());
            }

            ;
        }, 0, 120000);
    }

    private void editOnAPI(Vocab tmp) {
        ControllerLoading l = new ControllerLoading();
        l.changeSize(410,700,180.0,250.0);
        table.setCenter(l);
        Vocab voc = new Vocab(tmp.getId(), tmp.getAnswer(), tmp.getQuestion(), tmp.getLanguage(), tmp.getPhase());
        Thread t3 = new Thread(() -> {
            api.editVoc(voc);

            try {
                api.getUsersVocab();
            } catch (IOException e) {
                e.printStackTrace();
            }
            getData();
            VocTableList.setItems(list);

            Platform.runLater(() -> table.setCenter(VocTableList));
        });
        t3.start();
    }

    /**
     * When text is entered, the list is searched for valid hits and the result is set to list
     */

    public void textEntered() {
        String input = SearchBarTextField.getText();
        if (input == null || input.isEmpty()) {
            VocTableList.setItems(list);
            return;
        }
        String lowerCaseFilter = input.toLowerCase();
        ObservableList<VocabSelection> tmp = FXCollections.observableArrayList();

        for (VocabSelection voc : list) {

            if (voc.getAnswer().toLowerCase().contains(lowerCaseFilter)) {
                tmp.add(voc);
                //System.out.println("test"+tmp.toString() );
            } else if (voc.getQuestion().toLowerCase().contains(lowerCaseFilter)) {
                tmp.add(voc);
                //System.out.println("test2"+tmp.toString() );
            } else if (voc.getLanguage().toLowerCase().contains(lowerCaseFilter)) {
                tmp.add(voc);
                //System.out.println("test3"+tmp.toString() );
            }
        }
        list = tmp;
        //System.out.println("erg" + list);
        VocTableList.setItems(list);
    }

    /**
     * When called, resets the table to it's unfiltered Stage
     */
    public void resetSearchPressed() {
        getData();
        VocTableList.setItems(list);
        SearchBarTextField.clear();
    }

    /**
     * When called, opens a new window an asks for new Vocabulary
     */
    public void addButtonPressed() {
        try {
            Variables.setC(this);
            Stage addStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/AddVoc.fxml"));

            Scene scene = new Scene(root, 300, 300);
            addStage.setTitle(LocalizationManager.get("addVoc"));
            addStage.setResizable(false);
            addStage.centerOnScreen();
            addStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo-512.png")));
            addStage.setScene(scene);
            addStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the vocabulary which is checked in the displayed Checkbox
     */

    public void deleteSelectedVoc() {
        ArrayList<VocabSelection> tmpArray = new ArrayList();
        for (VocabSelection voc : list) {
            if (voc.getSelectVocab().isSelected()) {
                handleDeletion(voc);
            }
        }
    }

    /**
     * Deletes the handed vocabulary from the API and refreshes the TableView
     * @param voc vocabulary that is supposed to be deleted
     */
    public void handleDeletion(VocabSelection voc) {
        ControllerLoading l = new ControllerLoading();
        l.changeSize(410,700,180.0,250.0);
        table.setCenter(l);
        new Thread(() -> {
            api.deleteVoc(voc.getId());
            try {
                api.getUsersVocab();
            } catch (IOException e) {
                e.printStackTrace();
            }
            getData();
            VocTableList.setItems(list);
            Platform.runLater(() -> table.setCenter(VocTableList));
        }).start();
    }

    /**
     * Method called when the language is changed.
     * Set's all Variables to the current selected language
     */
    public void setLang() {
        SearchBarTextField.setPromptText(LocalizationManager.get("searchField"));
        SearchButton.setText(LocalizationManager.get("search"));
        AddButton.setText(LocalizationManager.get("add"));
        DeleteButton.setText(LocalizationManager.get("delete"));
        EditEnableToggle.setText(LocalizationManager.get("enable"));
    }

    /**
     * Gets the userdata from the variables class and adds it to the list displayed at the TableView
     */
    public void getData() {
        ArrayList<VocabSelection> tmp = new ArrayList<>();
        for (Vocab v : Variables.getUsersVocab()) {
            tmp.add(new VocabSelection(v, false));
        }
        list = FXCollections.observableList(tmp);
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
    public void buttonFeedback(ToggleButton b) {
        b.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                b.setStyle("-fx-font-size: 15; -fx-padding: 4 0 4 0");
            } else {
                b.setStyle("");
            }
        });
    }
}