package indy;

import javafx.scene.control.Slider;

public class Pressure {

    private Slider pressureSlider;
    private Particle particle;

    public Pressure(Slider pressureSlider, Particle particle) {
        this.pressureSlider = pressureSlider;
        this.particle = particle;
    }

    public boolean solidToPowder() {
        return this.pressureSlider.getValue() < 8 && this.particle.getState() == State.ICE;
    }

    public boolean powderToSolid() {
        return this.pressureSlider.getValue() >= 8 && this.particle.getState() == State.SNOW;
    }
}
