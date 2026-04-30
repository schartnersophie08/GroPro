package org.example;

import org.example.algorithmus.AlgorithmusImpl;
import org.example.io.ausgabe.ErrorWriter;
import org.example.io.ausgabe.FileOutputWriter;
import org.example.io.ausgabe.GnuplotWriter;
import org.example.io.eingabe.InputReaderImpl;
import org.example.execution.Execution;

/**
 * Einstiegspunkt der Anwendung.
 * <p>
 * Erstellt alle benötigten Komponenten (Reader, Writer, Algorithmus)
 * und startet die {@link Execution}, die den gesamten Verarbeitungsablauf steuert.
 * </p>
 */
public class Main {

    /**
     * Hauptmethode – startet die Anwendung.
     * <p>
     * Im Fehlerfall wird eine Fehlermeldung auf {@code System.err} ausgegeben
     * und die Anwendung mit Exit-Code 1 beendet.
     * </p>
     *
     * @param args Kommandozeilenargumente (werden aktuell nicht verwendet)
     */
    public static void main(String[] args) {

        String filename = "gro-pro-main/testfälle/Test";

        try {
            Execution execution = new Execution(
                new InputReaderImpl(),
                new FileOutputWriter(),
                new AlgorithmusImpl(),
                filename,
                new ErrorWriter(),
                    new GnuplotWriter()
            );
            execution.execute();
        } catch (RuntimeException e) {
            String fehlermeldung = "[ERR] Es ist ein Fehler bei der Verarbeitung aufgetreten: " + e.getMessage();
            System.err.println(fehlermeldung);
            System.exit(1);
        }
    }
}
