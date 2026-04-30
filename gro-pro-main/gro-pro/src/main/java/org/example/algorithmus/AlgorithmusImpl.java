package org.example.algorithmus;

import org.example.model.InputDataImpl;
import org.example.model.OutputDataImpl;
import org.example.model.PlotDataImpl;

/**
 * Dummy-Implementierung von {@link Algorithmus}.
 * <p>
 * Verarbeitet ein {@link InputDataImpl}-Objekt und erzeugt ein {@link OutputDataImpl}-Ergebnis.
 * Die eigentliche Algorithmus-Logik ist hier noch nicht implementiert und muss
 * für die konkrete Prüfungsaufgabe ergänzt werden.
 * </p>
 */
public class AlgorithmusImpl implements Algorithmus<InputDataImpl, OutputDataImpl> {

    /**
     * Führt den Algorithmus aus und gibt das Ergebnis zurück.
     * <p>
     * Aktuell wird lediglich der Dateiname bereinigt und die Eingabedaten
     * unverändert als Ausgabe weitergegeben. Hier ist die eigentliche Logik zu ergänzen.
     * </p>
     *
     * @param inputData konkrete Eingabedaten vom Typ {@link InputDataImpl}
     * @return berechnetes Ergebnis als {@link OutputDataImpl}
     */
    @Override
    public OutputDataImpl run(InputDataImpl inputData) {
        // TODO: eigentliche Algorithmus-Logik hier implementieren

        // Nur Dateiname ohne Pfad, damit FileOutputWriter korrekt in output/ schreibt
        String nurDateiName = inputData.getFileName().replaceAll(".*[/\\\\]", "");
        PlotDataImpl plotData = new PlotDataImpl();
        return new OutputDataImpl(plotData,nurDateiName, inputData.getText(), inputData.getIndex());
    }
}
