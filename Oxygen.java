package indy;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * This class creates an Oxygen particle, which implements the Particle interface. This class
 * contains logic to make the oxygen float up, disperse, and contains methods to get the
 * particle and its state.
 */
public class Oxygen implements Particle {

    // Declares the particle and position/velocity variables to be calculated
    private Circle particle;
    private double velocity;
    private double yPosition;

    /**
     * This method is the constructor of the Oxygen class, and takes in x/y, radius, and color
     * values to initialize the particle.
     * @param x the x value of the mouse
     * @param y the y value of the mouse
     * @param radius the radius that the particle will have
     * @param color the color that the particle will have
     */
    public Oxygen(double x, double y, int radius, Color color) {
        this.particle = new Circle(x, y, radius, color);
    }

    /**
     * This method makes the Oxygen gas float up using a method called leapfrog integration.
     * It takes the halfway point of the velocity to calculate the next position of the particle,
     * so that position and velocity are "leapfrogging". If the particle is at the top of the
     * screen, the particle is pushed down.
     */
    @Override
    public void gravity() {
        Constants.GRAVITY = Constants.LIGHT_GAS_SPEED;

        // Pushing the particle down if at the top of the screen
        if(this.particle.getCenterY() < Constants.TOP_CHECK) {
            this.particle.setCenterY(this.particle.getCenterY() + Constants.GAS_BOUNDARY);
        } else {
            double velocityMid = 0.5 * Constants.GRAVITY * Constants.DURATION;
            this.velocity += velocityMid;
            this.yPosition = this.particle.getCenterY() + (this.velocity * Constants.DURATION);
            this.particle.setCenterY(this.yPosition);
        }
    }

    /**
     * This method makes the particles disperse randomly to mimic the behavior of gases. It
     * uses a switch statement with a random number to determine how the x value of the
     * particle is altered.
     */
    @Override
    public void disperse() {
        int random  = (int) (Math.random() * Constants.DISPERSION_POSSIBILITIES);
        switch (random) {
            case 0:
                this.getParticle().setCenterX(this.getParticle().getCenterX() -
                                              Constants.LIGHT_GAS_DISPERSION);
                break;
            case 1:
                break;
            case 2:
                this.getParticle().setCenterX(this.getParticle().getCenterX() +
                                              Constants.LIGHT_GAS_DISPERSION);
                break;
            case 3:
                this.getParticle().setCenterY(this.getParticle().getCenterY() -
                                              Constants.LIGHT_GAS_DISPERSION);
                break;
            case 4:
                this.getParticle().setCenterY(this.getParticle().getCenterY() +
                                              Constants.LIGHT_GAS_DISPERSION);
                break;
            default:
                break;
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
        return State.OXYG;
    }

}
