package indy;

import javafx.scene.control.Slider;

/**
 * This class checks for different state changes based on the pressure of the simulation.
 */
public class Pressure {

    // Declares variables for the pressure slider and particle
    private Slider pressureSlider;
    private Particle particle;

    /**
     * This method initializes the instance variables based on the parameters of the method
     * @param pressureSlider the slider that controls pressure
     * @param particle The particle to check for state changes
     */
    public Pressure(Slider pressureSlider, Particle particle) {
        this.pressureSlider = pressureSlider;
        this.particle = particle;
    }

    /**
     * This method checks if a particle should transition from a solid state to a powder
     * @return a boolean of whether the particle should change or not
     */
    public boolean solidToPowder() {
        return this.pressureSlider.getValue() < Constants.PRESSURE_CHECK &&
               this.particle.getState() == State.ICE;
    }

    /**
     * This method checks if a particle should transition from a powder state to a solid
     * @return a boolean of whether the particle should change or not
     */
    public boolean powderToSolid() {
        return this.pressureSlider.getValue() >= Constants.PRESSURE_CHECK &&
               this.particle.getState() == State.SNOW;
    }
}
