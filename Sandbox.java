package indy;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Sandbox {

    private Pane centerPane;
    private VBox sidePane;
    private ArrayList<Circle> drawNonSolid;
    private Circle currentParticle;
    private Circle cursorBoundary;
    private int cursorRadius;
    private Label temperatureLabel;
    private Slider temperatureSlider;

    public Sandbox(Pane centerPane, VBox sidePane) {
        this.cursorRadius = 5;
        this.drawNonSolid = new ArrayList<>();
        this.centerPane = centerPane;
        this.sidePane = sidePane;
        this.setupMousePressHandler();
        this.setupMouseDragHandler();
        this.setupMouseScrollHandler();
        this.setupTemperature();
    }

    private void setupTemperature() {
        this.temperatureLabel = new Label("Temperature: 22.0 C");
        this.temperatureLabel.setTextFill(Color.WHITE);
        this.temperatureSlider = new Slider(-500, 500, 22.0);
        this.sidePane.getChildren().addAll(this.temperatureLabel, this.temperatureSlider);
        this.temperatureSlider.setOnMouseDragged((MouseEvent e) -> this.sliderHandler());
        this.temperatureSlider.setOnKeyPressed((KeyEvent e) -> this.sliderHandler());
    }

    private void sliderHandler() {
        this.temperatureLabel.setText("Temperature: " + (int) (this.temperatureSlider.getValue()) + " C");
    }

    private void setupMousePressHandler() {
        this.centerPane.setOnMousePressed((MouseEvent e) -> this.mouseHandler(e));
    }

    private void setupMouseDragHandler() {
        this.centerPane.setOnMouseDragged((MouseEvent e) -> this.mouseHandler(e));
    }

    private void setupMouseScrollHandler() {
        this.centerPane.setOnScroll((ScrollEvent e) -> this.mouseScrollHandler(e));
    }

    private void mouseHandler(MouseEvent e) {
        switch(e.getButton()) {
            case PRIMARY:
                this.currentParticle = new Circle(e.getX(), e.getY(), 5, Color.SKYBLUE);
                this.drawNonSolid.add(this.currentParticle);

                this.centerPane.getChildren().add(this.currentParticle);
                break;
            case SECONDARY:
                this.cursorBoundary = new Circle(e.getX(), e.getY(), this.cursorRadius);
                for(int i = 0; i < this.drawNonSolid.size(); i++) {
                    if(this.cursorBoundary.intersects(this.drawNonSolid.get(i).getBoundsInLocal())) {
                        this.centerPane.getChildren().remove(this.drawNonSolid.get(i));
                        this.drawNonSolid.remove(this.drawNonSolid.get(i));
                    }
                }

                break;
        }
    }

    private void mouseScrollHandler(ScrollEvent e) {
        if(e.getDeltaY() > 0 && this.cursorRadius < 100) {
            this.cursorRadius++;
        } else if (e.getDeltaY() < 0 && this.cursorRadius > 1) {
            this.cursorRadius--;
        }
    }
}
