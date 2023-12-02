package indy;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PaneOrganizer {

    private BorderPane root;
    private Pane centerPane;
    private HBox bottomPane;
    private VBox sidePane;
    private State state;

    public PaneOrganizer() {
        this.root = new BorderPane();
        this.state = State.SOLID;
        this.setupCenterPane();
        this.setupSidePane();
        this.setupBottomPane();
        new Sandbox(this.centerPane, this.sidePane, this.bottomPane, this.state);
    }

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
        this.bottomPane.setSpacing(10);
        this.bottomPane.setPadding(new Insets(0, 0, 0, 10));

        this.root.setBottom(this.bottomPane);
        this.bottomPane.setFocusTraversable(false);
    }

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
        this.sidePane.setSpacing(30);
        this.sidePane.setPadding(new Insets(10, 0, 0, 0));

        Button quitButton = new Button("QUIT");
        quitButton.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        quitButton.setFont(new Font("Comic Sans MS", 20));
        quitButton.setStyle("-fx-background-color: #F00000");
        quitButton.setTextFill(Color.BLACK);
        quitButton.setOnAction((ActionEvent e) -> System.exit(0));

        this.sidePane.getChildren().add(quitButton);
        this.root.setRight(this.sidePane);
        this.sidePane.setFocusTraversable(false);
    }

    public BorderPane getRoot() {
        return this.root;
    }
}
