package Simulation;

import Generators.ExpGenerator;
import Generators.GaussGenerator;
import cz.zcu.fav.kiv.jsim.*;

import java.util.ArrayList;

public class Simulation {

    private int actualRequestsCount = 0;

    // how many requests must go through the system
    private int totalRequestsCountToPass;

    // how many request has already passed through
    private int totalPassedRequests = 0;
    private Histogram histogram = new Histogram(Constans.histogramSize);
    public void runSimulation(int count){
        runSimulation(count, -1);
    }


    public void runSimulation(int count, double variance) {
            this.totalRequestsCountToPass = count;
            this.actualRequestsCount = count;
        try {
            JSimSimulation simulation = new JSimSimulation("Simulace datových schránek");

            //v simulaci jsou 4 mistnosti
            ArrayList<Server> servers = new ArrayList<Server>();
            ArrayList<RequestGenerator> requestGenerators = new ArrayList<RequestGenerator>();
            Server post ;
            Server internet;
            Server portal;
            Server ministry;

            if (variance == -1) {
                simulation.message("\nSpoustim simulaci pro exponencialni rozdeleni:");

                post = new Server("Postovni prepazka", simulation, new ExpGenerator(Constans.MI1), new QueueStatistics("Postovni prepazka"));
                internet = new Server("Elektronicka prihlaska", simulation, new ExpGenerator(Constans.MI2));
                portal = new Server("Informační systém", simulation, new ExpGenerator(Constans.MI3));
                ministry = new Server("Ministerstvo vnitra CR", simulation, new ExpGenerator(Constans.MI4));

                servers.add(post);
                servers.add(internet);
                servers.add(portal);
                servers.add(ministry);

                RequestGenerator postGenerator = new RequestGenerator("Posta", simulation, new ExpGenerator(Constans.LAMDA1), this);
                RequestGenerator internetGenerator = new RequestGenerator("internet", simulation, new ExpGenerator(Constans.LAMDA2),this);;
                requestGenerators.add(postGenerator);
                requestGenerators.add(internetGenerator);

            } else {
                simulation.message("\nSpoustim simulaci pro Gauss rozdeleni:");
                simulation.message("Koeficient C=" + variance);


                post = new Server("Postovni prepazka", simulation, new GaussGenerator(1./Constans.MI1, variance * Constans.MI1));
                internet = new Server("Elektronicka prihlaska", simulation, new GaussGenerator(1./Constans.MI2, variance * Constans.MI2));
                portal = new Server("Informační systém", simulation, new GaussGenerator(1./Constans.MI3, variance * Constans.MI3));
                ministry = new Server("Ministerstvo vnitra CR", simulation, new GaussGenerator(1./Constans.MI4, variance * Constans.MI4));

                servers.add(post);
                servers.add(internet);
                servers.add(portal);
                servers.add(ministry);

                RequestGenerator postGenerator = new RequestGenerator("Posta", simulation, new GaussGenerator(Constans.MI1, variance * Constans.MI1), this);
                RequestGenerator internetGenerator = new RequestGenerator("internet", simulation, new GaussGenerator(Constans.MI2, variance * Constans.MI2), this);
                requestGenerators.add(postGenerator);
                requestGenerators.add(internetGenerator);
            }

            ArrayList<Confirmation> confirmation = new ArrayList<Confirmation>();
            Confirmation confirmationPortal = new Confirmation(simulation,this);
            confirmation.add(confirmationPortal);
            Confirmation confirmationMinistry = new Confirmation(simulation,this);
            confirmation.add(confirmationMinistry);

            ArrayList<Cross> crosses = new ArrayList<Cross>();

            Cross crossPost = new Cross();
            crossPost.addChoice(Constans.P1, portal);
            crossPost.addChoice(post);

            Cross crossInternet = new Cross();
            crossInternet.addChoice(Constans.P2, portal);
            crossInternet.addChoice(internet);

            Cross crossPortal = new Cross();
            crossPortal.addChoice(Constans.P3, confirmationPortal);
            crossPortal.addChoice(ministry);

            Cross crossMinistry = new Cross();
            crossMinistry.addChoice(Constans.P4, confirmationMinistry);
            crossMinistry.addChoice(Constans.P5, post);
            crossMinistry.addChoice(internet);

            crosses.add(crossPost);
            crosses.add(crossInternet);
            crosses.add(crossPortal);
            crosses.add(crossMinistry);

            requestGenerators.get(0).setOutput(post);
            post.setOutput(crossPost);
            requestGenerators.get(1).setOutput(internet);
            internet.setOutput(crossInternet);

            portal.setOutput(crossPortal);
            ministry.setOutput(crossMinistry);

            requestGenerators.get(0).activateNow();
            requestGenerators.get(1).activateNow();

            int i = 0;
            while((totalPassedRequests <= totalRequestsCountToPass) && (simulation.step() == true));

            // ukonceni simulace
            simulation.shutdown();

            long total = 0;
            double lq = 0;
            simulation.message("Simulace prerusena v case " + simulation.getCurrentTime());
            for (RequestGenerator item : requestGenerators) {
                simulation.message(item.getName() + " - generovano pacientu: " + item.getNumbersCount());
                total += item.getNumbersCount();

            }

            long sumOfRequest = 0;
            for (Server item : servers) {
                simulation.message("Statistika mistosti " + item.getName() + ":");

                ServerStatistics statistics =  item.getStatistics();
                simulation.message("  Lq = " + statistics.getLq());
                simulation.message("  Tq = " + statistics.getTq());
                simulation.message("  load = " + statistics.getLoad());
                simulation.message("  Lw = " + statistics.getLw());

                sumOfRequest+= statistics.getRequestCount();
                lq+= statistics.getLq();
            }



            sumOfRequest = 0;
            double sumOfTq = 0;
            for(Confirmation confirmationAct: confirmation) {
                sumOfRequest += confirmationAct.getRequestCount();
                sumOfTq += confirmationAct.getSumOfTq();
            }

            simulation.message("Celkova statistika:");
            simulation.message(" Lq = " + lq);
            simulation.message(" Tq = " + sumOfTq/sumOfRequest);

            simulation.message("Celkovy pocet pacientu: " + total);


            QueueStatistics queueStatistics = servers.get(0).getQueueStatistics();
            simulation.message("Statistika fronty " + queueStatistics.getName() + ":");
            simulation.message("  Stredni delka: " + queueStatistics.getQueueLengthMedian());
            simulation.message("  Rozptyl: " + queueStatistics.getQueueLengthVariance());
            simulation.message("  Smerodatna odchylka: " + queueStatistics.getQueueLengthSigma());
            simulation.message("  Minimalni delka: " + queueStatistics.getMinQueueLength());
            simulation.message("  maximalni delka: " + queueStatistics.getMaxQueueLength());
            histogram.createHistogram(queueStatistics.getQueueHistogram());
            simulation.message("Histogram:");
            simulation.message(histogram.printHistogram());

        } catch (JSimInvalidParametersException e) {
            e.printStackTrace();
        } catch (JSimTooManyProcessesException e) {
            e.printStackTrace();
        } catch (JSimTooManyHeadsException e) {
            e.printStackTrace();
        } catch (JSimSimulationAlreadyTerminatedException e) {
            e.printStackTrace();
        } catch (JSimException e) {
            e.printStackTrace();
        }


    }

    public synchronized int requestEnteredToSystem() {

       return actualRequestsCount--;
    }

    /**
     * Must be called, when a request leaves the system.
     */
    public synchronized int requestLeftSystem() {

       return totalPassedRequests++;
    }
}
