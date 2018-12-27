package Generators;

import java.util.Random;

public class ExpGenerator implements IGenerator {

    private Random rng;
    private double lambda;

    public ExpGenerator(double lambda){
        this.rng = new Random();
        this.lambda = lambda;
    }


    public double getNextValue()
    {
        return  Math.log(1-Math.random())/(-lambda);
    }


}
