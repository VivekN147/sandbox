package indy;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Water implements Particle {

    private Circle particle;
    private double yPosition;
    private double yVelocity;
    private boolean isSettled;

    public Water(double x, double y, int radius, Color color) {
        this.particle = new Circle(x, y, radius, color);
        this.isSettled = false;
    }

    @Override
    public void gravity() {
        Constants.GRAVITY = 900;
        // Discrete method w/ timeline accumulates error, so I'm brute forcing to correct errors
        if(this.particle.getCenterY() > Constants.BOTTOM_CHECK - Constants.ERROR_CORRECTION) {
            this.particle.setCenterY(Constants.BOTTOM_CHECK);
        } else if(!this.isSettled) {
            double velocityMid = 0.5 * Constants.GRAVITY * Constants.DURATION;
            this.yVelocity += velocityMid;
            this.yPosition = this.particle.getCenterY() + (this.yVelocity * Constants.DURATION);
            this.particle.setCenterY(this.yPosition);
        }
    }

    @Override
    public void collide(int particleIndex, ArrayList<Particle> particleList) {
//        if(particleList.size() > 1) {
//            for(int i = 0; i < particleList.size(); i++) {
//                if(particleIndex == i) continue;
//                if(particleList.get(particleIndex).getParticle().getBoundsInParent().intersects(particleList.get(i).getParticle().getBoundsInParent())) {
//                    this.isSettled = true;
//                }
//            }
//        }
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
        return particleList;
    }

    @Override
    public State getState() {
        return State.WATR;
    }

    @Override
    public boolean getIsSettled() {
        return this.isSettled;
    }
}
