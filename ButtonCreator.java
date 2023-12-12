package indy;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ButtonCreator {

    private HBox bottomPane;
    private State state;
    private Button elementButton;

    public ButtonCreator(HBox bottomPane, State state) {
        Button waterButton = new Button("WATR");
        waterButton.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        waterButton.setFont(new Font("Comic Sans MS", 20));
        waterButton.setStyle("-fx-background-color: #0000CD");
        waterButton.setTextFill(Color.WHITE);
        waterButton.setOnAction((ActionEvent e) -> {
            this.state = State.WATR;
        });

        Button waterVaporButton = new Button("WTRV");
        waterVaporButton.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        waterVaporButton.setFont(new Font("Comic Sans MS", 20));
        waterVaporButton.setStyle("-fx-background-color: #6495ED");
        waterVaporButton.setTextFill(Color.BLACK);
        waterVaporButton.setOnAction((ActionEvent e) -> {
            this.state = State.WTRV;
        });

        Button iceButton = new Button("ICE");
        iceButton.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        iceButton.setFont(new Font("Comic Sans MS", 20));
        iceButton.setStyle("-fx-background-color: #F0F8FF");
        iceButton.setTextFill(Color.BLACK);
        iceButton.setOnAction((ActionEvent e) -> {
            this.state = State.ICE;
        });

        Button snowButton = new Button("SNOW");
        snowButton.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        snowButton.setFont(new Font("Comic Sans MS", 20));
        snowButton.setStyle("-fx-background-color: #F5FFFA");
        snowButton.setTextFill(Color.BLACK);
        snowButton.setOnAction((ActionEvent e) -> {
            this.state = State.SNOW;
        });

        bottomPane.getChildren().addAll(waterButton, waterVaporButton, iceButton, snowButton);
    }

    private void addButton(String text, String bgColor, Color textFill) {
        this.elementButton = new Button(text);
        this.elementButton.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        this.elementButton.setFont(new Font("Comic Sans MS", 20));
        this.elementButton.setStyle(bgColor);
        this.elementButton.setTextFill(textFill);


        this.bottomPane.getChildren().add(this.elementButton);
    }

    public State setState(State state) {
        this.elementButton.setOnAction((ActionEvent e) -> {
            this.state = state;
        });
        return this.state;
    }

}
