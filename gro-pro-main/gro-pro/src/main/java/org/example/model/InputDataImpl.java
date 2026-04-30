package org.example.model;

/**
 * Dummy-Implementierung von {@link InputData}.
 * <p>
 * Repräsentiert die eingelesenen Rohdaten einer Eingabedatei.
 * In der Dummy-Version werden lediglich Dateiname, eine Textzeile und ein Index gespeichert.
 * Für die konkrete Aufgabe in der Prüfung sind diese Felder entsprechend zu ersetzen oder zu erweitern.
 * </p>
 */
public class InputDataImpl implements InputData {

    String fileName;
    String text;
    String index;

    /**
     * Erstellt ein neues InputDataImpl-Objekt mit den übergebenen Rohdaten.
     *
     * @param fileName Pfad zur Eingabedatei
     * @param text     erste Zeile der Eingabedatei (Dummy-Textinhalt)
     * @param index    zweite Zeile der Eingabedatei (Dummy-Index)
     */
    public InputDataImpl(String fileName, String text, String index) {
        this.fileName = fileName;
        this.text = text;
        this.index = index;
    }

    /**
     * Gibt den Dateipfad der Eingabedatei zurück.
     *
     * @return Dateipfad als String
     */
    @Override
    public String getFileName() {
        return fileName;
    }

    /**
     * Gibt den Index-Wert aus der Eingabedatei zurück.
     *
     * @return Index als String
     */
    public String getIndex() {
        return index;
    }

    /**
     * Gibt den Textinhalt aus der Eingabedatei zurück.
     *
     * @return Text als String
     */
    public String getText() {
        return text;
    }
}
