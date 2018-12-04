package Generators;

public class UniformGenerator implements IGenerator {
    private double a;

    private double b;

    /**
     * Creates a new instance of UniformDistribution with required params
     *
     * @param a
     *            the 'a' parameter
     * @param b
     *            the 'b' parameter
     */
    public UniformGenerator(double a, double b) {
        if (a <= b) {
            this.a = a;
            this.b = b;
        } else {
            this.a = b;
            this.b = a;
        }
    }



    public double getNextValue() {
        double number = Math.random();

        if (Math.random() < 0.5)
            number = 1 - number;

        return (number * (b - a)) + a;
    }

}
