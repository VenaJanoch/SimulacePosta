package Simulation;

import cz.zcu.fav.kiv.jsim.JSimHead;
import cz.zcu.fav.kiv.jsim.JSimProcess;

public interface IStatistic {

    public void processRequest(Request request, double queueEnterTime, double processingTime);

    /**
     * Vraci stredni delku fronty zdroje
     * @return	stredni delka fronty
     */
    public double getLw();

    /**
     * Vraci prumernou stredni dobu odezvy serveru
     * @return	prumernou stredni dobu odezvy serveru
     */
    public double getTq();

    /**
     * Vraci soucet strednich dob odezvy serveru za vsechny pozadavky
     * @return	soucet stredni doby odezvy
     */
    public double getSumOfTq();

    /**
     * Vraci zatizeni serveru
     * @return	zatizeni serveru
     */
    public double getLoad();

    /**
     * Vraci pocet zpracovanych pozadavku
     * @return	pocet zpracovanych pozadavku
     */
    public long getRequestCount();

    /**
     * Vraci referenci na objekt zdroje, na kterem se pocita statistika
     * @return	referenci na zdroj
     */
    public JSimProcess getSource();

    /**
     * Vraci frontu zdroje
     * @return	fronta zdroje
     */
    public JSimHead getQueue();

    /**
     * Vraci stredni pocet pozadavku zdroje
     * @return	stredni pocet zdroje
     */
    public double getLq();


}
