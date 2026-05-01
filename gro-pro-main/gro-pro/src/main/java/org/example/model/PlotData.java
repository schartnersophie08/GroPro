package org.example.model;

/**
 * Interface für GNUPlot-Plotdaten.
 * <p>
 * Implementierungen liefern ein vollständiges GNUPlot-Skript als String,
 * das vom {@link org.example.io.ausgabe.GnuplotWriter} in eine Datei geschrieben
 * und anschließend von GNUPlot ausgeführt wird.
 * </p>
 */
public interface PlotData {

    /**
     * Gibt das vollständige GNUPlot-Skript als String zurück.
     *
     * @param pngPfad Zielpfad für die zu erzeugende PNG-Datei
     * @return GNUPlot-Skriptinhalt inklusive terminal-, output- und plot-Befehlen
     */
    String toGnuplotString(String pngPfad);
}
