package Simulation;

import Generators.IGenerator;
import cz.zcu.fav.kiv.jsim.*;

public class Server extends JSimProcess implements IRequestAcceptor {
    private JSimHead queue;
    private IGenerator generator;
    private IRequestAcceptor output;
    private IStatistic statistics;

    public Server(String name, JSimSimulation jSimSimulation, IGenerator generator) throws JSimSimulationAlreadyTerminatedException, JSimInvalidParametersException, JSimTooManyProcessesException, JSimTooManyHeadsException {
        super(name, jSimSimulation);

        this.queue = new JSimHead(name + " - Queue", jSimSimulation);
        this.generator = generator;
        this.statistics = new Statistics(this, queue);
    }

    @Override
    protected void life() {
        try {
            while(true) {
                //pokud je prazdna fronta uspime process
                if (queue.empty()) {
                    passivate();
                } else {
                    double processingTime = Math.abs(generator.getNextValue());

                    JSimLink link = queue.first();

                    Request request = (Request) link.getData();

                    hold(processingTime);

                    if (statistics != null)
                        statistics.processRequest(request, link.getEnterTime(), processingTime);

                    link.out();

                    if (output != null)
                        output.acceptRequest(request);

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

    public IRequestAcceptor getOutput() {
        return output;
    }

    public void setOutput(IRequestAcceptor output) {
        this.output = output;
    }

    public IStatistic getStatistics() {
        return statistics;
    }
}
