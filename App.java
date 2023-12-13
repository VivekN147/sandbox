package indy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class starts the program setting up the GUI and running the launch method.
 */

public class App extends Application {

    /**
     * This method creates the PaneOrganizer and assigns it to a new scene along with some
     * dimensions. It then sets the scene and title of the stage and shows the stage.
     */
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
        launch(args); // launch is a method inherited from Application
    }
}
