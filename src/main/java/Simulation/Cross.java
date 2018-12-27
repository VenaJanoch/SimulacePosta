package Simulation;

import Generators.IGenerator;
import Generators.UniformGenerator;

import java.util.HashSet;
import java.util.Set;

public class Cross implements IRequestAcceptor {
    private Set<CrossTarget> crossSet;
    private double probably = 0;
    private IGenerator generator;

    /**
     * Konstruktor tridy pro vytvoreni volby cile pozadavku na zaklade pravdepodobnosti
     */
    public Cross() {
        crossSet = new HashSet<CrossTarget>();
        generator = new UniformGenerator(0, 1);
    }

    /**
     * Prida volbu presmerovani pozadavku na zaklade pravdepodobnosti 1 - p
     * @param destination	cil presmerovani
     * @return referenci tridy
     */
    public Cross addChoice(IRequestAcceptor destination) {
        return addChoice(1 - probably, destination);
    }

    /**
     * Prida volbu presmerovani pozadavku na zaklade pravdepodobnosti
     * @param probably		pravdepodobnost presmerovani pozadavku
     * @param destination	cil presmerovani
     * @return referenci tridy
     */
    public Cross addChoice(double probably, IRequestAcceptor destination) {
        this.probably += probably;

        if (probably < 0 || probably > 1)
            throw new IllegalArgumentException("Zadana pravdepodobnost musi byt v rozsahu <0,1>.");

        if (this.probably > 1)
            throw new IllegalArgumentException("Celkova pravdepodobnost musi byt mensi nebo rovna 1.");

        crossSet.add(new CrossTarget(probably, destination)); //TODO mozna chyba, v predesle verzi je this.probably

        return this;
    }

    public void acceptRequest(Request request) {
        CrossTarget latesTarget = new CrossTarget(0, null);
        double number = generator.getNextValue();

        if (crossSet.isEmpty()){
            return;
        }

        // vybereme dle pravdepodobnosti prislusnou volbu a predame pozadavek
        for (CrossTarget target: crossSet) {
            latesTarget = target;
            if (number <= target.getProbability()) {
                target.getDestination().acceptRequest(request);
                return;
            }
        }

        latesTarget.getDestination().acceptRequest(request);
    }
}
