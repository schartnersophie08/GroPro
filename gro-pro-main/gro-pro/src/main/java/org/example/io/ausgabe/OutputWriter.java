package org.example.io.ausgabe;

import java.io.IOException;
import org.example.model.OutputData;

/**
 * Interface für die Ausgabe von Ergebnissen in verschiedene Formate.
 * <p>
 * Ermöglicht flexible Implementierungen für verschiedene Ausgabekanäle
 * (Textdatei, Datenbank, API, etc.).
 * </p>
 */
public interface OutputWriter {

    /**
     * Schreibt die übergebenen Ausgabedaten in das jeweilige Ausgabeformat.
     *
     * @param outputData die zu schreibenden Ergebnisdaten
     * @throws IOException wenn das Schreiben fehlschlägt
     */
    void write(OutputData outputData) throws IOException;
}
