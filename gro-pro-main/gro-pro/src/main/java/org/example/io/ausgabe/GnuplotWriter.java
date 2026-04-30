package org.example.io.ausgabe;

import org.example.model.PlotData;
import java.io.*;
import java.nio.file.*;

/**
 * Schreibt ein GNUPlot-Skript in eine Datei und führt GNUPlot zur PNG-Erzeugung aus.
 * <p>
 * Das Skript wird aus dem übergebenen {@link PlotData}-Objekt bezogen und als
 * {@code .gp}-Datei im Ausgabeverzeichnis gespeichert. Anschließend wird GNUPlot
 * als externer Prozess gestartet, der das Skript ausführt und eine PNG-Datei erzeugt.
 * </p>
 * <p>
 * Voraussetzung: GNUPlot muss installiert und im System-PATH verfügbar sein.
 * </p>
 */
public class GnuplotWriter {

    /** Relatives Verzeichnis, in das das GNUPlot-Skript geschrieben wird. */
    private static final String OUTPUT_DIR = "gro-pro-main/output/";

    /**
     * Schreibt das GNUPlot-Skript in eine {@code .gp}-Datei und führt GNUPlot aus.
     * <p>
     * Der Dateiname wird aus dem übergebenen {@code fileName} extrahiert (ohne Pfad).
     * </p>
     *
     * @param plotData {@link PlotData}-Objekt, das das GNUPlot-Skript liefert
     * @param fileName Pfad oder Name der Eingabedatei – es wird nur der reine Dateiname verwendet
     */
    public void write(PlotData plotData, String fileName) {
        String pureName = Path.of(fileName).getFileName().toString();
        String scriptPath = OUTPUT_DIR + pureName + ".gp";

        try {
            Files.createDirectories(Path.of(OUTPUT_DIR));
            Files.writeString(Path.of(scriptPath), plotData.toGnuplotString());

            // GNUPlot ausführen
            ProcessBuilder pb = new ProcessBuilder("gnuplot", "--persist", scriptPath);
            pb.redirectErrorStream(true);
            pb.start();
        } catch (IOException e) {
            System.err.println("Fehler beim Schreiben/Ausführen des GNUPlot-Skripts: " + e.getMessage());
        }
    }
}
