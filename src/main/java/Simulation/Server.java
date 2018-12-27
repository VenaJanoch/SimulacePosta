package Simulation;

import Generators.IGenerator;
import cz.zcu.fav.kiv.jsim.*;

public class Server extends JSimProcess implements IRequestAcceptor {
    private JSimHead queue;
    private IGenerator generator;
    private IRequestAcceptor output;
    private ServerStatistics statistics;
    private QueueStatistics queueStatistics;

    public Server(String name, JSimSimulation jSimSimulation, IGenerator generator) throws JSimSimulationAlreadyTerminatedException, JSimInvalidParametersException, JSimTooManyProcessesException, JSimTooManyHeadsException {
        super(name, jSimSimulation);

        this.queue = new JSimHead(name + " - Queue", jSimSimulation);
        this.generator = generator;
        this.statistics = new ServerStatistics(this, queue);
}


    public Server(String name, JSimSimulation jSimSimulation, IGenerator generator, QueueStatistics queueStatistics) throws JSimSimulationAlreadyTerminatedException, JSimInvalidParametersException, JSimTooManyProcessesException, JSimTooManyHeadsException {
        this(name, jSimSimulation, generator);
        this.queueStatistics = queueStatistics;
        queueStatistics.setQueue(queue);
    }

    @Override
    protected void life() {
        try {
            while(true) {

                if (queue.empty()) {
                    passivate();
                } else {
                    double processingTime = Math.abs(generator.getNextValue());

                    JSimLink link = queue.first();

                    Request request = (Request) link.getData();

                    hold(processingTime);

                    if (statistics != null){
                        statistics.processRequest(link.getEnterTime(), processingTime);
                    }

                    if (queueStatistics != null){
                        queueStatistics.processRequest();
                    }

                    link.out();
                    if (output != null){
                        output.acceptRequest(request);
                    }
                }
            }
        } catch (JSimSecurityException e) {
            e.printStackTrace();
        } catch (JSimProcessDeath e) {
            //konec
            //e.printStackTrace();
        } catch (JSimInvalidParametersException e) {
            e.printStackTrace();
        }
    }

    public void acceptRequest(Request request) {
        JSimLink link = new JSimLink(request);
        try {
            link.into(queue);
            if (isIdle())
                activate(myParent.getCurrentTime());
        } catch (JSimSecurityException e) {
            System.err.println("Nepovedlo se pridat pozadavek do fronty. J-Sim hlasi: " + e.getMessage());
        } catch (JSimInvalidParametersException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setOutput(IRequestAcceptor output) {
        this.output = output;
    }

    public ServerStatistics getStatistics() {
        return statistics;
    }

    public QueueStatistics getQueueStatistics() {
        return queueStatistics;
    }
}
