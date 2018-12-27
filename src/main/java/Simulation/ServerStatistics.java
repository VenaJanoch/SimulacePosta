package Simulation;

import cz.zcu.fav.kiv.jsim.JSimHead;
import cz.zcu.fav.kiv.jsim.JSimProcess;

public class ServerStatistics {
    private JSimProcess source;
    private JSimHead queue;
    private long requestCount;
    private double sumOfTq;
    private double sumOfProcessingTime;

    public ServerStatistics(JSimProcess source, JSimHead queue) {
        this.source = source;
        this.queue = queue;

        reset();
    }


    /**
     * Nastvi pocatecni hodnoty pocitanych statistik
     */
    public void reset() {
        requestCount = 0;
        sumOfTq = 0;
        sumOfProcessingTime = 0;
    }

    public void processRequest(double queueEnterTime, double processingTime) {

        requestCount++;

        sumOfTq+= source.getParent().getCurrentTime() - queueEnterTime;
        sumOfProcessingTime+=processingTime;


    }

    /**
     * Vraci stredni delku fronty zdroje
     * @return	stredni delka fronty
     */
    public double getLw() {
        return queue.getLw();
    }

    /**
     * Vraci prumernou stredni dobu odezvy serveru
     * @return	prumernou stredni dobu odezvy serveru
     */
    public double getTq() {
        return sumOfTq / requestCount;
    }

    /**
     * Vraci soucet strednich dob odezvy serveru za vsechny pozadavky
     * @return	soucet stredni doby odezvy
     */
    public double getSumOfTq() {
        return sumOfTq;
    }

    /**
     * Vraci zatizeni serveru
     * @return	zatizeni serveru
     */
    public double getLoad() {
        return sumOfProcessingTime / source.getParent().getCurrentTime();
    }

    /**
     * Vraci stredni pocet pozadavku zdroje
     * @return	stredni pocet zdroje
     */
    public double getLq() {
        return getLoad() / (1 - getLoad());
    }

    public long getRequestCount() {
        return requestCount;
    }
}
