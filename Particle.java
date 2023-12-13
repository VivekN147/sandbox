package indy;

import javafx.scene.shape.Circle;

/**
 * This interface contains methods for gravity and dispersion, as well as accessors for the
 * particle at its state. It is implemented by the particles in the simulation.
 */
public interface Particle {
    /**
     * This method when implemented should affect the position of the particle based on some
     * gravity value.
     */
    public void gravity();

    /**
     * This method when implemented should disperse particle some amount.
     */
    public void disperse();
    /**
     * This method when implemented should return the particle so that the Circle the
     * particle is made of can be accessed.
     * @return the Circle that makes up the particle
     */
    public Circle getParticle();
    /**
     * This method when implemented should return the state of the particle to be used in
     * reactions/state changes.
     * @return the state of the particle.
     */
    public State getState();
}
