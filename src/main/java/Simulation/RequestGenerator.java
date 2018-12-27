package Simulation;

import Generators.IGenerator;
import cz.zcu.fav.kiv.jsim.*;

public class RequestGenerator extends JSimProcess{

    private Simulation locSimulation;
    private Long numbersCount = 0L;

    private IGenerator generator;
    private IRequestAcceptor  output;


    public RequestGenerator(String name, JSimSimulation simulation, IGenerator generator, Simulation locSimulation)
            throws JSimException, IllegalArgumentException {
        super(name, simulation);
        this.locSimulation = locSimulation;
        this.generator = generator;

    }

    @Override
    protected void life() {
        try {
            while(locSimulation.requestEnteredToSystem() > 0) {
                hold(Math.abs(generator.getNextValue()));

                numbersCount++;

                if (output != null){
                    output.acceptRequest(new Request(myParent.getCurrentTime()));
                }
            }

        } catch (JSimSecurityException e) {
            e.printStackTrace();
        } catch (JSimInvalidParametersException e) {
            e.printStackTrace();
        }
    }

    public Long getNumbersCount() {
        return numbersCount;
    }

    public void setOutput(IRequestAcceptor output) {
        this.output = output;
    }
}
