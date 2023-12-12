package indy;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class WaterVapor implements Particle {

    private Circle particle;
    private double velocity;
    private double yPosition;

    public WaterVapor(double x, double y, int radius, Color color) {
        this.particle = new Circle(x, y, radius, color);
    }

    @Override
    public void gravity() {
        Constants.GRAVITY = -25;
        //System.out.println("am getting called");
        // Discrete method w/ timeline accumulates error, so I'm brute forcing to correct errors
        if(this.particle.getCenterY() < 5) {
            this.particle.setCenterY(this.particle.getCenterY() + 1);
        } else {
            double velocityMid = 0.5 * Constants.GRAVITY * Constants.DURATION;
            this.velocity += velocityMid;
            this.yPosition = this.particle.getCenterY() + (this.velocity * Constants.DURATION);
            this.particle.setCenterY(this.yPosition);
        }
    }

    @Override
    public void collide(int particleIndex, ArrayList<Particle> particleList) {

    }

    @Override
    public void disperse() {
        int random  = (int) (Math.random() * 5);
        switch (random) {
            case 0:
                this.getParticle().setCenterX(this.getParticle().getCenterX() - 1);
                break;
            case 1:
                break;
            case 2:
                this.getParticle().setCenterX(this.getParticle().getCenterX() + 1);
                break;
            case 3:
                this.getParticle().setCenterY(this.getParticle().getCenterY() - 1);
                break;
            case 4:
                this.getParticle().setCenterY(this.getParticle().getCenterY() + 1);
                break;
            default:
                break;
        }
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
        return State.WTRV;
    }

    @Override
    public boolean getIsSettled() {
        return false;
    }
}
