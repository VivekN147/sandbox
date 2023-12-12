package indy;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Snow implements Particle {

    private Circle particle;
    private double yPosition;
    private double velocity;
    private boolean isSettled;

    public Snow(double x, double y, int radius, Color color) {
        this.particle = new Circle(x, y, radius, color);
        this.isSettled = false;
    }

    @Override
    public void gravity() {
        Constants.GRAVITY = 300;
        // Discrete method w/ timeline accumulates error, so I'm brute forcing to correct errors
        if(this.particle.getCenterY() > Constants.BOTTOM_CHECK - Constants.ERROR_CORRECTION) {
            this.particle.setCenterY(Constants.BOTTOM_CHECK);
            this.isSettled = true;
        } else if(!this.isSettled) {
            double velocityMid = 0.5 * Constants.GRAVITY * Constants.DURATION;
            this.velocity += velocityMid;
            this.yPosition = this.particle.getCenterY() + (this.velocity * Constants.DURATION);
            this.particle.setCenterY(this.yPosition);
        }
    }

    @Override
    public void collide(int particleIndex, ArrayList<Particle> particleList) {
        if(particleList.size() > 1) {
//            System.out.println(particleList.size());
            for(int i = 0; i < particleList.size(); i++) {
//                System.out.println(particleList.get(i).getState());
                if(particleIndex == i) continue;
                if(particleList.get(i).getIsSettled() &&
                   //this.checkBounds(particleIndex, particleList) &&
                   particleList.get(particleIndex).getParticle().getBoundsInParent().intersects(particleList.get(i).getParticle().getBoundsInParent())) {
                    this.isSettled = true;
                }
            }
        }
    }

    private boolean checkBounds(int particleIndex, ArrayList<Particle> particleList) {
        for (int i = 0; i < particleList.size(); i++) {
            if(particleIndex == i) continue;
            if (particleList.get(i).getParticle().getCenterY() <= particleList.get(particleIndex).getParticle().getCenterY() + 5 &&
                particleList.get(i).getParticle().getCenterY() >= particleList.get(particleIndex).getParticle().getCenterY() + 3 &&
                particleList.get(i).getParticle().getCenterX() >= particleList.get(particleIndex).getParticle().getCenterX() - 0.05 &&
                particleList.get(i).getParticle().getCenterX() <= particleList.get(particleIndex).getParticle().getCenterX() + 0.05) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void disperse() {
        if(!this.isSettled) {
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
                default:
                    break;
            }
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
        return State.SNOW;
    }

    @Override
    public boolean getIsSettled() {
        return this.isSettled;
    }
}
