package org.example.algorithmus;

import java.util.ArrayList;
import java.util.List;
import org.example.model.Intervall;
import org.example.model.Zug;
import org.example.model.ZugInput;
import org.example.model.ZugOutput;

public class EinfacheFahrt implements Algorithmus {

    @Override
    public ZugOutput loese(ZugInput inputData) {
        int n = inputData.getAbstaende().length; // Anzahl Segmente in eine Richtung

        // Hin- und Rückweg-Abstände aufbauen
        List<Integer> abstaende = new ArrayList<>();
        int[] rohdaten = inputData.getAbstaende();
        for (int abstand : rohdaten) abstaende.add(abstand);
        for (int i = rohdaten.length - 1; i >= 0; i--) abstaende.add(rohdaten[i]);

        // Zug1 platzieren
        Zug zug1 = new Zug();
        zug1.platziereOhneRuecksicht(inputData.getStart(), abstaende);

        // Tatsächliche Gesamtreisezeit ermitteln
        int startMinuten = inputData.getStart();
        List<Intervall> alleIntervalle = zug1.getIntervalle();
        int endeMinuten  = alleIntervalle.get(alleIntervalle.size() - 1).getEnde().toMinuten();
        int gesamtReisezeit = endeMinuten - startMinuten;

        // Anzahl weiterer Züge die in der Zwischenzeit gestartet sind
        int anzahlAndereZuege = gesamtReisezeit / 60;

        // Andere Züge aufbauen (nur Hinweg wird für den Vergleich benötigt)
        List<Zug> andereZuege = new ArrayList<>();
        List<Integer> hinwegAbstaende = abstaende.subList(0, n);
        for (int k = 1; k <= anzahlAndereZuege; k++) {
            Zug andererZug = new Zug();
            andererZug.platziereOhneRuecksicht(inputData.getStart() + k * 60, hinwegAbstaende);
            andereZuege.add(andererZug);
        }

        // Kollisionspunkte aufbauen: [Station0, Marker0, Station1, Marker1, ..., StationN]
        // Physisches Segment j (vorwärts): Zug1-Rückweg-Intervall (n-1-j) vs. andere Züge Hinweg-Intervall j
        String[] strecke = inputData.getStrecke();
        List<String> kollisionspunkte = new ArrayList<>();
        for (int j = 0; j < n; j++) {
            kollisionspunkte.add(strecke[j]);
            Intervall rueckIntervall = zug1.getIntervalle().get(n + (n - 1 - j));
            boolean kollision = false;
            for (Zug anderer : andereZuege) {
                if (rueckIntervall.kollidiert(anderer.getIntervalle().get(j))) {
                    kollision = true;
                    break;
                }
            }
            kollisionspunkte.add(kollision ? "X" : "");
        }
        kollisionspunkte.add(strecke[n]);

        return ZugOutput.vonZug("Einfache Fahrt", inputData, zug1, kollisionspunkte);
    }
}
