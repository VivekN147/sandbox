package indy;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * This class creates an Ice particle, which implements the Particle interface. This class
 * creates the particle and provides functionality to get the particle and its state.
 */
public class Ice implements Particle {

    // Declaring the variable that will hold the particle
    private Circle particle;

    /**
     * This method is the constructor of the Ice class. It initializes the particle
     * to be an Ice particle with the x,y, radius, and color values from its caller.
     * @param x the x value of the mouse
     * @param y the y value of the mouse
     * @param radius the radius the Circle will have
     * @param color the color of the Circle
     */
    public Ice(double x, double y, int radius, Color color) {
        this.particle = new Circle(x, y, radius, color);

    }

    /**
     * This method doesn't contain anything to affect the solid by gravity by choice, so that
     * solids have their own unique functionality. It is included since Ice implements
     * the Particle interface and thus must have the method.
     */
    @Override
    public void gravity() {
    }

    /**
     * This method is included since Ice implements Particle, so it must have the method. It is
     * empty since Ice shouldn't disperse by design.
     */
    @Override
    public void disperse() {
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
        return State.ICE;
    }
}
