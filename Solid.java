package indy;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Solid implements Particle {

    private Circle particle;
    private ArrayList<Particle> particleList;

    public Solid(double x, double y, int radius, Color color, ArrayList<Particle> particleList) {
        this.particle = new Circle(x, y, radius, color);
        this.particleList = particleList;
    }

    public void gravity() {

    }

    public void collide() {

    }

    public void disperse() {

    }

    @Override
    public void moveLiquid() {

    }

    public Circle getParticle() {
        return this.particle;
    }

    @Override
    public ArrayList<Particle> getParticleList() {
        return null;
    }

    @Override
    public State getState() {
        return State.SOLID;
    }
}
