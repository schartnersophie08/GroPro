package org.example.model;

/**
 * Dummy-Implementierung von {@link OutputData}.
 * <p>
 * Hält die Ergebnisdaten des Algorithmus und stellt sie für die Dateiausgabe
 * und GNUPlot-Visualisierung bereit.
 * Für die konkrete Prüfungsaufgabe sind die Felder und Methoden entsprechend anzupassen.
 * </p>
 */
public class OutputDataImpl implements OutputData {

    String fileName;
    String text;
    String index;
    PlotData plotData;

    /**
     * Erstellt ein neues OutputDataImpl-Objekt mit allen Ausgabedaten.
     *
     * @param plotData {@link PlotData}-Objekt mit dem GNUPlot-Skript
     * @param fileName Dateiname der Ausgabedatei (ohne Verzeichnispfad)
     * @param text     Textinhalt der Ausgabe
     * @param index    Index-Wert der Ausgabe
     */
    public OutputDataImpl(PlotData plotData, String fileName, String text, String index) {
        this.plotData = plotData;
        this.fileName = fileName;
        this.text = text;
        this.index = index;
    }

    /**
     * Gibt den Ausgabeinhalt als formatierten String zurück.
     * <p>
     * Der Text besteht aus text, index und fileName, zeilenweise getrennt.
     * </p>
     *
     * @return formatierter Ausgabestring
     */
    @Override
    public String toOutputString() {
        return text + "\n" + index + "\n" + fileName;
    }

    /**
     * Gibt den Dateinamen der Ausgabedatei zurück.
     *
     * @return Dateiname ohne Verzeichnispfad
     */
    @Override
    public String getFileName() {
        return fileName;
    }

    /**
     * Gibt den Textinhalt der Ausgabe zurück.
     *
     * @return Textinhalt
     */
    public String getText() {
        return text;
    }

    /**
     * Gibt den Index der Ausgabe zurück.
     *
     * @return Index als String
     */
    public String getIndex() {
        return index;
    }

    /**
     * Gibt das {@link PlotData}-Objekt zurück, das das GNUPlot-Skript enthält.
     *
     * @return PlotData-Objekt
     */
    @Override
    public PlotData getPlotData() {
        return plotData;
    }
}
