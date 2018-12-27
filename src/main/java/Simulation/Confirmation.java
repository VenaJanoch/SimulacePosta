package Simulation;

import cz.zcu.fav.kiv.jsim.JSimSimulation;

public class Confirmation implements IRequestAcceptor {
    private Simulation locSimulation;
    private long requestCount;
    private double sumOfTq;
    private JSimSimulation simulation;


    public Confirmation(JSimSimulation simulation, Simulation locSimulation) {
        this.locSimulation = locSimulation;
        this.simulation = simulation;
    }

    public void acceptRequest(Request request) {
        locSimulation.requestLeftSystem();
        requestCount++;
        sumOfTq+= simulation.getCurrentTime() - request.getInputTime();
    }

    public double getSumOfTq() {
        return sumOfTq;
    }

    public long getRequestCount() {
        return requestCount;
    }
}
