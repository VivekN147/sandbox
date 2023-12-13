package indy;

import java.util.ArrayList;

/**
 * This class contains the logic to make liquids act naturally. It contains methods that
 * calculate how much to move the liquid and then moves the liquid.
 */
public class LiquidControl {

    // Declaring positions/velocities and lists
    private ArrayList<Particle> particleList;
    private ArrayList<double[]> particlePositionList;
    private double yPosition;
    private double xPosition;
    private double[] liquidVelocity;
    private double[] position;

    /**
     * This method is the constructor of the LiquidControl class. It initializes some
     * instance vairables and calculates the positions of each particle then determines
     * how a particle should move.
     * @param particleList the list of particle in an area to determine movement
     * @param mass the mass of the particle
     */
    public LiquidControl(ArrayList<Particle> particleList, int mass) {
        this.particleList = particleList;
        this.particlePositionList = new ArrayList<>();
        this.liquidVelocity = new double[2];

        // Calculating positions of particles
        for(Particle particle1 : this.particleList) {
            this.position = new double[2];
            this.position[0] = particle1.getParticle().getCenterX();
            this.position[1] = particle1.getParticle().getCenterY();
            this.particlePositionList.add(this.position);
        }
        this.moveLiquid(mass);
    }

    /**
     * This method moves the liquid by taking the value from the density gradient and adding
     * it to the current position of the particle to get the new position.
     * @param mass the mass of the liquid
     */
    public void moveLiquid(int mass) {
        if(this.particleList != null) {
            for(int i = 0; i < this.particlePositionList.size(); i++) {
                // Calculating the amount the particle should move
                double[] newPressure = this.calculatedDensityGradient(i, mass);
                this.liquidVelocity[0] = newPressure[0];
                this.liquidVelocity[1] = newPressure[1];

                // Changing the position
                this.xPosition = this.particlePositionList.get(i)[0] + (this.liquidVelocity[0]);
                this.particleList.get(i).getParticle().setCenterX(this.xPosition);

                this.yPosition = this.particlePositionList.get(i)[1] + this.liquidVelocity[1];
                this.particleList.get(i).getParticle().setCenterY(this.yPosition);
            }
        }
    }

    /**
     * This method is a kernel function which determines how much a particle should influence
     * the other particles around it. Plotting this function would result in a spike-like
     * graph, which is good for calcualting density/pressure.
     * @param distance distance from one particle to another
     * @param radius the smoothing radius, or how far one particles influence can reach out
     * @return the value of the kernel function at a certain distance/radius
     */
    private double smoothingKernelDerivative(double distance, double radius) {
        if(distance >= radius) return 0;
        double f = radius - distance;
        double scale = Constants.KERNEL_SCALAR / (Math.pow(radius, Constants.KERNEL_EXPONENT));
        return scale * f * f;
    }

    /**
     * This method calculates the density gradient of a particle, which is the direction in
     * which it should move (since liquids move from high to low density). It works by
     * calculating the direction that a particle should move in and then scaling that value
     * based on the mass and value of the smoothing kernel to determine the density gradient.
     * @param particleIndex the index of the current particle
     * @param mass the mass of the particle
     * @return the density gradient, or how much the particle should move
     */
    private double[] calculatedDensityGradient(int particleIndex, int mass) {
        double[] densityGradient = new double[2];
        densityGradient[0] = 0;
        densityGradient[1] = 0;

        for(int otherParticleIndex = 0;
            otherParticleIndex < this.particleList.size();
            otherParticleIndex++) {
            // If the particle is the same, go to the next particle
            if(particleIndex == otherParticleIndex) continue;

            // Calculating the distance from one particle to another
            double[] offset = new double[2];
            offset[0] = this.particlePositionList.get(otherParticleIndex)[0] -
                        this.particlePositionList.get(particleIndex)[0];
            offset[1] = this.particlePositionList.get(otherParticleIndex)[1] -
                        this.particlePositionList.get(particleIndex)[1];
            double distance = Math.sqrt(Math.pow(offset[0], 2) + Math.pow(offset[1], 2));

            double[] direction = new double[2];

            // If the particles are in the same place, then move in a random direction
            if(distance == 0) {
                int random  = (int) (Math.random() * 2);
                switch(random) {
                    case 0:
                        direction[0] = 1;
                        direction[1] = 0;
                        break;
                    case 1:
                        direction[0] = -1;
                        direction[1] = 0;
                        break;
                    default:
                        break;
                }
            } else {
                // Calculating the direction of the particle based on the offset and distance
                direction[0] = offset[0] / distance;
                direction[1] = offset[1] / distance;
            }

            double slopeOfInfluence = this.smoothingKernelDerivative(distance,
                                                                     Constants.SMOOTHING_RADIUS);

            // Scaling the direction
            direction[0] = direction[0] * mass * slopeOfInfluence;
            direction[1] = direction[1] * mass * slopeOfInfluence;

            // Summing for each particle in the neighborhood
            densityGradient[0] += direction[0];
            densityGradient[1] += direction[1];
        }
        return densityGradient;
    }
}
