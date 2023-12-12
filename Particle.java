package indy;

import javafx.scene.shape.Circle;

import java.util.ArrayList;

public interface Particle {
    public void gravity();
    public void collide(int particleIndex, ArrayList<Particle> particleList);
    public void disperse();
    //public void moveLiquid();
    public Circle getParticle();
    public ArrayList<Particle> getParticleList(ArrayList<Particle> particleList);
    public State getState();
    public boolean getIsSettled();
}
