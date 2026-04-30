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
     * Das erzeugte PNG wird unter {@code gro-pro-main/output/testplot.png} gespeichert.
     * </p>
     *
     * @return vollständiges GNUPlot-Skript als String
     */
    @Override
    public String toGnuplotString() {
        return """
            set terminal png size 800,600
            set output "gro-pro-main/output/testplot.png"
            set title "Test-Plot"
            set xlabel "x"
            set ylabel "y"
            set grid
            plot sin(x) title "sin(x)" with lines
            """;
    }
}
