package Simulation;

public class CrossTarget {

    private double probability;
    private IRequestAcceptor destination;

    public CrossTarget(double probability, IRequestAcceptor destination){
        this.probability = probability;
        this.destination = destination;
    }

    public double getProbability() {
        return probability;
    }

    public IRequestAcceptor getDestination() {
        return destination;
    }
}
