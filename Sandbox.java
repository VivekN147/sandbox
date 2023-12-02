package indy;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;

public class Sandbox {

    private Pane centerPane;
    private VBox sidePane;
    private HBox bottomPane;
    private ArrayList<Particle> particleList;
    private Particle currentParticle;
    private Circle cursorBoundary;
    private int cursorRadius;
    private Label temperatureLabel;
    private Slider temperatureSlider;
    private Timeline timeline;
    private State state;

    public Sandbox(Pane centerPane, VBox sidePane, HBox bottomPane, State state) {
        this.state = state;
        this.cursorRadius = 5;
        this.particleList = new ArrayList<>();
        this.centerPane = centerPane;
        this.sidePane = sidePane;
        this.bottomPane = bottomPane;
        this.addButtons();
        this.setupMousePressHandler();
        this.setupMouseDragHandler();
        this.setupMouseScrollHandler();
        this.setupTemperature();
        this.setupTimeline();
    }

    private void addButtons() {
        Button waterButton = new Button("WATR");
        waterButton.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        waterButton.setFont(new Font("Comic Sans MS", 20));
        waterButton.setStyle("-fx-background-color: #0000CD");
        waterButton.setTextFill(Color.WHITE);
        waterButton.setOnAction((ActionEvent e) -> {
            this.state = State.LIQUID;
        });

        Button waterVaporButton = new Button("WTRV");
        waterVaporButton.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        waterVaporButton.setFont(new Font("Comic Sans MS", 20));
        waterVaporButton.setStyle("-fx-background-color: #6495ED");
        waterVaporButton.setTextFill(Color.BLACK);
        waterVaporButton.setOnAction((ActionEvent e) -> {
            this.state = State.GAS;
        });

        Button iceButton = new Button("ICE");
        iceButton.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        iceButton.setFont(new Font("Comic Sans MS", 20));
        iceButton.setStyle("-fx-background-color: #F0F8FF");
        iceButton.setTextFill(Color.BLACK);
        iceButton.setOnAction((ActionEvent e) -> {
            this.state = State.SOLID;
        });

        Button snowButton = new Button("SNOW");
        snowButton.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        snowButton.setFont(new Font("Comic Sans MS", 20));
        snowButton.setStyle("-fx-background-color: #F5FFFA");
        snowButton.setTextFill(Color.BLACK);
        snowButton.setOnAction((ActionEvent e) -> {
            this.state = State.POWDER;
        });

        this.bottomPane.getChildren().addAll(waterButton, waterVaporButton, iceButton, snowButton);
    }

    private void setupTimeline() {
        KeyFrame kf = new KeyFrame(Duration.seconds(Constants.DURATION),
                (ActionEvent e) -> this.updateTimeline());
        this.timeline = new Timeline(kf);
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.timeline.play();
    }

    private void updateTimeline() {
        if(!this.particleList.isEmpty()) {
            for(Particle particle : this.particleList) {
                //particle.collide();
                particle.gravity();
                if((int) (particle.getParticle().getCenterY()) < Constants.BOTTOM_CHECK) {
                    particle.disperse();
                }
                particle.moveLiquid();
                //System.out.println(particle.getParticle().getCenterY());
            }
        }
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
                switch(this.state) {
                    case GAS:
                        break;
                    case LIQUID:
                        this.currentParticle = new Liquid(e.getX(), e.getY(), 7, Color.CORNFLOWERBLUE, this);
                        break;
                    case POWDER:
                        this.currentParticle = new Powder(e.getX(), e.getY(), 7, Color.WHITE, this.particleList);
                        break;
                    case SOLID:
                        this.currentParticle = new Solid(e.getX(), e.getY(), 7, Color.WHITE, this.particleList);
                        break;
                    default:
                        break;
                }
                //this.currentParticle.getParticleList() = this.getParticleList();
                this.particleList.add(this.currentParticle);
                this.centerPane.getChildren().add(this.currentParticle.getParticle());
                break;
            case SECONDARY:
                this.cursorBoundary = new Circle(e.getX(), e.getY(), this.cursorRadius, Color.TRANSPARENT);
                for(int i = 0; i < this.particleList.size(); i++) {
                    if(this.cursorBoundary.intersects(this.particleList.get(i).getParticle().getBoundsInLocal())) {
                        this.centerPane.getChildren().remove(this.particleList.get(i).getParticle());
                        this.particleList.remove(this.particleList.get(i));
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

    public ArrayList<Particle> getParticleList() {
        return this.particleList;
    }
}
