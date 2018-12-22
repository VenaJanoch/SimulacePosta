package Simulation;

import cz.zcu.fav.kiv.jsim.JSimHead;

import java.util.ArrayList;
import java.util.HashMap;

public class QueueStatistics {

    private JSimHead queue;
    private long request;
    private String name;

    private long sumOfQueueLength;
    private long sumOfQueueLengthSquare;
    private long minQueueLength;
    private long maxQueueLength;
    private ArrayList<Long> queueHistogram;

    public QueueStatistics(String name){
        this.name = name;
        request = 0;
        sumOfQueueLength = 0;
        sumOfQueueLengthSquare = 0;
        minQueueLength = Long.MAX_VALUE;
        maxQueueLength = Long.MIN_VALUE;
        queueHistogram = new ArrayList<Long>();
    }



    public void processRequest() {

        // odeceteme aktualni pozadavek od celkoveho poctu pozadavku fronty
        request++;
        long queueLength = queue.cardinal() - 1;

        sumOfQueueLength+= queueLength;
        sumOfQueueLengthSquare+= queueLength * queueLength;

        //nastavime minumum / maximum delky fronty
        if (queueLength > maxQueueLength)
            maxQueueLength = queueLength;
        if (queueLength < minQueueLength)
            minQueueLength = queueLength;

        //pridame do histogramu delku fronty
        queueHistogram.add(queueLength);
    }

    /**
     * Vrac� st�edn� delku fronty
     * @return	st�edn� delka fronty
     */
    public double getQueueLengthMedian() {
        return ((double)sumOfQueueLength) / request;
    }

    /**
     * Vrac� rozptyl delky fronty
     * @return	rozptyl delky fronty
     */
    public double getQueueLengthVariance() {
        double median = getQueueLengthMedian();

        return (((double)sumOfQueueLengthSquare) / request) - median * median;
    }

    /**
     * Vraci smerodatnou odchylku delky fronty
     * @return	smerodatnou odchylku delky fronty
     */
    public double getQueueLengthSigma() {
        return Math.sqrt(getQueueLengthVariance());
    }

    /**
     * Vraci maximalni delku fronty
     * @return	maximalni delka fronty
     */
    public long getMaxQueueLength() {
        return maxQueueLength;
    }

    /**
     * Vraci minimalni delku fronty
     * @return	minimalni delka fronty
     */
    public long getMinQueueLength() {
        return minQueueLength;
    }

    /**
     * Vraci velikost kroku histogramu fronty
     * @param size	velikost histrogramu (pocet sloupcu)
     * @return	velikost kroku
     */
    public double getQueueLengthHistogramStep(int size) {
        return Math.abs((double)maxQueueLength-minQueueLength) / size;
    }

    public JSimHead getQueue() {
        return queue;
    }

    public void setQueue(JSimHead queue) {
        this.queue = queue;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Long> getQueueHistogram() {
        return queueHistogram;
    }
}
