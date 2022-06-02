package application;

import java.io.FileWriter;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the Main Window where the other sub-Content-Windows are situated
 * Controls Navigation Bar, Window switching, Title Bar and the main Content
 */
public class ControllerMainScene extends Main implements Initializable {
	@FXML
	private Label loginAs;
	@FXML
	Button vocab, learn, goals, logout, settings;
	@FXML
	ImageView logo;
	@FXML
	VBox navBar, mainContentFrame;
	@FXML
	BorderPane mainContent;
	@FXML
	AnchorPane navBox;
	@FXML
	AnchorPane userInfo;
	@FXML
	Label page;

	//Standard values for buttons
	String colorVoc = "-fx-background-color: #ffec00 ; -fx-border-width: 0 0 0 5; -fx-border-color: white",colorGoals = "-fx-background-color: #ffe600",colorLearn = "-fx-background-color: #ffe600";

	/**
	 * inits the Controler
	 * -Defaults for language, Position of window, Default content, Positions and Bindings and Button Feedback
	 */
	public void initialize(URL arg0, ResourceBundle arg1) {
		//set Language stuff
		LocalizationManager.Init();
		setLang();

		//Position
		Variables.setX(primarStage.getX());
		Variables.setY(primarStage.getY());
		primarStage.xProperty().addListener((observable, oldValue, newValue) -> Variables.setX(newValue.doubleValue()));
		primarStage.yProperty().addListener((observable, oldValue, newValue) -> Variables.setY(newValue.doubleValue()));

		//Tooltips
		logout.setTooltip(new Tooltip("Logout"));

		//SetDefaultmainContent
		listThread();
		logo.setFitWidth(120);
		logo.setPreserveRatio(true);
		userInfo.prefWidthProperty().bind(primarStage.widthProperty());
		mainContentFrame.prefHeightProperty().bind(primarStage.heightProperty().subtract(125));
		mainContent.prefHeightProperty().bind(mainContentFrame.heightProperty());
		navBox.prefHeightProperty().bind(primarStage.heightProperty());
		AnchorPane.setTopAnchor(navBar, 0.0);
		AnchorPane.setBottomAnchor(settings, 10.0);
		AnchorPane.setLeftAnchor(settings, 10.0);

		//User Info for User
		loginAs.setText(Variables.getMail());

		//Button feedback:
		vocab.setStyle(colorVoc);
		vocab.hoverProperty().addListener((observableValue, oldV, newV) -> {
			if (newV) {
				vocab.setStyle("-fx-font-size: 17;"+colorVoc);
			} else if(oldV) {
				vocab.setStyle("-fx-font-size: 15;"+colorVoc);
			}
		});
		learn.hoverProperty().addListener((observableValue, oldV, newV) -> {
			if (newV) {
				learn.setStyle("-fx-font-size: 17;"+colorLearn);
			} else if(oldV) {
				learn.setStyle("-fx-font-size: 15;"+colorLearn);
			}
		});
		goals.hoverProperty().addListener((observableValue, oldV, newV) -> {
			if (newV) {
				goals.setStyle("-fx-font-size: 17;"+colorGoals);
			} else if(oldV) {
				goals.setStyle("-fx-font-size: 15;"+colorGoals);
			}
		});
		settings.hoverProperty().addListener(((observable, oldValue, newValue) -> {
			if(newValue) {
				settings.setStyle("-fx-background-size: 40px");
			} else if(oldValue) {
				settings.setStyle("-fx-background-size: 35px");
			}
		}));

	}

	/**
	 * Sets Language specific String names
	 */
	public void setLang() {
		vocab.setText(LocalizationManager.get("vocab"));
		learn.setText(LocalizationManager.get("learn"));
		goals.setText(LocalizationManager.get("goals"));
	}

	/**
	 * Switch main content to VocList
	 */
	public void gotoVocList() {
		listThread();
		vocab.setStyle("-fx-background-color: #ffec00; -fx-border-width: 0 0 0 5; -fx-border-color: white");
		goals.setStyle("-fx-background-color: #ffe600; -fx-border-width: 0 0 0 0");
		learn.setStyle("-fx-background-color: #ffe600; -fx-border-width: 0 0 0 0");
		colorVoc = "-fx-background-color: #ffec00; -fx-border-width: 0 0 0 5; -fx-border-color: white";
		colorGoals = "-fx-background-color: #ffe600";
		colorLearn = "-fx-background-color: #ffe600";
	}

	private void listThread() {
		ControllerMainScene t = this;
		ControllerLoading l = new ControllerLoading();
		l.changeSize(410,700,280.0,350.0);
		mainContent.setCenter(l);
		new Thread(() -> {
			ControllerVocList VocListCont = new ControllerVocList(t);
			Platform.runLater(() -> mainContent.setCenter(VocListCont));
		}).start();
		page.setText(vocab.getText());
	}

	/**
	 * if you want to get back to the learning Tab via the Selection-Bar	on the left Side.
	 */
	public void gotoLearn() {
		List<Vocab> lischt = Variables.getUsersVocab();
		gotoLearn(lischt);
	}

	/**
	 * if you want to start learning
	 */
	public void gotoLearn(List<Vocab> vocabList) {
		Collections.shuffle(vocabList);
		Variables.setSelectedVocab(vocabList);
		ControllerLearning learnCont = new ControllerLearning(this);
		mainContent.setCenter(learnCont);
		page.setText(learn.getText());
		learn.setStyle("-fx-background-color: #ffec00; -fx-border-width: 0 0 0 5; -fx-border-color: white");
		vocab.setStyle("-fx-background-color: #ffe600; -fx-border-width: 0 0 0 0");
		goals.setStyle("-fx-background-color: #ffe600; -fx-border-width: 0 0 0 0");
		colorVoc = "-fx-background-color: #ffe600";
		colorGoals = "-fx-background-color: #ffe600";
		colorLearn = "-fx-background-color: #ffec00; -fx-border-width: 0 0 0 5; -fx-border-color: white";
	}

	/**
	 * Switch main content to Goals (what to learn)
	 */
	public void gotoGoals() {
		ControllerGoals goalsCont = new ControllerGoals(this);
		mainContent.setCenter(goalsCont);
		page.setText(goals.getText());
		goals.setStyle("-fx-background-color: #ffec00; -fx-border-width: 0 0 0 5; -fx-border-color: white");
		vocab.setStyle("-fx-background-color: #ffe600; -fx-border-width: 0 0 0 0");
		learn.setStyle("-fx-background-color: #ffe600; -fx-border-width: 0 0 0 0");
		colorVoc = "-fx-background-color: #ffe600";
		colorGoals = "-fx-background-color: #ffec00; -fx-border-width: 0 0 0 5; -fx-border-color: white";
		colorLearn = "-fx-background-color: #ffe600";
	}

	/**
	 * Switch main content to Settings
	 */
	public void openSettings(){
		goals.setStyle("-fx-background-color: #ffe600; -fx-border-width: 0 0 0 0");
		vocab.setStyle("-fx-background-color: #ffe600; -fx-border-width: 0 0 0 0");
		learn.setStyle("-fx-background-color: #ffe600; -fx-border-width: 0 0 0 0");
		colorVoc = "-fx-background-color: #ffe600";
		colorGoals = "-fx-background-color: #ffe600";
		colorLearn = "-fx-background-color: #ffe600";
		page.setText(LocalizationManager.get("setting"));
		ControllerSettings settingsCont = new ControllerSettings();
		mainContent.setCenter(settingsCont);
	}

	/**
	 * Method to Logout and return to Login window
	 */
	public void logout() throws Exception {
		Gson g = new GsonBuilder()
				.setPrettyPrinting()
				.create();

		//Make User Info empty
		User u = new User("", "");
		String s = g.toJson(u);

		//login Info To JSon
		FileWriter file = new FileWriter("src/main/resources/logInfo.json");
										file.write(s);
										file.close();
		changeScene("FXLogin.fxml", 850,520, false, false, Variables.getX(), Variables.getY());
	}
}
