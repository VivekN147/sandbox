package indy;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This method sets up the GUI of the App, including the workspace, the side panel, and the
 * bottom panel as well as the quit button.
 */
public class PaneOrganizer {

    // Declaring variables for the different panes/root
    private BorderPane root;
    private Pane centerPane;
    private HBox bottomPane;
    private VBox sidePane;
    private State state;

    /**
     * This method is the constructor of the PaneOrganizer class, and contains the logic to
     * setup the different panes and start the simulation.
     */
    public PaneOrganizer() {
        this.root = new BorderPane();
        this.state = State.ICE;
        this.setupCenterPane();
        this.setupSidePane();
        this.setupBottomPane();
        new Sandbox(this.centerPane, this.sidePane, this.bottomPane, this.state);
    }

    /**
     * This method setups the workspace, styles it and gets the focus of the App.
     */
    private void setupCenterPane() {
        this.centerPane = new Pane();
        this.centerPane.setPrefSize(Constants.CENTER_WIDTH, Constants.CENTER_HEIGHT);
        this.centerPane.setStyle("-fx-background-color: #000000");
        this.centerPane.setBorder(new Border
                                      (new BorderStroke
                                              (Color.GRAY,
                                              BorderStrokeStyle.SOLID,
                                              null,
                                              new BorderWidths(Constants.BORDER_WIDTH))));

        this.root.setCenter(this.centerPane);
        this.centerPane.setFocusTraversable(true);
        this.centerPane.requestFocus();
    }

    /**
     * This method sets up the bottom pane, including padding and spacing as well as other styles.
     */
    private void setupBottomPane() {
        this.bottomPane = new HBox();
        this.bottomPane.setPrefSize(Constants.BOTTOM_WIDTH, Constants.BOTTOM_HEIGHT);
        this.bottomPane.setStyle("-fx-background-color: #000000");
        this.bottomPane.setBorder(new Border
                                      (new BorderStroke
                                              (Color.GRAY,
                                              BorderStrokeStyle.SOLID,
                                              null,
                                              new BorderWidths(Constants.BORDER_WIDTH))));
        this.bottomPane.setAlignment(Pos.CENTER_LEFT);
        this.bottomPane.setSpacing(Constants.BOTTOM_SPACING);
        this.bottomPane.setPadding(new Insets(0, 0, 0, Constants.PADDING));

        this.root.setBottom(this.bottomPane);
        this.bottomPane.setFocusTraversable(false);
    }

    /**
     * This method sets up the side panel and adds the quit button as well as its functionality.
     */
    private void setupSidePane() {
        this.sidePane = new VBox();
        this.sidePane.setPrefSize(Constants.SIDE_WIDTH, Constants.SIDE_HEIGHT);
        this.sidePane.setStyle("-fx-background-color: #000000");
        this.sidePane.setBorder(new Border
                                    (new BorderStroke
                                            (Color.GRAY,
                                            BorderStrokeStyle.SOLID,
                                            null,
                                            new BorderWidths(Constants.BORDER_WIDTH))));
        this.sidePane.setAlignment(Pos.TOP_CENTER);
        this.sidePane.setSpacing(Constants.SIDE_SPACING);
        this.sidePane.setPadding(new Insets(Constants.PADDING, 0, 0, 0));

        // Setting up quit button
        Button quitButton = new Button("QUIT");
        quitButton.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        quitButton.setFont(new Font("Comic Sans MS", Constants.FONT_SIZE));
        quitButton.setStyle("-fx-background-color: #F00000");
        quitButton.setTextFill(Color.BLACK);
        quitButton.setOnAction((ActionEvent e) -> System.exit(0));

        this.sidePane.getChildren().add(quitButton);
        this.root.setRight(this.sidePane);
        this.sidePane.setFocusTraversable(false);
    }

    /**
     * This method gets the root node of the JavaFX tree
     * @return the root node
     */
    public BorderPane getRoot() {
        return this.root;
    }
}
