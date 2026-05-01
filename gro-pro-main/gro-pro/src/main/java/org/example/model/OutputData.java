package org.example.model;

/**
 * Basisinterface für alle Ausgabedaten.
 * <p>
 * Definiert die minimale Schnittstelle, die jedes Ergebnisobjekt bereitstellen muss,
 * damit es vom {@link org.example.io.ausgabe.FileOutputWriter} und
 * {@link org.example.io.ausgabe.GnuplotWriter} verarbeitet werden kann.
 * </p>
 */
public interface OutputData {

    /**
     * Gibt den Inhalt der Ausgabedatei als formatierten String zurück.
     *
     * @return Textinhalt für die Ausgabedatei
     */
    String toOutputString();

    /**
     * Gibt den Dateinamen der Ausgabedatei zurück (ohne Verzeichnispfad).
     *
     * @return Dateiname der Ausgabe
     */
    String getFileName();

    /**
     * Gibt die Plot-Daten zurück, die für die GNUPlot-Visualisierung benötigt werden.
     *
     * @return {@link PlotData}-Objekt mit dem GNUPlot-Skriptinhalt
     */
    PlotData getPlotData();
}
