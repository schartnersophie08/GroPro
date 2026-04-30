package org.example.io.eingabe;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import org.example.model.InputDataImpl;

/**
 * Dummy-Implementierung von {@link InputReader} für {@link InputDataImpl}.
 * <p>
 * Liest eine Textdatei zeilenweise ein und befüllt ein {@link InputDataImpl}-Objekt
 * mit dem Dateipfad, der ersten und der zweiten Zeile der Datei.
 * </p>
 */
public class InputReaderImpl implements InputReader<InputDataImpl> {

    /**
     * Liest die angegebene Datei ein und gibt ein {@link InputDataImpl} zurück.
     * <p>
     * Erwartet mindestens zwei Zeilen in der Eingabedatei:
     * <ul>
     *   <li>Zeile 1 → text</li>
     *   <li>Zeile 2 → index</li>
     * </ul>
     * </p>
     *
     * @param filename Pfad zur Eingabedatei
     * @return befülltes {@link InputDataImpl}-Objekt
     * @throws IOException wenn die Datei nicht lesbar ist oder weniger als 2 Zeilen enthält
     */
    @Override
    public InputDataImpl readInput(String filename) throws IOException {

        List<String> alleZeilen = Files.readAllLines(new File(filename).toPath(), StandardCharsets.UTF_8);
        return new InputDataImpl(filename, alleZeilen.get(0), alleZeilen.get(1));
    }
}
