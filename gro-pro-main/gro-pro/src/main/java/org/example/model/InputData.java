package org.example.model;

/**
 * Basisinterface für alle Eingabedaten.
 * <p>
 * Jede konkrete Eingabe-Implementierung muss dieses Interface implementieren.
 * So bleibt der Algorithmus unabhängig von der konkreten Eingabestruktur.
 * </p>
 */
public interface InputData {

    /**
     * Gibt den Pfad bzw. Namen der Eingabedatei zurück.
     *
     * @return Dateiname oder Dateipfad der Eingabedatei
     */
    String getFileName();
}
