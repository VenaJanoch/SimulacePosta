package Simulation;

public class Request {

    private double inputTime;

    /**
     * Konstruktor pacienta s udanim casu prichodu do simulace
     * @param inputTime	cas prichodu do simulace
     */
    protected Request(double inputTime) {
        this.inputTime = inputTime;
    }

    public double getInputTime() {
        return inputTime;
    }

}
