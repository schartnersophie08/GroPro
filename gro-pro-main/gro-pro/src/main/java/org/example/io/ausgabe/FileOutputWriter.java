package org.example.io.ausgabe;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.example.model.OutputData;

/**
 * Schreibt Ergebnisdaten als Textdatei in das Ausgabeverzeichnis.
 * <p>
 * Der Ausgabepfad setzt sich aus dem festen Verzeichnis {@code gro-pro-main/output}
 * und dem Dateinamen aus {@link OutputData#getFileName()} zusammen.
 * Der Ordner wird automatisch erstellt, falls er noch nicht existiert.
 * </p>
 */
public class FileOutputWriter implements OutputWriter {

    /** Relatives Verzeichnis, in das alle Ausgabedateien geschrieben werden. */
    private static final String OUTPUT_DIR = "gro-pro-main/output";

    /**
     * Schreibt den Inhalt von {@link OutputData#toOutputString()} in eine Textdatei.
     *
     * @param data die zu schreibenden Ausgabedaten
     * @throws IOException wenn die Datei nicht erstellt oder beschrieben werden kann
     */
    @Override
    public void write(OutputData data) throws IOException {
        File outputDir = new File(OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        String filePath = OUTPUT_DIR + File.separator + data.getFileName();

        try (PrintWriter writer = new PrintWriter(new java.io.FileWriter(filePath))) {
            writer.print(data.toOutputString());
        }
    }
}
