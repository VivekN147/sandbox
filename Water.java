package indy;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * This class creates a Water particle, which implements the Particle interface. This class
 * contains logic to make gravity affect the Particle and to get the particle as well as
 * it's state.
 */
public class Water implements Particle {

    // Declaring the particle, the position/velocity of the particle, and a boundary checker
    private Circle particle;
    private double yPosition;
    private double yVelocity;
    private boolean isSettled;

    /**
     * This method is the constructor of the Water class, and takes in x/y, radius, and color
     * values to initialize the particle. It also initializes the boundary check to be false.
     * @param x the x value of the mouse
     * @param y the y value of the mouse
     * @param radius the radius that the particle will have
     * @param color the color that the particle will have
     */
    public Water(double x, double y, int radius, Color color) {
        this.particle = new Circle(x, y, radius, color);
        this.isSettled = false;
    }

    /**
     * This method makes the Water fall using a method called leapfrog integration.
     * It takes the halfway point of the velocity to calculate the next position of the particle,
     * so that position and velocity are "leapfrogging". If the particle is at the bottom of the
     * screen, the particle is no longer affected by gravity.
     */
    @Override
    public void gravity() {
        Constants.GRAVITY = Constants.WATER_GRAVITY;
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

    /**
     * This method is included since Water implements Particle, so it must have the method. It is
     * empty since Water shouldn't disperse by design.
     */
    @Override
    public void disperse() {}

    /**
     * This method returns the particle so that the Circle the particle is made of can be
     * accessed.
     * @return the Circle that makes up the particle
     */
    @Override
    public Circle getParticle() {
        return this.particle;
    }

    /**
     * This method returns the state of the particle to be used in reactions/state changes.
     * @return the state of the particle.
     */
    @Override
    public State getState() {
        return State.WATR;
    }
}
