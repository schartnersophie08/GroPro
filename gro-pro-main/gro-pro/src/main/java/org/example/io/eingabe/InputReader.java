package org.example.io.eingabe;

import java.io.IOException;
import org.example.model.InputData;

/**
 * Generisches Interface zum Einlesen von Eingabedateien.
 * <p>
 * Jede konkrete Implementierung ist für einen bestimmten Eingabetyp {@code I} zuständig
 * und wandelt den Dateiinhalt in ein typisiertes {@link InputData}-Objekt um.
 * </p>
 *
 * @param <I> konkreter Eingabetyp, muss {@link InputData} implementieren
 */
public interface InputReader<I extends InputData> {

    /**
     * Liest die Eingabedatei ein und gibt die geparsten Daten zurück.
     *
     * @param filename Pfad zur Eingabedatei
     * @return eingelesene Daten als konkretes {@link InputData}-Objekt
     * @throws IOException wenn die Datei nicht gefunden oder nicht gelesen werden kann
     */
    I readInput(String filename) throws IOException;
}
