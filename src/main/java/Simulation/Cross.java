package Simulation;

import Generators.IGenerator;
import Generators.UniformGenerator;

import java.util.HashMap;

public class Cross implements IRequestAcceptor {
    private HashMap<Double, IRequestAcceptor> crossMap;
    private double currentMaxProbably = 0;
    private IGenerator generator;

    /**
     * Konstruktor tridy pro vytvoreni volby cile pozadavku na zaklade pravdepodobnosti
     */
    public Cross() {
        crossMap = new HashMap<Double, IRequestAcceptor>();

        generator = new UniformGenerator(0, 1);
    }

    /**
     * Prida volbu presmerovani pozadavku na zaklade pravdepodobnosti 1 - p
     * @param destination	cil presmerovani
     * @return referenci tridy
     */
    public Cross addChoice(IRequestAcceptor destination) {
        return addChoice(1 - currentMaxProbably, destination);
    }

    /**
     * Prida volbu presmerovani pozadavku na zaklade pravdepodobnosti
     * @param probably		pravdepodobnost presmerovani pozadavku
     * @param destination	cil presmerovani
     * @return referenci tridy
     */
    public Cross addChoice(double probably, IRequestAcceptor destination) {
        currentMaxProbably+= probably;

        if (probably < 0 || probably > 1)
            throw new IllegalArgumentException("Zadana pravdepodobnost musi byt v rozsahu <0,1>.");

        if (currentMaxProbably > 1)
            throw new IllegalArgumentException("Celkova pravdepodobnost musi byt mensi nebo rovna 1.");

        crossMap.put(currentMaxProbably, destination);

        return this;
    }

    public void acceptRequest(Request request) {
        Double latesProbability = new Double(0);

        double number = generator.getNextValue();

        if (crossMap.isEmpty())
            return;

        // vybereme dle pravdepodobnosti prislusnou volbu a predame pozadavek
        for (Double probability: crossMap.keySet()) {
            latesProbability = probability;

            if (number <= latesProbability) {
                crossMap.get(probability).acceptRequest(request);
                return;
            }
        }

        // osetrime volbu, pokud posledni ma pravdepodobnost mensi nez 1
        crossMap.get(latesProbability).acceptRequest(request);
    }
}
