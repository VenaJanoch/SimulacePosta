package Simulation;

import cz.zcu.fav.kiv.jsim.JSimHead;
import cz.zcu.fav.kiv.jsim.JSimProcess;

public class Statistics implements IStatistic {
    protected JSimProcess source;
    protected JSimHead queue;
    private long requestCount;
    private double sumOfTq;
    private double sumOfProcessingTime;

    public Statistics(JSimProcess source, JSimHead queue) {
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

    public void processRequest(Request request, double queueEnterTime, double processingTime) {

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
     * Vraci pocet zpracovanych pozadavku
     * @return	pocet zpracovanych pozadavku
     */
    public long getRequestCount() {
        return requestCount;
    }

    /**
     * Vraci referenci na objekt zdroje, na kterem se pocita statistika
     * @return	referenci na zdroj
     */
    public JSimProcess getSource() {
        return source;
    }

    /**
     * Vraci frontu zdroje
     * @return	fronta zdroje
     */
    public JSimHead getQueue() {
        return queue;
    }

    /**
     * Vraci stredni pocet pozadavku zdroje
     * @return	stredni pocet zdroje
     */
    public double getLq() {
        return getLoad() / (1 - getLoad());
    }

}
