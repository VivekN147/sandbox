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
import java.util.HashMap;

public class Sandbox {

    private Pane centerPane;
    private VBox sidePane;
    private HBox bottomPane;
    private ArrayList<Particle> particleList;
    private ArrayList<Particle> liquidList;
    private ArrayList<Particle> tempParticleList;
    private Particle currentParticle;
    private Particle particleChange;
    private Circle cursorBoundary;
    private int cursorRadius;
    private Timeline timeline;
    private State state;
    private Label temperatureLabel;
    private Slider temperatureSlider;
    private Label pressureLabel;
    private Slider pressureSlider;
    private HashMap<String, ArrayList<Particle>> particleGrid;
    private ButtonCreator buttonCreator;
    private boolean changeOccurred;

    public Sandbox(Pane centerPane, VBox sidePane, HBox bottomPane, State state) {
        this.changeOccurred = false;
        this.particleGrid = new HashMap<>();
        this.state = state;
        this.cursorRadius = 5;
        this.particleList = new ArrayList<>();
        this.liquidList = new ArrayList<>();
        this.tempParticleList = new ArrayList<>();
        this.centerPane = centerPane;
        this.sidePane = sidePane;
        this.bottomPane = bottomPane;
        this.addButtons();
        this.setupMousePressHandler();
        this.setupMouseDragHandler();
        this.setupMouseScrollHandler();
        this.setupTemperature();
        this.setupPressure();
        this.setupTimeline();
    }

    private void addButtons() {
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

        Button oxygenButton = new Button("OXYG");
        oxygenButton.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        oxygenButton.setFont(new Font("Comic Sans MS", 20));
        oxygenButton.setStyle("-fx-background-color: #8B0000");
        oxygenButton.setTextFill(Color.WHITE);
        oxygenButton.setOnAction((ActionEvent e) -> {
            this.state = State.OXYG;
        });

        Button hydrogenButton = new Button("HYGN");
        hydrogenButton.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        hydrogenButton.setFont(new Font("Comic Sans MS", 20));
        hydrogenButton.setStyle("-fx-background-color: #32CD32");
        hydrogenButton.setTextFill(Color.BLACK);
        hydrogenButton.setOnAction((ActionEvent e) -> {
            this.state = State.HYGN;
        });

        Button lavaButton = new Button("LAVA");
        lavaButton.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        lavaButton.setFont(new Font("Comic Sans MS", 20));
        lavaButton.setStyle("-fx-background-color: #FF4500");
        lavaButton.setTextFill(Color.BLACK);
        lavaButton.setOnAction((ActionEvent e) -> {
            this.state = State.LAVA;
        });

        Button stoneButton = new Button("STNE");
        stoneButton.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        stoneButton.setFont(new Font("Comic Sans MS", 20));
        stoneButton.setStyle("-fx-background-color: #808080");
        stoneButton.setTextFill(Color.BLACK);
        stoneButton.setOnAction((ActionEvent e) -> {
            this.state = State.STNE;
        });

        this.bottomPane.getChildren().addAll(waterButton, waterVaporButton, iceButton, snowButton, oxygenButton, hydrogenButton, lavaButton, stoneButton);
    }

    private void createGrid() {
        this.particleGrid.clear();
        for(Particle particle : this.particleList) {
            int xCoord = (int) (Math.floor(particle.getParticle().getCenterX() / Constants.SMOOTHING_RADIUS));
            int yCoord = (int) (Math.floor(particle.getParticle().getCenterY() / Constants.SMOOTHING_RADIUS));

            if(this.particleGrid.containsKey(xCoord + ", " + yCoord)) {
                this.particleGrid.get(xCoord + ", " + yCoord).add(particle);
            } else {
                ArrayList<Particle> bucket = new ArrayList<>();
                bucket.add(particle);
                this.particleGrid.put(xCoord + ", " + yCoord, bucket);
            }
        }
    }

    private ArrayList<Particle> computeNeighbors(Particle particle1) {
        ArrayList<Particle> neighbors = new ArrayList<>();
        neighbors.add(particle1);
        int xCoord = (int) (Math.floor(particle1.getParticle().getCenterX() / Constants.SMOOTHING_RADIUS));
        int yCoord = (int) (Math.floor(particle1.getParticle().getCenterY() / Constants.SMOOTHING_RADIUS));

        this.tempParticleList.clear();
        if(this.particleGrid.get(xCoord + ", " + yCoord) != null) this.tempParticleList.addAll(this.particleGrid.get(xCoord + ", " + yCoord));
        for(Particle particle2 : this.tempParticleList) {
            double xOffset = particle1.getParticle().getCenterX() - particle2.getParticle().getCenterX();
            double yOffset = particle1.getParticle().getCenterY() - particle2.getParticle().getCenterY();
            boolean distanceCheck = Math.pow(xOffset, 2) + Math.pow(yOffset, 2) <= Constants.SMOOTHING_RADIUS * Constants.SMOOTHING_RADIUS;
            if(!particle1.equals(particle2) && distanceCheck) {
                neighbors.add(particle2);
            }
        }

        this.tempParticleList.clear();
        if(this.particleGrid.containsKey((xCoord + 1) + ", " + yCoord)) {
            this.tempParticleList.addAll(this.particleGrid.get((xCoord + 1) + ", " + yCoord));
        }
        if(this.particleGrid.containsKey((xCoord - 1) + ", " + yCoord)) {
            this.tempParticleList.addAll(this.particleGrid.get((xCoord - 1) + ", " + yCoord));
        }
        if(this.particleGrid.containsKey(xCoord + ", " + (yCoord + 1))) {
            this.tempParticleList.addAll(this.particleGrid.get(xCoord + ", " + (yCoord + 1)));
        }
        if(this.particleGrid.containsKey(xCoord + ", " + (yCoord - 1))) {
            this.tempParticleList.addAll(this.particleGrid.get(xCoord + ", " + (yCoord - 1)));
        }

        for(Particle particle2 : this.tempParticleList) {
            double xOffset = particle1.getParticle().getCenterX() - particle2.getParticle().getCenterX();
            double yOffset = particle1.getParticle().getCenterY() - particle2.getParticle().getCenterY();
            boolean distanceCheck = Math.pow(xOffset, 2) + Math.pow(yOffset, 2) <= Constants.SMOOTHING_RADIUS * Constants.SMOOTHING_RADIUS;
            if(distanceCheck) {
                neighbors.add(particle2);
            }
        }
        return neighbors;
    }

    private void setupTemperature() {
        this.temperatureLabel = new Label("Temperature: 22.0 C");
        this.temperatureLabel.setTextFill(Color.WHITE);
        this.temperatureSlider = new Slider(-200, 200, 22.0);
        this.sidePane.getChildren().addAll(this.temperatureLabel, this.temperatureSlider);
        this.temperatureSlider.setOnMouseDragged((MouseEvent e) -> this.sliderHandler());
        this.temperatureSlider.setOnKeyPressed((KeyEvent e) -> this.sliderHandler());
    }

    private void sliderHandler() {
        this.temperatureLabel.setText("Temperature: " + (int) (this.temperatureSlider.getValue()) + " C");
    }

    private void setupPressure() {
        this.pressureLabel = new Label("Pressure: 0 atm");
        this.pressureLabel.setTextFill(Color.WHITE);
        this.pressureSlider = new Slider(-20, 20, 0);
        this.sidePane.getChildren().addAll(this.pressureLabel, this.pressureSlider);
        this.pressureSlider.setOnMouseDragged((MouseEvent e) -> this.pressureHandler());
        this.pressureSlider.setOnKeyPressed((KeyEvent e) -> this.pressureHandler());
    }

    private void pressureHandler() {
        this.pressureLabel.setText("Pressure: " + (int) (this.pressureSlider.getValue()) + " atm");
    }

    private void setupTimeline() {
        KeyFrame kf = new KeyFrame(Duration.seconds(Constants.DURATION),
                (ActionEvent e) -> this.updateTimeline());
        this.timeline = new Timeline(kf);
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.timeline.play();
    }

    private ArrayList<Particle> updateLiquidList(ArrayList<Particle> particleList) {
        this.liquidList.clear();
        for(Particle particle : particleList) {
            if(particle.getState() == State.WATR || particle.getState() == State.LAVA) {
                this.liquidList.add(particle);
            }
        }
        return this.liquidList;
    }

    private void updateTimeline() {
        if(!this.particleList.isEmpty()) {
            this.createGrid();
            for(int i = 0; i < this.particleList.size(); i++ ) {
                ArrayList<Particle> neighbors = this.computeNeighbors(this.particleList.get(i));
                this.liquidList = this.updateLiquidList(neighbors);

                this.particleList.get(i).gravity();

                if((int) (this.particleList.get(i).getParticle().getCenterY()) < Constants.BOTTOM_CHECK) {
                    this.particleList.get(i).disperse();
                }

                if((this.particleList.get(i).getState() == State.WATR || this.particleList.get(i).getState() == State.LAVA) && this.currentParticle != null) {
                    new LiquidControl(this.liquidList);
                }

                this.stateChange(i, neighbors);

                if(i >= 0) {
                    this.particleList.get(i).collide(0, neighbors);
                }

                if(this.changeOccurred) {
                    i--;
                    this.changeOccurred = false;
                }

                this.reaction(i, neighbors);
            }
        }
    }

    private void stateChange(int i, ArrayList<Particle> neighbors) {
        Temperature temp = new Temperature(this.temperatureSlider, this.particleList.get(i));
        Pressure pressure = new Pressure(this.pressureSlider, this.particleList.get(i));
        if(temp.liquidToGas()) {
            this.centerPane.getChildren().remove(this.particleList.get(i).getParticle());
            this.particleChange = new WaterVapor(this.particleList.get(i).getParticle().getCenterX(), this.particleList.get(i).getParticle().getCenterY(), 10, Color.CORNFLOWERBLUE);
            this.particleList.add(this.particleChange);
            this.particleList.remove(i);
            neighbors.add(this.particleChange);
            neighbors.remove(0);
            this.liquidList.remove(0);
            this.centerPane.getChildren().add(this.particleChange.getParticle());
            this.changeOccurred = true;
        } else if(temp.gasToLiquid()) {
            this.centerPane.getChildren().remove(this.particleList.get(i).getParticle());
            this.particleChange = new Water(this.particleList.get(i).getParticle().getCenterX(), this.particleList.get(i).getParticle().getCenterY(), 10, Color.MEDIUMBLUE);
            this.particleList.add(this.particleChange);
            this.liquidList.add(this.particleChange);
            this.particleList.remove(i);
            neighbors.add(this.particleChange);
            neighbors.remove(0);
            this.centerPane.getChildren().add(this.particleChange.getParticle());
            this.changeOccurred = true;
        } else if(pressure.solidToPowder()) {
            this.centerPane.getChildren().remove(this.particleList.get(i).getParticle());
            this.particleChange = new Snow(this.particleList.get(i).getParticle().getCenterX(), this.particleList.get(i).getParticle().getCenterY(), 10, Color.ALICEBLUE);
            this.particleList.add(this.particleChange);
            this.particleList.remove(i);
            neighbors.add(this.particleChange);
            neighbors.remove(0);
//                    System.out.println(neighbors);
            this.centerPane.getChildren().add(this.particleChange.getParticle());
            this.changeOccurred = true;
        } else if(pressure.powderToSolid()) {
            this.centerPane.getChildren().remove(this.particleList.get(i).getParticle());
            this.particleChange = new Ice(this.particleList.get(i).getParticle().getCenterX(), this.particleList.get(i).getParticle().getCenterY(), 10, Color.WHITE);
            this.particleList.add(this.particleChange);
            this.particleList.remove(i);
            neighbors.remove(0);
            neighbors.add(this.particleChange);
            this.centerPane.getChildren().add(this.particleChange.getParticle());
            this.changeOccurred = true;
        }
    }

    private void reaction(int i, ArrayList<Particle> neighbors) {
        if(i >= 0) {
            if(neighbors.size() > 1) {
                for(int j = 0; j < neighbors.size(); j++) {
                    if(i == j) continue;
                    if(this.particleList.get(i).getState() == State.WATR &&
                            neighbors.get(j).getState() == State.ICE &&
                            this.particleList.get(i).getParticle().getBoundsInParent().intersects(neighbors.get(j).getParticle().getBoundsInParent())) {
                        this.centerPane.getChildren().remove(neighbors.get(j).getParticle());
                        this.centerPane.getChildren().remove(this.particleList.get(i).getParticle());

                        this.temperatureSlider.setValue(110);
                        this.sliderHandler();
                        Particle changeParticle = new WaterVapor(this.particleList.get(i).getParticle().getCenterX(), this.particleList.get(i).getParticle().getCenterY(), 10, Color.CORNFLOWERBLUE);

                        this.particleList.add(changeParticle);
                        this.liquidList.remove(neighbors.get(0));
                        neighbors.remove(this.particleList.get(j));
                        neighbors.remove(this.particleList.get(i));
                        this.particleList.remove(j);
                        this.particleList.remove(i - 1);

                        this.centerPane.getChildren().add(changeParticle.getParticle());
                        i = i - 2;
                        j = j - 2;
                    }
                }
            }
        }
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
                    case WTRV:
                        this.currentParticle = new WaterVapor(e.getX(), e.getY(), 10, Color.CORNFLOWERBLUE);
                        break;
                    case WATR:
                        this.currentParticle = new Water(e.getX(), e.getY(), 10, Color.MEDIUMBLUE);
                        break;
                    case SNOW:
                        this.currentParticle = new Snow(e.getX(), e.getY(), 10, Color.ALICEBLUE);
                        break;
                    case ICE:
                        this.currentParticle = new Ice(e.getX(), e.getY(), 10, Color.WHITE);
                        break;
                    case OXYG:
                        this.currentParticle = new Oxygen(e.getX(), e.getY(), 10, Color.DARKRED);
                        break;
                    case HYGN:
                        this.currentParticle = new Hydrogen(e.getX(), e.getY(), 10, Color.LIMEGREEN);
                        break;
                    case LAVA:
                        this.currentParticle = new Lava(e.getX(), e.getY(), 10, Color.ORANGERED);
                        break;
                    case STNE:
                        this.currentParticle = new Stone(e.getX(), e.getY(), 10, Color.GREY);
                        break;
                    default:
                        break;
                }
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
