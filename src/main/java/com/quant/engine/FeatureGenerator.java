package com.quant.engine;

import java.util.Random;

public class FeatureGenerator {
    private Random random = new Random();

    public double[] generateRandomFeatures(int numFeatures) {
        double[] features = new double[numFeatures];
        for (int i = 0; i < numFeatures; i++) {
            features[i] = random.nextDouble();
        }
        return features;
    }
}