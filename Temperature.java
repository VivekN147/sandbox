package indy;

import javafx.scene.control.Slider;

public class Temperature {

    private Slider temperatureSlider;
    private Particle particle;

    public Temperature(Slider temperatureSlider, Particle particle) {
        this.temperatureSlider = temperatureSlider;
        this.particle = particle;
    }

    public boolean liquidToGas() {
        return this.temperatureSlider.getValue() >= 100 && this.particle.getState() == State.WATR;
    }

    public boolean gasToLiquid() {
        return this.temperatureSlider.getValue() < 100 && this.particle.getState() == State.WTRV;
    }
}
