package Simulation;

public class Request {

    private double inputTime;

    /**
     * Konstruktor pozadavku s casem prichodu do simulace
     * @param inputTime
     */
    protected Request(double inputTime) {
        this.inputTime = inputTime;
    }

    public double getInputTime() {
        return inputTime;
    }

}
