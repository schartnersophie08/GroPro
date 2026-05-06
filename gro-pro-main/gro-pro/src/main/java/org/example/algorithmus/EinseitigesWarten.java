package org.example.algorithmus;

import org.example.Konstanten;
import org.example.model.Intervall;
import org.example.model.Zeit;
import org.example.model.Zug;
import org.example.model.ZugInput;
import org.example.model.ZugOutput;

import java.util.ArrayList;
import java.util.List;

public class EinseitigesWarten implements Algorithmus {

    private static final int HALTEZEIT       = Konstanten.HALTEZEIT;
    private static final int SICHERHEITSZEIT = Konstanten.SICHERHEITSZEIT;

    @Override
    public ZugOutput loese(ZugInput inputData) {
        int[] rohdaten = inputData.getAbstaende();
        int n = rohdaten.length;

        // Hinfahrt ohne Rücksicht
        List<Integer> hinwegAbstaende = new ArrayList<>();
        for (int a : rohdaten) hinwegAbstaende.add(a);

        Zug zug1 = new Zug();
        zug1.platziereOhneRuecksicht(inputData.getStart(), hinwegAbstaende);

        // Großzügig Gegenzüge erzeugen (k=1..20 reicht für alle Praxisfälle)
        List<Zug> andereZuege = new ArrayList<>();
        for (int k = 1; k <= 20; k++) {
            Zug anderer = new Zug();
            anderer.platziereOhneRuecksicht(inputData.getStart() + k * 60, hinwegAbstaende);
            andereZuege.add(anderer);
        }

        // Rückfahrt Segment für Segment aufbauen
        // Rückfahrt-Segment j (j=0..n-1) entspricht physischem Segment (n-1-j)
        List<Intervall> hinIntervalle = zug1.getIntervalle();
        int aktuelleStart = hinIntervalle.get(n - 1).getEnde().toMinuten() + SICHERHEITSZEIT;

        for (int j = 0; j < n; j++) {
            int physSegment = n - 1 - j;
            int dauer = rohdaten[physSegment];

            boolean kollision;
            do {
                kollision = false;
                Intervall versuch = new Intervall(
                        new Zeit(aktuelleStart / 60, aktuelleStart % 60),
                        new Zeit((aktuelleStart + dauer) / 60, (aktuelleStart + dauer) % 60)
                );
                for (Zug anderer : andereZuege) {
                    Intervall gegner = anderer.getIntervalle().get(physSegment);
                    if (versuch.kollidiert(gegner)) {
                        // Warte bis Ende des Gegenzug-Intervalls + SICHERHEITSZEIT
                        aktuelleStart = gegner.getEnde().toMinuten() + SICHERHEITSZEIT;
                        kollision = true;
                        break;
                    }
                }
                if (!kollision) {
                    zug1.addIntervall(versuch);
                    aktuelleStart = aktuelleStart + dauer + HALTEZEIT;
                }
            } while (kollision);
        }

        // Kollisionspunkte: kein X, da Kollisionen durch Warten vermieden werden
        String[] strecke = inputData.getStrecke();
        List<String> kollisionspunkte = new ArrayList<>();
        for (int j = 0; j < n; j++) {
            kollisionspunkte.add(strecke[j]);
            kollisionspunkte.add("");
        }
        kollisionspunkte.add(strecke[n]);

        // Score = sumWarteHin² + sumWarteRueck²
        // Hinfahrt hat keine Wartezeiten, Rückfahrt: Differenz zwischen tatsächlichem und minimalem Start
        List<Intervall> alleIv = zug1.getIntervalle();
        int minRueckStart = alleIv.get(n - 1).getEnde().toMinuten() + SICHERHEITSZEIT;
        int istRueckStart = alleIv.get(n).getStart().toMinuten();
        int sumWaRueck = istRueckStart - minRueckStart;
        // Zusätzliche Wartezeiten an Zwischenstationen der Rückfahrt
        for (int j = 1; j < n; j++) {
            int ankunft = alleIv.get(n + j - 1).getEnde().toMinuten();
            int abfahrt = alleIv.get(n + j).getStart().toMinuten();
            sumWaRueck += Math.max(0, abfahrt - ankunft - HALTEZEIT);
        }

        return ZugOutput.vonZug("Einseitiges Warten", inputData, zug1, kollisionspunkte);
    }
}
