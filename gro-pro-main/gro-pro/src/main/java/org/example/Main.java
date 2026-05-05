package org.example;

import org.example.algorithmus.BeidseitigesWarten;
import org.example.algorithmus.EinfacheFahrt;
import org.example.algorithmus.EinseitigesWarten;
import org.example.io.output.ErrorWriter;
import org.example.io.output.FileOutputWriter;
import org.example.io.input.InputReaderImpl;
import org.example.execution.Execution;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("[ERR] Kein Dateiname übergeben. Verwendung: java -jar gro-pro-main.jar <pfad-zur-eingabedatei>");
            System.exit(1);
        }

        String filename = args[0];

        try {
            Execution execution = new Execution(
                new InputReaderImpl(),
                new FileOutputWriter(),
                List.of(new EinfacheFahrt(),new EinseitigesWarten(),new BeidseitigesWarten()),
                filename,
                new ErrorWriter()
            );
            execution.execute();
        } catch (RuntimeException e) {
            String fehlermeldung = "[ERR] Es ist ein Fehler bei der Verarbeitung aufgetreten: " + e.getMessage();
            System.err.println(fehlermeldung);
            System.exit(1);
        }
    }
}
