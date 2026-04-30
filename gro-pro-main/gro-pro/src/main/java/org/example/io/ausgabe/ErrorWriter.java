package org.example.io.ausgabe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Schreibt Fehlermeldungen in dedizierte Fehlerdateien im Ausgabeverzeichnis.
 * Ermöglicht eine strukturierte Fehlerbehandlung und -dokumentation.
 */
public class ErrorWriter {

    /**
     * Erstellt eine Fehlerdatei im output-Verzeichnis in welche die übergebene Fehlermeldung geschrieben wird.
     * Der Dateiname wird automatisch aus dem Eingabedateinamen generiert mit dem Prefix "FEHLER_".
     * Dies ermöglicht eine klare Zuordnung von Fehlern zu den verursachenden Eingabedateien.
     *
     * @param filename Name der Eingabedatei ohne Pfad-Prefix
     * @param message  Fehlermeldung, die in die Fehlerdatei geschrieben werden soll
     */
    public void writeError(String filename, String message) {
        try {
            // Erstelle output-Ordner falls nicht vorhanden
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("gro-pro-main/output"));

            // Extrahiere nur den Dateinamen (ohne Pfad) aus filename
            String nurDateiName = filename;
            if (filename != null && filename.contains("/")) {
                nurDateiName = filename.substring(filename.lastIndexOf("/") + 1);
            } else if (filename != null && filename.contains("\\")) {
                nurDateiName = filename.substring(filename.lastIndexOf("\\") + 1);
            }

            String fehlerDateiName = "FEHLER_" + nurDateiName;
            java.nio.file.Path fehlerPfad = java.nio.file.Paths.get("gro-pro-main/output", fehlerDateiName);

            try (PrintWriter printWriter = new PrintWriter(fehlerPfad.toFile(), StandardCharsets.UTF_8)) {
                // Fehlermeldung in die Datei schreiben
                printWriter.print(message);
                printWriter.flush();
            }

            System.err.println("Fehlerdatei erstellt: " + fehlerPfad);

        } catch (FileNotFoundException exception) {
            String nachricht = "[ERR] Die Fehler-Ausgabedatei konnte nicht geoeffnet werden!";
            System.err.println(nachricht);
        } catch (IOException e) {
            String nachricht = "[ERR] Fehler beim Erstellen der Fehlerdatei: " + e.getMessage();
            System.err.println(nachricht);
        }
    }
}
