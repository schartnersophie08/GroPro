package org.example.execution;

import org.example.algorithmus.Algorithmus;
import org.example.io.ausgabe.ErrorWriter;
import org.example.io.ausgabe.GnuplotWriter;
import org.example.io.ausgabe.OutputWriter;
import org.example.io.eingabe.InputReader;
import org.example.model.InputData;
import org.example.model.OutputData;

/**
 * Steuerklasse für den gesamten Verarbeitungsablauf.
 * <p>
 * Koordiniert das Einlesen der Eingabedatei, die Ausführung des Algorithmus,
 * die Textausgabe sowie die GNUPlot-Visualisierung.
 * Tritt ein Fehler auf, wird dieser über den {@link ErrorWriter} in eine Fehlerdatei geschrieben.
 * </p>
 *
 * @param <I> konkreter Eingabetyp, muss {@link InputData} implementieren
 * @param <O> konkreter Ausgabetyp, muss {@link OutputData} implementieren
 */
public class Execution<I extends InputData, O extends OutputData> {

    /** Liest die Eingabedatei ein und liefert ein typisiertes {@link InputData}-Objekt. */
    private final InputReader<I> inputReader;

    /** Schreibt das Ergebnis als Textdatei in das Ausgabeverzeichnis. */
    private final OutputWriter outputWriter;

    /** Führt die eigentliche Berechnungslogik aus. */
    private final Algorithmus<I, O> algorithmus;

    /** Pfad zur Eingabedatei. */
    private final String filename;

    /** Schreibt Fehlermeldungen in eine dedizierte Fehlerdatei. */
    private final ErrorWriter errorWriter;

    /** Erzeugt ein GNUPlot-Skript und führt GNUPlot zur PNG-Erzeugung aus. */
    private final GnuplotWriter gnuplotWriter;

    /**
     * Erstellt eine neue Execution-Instanz mit allen benötigten Komponenten.
     *
     * @param inputReader    Reader zum Einlesen der Eingabedatei
     * @param outputWriter   Writer für die Textausgabe
     * @param algorithmus    Algorithmus-Implementierung
     * @param filePath       Pfad zur Eingabedatei
     * @param errorWriter    Writer für Fehlermeldungen
     * @param gnuplotWriter  Writer für GNUPlot-Skripte und PNG-Erzeugung
     */
    public Execution(InputReader<I> inputReader, OutputWriter outputWriter, Algorithmus<I, O> algorithmus, String filePath,
            ErrorWriter errorWriter, GnuplotWriter gnuplotWriter) {
        this.inputReader = inputReader;
        this.outputWriter = outputWriter;
        this.algorithmus = algorithmus;
        this.filename = filePath;
        this.errorWriter = errorWriter;
        this.gnuplotWriter = gnuplotWriter;
    }

    /**
     * Führt den gesamten Verarbeitungsablauf aus.
     * <ol>
     *   <li>Eingabedatei einlesen</li>
     *   <li>Algorithmus ausführen</li>
     *   <li>Ergebnis als Textdatei schreiben</li>
     *   <li>GNUPlot-PNG erzeugen</li>
     * </ol>
     * <p>
     * Bei einem Fehler in einem der Schritte wird die Exception abgefangen
     * und eine Fehlerdatei im Ausgabeverzeichnis erstellt.
     * </p>
     */
    public void execute() {
        try {
            var inputData = inputReader.readInput(filename);
            var outputData = algorithmus.run(inputData);
            outputWriter.write(outputData);
            gnuplotWriter.write(outputData.getPlotData(), filename);
        } catch (Exception e) {
            errorWriter.writeError(filename, e.getMessage());
        }
    }
}
