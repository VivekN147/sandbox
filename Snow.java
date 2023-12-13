package indy;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * This class creates a Snow particle, which implements the Particle interface. This class
 * contains logic to make the snow be affected by gravity, disperse, and get the
 * particle and its state.
 */
public class Snow implements Particle {

    // Declares the particle and position/velocity variables to be calculated
    private Circle particle;
    private double yPosition;
    private double velocity;
    private boolean isSettled;

    /**
     * This method is the constructor of the Snow class, and takes in x/y, radius, and color
     * values to initialize the particle. It also initializes the boundary check to be false.
     * @param x the x value of the mouse
     * @param y the y value of the mouse
     * @param radius the radius that the particle will have
     * @param color the color that the particle will have
     */
    public Snow(double x, double y, int radius, Color color) {
        this.particle = new Circle(x, y, radius, color);
        this.isSettled = false;
    }

    /**
     * This method makes the Snow fall using a method called leapfrog integration.
     * It takes the halfway point of the velocity to calculate the next position of the particle,
     * so that position and velocity are "leapfrogging". If the particle is at the bottom of the
     * screen, the particle is no longer affected by gravity.
     */
    @Override
    public void gravity() {
        Constants.GRAVITY = Constants.POWDER_GRAVITY;

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

    /**
     * This method makes the particles disperse randomly to mimic the behavior of powders. It
     * uses a switch statement with a random number to determine how the x value of the
     * particle is altered.
     */
    @Override
    public void disperse() {
        if(!this.isSettled) {
            int random  = (int) (Math.random() * Constants.POWDER_DISPERSION_POSSIBILITIES);
            switch (random) {
                case 0:
                    this.getParticle().setCenterX(this.getParticle().getCenterX() -
                                                  Constants.POWDER_DISPERSION);
                    break;
                case 1:
                    break;
                case 2:
                    this.getParticle().setCenterX(this.getParticle().getCenterX() +
                                                  Constants.POWDER_DISPERSION);
                    break;
                default:
                    break;
            }
        }
    }

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
        return State.SNOW;
    }

}
