package org.example.algorithmus;

import org.example.model.InputData;
import org.example.model.OutputData;

/**
 * Generisches Interface für den Algorithmus.
 * <p>
 * Durch die Typ-Parameter {@code I} und {@code O} kann jede konkrete Implementierung
 * ihre eigenen Ein- und Ausgabetypen deklarieren – vollständig typsicher, ohne Casts.
 * </p>
 *
 * @param <I> konkreter Eingabetyp, muss {@link InputData} implementieren
 * @param <O> konkreter Ausgabetyp, muss {@link OutputData} implementieren
 */
public interface Algorithmus<I extends InputData, O extends OutputData> {

    /**
     * Führt den Algorithmus mit den übergebenen Eingabedaten aus.
     *
     * @param inputData konkrete Eingabedaten
     * @return berechnete Ausgabedaten
     */
    O run(I inputData);
}
