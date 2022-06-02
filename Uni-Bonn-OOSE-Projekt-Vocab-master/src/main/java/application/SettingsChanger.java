package application;

/**
 * Class to make "Apply" in settings work
 */
public class SettingsChanger extends ControllerMainScene {
        public SettingsChanger() {
            try {
                changeScene("MainScene.fxml", 1080,720, true, false, Variables.getX(), Variables.getY());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
}