package org.example.model;

public class ZugOutput {

    private final String algoName;
    private final double[][] fahrzeiten;
    private final double strafen;

    public ZugOutput(String algoName, double[][] fahrzeiten, double strafen) {
        this.algoName = algoName;
        this.fahrzeiten = fahrzeiten;
        this.strafen = strafen;
    }

    public double[][] getFahrzeiten() {
        return fahrzeiten;
    }

    public double getStrafen() {
        return strafen;
    }

    @Override
    public String toString() {
        return  algoName + ":";
    }
}
