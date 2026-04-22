package com.quant.engine;

import java.util.Random;

public class MLClient {
    private String apiUrl;

    public MLClient(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public double getPrediction(double[] features) throws Exception {
        Random random = new Random();
        return random.nextDouble();
    }
}