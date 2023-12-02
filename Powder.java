package indy;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Powder implements Particle {

    private Circle particle;
    private ArrayList<Particle> particleList;
    private double yPosition;
    private double velocity;
    private boolean isSettled;

    public Powder(double x, double y, int radius, Color color, ArrayList<Particle> particleList) {
        this.particle = new Circle(x, y, radius, color);
        this.particleList = particleList;
        this.isSettled = false;
    }

    public void gravity() {
        Constants.GRAVITY = 300;
        // Discrete method w/ timeline accumulates error, so I'm brute forcing to correct errors
        if(this.particle.getCenterY() > Constants.BOTTOM_CHECK - Constants.ERROR_CORRECTION) {
            this.particle.setCenterY(Constants.BOTTOM_CHECK);
        } else if(!this.isSettled) {
            double velocityMid = 0.5 * Constants.GRAVITY * Constants.DURATION;
            this.velocity += velocityMid;
            this.yPosition = this.particle.getCenterY() + (this.velocity * Constants.DURATION);
            this.particle.setCenterY(this.yPosition);
        }
    }

    public void collide() {
        if(this.particleList.size() > 1) {
            ArrayList<Particle> tempList = this.particleList;
            for(int i = 0; i < this.particleList.size(); i++) {
                tempList.remove(tempList.get(i));
                for(Particle particle : this.particleList) {
                    if(this.particleList.get(i).getParticle().intersects(particle.getParticle().getLayoutBounds())) {
                        this.isSettled = true;
                        System.out.println(this.particleList.size());
                    }
                }
            }
        }
    }

    public void disperse() {
        int random  = (int) (Math.random() * 3);
        switch (random) {
            case 0:
                this.getParticle().setCenterX(this.getParticle().getCenterX() - 1);
                break;
            case 1:
                break;
            case 2:
                this.getParticle().setCenterX(this.getParticle().getCenterX() + 1);
                break;
        }
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
        return State.POWDER;
    }
}
