package org.example.model;

/**
 * Dummy-Implementierung von {@link PlotData} zu Testzwecken.
 * <p>
 * Erzeugt ein GNUPlot-Skript, das eine einfache Sinuskurve als PNG-Datei ausgibt.
 * Dient dazu zu prüfen, ob die GNUPlot-Ausgabe korrekt funktioniert,
 * ohne dass ein konkreter Algorithmus bereits implementiert sein muss.
 * </p>
 */
public class PlotDataImpl implements PlotData {

    /**
     * Gibt ein GNUPlot-Skript zurück, das eine Sinuskurve als PNG rendert.
     * <p>
     * Das erzeugte PNG wird unter dem übergebenen {@code pngPfad} gespeichert.
     * </p>
     *
     * @param pngPfad Zielpfad für die PNG-Ausgabe
     * @return vollständiges GNUPlot-Skript als String
     */
    @Override
    public String toGnuplotString(String pngPfad) {
        return "set terminal png size 800,600\n"
             + "set output \"" + pngPfad + "\"\n"
             + "set title \"Test-Plot\"\n"
             + "set xlabel \"x\"\n"
             + "set ylabel \"y\"\n"
             + "set grid\n"
             + "plot sin(x) title \"sin(x)\" with lines\n";
    }
}
