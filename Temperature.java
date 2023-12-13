package indy;

import javafx.scene.control.Slider;

/**
 * This class checks for different state changes based on the temperature of the simulation.
 */
public class Temperature {

    // Declares variables for the temperature slider and particle
    private Slider temperatureSlider;
    private Particle particle;

    /**
     * This method initializes the instance variables based on the parameters of the method
     * @param temperatureSlider the slider that controls temperature
     * @param particle The particle to check for state changes
     */
    public Temperature(Slider temperatureSlider, Particle particle) {
        this.temperatureSlider = temperatureSlider;
        this.particle = particle;
    }

    /**
     * This method checks if a particle should transition from a liquid state to a vapor
     * @return a boolean of whether the particle should change or not
     */
    public boolean waterToVapor() {
        return this.temperatureSlider.getValue() >= Constants.WATER_TO_VAPOR &&
               this.particle.getState() == State.WATR;
    }

    /**
     * This method checks if a particle should transition from a vapor state to a liquid
     * @return a boolean of whether the particle should change or not
     */
    public boolean vaporToWater() {
        return this.temperatureSlider.getValue() < Constants.VAPOR_TO_WATER &&
               this.particle.getState() == State.WTRV;
    }

    /**
     * This method checks if a particle should transition from a liquid state to a solid
     * @return a boolean of whether the particle should change or not
     */
    public boolean waterToIce() {
        return this.temperatureSlider.getValue() < Constants.WATER_TO_ICE &&
               this.particle.getState() == State.WATR;
    }

    /**
     * This method checks if a particle should transition from a solid state to a liquid
     * @return a boolean of whether the particle should change or not
     */
    public boolean iceToWater() {
        return this.temperatureSlider.getValue() > Constants.ICE_TO_WATER &&
               this.particle.getState() == State.ICE;
    }

    /**
     * This method checks if a particle should transition from a powder state to a liquid
     * @return a boolean of whether the particle should change or not
     */
    public boolean snowToWater() {
        return this.temperatureSlider.getValue() > Constants.SNOW_TO_WATER &&
               this.particle.getState() == State.SNOW;
    }

    /**
     * This method checks if a particle should transition from a solid powder to a liquid
     * @return a boolean of whether the particle should change or not
     */
    public boolean stoneToLava() {
        return this.temperatureSlider.getValue() > Constants.STONE_TO_LAVA &&
               this.particle.getState() == State.STNE;
    }

    /**
     * This method checks if a particle should transition from a liquid state to a powder
     * @return a boolean of whether the particle should change or not
     */
    public boolean lavaToStone() {
        return this.temperatureSlider.getValue() < Constants.LAVA_TO_STONE &&
               this.particle.getState() == State.LAVA;
    }
}
