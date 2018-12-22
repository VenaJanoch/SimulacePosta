package Simulation;

import java.util.ArrayList;
import java.util.Collections;

public class Histogram {
    int[] histogram;
    long minValue;
    long maxValue;
    int queueSizes;

    public Histogram(int values){
        histogram = new int[values];
    }

    public int[] createHistogram(ArrayList<Long> results) {
        Collections.sort(results);

        minValue = results.get(0);
        maxValue = results.get(results.size() - 1);
        queueSizes = results.size();
        for (long value : results) {

            int index = (int)((value - minValue) * (histogram.length - 1) / (maxValue - minValue));
            histogram[index]++;
        }

        return histogram;

    }

    public String printHistogram() {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < histogram.length; i++) {

            int width = histogram[i] * 40 / queueSizes;
            builder.append(((i * (maxValue - minValue) / (histogram.length - 1)) + minValue) + ":");
            builder.append(repeat("*", width));
            builder.append("\n");
        }
        builder.append("\n");
        return builder.toString();
    }

    private static String repeat(String s, int count) {
        if (count <= 0) return "";
        else return s + repeat(s, count - 1);
    }
}
