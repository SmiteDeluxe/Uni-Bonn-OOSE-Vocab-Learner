package application;

import animatefx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ***
 */
public class ControllerLearning extends AnchorPane implements Initializable {

    @FXML
    private Label SelectedVocable, selectedVocable, UserErrors, userErrors, userTranslationFinal, UserTranslation, UserScored, userScored, SelectedVocableTranslation, selectedVocableTranslation, Phase, phase,  UserScore, userScore, TestedVocables, testedVocables, CorrectVocables, correctVocables, AverageScored, averageScored, WrongVocables, wrongVocables, ScoreFinal, scoreFinal, PartCorrectVocables, partCorrectVocables, ErrorNoVocables;
    @FXML
    private HBox learningButtons, errorCorrectionBox, results, resultButtons, resultButtons2, errorNoVocables, errorButtons;
    @FXML
    private VBox mainLearning, normalLoad;
    @FXML
    private Button solveButton, nextButton, manualCorrectionButton, manualCorrectButton, manualPartlyCorrectButton,
            manualWrongButton, submitErrorsButton, showAllVocablesButton, hideAllVocablesButton, restartLearningButton, changeToVocabulary, changeToGoals;
    @FXML
    private TextField userTranslation, errorCorrection;
    @FXML
    private TableView<VocabList> allVocables;
    @FXML
    private AnchorPane base, inBase;
    @FXML
    private StackPane scoreCircle;
    @FXML
    private BorderPane toLoad;

    private List<Vocab> list;
    private List<VocabList> listTestedVocables = new ArrayList<>();
    private int currentVocIndex = 0;
    private Vocab currentVocable;

    private double score = 0;
    private int completelyCorrect = 0;
    private int partlyCorrect = 0;
    private int completelyWrong = 0;
    private int testedAmount = 0;
    private int scored;
    private int newPhase;
    private int errors;

    private boolean manualCorrectionVisibility = false;


    private final ControllerMainScene controllerMainScene; // for restartLearning and to get the vocables, that are selected for learning

    /**
     * constructor
     * @param controllerMainScene  mainController, where all other controllers and the vocables, that are selected for learning are referenced
     */
    public ControllerLearning(ControllerMainScene controllerMainScene)
    {
        this.controllerMainScene = controllerMainScene;
        FXMLLoader goLog = new FXMLLoader(getClass().getResource("/Learning.fxml"));
        goLog.setRoot(this);
        goLog.setController(this);
        try {
            goLog.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * adds a few listeners for textfields, inorder to enable some buttons
     * setup for the scenes and learning-scene
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLang();
        if(Variables.getSelectedVocab().isEmpty()) {
            changeLearningScene(false, false, false, false, false, false,true,true);
            return;
        } else {
            list = Variables.getSelectedVocab();
        }
        changeLearningScene(true,true,false,false,false, false,false,false);

        buttonFeedback(solveButton);
        buttonFeedback(nextButton);
        buttonFeedback(manualCorrectionButton);
        buttonFeedback(manualCorrectButton);
        buttonFeedback(manualPartlyCorrectButton);
        buttonFeedback(manualWrongButton);
        buttonFeedback(submitErrorsButton);
        buttonFeedback(showAllVocablesButton);
        buttonFeedback(hideAllVocablesButton);
        buttonFeedback(restartLearningButton);
        buttonFeedback(changeToVocabulary);
        buttonFeedback(changeToGoals);

        base.prefHeightProperty().bind(Main.primarStage.heightProperty().subtract(255));
        inBase.prefHeightProperty().bind(base.prefHeightProperty());

        userTranslation.textProperty().addListener((observable, oldValue, newValue) -> solveButton.setDisable(newValue.isEmpty()));
        errorCorrection.textProperty().addListener((observable, oldValue, newValue) -> submitErrorsButton.setDisable(newValue.isEmpty()));
        errorCorrection.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                errorCorrection.setText(newValue.replaceAll("[^\\d]", ""));
            }
        }
        );
        changeToNextVocable();

        this.setOnKeyPressed(event -> {
            if(event.getCode()== KeyCode.ENTER&&userTranslation.textProperty().isNotEmpty().get()) {
                solveVocable();
            }
        });
    }

    /**
     * sets the texts of the selected language for all UI elements
     */
    public void setLang(){
        SelectedVocable.setText(LocalizationManager.get("SelectedVocable"));
        UserErrors.setText(LocalizationManager.get("UserErrors"));
        UserTranslation.setText(LocalizationManager.get("UserTranslation"));
        UserScored.setText(LocalizationManager.get("UserScored"));
        SelectedVocableTranslation.setText(LocalizationManager.get("SelectedVocableTranslation"));
        Phase.setText(LocalizationManager.get("Phase"));
        UserScore.setText(LocalizationManager.get("UserScore"));
        manualCorrectionButton.setText(LocalizationManager.get("manualCorrectionButton"));
        manualCorrectButton.setText(LocalizationManager.get("manualCorrectButton"));
        manualPartlyCorrectButton.setText(LocalizationManager.get("manualPartlyCorrectButton"));
        manualWrongButton.setText(LocalizationManager.get("manualWrongButton"));
        errorCorrection.setPromptText(LocalizationManager.get("ErrorCorrection"));
        submitErrorsButton.setText(LocalizationManager.get("submitErrorsButton"));
        solveButton.setText(LocalizationManager.get("solveButton"));
        nextButton.setText(LocalizationManager.get("nextButton"));
        TestedVocables.setText(LocalizationManager.get("TestedVocables"));
        CorrectVocables.setText(LocalizationManager.get("CorrectVocables"));
        AverageScored.setText(LocalizationManager.get("AverageScored"));
        WrongVocables.setText(LocalizationManager.get("WrongVocables"));
        ScoreFinal.setText(LocalizationManager.get("ScoreFinal"));
        PartCorrectVocables.setText(LocalizationManager.get("PartCorrectVocables"));
        showAllVocablesButton.setText(LocalizationManager.get("showAllVocablesButton"));
        restartLearningButton.setText(LocalizationManager.get("restartLearningButton"));
        hideAllVocablesButton.setText(LocalizationManager.get("hideAllVocablesButton"));
        ErrorNoVocables.setText(LocalizationManager.get("ErrorNoVocables"));
        changeToVocabulary.setText(LocalizationManager.get("changeToVocabulary"));
        changeToGoals.setText(LocalizationManager.get("changeToGoals"));
    }

    /**
     * Hides different UI-Elements inorder to change Scenes
     * @param pMainLearning
     * @param pLearningButtons
     * @param pResults
     * @param pResultButtons
     * @param pAllVocables
     */
    public void changeLearningScene(boolean pMainLearning, boolean pLearningButtons, boolean pResults, boolean pResultButtons, boolean pAllVocables, boolean pResultButtons2, boolean pErrorButtons, boolean pErrorNoVocables){
        mainLearning.setVisible(pMainLearning);
        learningButtons.setVisible(pLearningButtons);
        results.setVisible(pResults);
        resultButtons.setVisible(pResultButtons);
        allVocables.setVisible(pAllVocables);
        resultButtons2.setVisible(pResultButtons2);
        errorNoVocables.setVisible(pErrorNoVocables);
        errorButtons.setVisible(pErrorButtons);
    }

    /**
    * switches to the result screen
     */
    public void openResults(){
        changeLearningScene(false,false,true,true,false, false,false,false);

        /*
          *  creates the Table to list all tested Vocables and their results in detail
         */
        TableColumn<VocabList, Integer> numberColumn = new TableColumn<>(LocalizationManager.get("numberColumn"));
        TableColumn<VocabList, String> questionColumn  = new TableColumn<>(LocalizationManager.get("questionColumn"));
        TableColumn<VocabList, String> answerColumn = new TableColumn<>(LocalizationManager.get("answerColumn"));
        TableColumn<VocabList, String> userTranslationColumn = new TableColumn<>(LocalizationManager.get("userTranslationColumn"));
        TableColumn<VocabList, Integer> errorColumn = new TableColumn<>(LocalizationManager.get("errorColumn"));
        TableColumn<VocabList, Integer> newPhaseColumn = new TableColumn<>(LocalizationManager.get("newPhaseColumn"));

        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("language1"));
        answerColumn.setCellValueFactory(new PropertyValueFactory<>("language2"));
        userTranslationColumn.setCellValueFactory(new PropertyValueFactory<>("userTranslation"));
        errorColumn.setCellValueFactory(new PropertyValueFactory<>("errors"));
        newPhaseColumn.setCellValueFactory(new PropertyValueFactory<>("phase"));

        questionColumn.setStyle("-fx-alignment: center");
        answerColumn.setStyle("-fx-alignment: center");
        userTranslationColumn.setStyle("-fx-alignment: center");
        errorColumn.setStyle("-fx-alignment: center");
        newPhaseColumn.setStyle("-fx-alignment: center");

        allVocables.setItems(FXCollections.observableList(listTestedVocables));
        allVocables.getColumns().addAll(numberColumn, questionColumn, answerColumn, userTranslationColumn, errorColumn, newPhaseColumn);
        allVocables.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        allVocables.setEditable(false);


        /*
         *  fills the textfields on the overall resultscreen with numbers and values
         */
        testedVocables.setText(String.valueOf(testedAmount));
        correctVocables.setText(String.valueOf(completelyCorrect));
        partCorrectVocables.setText(String.valueOf(partlyCorrect));
        wrongVocables.setText(String.valueOf(completelyWrong));
        scoreFinal.setText(String.valueOf(score));
        averageScored.setText(String.valueOf(score/testedAmount));
    }

    /**
     *  from the Errorscreen switch to Vocabulary
     */
    public void gotoVocabList(){
        controllerMainScene.gotoVocList();
    }

    /**
     *  from the Errorscreen switch to Goals
     */
    public void gotoGoals(){
        controllerMainScene.gotoGoals();
    }

    /**
    * start learning again, with the same vocables
     */
    public void restartLearning(){
        List<Vocab> vocabToLearn = Variables.getSelectedVocab();
        Collections.shuffle(vocabToLearn);
        controllerMainScene.gotoLearn(vocabToLearn);
    }

    /**
     *  switches from the results overall to stats for every single vocable
     */
    public void showAllVocables(){
        changeLearningScene(false,false,false,false,true, true,false,false);
    }

    public void hideAllVocables(){
        changeLearningScene(false,false,true,true,false, false,false,false);
    }

    /**
     * shows the a new vocable that shall be translated
     */
    public void nextVocable(){
        saveCurrentResults();

        currentVocIndex++;
        if (currentVocIndex == list.size()) {
            openResults();
            return;
        }
        changeToNextVocable();
    }

    /**
     * changes the scene to displays the next vocable
     */
    public void changeToNextVocable(){
        nextButton.setVisible(false);
        userTranslation.setVisible(true);
        userTranslationFinal.setVisible(false);
        UserErrors.setVisible(false);
        UserScored.setVisible(false);
        SelectedVocableTranslation.setVisible(false);
        Phase.setVisible(false);
        userTranslation.setDisable(false);

        currentVocable = list.get(currentVocIndex);
        selectedVocable.setText(currentVocable.question);
        userTranslation.setText("");
        userErrors.setText("");
        userScored.setText("");
        phase.setText("");
        selectedVocableTranslation.setText("");

        manualCorrectionButton.setVisible(false);
        manualCorrectButton.setVisible(false);
        manualPartlyCorrectButton.setVisible(false);
        manualWrongButton.setVisible(false);
        errorCorrectionBox.setVisible(false);
    }

    /**
     * shows the answer and result for the current vocable
     */
    public void solveVocable(){
        nextButton.setVisible(true);
        userTranslation.setVisible(false);
        userTranslationFinal.setVisible(true);
        UserErrors.setVisible(true);
        UserScored.setVisible(true);
        SelectedVocableTranslation.setVisible(true);
        Phase.setVisible(true);

        manualCorrectionButton.setVisible(true);
        //solveButton.setDisable(true);
        userTranslation.setDisable(true);

        selectedVocableTranslation.setText((currentVocable.answer));
        compareAnswer();
    }

    /**
     * compares the translation from the user with the answer of the asked vocable
     * highlight the differences between the given answer and solution
     * changes the Phase
     */
    public void compareAnswer(){
        /*
         * compares the translation from the user with the answer of the asked vocable
         */
        StringCompareAlgorithm.Cost cost = StringCompareAlgorithm.calculate(userTranslation.getText(),currentVocable.answer, true);
        errors = cost.getCost();

        /*
        * highlight the differences between the given answer and solution
         */

        List<Label> labels = userTranslation.getText()
                .chars()
                .mapToObj(codePoint -> {
                    var label = new Label(Character.toString(codePoint));
                    label.setFont(Font.font(null, FontWeight.BOLD, 16));
                    label.setTextFill(Paint.valueOf("767676"));
                    return label;
                })
                .collect(Collectors.toCollection(ArrayList::new));

        int additionalOffset = 0;
        for (StringCompareAlgorithm.Modification modification : cost.getModifications()) {
            int modificationIndex = modification.getPosition() + additionalOffset;

            switch (modification.getType()) {
                case SUBSTITUTION:
                    labels.get(modificationIndex).setTextFill(Color.ORANGE);
                    break;
                case DELETION:
                    labels.get(modificationIndex).setTextFill(Color.RED);
                    break;
                case INSERTION:
                    var insertionLabel = new Label("_");
                    insertionLabel.setFont(Font.font(null, FontWeight.BOLD, 16));
                    insertionLabel.setTextFill(Color.RED);
                    labels.add(modificationIndex, insertionLabel);

                    additionalOffset++;
                    break;
            }
        }

        var hBox = new HBox(0, labels.toArray(Label[]::new));
        hBox.setAlignment(Pos.CENTER_LEFT);

        userTranslationFinal.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        userTranslationFinal.setGraphic(hBox);

        /*
         *  checks if the Vocable is either correct, partly correct or wrong
         */
        if(errors == 0) {
            correctAnswer();
        }
        else {
            if (errors == 1) {
                partlyCorrectAnswer();
            }
            else {
                wrongAnswerGiven();
            }
        }
    }

    /**
    *  sets the results for a wrong answer
     */
    public void wrongAnswerGiven(){
        if(currentVocable.phase != 0) {
            newPhase = currentVocable.phase - 1;
        }
        else{
            newPhase = 0;
        }
        scored = 0;
        showCurrentResults();
    }

    /**
     * sets the results for a completely correct answer
     */
    public void correctAnswer(){
        manualCorrectionVisibility = false;
        showManualCorrectionButtons();

        if(currentVocable.phase != 4) {
            newPhase = currentVocable.phase + 1;
        }else{
            newPhase = 4;
        }
        scored = 3;
        errors = 0;
        showCurrentResults();
    }

    /**
     *sets the results for a partly correct answer
     */
    public void partlyCorrectAnswer(){
        manualCorrectionVisibility = false;
        showManualCorrectionButtons();

        newPhase = currentVocable.phase;
        scored = 1;
        errors = 1;
        showCurrentResults();
    }

    /**
     * sets the results for a wrong answer manually
     */
    public void wrongAnswer(){
        if(manualCorrectionVisibility) {
            errorCorrectionBox.setVisible(true);
            errorCorrection.setText("");
        }
    }

    /**
     * sets the ammount of for a errors in an answer manually
     */
    public void submitErrors(){
        manualCorrectionVisibility = false;
        showManualCorrectionButtons();

        errors = Integer.parseInt(errorCorrection.getText());
        wrongAnswerGiven();
    }

    /**
     * toggles the buttons for the manual correction
     */
    public void manualCorrection(){
        manualCorrectionVisibility ^= true;
        showManualCorrectionButtons();
    }

    /**
     * sets the visibility of the buttons for the manual correction
     */
    public void showManualCorrectionButtons(){
        manualCorrectButton.setVisible(manualCorrectionVisibility);
        manualPartlyCorrectButton.setVisible(manualCorrectionVisibility);
        manualWrongButton.setVisible(manualCorrectionVisibility);
        manualCorrectButton.setDisable(false);
        manualPartlyCorrectButton.setDisable(false);
        manualWrongButton.setDisable(false);
        errorCorrectionBox.setVisible(false);
    }

    /**
     * shows results of the current vocable
     */
    public void showCurrentResults(){
        userErrors.setText(" " + errors);
        userScored.setText(" + " + scored);
        userScore.setText(" " + (score + scored) );
        if(scored>0) {
            new Pulse(scoreCircle).setSpeed(2.0).setCycleCount(2).play();
        } else {
            new Shake(scoreCircle).setCycleCount(1).play();
        }
        phase.setText(" " + newPhase);
    }

    /**
     * saves results of the current vocable
     * increases counters for the result screen
     */
    public void saveCurrentResults(){
        ControllerLoading l = new ControllerLoading();
        l.changeSize(0,300, 0.0,100.0);
        toLoad.setCenter(l);

        if(errors == 0) {
            completelyCorrect++;
        }
        else {
            if (errors == 1) {
                partlyCorrect++;
            }
            else {
                completelyWrong++;
            }
        }
        testedAmount++;

        score +=  scored;

        listTestedVocables.add(new VocabList( testedAmount,  currentVocable.question, currentVocable.answer, userTranslation.getText(), errors, newPhase));

        if(newPhase != currentVocable.getPhase()) { // update Vocable only if the phase changes
            Vocab phaseOfcurrentVocable = new Vocab(currentVocable.id, currentVocable.answer, currentVocable.question, currentVocable.language, newPhase);
            new Thread(() -> {
                APICalls api = new APICalls();
                api.editVoc(phaseOfcurrentVocable);
                Platform.runLater(() -> toLoad.setCenter(normalLoad));
            }).start();
        } else {
            toLoad.setCenter(normalLoad);
        }
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