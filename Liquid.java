package indy;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Liquid implements Particle {

    private Circle particle;
    private ArrayList<Particle> particleList;
    private ArrayList<double[]> particlePositionList;
    private double yPosition;
    private double xPosition;
    private double yVelocity;
    private double[] liquidVelocity;
    private boolean isSettled;
    private Sandbox sandbox;

    public Liquid(double x, double y, int radius, Color color, Sandbox sandbox) {
        this.sandbox = sandbox;
        this.particle = new Circle(x, y, radius, color);
        this.particleList = sandbox.getParticleList();
        this.particlePositionList = new ArrayList<>();
        this.liquidVelocity = new double[2];
        for(Particle particle : this.particleList) {
            System.out.println(particle);
            double[] position = new double[2];
            position[0] = particle.getParticle().getCenterX();
            position[1] = particle.getParticle().getCenterY();
            this.particlePositionList.add(position);
            System.out.println(this.particleList.size());
        }
        this.isSettled = false;
    }

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

    }

    public void moveLiquid() {
        for(int i = 0; i < this.particlePositionList.size(); i++) {
            double[] newDensity = this.calculatedPressureGradient(i);
            this.liquidVelocity[0] = newDensity[0];
            this.liquidVelocity[1] = newDensity[1];

            this.xPosition = this.particle.getCenterX() + (this.liquidVelocity[0] * Constants.DURATION);
            this.particle.setCenterX(this.xPosition);

            this.yPosition = this.particle.getCenterY() + (this.liquidVelocity[1] * Constants.DURATION);
            this.particle.setCenterY(this.yPosition);
        }
    }

    private double smoothingKernel(double distance, double radius) {
        if(distance >= radius) return 0;

        double volume = (Math.PI * Math.pow(radius, 4)) / 6;
        return (radius - distance) * (radius - distance) / volume;
    }

    private double smoothingKernelDerivative(double distance, double radius) {
        if(distance >= radius) return 0;
        double scale = 12 / (Math.pow(radius, 4) * Math.PI);
        return (distance - radius) * scale;
    }

    private double calculateDensity(double[] samplePoint) {
        double density = 0;
        double mass = 1;

        for(double[] position : this.particlePositionList) {
            double distance = Math.pow((position[0] - samplePoint[0]), 2) + Math.pow((position[1] - samplePoint[1]), 2);
            double influence = this.smoothingKernel(distance, 10);
            density += mass * influence;
        }
        return density;
    }

    private double[] calculatedPressureGradient(int particleIndex) {
        //System.out.println(particleIndex);
        double[] densityGradient = new double[2];
        densityGradient[0] = 0;
        densityGradient[1] = 0;
        double mass = 1;

        for(int otherParticleIndex = 0; otherParticleIndex < this.particleList.size(); otherParticleIndex++) {
            System.out.println("hello");
            if(particleIndex == otherParticleIndex) continue;

            double[] offset = new double[2];
            offset[0] = this.particlePositionList.get(otherParticleIndex)[0] - this.particlePositionList.get(particleIndex)[0];
            offset[1] = this.particlePositionList.get(otherParticleIndex)[1] - this.particlePositionList.get(particleIndex)[1];
            double distance = Math.pow(offset[0], 2) + Math.pow(offset[1], 2);

            double[] direction = new double[2];
            direction[0] = offset[0] / distance;
            direction[1] = offset[1] / distance;

            double slopeOfInfluence = this.smoothingKernelDerivative(distance, 10);

            direction[0] = direction[0] * mass * slopeOfInfluence;
            direction[1] = direction[1] * mass * slopeOfInfluence;
            densityGradient[0] += direction[0];
            densityGradient[1] += direction[1];
        }
        return densityGradient;
    }

    public Circle getParticle() {
        return this.particle;
    }

    public ArrayList<Particle> getParticleList() {
        return this.particleList;
    }

    @Override
    public State getState() {
        return State.LIQUID;
    }
}
