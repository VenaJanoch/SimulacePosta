package Run;

import Simulation.Simulation;
import Simulation.Constans;


public class Main {
    public static void main(String[] args) {

        Simulation simulation = new Simulation();
        int count = 10000;

        if (args.length == 2) {

            try {
                count = Integer.parseInt(args[0]);
            } catch (Exception e) {
            }



            if (args[1].equals("EXP")) {
                simulation.runSimulation(count);
            } else if (args[1].equalsIgnoreCase("GAUSS")) {
                simulation.runSimulation(Constans.gaussSteps1, Constans.variace1);
                simulation.runSimulation(Constans.gaussSteps2, Constans.variace2); //Todo steps i tedy?
                simulation.runSimulation(Constans.gaussSteps3, Constans.variace3);
            }
        } else {
            simulation.runSimulation(count);
            simulation.runSimulation(Constans.gaussSteps1, Constans.variace1);
            simulation.runSimulation(Constans.gaussSteps2, Constans.variace2);
            simulation.runSimulation(Constans.gaussSteps3, Constans.variace3);
        }


    }
}
