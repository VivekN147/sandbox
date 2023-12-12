package indy;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Ice implements Particle {

    private Circle particle;
    private boolean isSettled;

    public Ice(double x, double y, int radius, Color color) {
        this.particle = new Circle(x, y, radius, color);
        this.isSettled = true;

    }

    @Override
    public void gravity() {

    }

    @Override
    public void collide(int particleIndex, ArrayList<Particle> particleList) {

    }

    @Override
    public void disperse() {

    }

    @Override
    public Circle getParticle() {
        return this.particle;
    }

    @Override
    public ArrayList<Particle> getParticleList(ArrayList<Particle> particleList) {
        return null;
    }

    @Override
    public State getState() {
        return State.ICE;
    }

    @Override
    public boolean getIsSettled() {
        return this.isSettled;
    }
}
