package org.example.execution;

import org.example.algorithmus.Algorithmus;
import org.example.io.output.ErrorWriter;
import org.example.io.output.OutputWriter;
import org.example.io.input.InputReader;
import org.example.model.ZugOutput;

import java.util.ArrayList;
import java.util.List;

public class Execution {

    private final InputReader inputReader;

    private final OutputWriter outputWriter;

    private final List<Algorithmus> algorithmen;

    private final String filename;

    private final ErrorWriter errorWriter;

    public Execution(InputReader inputReader, OutputWriter outputWriter, List<Algorithmus> algorithmen, String filename,
            ErrorWriter errorWriter) {
        this.inputReader = inputReader;
        this.outputWriter = outputWriter;
        this.algorithmen = algorithmen;
        this.filename = filename;
        this.errorWriter = errorWriter;
    }

    public void execute() {
        try {
            var inputData = inputReader.readInput(filename);
            List<ZugOutput> ergebnisse = new ArrayList<>();
            for (Algorithmus algorithmus : algorithmen) {
                ergebnisse.add(algorithmus.loese(inputData));
            }
            outputWriter.write(inputData,ergebnisse);
        } catch (Exception e) {
            errorWriter.writeError(filename, e.getMessage());
        }
    }
}
