package indy;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Sandbox {

    private Pane centerPane;
    private ArrayList<Circle> draw;
    private Circle currentParticle;

    public Sandbox(Pane centerPane) {
        this.draw = new ArrayList<>();
        this.centerPane = centerPane;
        this.setupMousePressHandler();
        this.setupMouseDragHandler();
        this.setupMouseScrollHandler();
    }

    private void setupMousePressHandler() {
        this.centerPane.setOnMousePressed((MouseEvent e) -> this.mousePressHandler(e));
    }

    private void setupMouseDragHandler() {
        this.centerPane.setOnMouseDragged((MouseEvent e) -> this.mouseDragHandler(e));
    }

    private void setupMouseScrollHandler() {
        this.centerPane.setOnScroll((ScrollEvent e) -> this.mouseScrollHandler(e));
    }

    private void mousePressHandler(MouseEvent e) {
        this.currentParticle = new Circle(e.getX(), e.getY(), 5, Color.SKYBLUE);
        this.draw.add(this.currentParticle);

        this.centerPane.getChildren().add(this.currentParticle);
    }

    private void mouseDragHandler(MouseEvent e) {
        this.currentParticle = new Circle(e.getX(), e.getY(), 5, Color.SKYBLUE);
        this.draw.add(this.currentParticle);

        this.centerPane.getChildren().add(this.currentParticle);
    }

    private int mouseScrollHandler(ScrollEvent e) {
        int radius = 5;
        if(e.getDeltaY() > 0) {
            radius++;
        } else if (e.getDeltaY() < 0) {
            radius--;
        }
        return radius;
    }
}
