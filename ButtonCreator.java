package indy;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ButtonCreator {

    private HBox bottomPane;

    public ButtonCreator(HBox bottomPane) {
        this.bottomPane = bottomPane;
        this.addButton("ICE", "-fx-background-color: #F0F8FF", Color.BLACK);

    }

    private void addButton(String text, String bgColor, Color textFill) {
        Button elementButton = new Button(text);
        elementButton.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        elementButton.setFont(new Font("Comic Sans MS", 20));
        elementButton.setStyle(bgColor);
        elementButton.setTextFill(textFill);
        this.bottomPane.getChildren().add(elementButton);
    }

}
