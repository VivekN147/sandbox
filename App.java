package indy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * It's time for Indy! This is the main class to get things started.
 *
 * Class comments here...
 *
 */

public class App extends Application {

    @Override
    public void start(Stage stage) {
        PaneOrganizer organizer = new PaneOrganizer();
        Scene scene = new Scene(organizer.getRoot(),
                                Constants.STAGE_WIDTH,
                                Constants.STAGE_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Sandbox!");
        stage.show();
    }

    public static void main(String[] args) {
;        launch(args); // launch is a method inherited from Application
    }
}
