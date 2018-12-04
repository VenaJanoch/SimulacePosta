package Generators;

public class GaussGenerator implements IGenerator {

    private Double boxMuller;
    private double sigma;
    private double mu;

    public GaussGenerator(double mu, double sigma){

        this.mu = mu;
        this.sigma = sigma;
    }

    public double getNextValue() {
        double out, x, y, r, z;

        if (boxMuller != null) {
            out = boxMuller.doubleValue();
            boxMuller = null;

        } else {
            do {
                x = (Math.random() * 2) -1;
                y = (Math.random() * 2) -1;
                r = x * x + y * y;
            } while (r >= 1.0);

            z = Math.sqrt(-2.0 * Math.log(r) / r);
            boxMuller = new Double(x * z);
            out = y * z;
        }

        return  sigma * out + mu;
    }


}
