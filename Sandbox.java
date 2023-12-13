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

/**
 * This class contains the logic for particle creation/deletion, particle reactions, for
 * changing states. It also creates the buttons for each element and sliders for the
 * temperature and pressure of the simulation.
 */
public class Sandbox {

    // Declaring instance variables
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
    private boolean changeOccurred;

    /**
     * This method is the constructor of the Sandbox class and intializes the instance variables
     * as well as setting up the input handlers, temperature and pressure, and starting the
     * simulation timeline.
     * @param centerPane the workspace of the simulation
     * @param sidePane the side panel to put the sliders on
     * @param bottomPane the bottom panel to put the element buttons on
     * @param state the initial state of the particle
     */
    public Sandbox(Pane centerPane, VBox sidePane, HBox bottomPane, State state) {
        // Initializing variables
        this.changeOccurred = false;
        this.particleGrid = new HashMap<>();
        this.state = state;
        this.cursorRadius = Constants.CURSOR_RADIUS;
        this.particleList = new ArrayList<>();
        this.liquidList = new ArrayList<>();
        this.tempParticleList = new ArrayList<>();
        this.centerPane = centerPane;
        this.sidePane = sidePane;
        this.bottomPane = bottomPane;

        // Setting up the simulation
        this.buttonAdder();
        this.setupMousePressHandler();
        this.setupMouseDragHandler();
        this.setupMouseScrollHandler();
        this.setupTemperature();
        this.setupPressure();
        this.setupTimeline();
    }

    /**
     * This method creates a button based on the text, background color, text color, and state
     * provided.
     * @param text the text to show on the button
     * @param bgColor The color of the button
     * @param textColor the color of the text
     * @param state the state to change to if the button is clicked
     */
    private void buttonCreator(String text, String bgColor, Color textColor, State state) {
        Button elementButton = new Button(text);
        elementButton.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        elementButton.setFont(new Font("Comic Sans MS", Constants.FONT_SIZE));
        elementButton.setStyle(bgColor);
        elementButton.setTextFill(textColor);
        elementButton.setOnAction((ActionEvent e) -> {
            this.state = state;
        });

        this.bottomPane.getChildren().addAll(elementButton);
    }

    /**
     * This method calls the buttonCreator method for each particle that can possibly be created.
     */
    private void buttonAdder() {
        this.buttonCreator("WATR", "-fx-background-color: #0000CD", Color.WHITE, State.WATR);
        this.buttonCreator("WTRV", "-fx-background-color: #6495ED", Color.BLACK, State.WTRV);
        this.buttonCreator("ICE", "-fx-background-color: #F0F8FF", Color.BLACK, State.ICE);
        this.buttonCreator("SNOW", "-fx-background-color: #F5FFFA", Color.BLACK, State.SNOW);
        this.buttonCreator("OXYG", "-fx-background-color: #8B0000", Color.WHITE, State.OXYG);
        this.buttonCreator("HYGN", "-fx-background-color: #32CD32", Color.BLACK, State.HYGN);
        this.buttonCreator("LAVA", "-fx-background-color: #FF4500", Color.BLACK, State.LAVA);
        this.buttonCreator("STNE", "-fx-background-color: #808080", Color.BLACK, State.STNE);
    }

    /**
     * This method employs a hashmap creates a grid of the workspace and adds a particle to
     * a bucket if it is within other particles or a new bucket if it isn't near any others.
     */
    private void createGrid() {
        this.particleGrid.clear();
        for(Particle particle : this.particleList) {
            // Gets the integer coordinates of the particle
            int xCoord = (int) (Math.floor(particle.getParticle().getCenterX() /
                                Constants.SMOOTHING_RADIUS));
            int yCoord = (int) (Math.floor(particle.getParticle().getCenterY() /
                                Constants.SMOOTHING_RADIUS));

            if(this.particleGrid.containsKey(xCoord + ", " + yCoord)) {
                // Adds to an existing bucket
                this.particleGrid.get(xCoord + ", " + yCoord).add(particle);
            } else {
                // Creates a new bucket and adds the particle to it
                ArrayList<Particle> bucket = new ArrayList<>();
                bucket.add(particle);
                this.particleGrid.put(xCoord + ", " + yCoord, bucket);
            }
        }
    }

    /**
     * This method computes the neighbors of a particle and adds it to a list to be iterated
     * over instead of the particle list.
     * @param particle1 the particle to check for neighbors
     * @return a list of the neighbors of the particle
     */
    private ArrayList<Particle> computeNeighbors(Particle particle1) {
        ArrayList<Particle> neighbors = new ArrayList<>();
        neighbors.add(particle1);

        // Gets the integer coordinates of the particle
        int xCoord = (int) (Math.floor(particle1.getParticle().getCenterX() /
                            Constants.SMOOTHING_RADIUS));
        int yCoord = (int) (Math.floor(particle1.getParticle().getCenterY() /
                            Constants.SMOOTHING_RADIUS));

        this.tempParticleList.clear();

        // Adds particles in a bucket to a temporary list
        if(this.particleGrid.get(xCoord + ", " + yCoord) != null) {
            this.tempParticleList.addAll(this.particleGrid.get(xCoord + ", " + yCoord));
        }
        //Checks for particles within the bucket to see if they are within distance
        for(Particle particle2 : this.tempParticleList) {
            double xOffset = particle1.getParticle().getCenterX() -
                             particle2.getParticle().getCenterX();
            double yOffset = particle1.getParticle().getCenterY() -
                             particle2.getParticle().getCenterY();
            boolean distanceCheck = Math.pow(xOffset, 2) + Math.pow(yOffset, 2) <=
                                    Constants.SMOOTHING_RADIUS * Constants.SMOOTHING_RADIUS;
            if(!particle1.equals(particle2) && distanceCheck) {
                neighbors.add(particle2);
            }
        }

        // Getting the particles in neighboring buckets
        this.tempParticleList.clear();
        if(this.particleGrid.containsKey((xCoord + Constants.NEIGHBOR_CHECK) + ", " + yCoord)) {
            this.tempParticleList.addAll(this.particleGrid.get((xCoord + Constants.NEIGHBOR_CHECK)
                                                                + ", "
                                                                + yCoord));
        }
        if(this.particleGrid.containsKey((xCoord - Constants.NEIGHBOR_CHECK) + ", " + yCoord)) {
            this.tempParticleList.addAll(this.particleGrid.get((xCoord - Constants.NEIGHBOR_CHECK)
                                                                + ", "
                                                                + yCoord));
        }
        if(this.particleGrid.containsKey(xCoord + ", " + (yCoord + Constants.NEIGHBOR_CHECK))) {
            this.tempParticleList.addAll(this.particleGrid.get(xCoord +
                                                               ", " +
                                                               (yCoord +
                                                                Constants.NEIGHBOR_CHECK)));
        }
        if(this.particleGrid.containsKey(xCoord + ", " + (yCoord - Constants.NEIGHBOR_CHECK))) {
            this.tempParticleList.addAll(this.particleGrid.get(xCoord +
                                                               ", " +
                                                               (yCoord -
                                                                Constants.NEIGHBOR_CHECK)));
        }

        // Checks for particles in neighboring buckets to see if they are close enough
        for(Particle particle2 : this.tempParticleList) {
            double xOffset = particle1.getParticle().getCenterX() -
                             particle2.getParticle().getCenterX();
            double yOffset = particle1.getParticle().getCenterY() -
                             particle2.getParticle().getCenterY();
            boolean distanceCheck = Math.pow(xOffset, 2) + Math.pow(yOffset, 2) <=
                                    Constants.SMOOTHING_RADIUS * Constants.SMOOTHING_RADIUS;
            if(distanceCheck) {
                neighbors.add(particle2);
            }
        }
        return neighbors;
    }

    /**
     * This method updates the list of liquids based on the neighborhood.
     * @param particleList The list to check for liquids in
     * @return the updated liquid list
     */
    private ArrayList<Particle> updateLiquidList(ArrayList<Particle> particleList) {
        this.liquidList.clear();
        for(Particle particle : particleList) {
            if(particle.getState() == State.WATR || particle.getState() == State.LAVA) {
                this.liquidList.add(particle);
            }
        }
        return this.liquidList;
    }

    /**
     * This method creates the temperature label and slider and adds it to the side pane,
     * as well as calling a handler when the slider is dragged or a key is pressed.
     */
    private void setupTemperature() {
        this.temperatureLabel = new Label("Temperature: 22.0 C");
        this.temperatureLabel.setTextFill(Color.WHITE);
        this.temperatureSlider = new Slider(Constants.MIN_TEMP,
                                            Constants.MAX_TEMP,
                                            Constants.INITIAL_TEMP);
        this.sidePane.getChildren().addAll(this.temperatureLabel, this.temperatureSlider);
        this.temperatureSlider.setOnMouseDragged((MouseEvent e) -> this.temperatureHandler());
        this.temperatureSlider.setOnKeyPressed((KeyEvent e) -> this.temperatureHandler());
    }

    /**
     * This method updates the text of the temperature label based on movement of the slider.
     */
    private void temperatureHandler() {
        this.temperatureLabel.setText("Temperature: " +
                                      (int) (this.temperatureSlider.getValue()) +
                                      " C");
    }

    /**
     * This method creates the pressure label and slider and adds it to the side pane,
     * as well as calling a handler when the slider is dragged or a key is pressed.
     */
    private void setupPressure() {
        this.pressureLabel = new Label("Pressure: 0 atm");
        this.pressureLabel.setTextFill(Color.WHITE);
        this.pressureSlider = new Slider(Constants.MIN_PRESSURE,
                                         Constants.MAX_PRESSURE,
                                         Constants.INITIAL_PRESSURE);
        this.sidePane.getChildren().addAll(this.pressureLabel, this.pressureSlider);
        this.pressureSlider.setOnMouseDragged((MouseEvent e) -> this.pressureHandler());
        this.pressureSlider.setOnKeyPressed((KeyEvent e) -> this.pressureHandler());
    }

    /**
     * This method updates the text of the pressure label based on movement of the slider.
     */
    private void pressureHandler() {
        this.pressureLabel.setText("Pressure: " +
                                   (int) (this.pressureSlider.getValue()) +
                                   " atm");
    }

    /**
     * This method sets up the timeline and starts it to run forever, with a keyframe that
     * updates the simulation.
     */
    private void setupTimeline() {
        KeyFrame kf = new KeyFrame(Duration.seconds(Constants.DURATION),
                (ActionEvent e) -> this.updateTimeline());
        this.timeline = new Timeline(kf);
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.timeline.play();
    }

    /**
     * This method updates the simulation based on reaction/state changes and updates
     * the position of particles based on gravity/dispersion/liquid movement.
     */
    private void updateTimeline() {
        if(!this.particleList.isEmpty()) {
            this.createGrid();
            for(int i = 0; i < this.particleList.size(); i++ ) {
                // Calculating neighbors
                ArrayList<Particle> neighbors = this.computeNeighbors(this.particleList.get(i));
                this.liquidList = this.updateLiquidList(neighbors);

                // Updates the position of the particle based on gravity
                this.particleList.get(i).gravity();

                // Disperses particles if they are not at the bottom of the workspace
                if((int) (this.particleList.get(i).getParticle().getCenterY()) <
                          Constants.BOTTOM_CHECK) {
                    this.particleList.get(i).disperse();
                }

                // Updates the position of liquids based on their masses
                if(this.particleList.get(i).getState() == State.WATR &&
                   this.currentParticle != null) {
                    new LiquidControl(this.liquidList, Constants.WATER_MASS);
                } else if(this.particleList.get(i).getState() == State.LAVA &&
                          this.currentParticle != null) {
                    new LiquidControl(this.liquidList, Constants.LAVA_MASS);
                }

                // Changes the state of particles that are applicable
                this.stateChange(i, neighbors);

                // Reacts particles if applicable
                this.reaction(i, neighbors, State.HYGN, State.OXYG, State.WTRV);
                this.reaction(i, neighbors, State.WATR, State.LAVA, State.STNE);

                // List updating in case a state change has occurred
                if(this.changeOccurred) {
                    i--;
                    this.changeOccurred = false;
                }
            }
        }
    }

    /**
     * This method corrects for changes in the list from reactions/state changes
     * @param i the index of the particle
     * @param neighbors the list of neighbors of the particle
     */
    private void correctLists(int i, ArrayList<Particle> neighbors) {
        this.centerPane.getChildren().remove(this.particleList.get(i).getParticle());
        this.particleList.add(this.particleChange);
        this.particleList.remove(i);
        neighbors.add(this.particleChange);
        neighbors.remove(0);
        this.centerPane.getChildren().add(this.particleChange.getParticle());
        this.changeOccurred = true;
    }

    /**
     * This method changes the state of particles if applicable using a lot of if-else statements.
     * @param i the index of the particle
     * @param neighbors the list of neighbors of the particle
     */
    private void stateChange(int i, ArrayList<Particle> neighbors) {
        // Sets up the temperature/pressure checkers
        Temperature temp = new Temperature(this.temperatureSlider, this.particleList.get(i));
        Pressure pressure = new Pressure(this.pressureSlider, this.particleList.get(i));

        // Checking each state change possibility and updating
        if(temp.waterToVapor()) {
            this.particleChange =
            new WaterVapor(this.particleList.get(i).getParticle().getCenterX(),
                           this.particleList.get(i).getParticle().getCenterY(),
                           Constants.PARTICLE_RADIUS,
                           Color.CORNFLOWERBLUE);
            this.correctLists(i, neighbors);
            this.liquidList.remove(0);
        } else if(temp.vaporToWater()) {
            this.particleChange =
            new Water(this.particleList.get(i).getParticle().getCenterX(),
                      this.particleList.get(i).getParticle().getCenterY(),
                      Constants.PARTICLE_RADIUS,
                      Color.MEDIUMBLUE);
            this.correctLists(i, neighbors);
            this.liquidList.add(this.particleChange);
        } else if(pressure.solidToPowder()) {
            this.particleChange =
            new Snow(this.particleList.get(i).getParticle().getCenterX(),
                     this.particleList.get(i).getParticle().getCenterY(),
                     Constants.PARTICLE_RADIUS,
                     Color.ALICEBLUE);
            this.correctLists(i, neighbors);
        } else if(pressure.powderToSolid()) {
            this.particleChange =
            new Ice(this.particleList.get(i).getParticle().getCenterX(),
                    this.particleList.get(i).getParticle().getCenterY(),
                    Constants.PARTICLE_RADIUS,
                    Color.WHITE);
            this.correctLists(i, neighbors);
        } else if(temp.iceToWater()) {
            this.particleChange =
            new Water(this.particleList.get(i).getParticle().getCenterX(),
                      this.particleList.get(i).getParticle().getCenterY(),
                      Constants.PARTICLE_RADIUS,
                      Color.MEDIUMBLUE);
            this.correctLists(i, neighbors);
            this.liquidList.add(this.particleChange);
        } else if(temp.waterToIce()) {
            this.particleChange =
            new Ice(this.particleList.get(i).getParticle().getCenterX(),
                    this.particleList.get(i).getParticle().getCenterY(),
                    Constants.PARTICLE_RADIUS,
                    Color.WHITE);
            this.correctLists(i, neighbors);
            this.liquidList.remove(0);
        } else if(temp.snowToWater()) {
            this.particleChange =
            new Water(this.particleList.get(i).getParticle().getCenterX(),
                      this.particleList.get(i).getParticle().getCenterY(),
                      Constants.PARTICLE_RADIUS,
                      Color.MEDIUMBLUE);
            this.correctLists(i, neighbors);
            this.liquidList.add(this.particleChange);
        }else if(temp.stoneToLava()) {
            this.particleChange =
            new Lava(this.particleList.get(i).getParticle().getCenterX(),
                     this.particleList.get(i).getParticle().getCenterY(),
                     Constants.PARTICLE_RADIUS,
                     Color.ORANGERED);
            this.correctLists(i, neighbors);
            this.liquidList.add(this.particleChange);
        } else if(temp.lavaToStone()) {
            this.particleChange =
            new Stone(this.particleList.get(i).getParticle().getCenterX(),
                      this.particleList.get(i).getParticle().getCenterY(),
                      Constants.PARTICLE_RADIUS,
                      Color.GREY);
            this.correctLists(i, neighbors);
            this.liquidList.remove(0);
        }
    }

    /**
     * This method checks if a reaction can occur and makes the reaction happen.
     * @param i the index of the particle
     * @param neighbors the list of neighbors to the particle
     * @param reactant1 the state of the first particle
     * @param reactant2 the state of the second particle
     * @param product the state of the product of the reaction
     */
    private void reaction(int i, ArrayList<Particle> neighbors, State reactant1, State reactant2, State product) {
        if(i >= 0) {
            if(neighbors.size() > 1) {
                for(int j = 0; j < neighbors.size(); j++) {
                    if(i == j) continue;
                    Particle changeParticle = null;

                    // Checks to see if the particles can react
                    if(this.particleList.get(i).getState() == reactant1 &&
                       neighbors.get(j).getState() == reactant2 &&
                       this.particleList.get(i).getParticle().getBoundsInParent().intersects
                       (neighbors.get(j).getParticle().getBoundsInParent())) {
                        this.centerPane.getChildren().remove(neighbors.get(j).getParticle());
                        this.centerPane.getChildren().remove(this.particleList.get(i).
                                                             getParticle());

                        // Creates product based on product state
                        if(product == State.WTRV) {
                            this.temperatureSlider.setValue(Constants.VAPOR_TEMP);
                            this.temperatureHandler();
                            changeParticle =
                            new WaterVapor(this.particleList.get(i).getParticle().getCenterX(),
                                           this.particleList.get(i).getParticle().getCenterY(),
                                           Constants.PARTICLE_RADIUS,
                                           Color.CORNFLOWERBLUE);
                        } else if (product == State.STNE) {
                            changeParticle =
                            new Stone(this.particleList.get(i).getParticle().getCenterX(),
                                      this.particleList.get(i).getParticle().getCenterY(),
                                      Constants.PARTICLE_RADIUS,
                                      Color.GREY);
                        }

                        // Correcting lists for particle deletion/addition
                        this.particleList.add(changeParticle);
                        this.liquidList.remove(neighbors.get(0));
                        this.particleList.remove(neighbors.get(0));
                        this.particleList.remove(neighbors.get(j));
                        neighbors.remove(neighbors.get(j));
                        neighbors.remove(0);

                        this.centerPane.getChildren().add(changeParticle.getParticle());
                        i = i - 2;
                        j = j - 2;
                        break;
                    }
                }
            }
        }
    }

    /**
     * This method checks if the mouse was pressed and if so, runs mouseHandler.
     */
    private void setupMousePressHandler() {
        this.centerPane.setOnMousePressed((MouseEvent e) -> this.mouseHandler(e));
    }

    /**
     * This method checks if the mouse was dragged and if so, runs mouseHandler.
     */
    private void setupMouseDragHandler() {
        this.centerPane.setOnMouseDragged((MouseEvent e) -> this.mouseHandler(e));
    }

    /**
     * This method checks if the mouse was scrolled and if so, runs mouseScrollHandler.
     */
    private void setupMouseScrollHandler() {
        this.centerPane.setOnScroll((ScrollEvent e) -> this.mouseScrollHandler(e));
    }

    /**
     * This method adds particles when left click is pressed/dragged based on the state of the
     * particle, and deletes particles when right click is pressed/dragged.
     * @param e the MouseEvent to check for the type of click.
     */
    private void mouseHandler(MouseEvent e) {
        switch(e.getButton()) {
            case PRIMARY:
                switch(this.state) {
                    case WTRV:
                        this.currentParticle = new WaterVapor(e.getX(),
                                                              e.getY(),
                                                              Constants.PARTICLE_RADIUS,
                                                              Color.CORNFLOWERBLUE);
                        break;
                    case WATR:
                        this.currentParticle = new Water(e.getX(),
                                                         e.getY(),
                                                         Constants.PARTICLE_RADIUS,
                                                         Color.MEDIUMBLUE);
                        break;
                    case SNOW:
                        this.currentParticle = new Snow(e.getX(),
                                                        e.getY(),
                                                        Constants.PARTICLE_RADIUS,
                                                        Color.ALICEBLUE);
                        break;
                    case ICE:
                        this.currentParticle = new Ice(e.getX(),
                                                       e.getY(),
                                                       Constants.PARTICLE_RADIUS,
                                                       Color.WHITE);
                        break;
                    case OXYG:
                        this.currentParticle = new Oxygen(e.getX(),
                                                          e.getY(),
                                                          Constants.PARTICLE_RADIUS,
                                                          Color.DARKRED);
                        break;
                    case HYGN:
                        this.currentParticle = new Hydrogen(e.getX(),
                                                            e.getY(),
                                                            Constants.PARTICLE_RADIUS,
                                                            Color.LIMEGREEN);
                        break;
                    case LAVA:
                        this.currentParticle = new Lava(e.getX(),
                                                        e.getY(),
                                                        Constants.PARTICLE_RADIUS,
                                                        Color.ORANGERED);
                        break;
                    case STNE:
                        this.currentParticle = new Stone(e.getX(),
                                                         e.getY(),
                                                         Constants.PARTICLE_RADIUS,
                                                         Color.GREY);
                        break;
                    default:
                        break;
                }
                this.particleList.add(this.currentParticle);
                this.centerPane.getChildren().add(this.currentParticle.getParticle());
                break;
            case SECONDARY:
                this.cursorBoundary = new Circle(e.getX(),
                                                 e.getY(),
                                                 this.cursorRadius,
                                                 Color.TRANSPARENT);
                for(int i = 0; i < this.particleList.size(); i++) {
                    if(this.cursorBoundary.intersects
                       (this.particleList.get(i).getParticle().getBoundsInLocal())) {
                        this.centerPane.getChildren().remove(this.particleList.get(i).
                                                             getParticle());
                        this.particleList.remove(this.particleList.get(i));
                    }
                }
                break;
        }
    }

    /**
     * This method increases/decreases the radius of the deletion circle when the mouse
     * is scrolled.
     * @param e the ScrollEvent to determine the direction of the scroll.
     */
    private void mouseScrollHandler(ScrollEvent e) {
        if(e.getDeltaY() > 0 && this.cursorRadius < Constants.CURSOR_MAX) {
            this.cursorRadius++;
        } else if (e.getDeltaY() < 0 && this.cursorRadius > Constants.CURSOR_MIN) {
            this.cursorRadius--;
        }
    }
}
