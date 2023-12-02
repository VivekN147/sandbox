package indy;

import javafx.scene.shape.Circle;

import java.util.ArrayList;

public interface Particle {
    public void gravity();
    public void collide();
    public void disperse();
    public void moveLiquid();
    public Circle getParticle();
    public ArrayList<Particle> getParticleList();
    public State getState();
}
