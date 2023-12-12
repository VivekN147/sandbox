package indy;

import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Arrays;

public class LiquidControl {

    private ArrayList<Particle> particleList;
    private ArrayList<double[]> particlePositionList;
    private double yPosition;
    private double xPosition;
    private double[] liquidVelocity;
    private double[] position;

    public LiquidControl(ArrayList<Particle> particleList) {
        this.particleList = particleList;
        this.particlePositionList = new ArrayList<>();
        this.liquidVelocity = new double[2];
        for(Particle particle1 : this.particleList) {
            this.position = new double[2];
            this.position[0] = particle1.getParticle().getCenterX();
            this.position[1] = particle1.getParticle().getCenterY();
            this.particlePositionList.add(this.position);
        }
        this.moveLiquid();
    }

    public void moveLiquid() {
        if(this.particleList != null) {
            for(int i = 0; i < this.particlePositionList.size(); i++) {
                double[] newDensity = this.calculatedPressureGradient(i);
                this.liquidVelocity[0] = newDensity[0];
                this.liquidVelocity[1] = newDensity[1];

                double[] position = new double[2];
                position[0] = this.xPosition;
                position[1] = this.yPosition;

                this.xPosition = this.particlePositionList.get(i)[0] + (this.liquidVelocity[0]);
                this.particleList.get(i).getParticle().setCenterX(this.xPosition);

                this.yPosition = this.particlePositionList.get(i)[1] + this.liquidVelocity[1];
                this.particleList.get(i).getParticle().setCenterY(this.yPosition);
            }
        }
    }

    private double smoothingKernel(double distance, double radius) {
        if(distance >= radius) return 0;

        double volume = (Math.PI * Math.pow(radius, 4)) / 6;
        return (radius - distance) * (radius - distance) / volume;
    }

    private double smoothingKernelDerivative(double distance, double radius) {
        if(distance >= radius) return 0;
        //double scale = 12 / (Math.pow(radius, 4) * Math.PI);
        //return (distance - radius) * scale;
        double f = radius * radius - distance * distance;
        double scale = -24 / (Math.PI * Math.pow(radius, 8));
        return scale * distance * f * f;
    }

    private double calculateDensity(double[] samplePoint) {
        double density = 0;
        double mass = 1;

        for(double[] position : this.particlePositionList) {
            double distance = Math.pow((position[0] - samplePoint[0]), 2) + Math.pow((position[1] - samplePoint[1]), 2);
            double influence = this.smoothingKernel(distance, 1);
            density += mass * influence;
        }
        return density;
    }

    private double[] calculatedPressureGradient(int particleIndex) {
        double[] densityGradient = new double[2];
        densityGradient[0] = 0;
        densityGradient[1] = 0;
        double mass = 50;

        for(int otherParticleIndex = 0; otherParticleIndex < this.particleList.size(); otherParticleIndex++) {
            if(particleIndex == otherParticleIndex) continue;

            double[] offset = new double[2];
            offset[0] = this.particlePositionList.get(otherParticleIndex)[0] - this.particlePositionList.get(particleIndex)[0];
            offset[1] = this.particlePositionList.get(otherParticleIndex)[1] - this.particlePositionList.get(particleIndex)[1];
            double distance = Math.sqrt(Math.pow(offset[0], 2) + Math.pow(offset[1], 2));

            double[] direction = new double[2];
            if(distance == 0) {
                direction[0] = 1000;
                direction[1] = 0;
            } else {
                direction[0] = offset[0] / distance;
                direction[1] = offset[1] / distance;
            }

            double slopeOfInfluence = this.smoothingKernelDerivative(distance, Constants.SMOOTHING_RADIUS);

            direction[0] = direction[0] * mass * slopeOfInfluence;
            direction[1] = direction[1] * mass * slopeOfInfluence;

            densityGradient[0] += direction[0];
            densityGradient[1] += direction[1];
        }
        return densityGradient;
    }
}
