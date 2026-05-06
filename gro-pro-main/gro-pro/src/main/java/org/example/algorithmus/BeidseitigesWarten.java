package org.example.algorithmus;

import org.example.model.Intervall;
import org.example.model.Zeit;
import org.example.model.Zug;
import org.example.model.ZugInput;
import org.example.model.ZugOutput;

import org.example.Konstanten;

import java.util.ArrayList;
import java.util.List;

public class BeidseitigesWarten implements Algorithmus {

    private static final int HALTEZEIT       = Konstanten.HALTEZEIT;
    private static final int SICHERHEITSZEIT = Konstanten.SICHERHEITSZEIT;

    @Override
    public ZugOutput loese(ZugInput inputData) {

        int[] streckenDauern = inputData.getAbstaende();
        int anzahlSegmente = streckenDauern.length;
        int startHinfahrt = inputData.getStart();

        long besterScore = Long.MAX_VALUE;
        Fahrplan besterFahrplan = null;

        for (int versetzungRueckfahrt = 0; versetzungRueckfahrt < 60; versetzungRueckfahrt++) {
            int startRueckfahrt = startHinfahrt + versetzungRueckfahrt;

            Fahrplan fahrplan = erzeugeGrundfahrplan(
                    streckenDauern, startHinfahrt, startRueckfahrt);

            boolean gueltig = loeseAlleKonflikteMitTakt(fahrplan, streckenDauern);
            if (!gueltig) continue;

            long score = berechneScoreAusWartezeiten(fahrplan, anzahlSegmente);
            if (score < besterScore) {
                besterScore = score;
                besterFahrplan = fahrplan;
            }
        }

        // Sicherheitsfallback
        if (besterFahrplan == null) {
            besterFahrplan = erzeugeGrundfahrplan(streckenDauern, startHinfahrt, startHinfahrt);
        }

        // Zugobjekt mit Intervallen befüllen
        Zug zug = new Zug();
        fuegeHinfahrtIntervalleHinzu(zug, besterFahrplan, streckenDauern);
        fuegeRueckfahrtIntervalleHinzu(zug, besterFahrplan, streckenDauern);

        List<String> kollisionspunkte = new ArrayList<>();
        String[] strecke = inputData.getStrecke();
        for (int i = 0; i < anzahlSegmente; i++) {
            kollisionspunkte.add(strecke[i]);
            kollisionspunkte.add("");
        }
        kollisionspunkte.add(strecke[anzahlSegmente]);

        return ZugOutput.vonZug(
                "Beidseitiges Warten",
                inputData,
                zug,
                kollisionspunkte
        );
    }

    private Fahrplan erzeugeGrundfahrplan(
            int[] streckenDauern,
            int startHinfahrt,
            int startRueckfahrt) {

        int n = streckenDauern.length;
        Fahrplan fahrplan = new Fahrplan(n);

        fahrplan.abfahrtHin[0] = startHinfahrt;
        for (int segment = 0; segment < n; segment++) {
            fahrplan.ankunftHin[segment + 1]
                    = fahrplan.abfahrtHin[segment] + streckenDauern[segment];
            if (segment + 1 < n) {
                fahrplan.abfahrtHin[segment + 1]
                        = fahrplan.ankunftHin[segment + 1] + HALTEZEIT;
            }
        }

        fahrplan.abfahrtRueck[n] = startRueckfahrt;
        for (int segment = n - 1; segment >= 0; segment--) {
            fahrplan.ankunftRueck[segment]
                    = fahrplan.abfahrtRueck[segment + 1] + streckenDauern[segment];
            if (segment > 0) {
                fahrplan.abfahrtRueck[segment]
                        = fahrplan.ankunftRueck[segment] + HALTEZEIT;
            }
        }

        return fahrplan;
    }

    private boolean loeseAlleKonflikteMitTakt(
            Fahrplan fahrplan,
            int[] streckenDauern) {

        int n = streckenDauern.length;
        int grobeGesamtdauer = 0;
        for (int d : streckenDauern) grobeGesamtdauer += d;
        grobeGesamtdauer += (n - 1) * HALTEZEIT;

        int maxTaktVerschiebung = Math.max(3, grobeGesamtdauer / 60 + 2);
        int maxIterationen = n * maxTaktVerschiebung * 2;
        int iteration = 0;

        while (iteration < maxIterationen) {
            iteration++;

            Konflikt konflikt = findeFruehestenKonflikt(fahrplan, n, maxTaktVerschiebung);
            if (konflikt == null) return true;

            int segment = konflikt.segment;
            int zeitVerschiebung = 60 * konflikt.taktFaktor;

            int hinStart  = fahrplan.abfahrtHin[segment];
            int hinEnde   = fahrplan.ankunftHin[segment + 1];
            int rueckStart = fahrplan.abfahrtRueck[segment + 1] + zeitVerschiebung;
            int rueckEnde  = fahrplan.ankunftRueck[segment]     + zeitVerschiebung;

            int delayHin   = Math.max(0, (rueckEnde  + SICHERHEITSZEIT) - hinStart);
            int delayRueck = Math.max(0, (hinEnde    + SICHERHEITSZEIT) - rueckStart);

            int bisherHin   = summeZusatzwartezeitHinfahrt(fahrplan, n);
            int bisherRueck = summeZusatzwartezeitRueckfahrt(fahrplan, n);

            long scoreWennHinWartet   = quadrat(bisherHin + delayHin)   + quadrat(bisherRueck);
            long scoreWennRueckWartet = quadrat(bisherHin) + quadrat(bisherRueck + delayRueck);

            if (delayHin > 0 && (delayRueck == 0 || scoreWennHinWartet <= scoreWennRueckWartet)) {
                verzoegereHinfahrtAbStation(fahrplan, streckenDauern, segment, delayHin);
            } else if (delayRueck > 0) {
                verzoegereRueckfahrtAbStation(fahrplan, streckenDauern, segment + 1, delayRueck);
            }
        }
        return false;
    }

    private Konflikt findeFruehestenKonflikt(Fahrplan fahrplan, int n, int maxTaktVerschiebung) {
        Konflikt fruehester = null;
        for (int segment = 0; segment < n; segment++) {
            int hinStart = fahrplan.abfahrtHin[segment];
            int hinEnde  = fahrplan.ankunftHin[segment + 1];
            for (int k = -maxTaktVerschiebung; k <= maxTaktVerschiebung; k++) {
                int shift      = 60 * k;
                int rueckStart = fahrplan.abfahrtRueck[segment + 1] + shift;
                int rueckEnde  = fahrplan.ankunftRueck[segment]     + shift;
                if (ueberlapptMitSicherheitszeit(hinStart, hinEnde, rueckStart, rueckEnde)) {
                    int konfliktStart = Math.max(hinStart, rueckStart);
                    if (fruehester == null || konfliktStart < fruehester.zeitpunkt) {
                        fruehester = new Konflikt(segment, k, konfliktStart);
                    }
                }
            }
        }
        return fruehester;
    }

    private boolean ueberlapptMitSicherheitszeit(int aStart, int aEnde, int bStart, int bEnde) {
        return (bStart < aEnde + SICHERHEITSZEIT) && (aStart < bEnde + SICHERHEITSZEIT);
    }

    private void verzoegereHinfahrtAbStation(
            Fahrplan fahrplan, int[] streckenDauern, int station, int minuten) {
        int n = streckenDauern.length;
        fahrplan.abfahrtHin[station] += minuten;
        for (int seg = station; seg < n; seg++) {
            fahrplan.ankunftHin[seg + 1] = fahrplan.abfahrtHin[seg] + streckenDauern[seg];
            if (seg + 1 < n) {
                fahrplan.abfahrtHin[seg + 1] = fahrplan.ankunftHin[seg + 1] + HALTEZEIT;
            }
        }
    }

    private void verzoegereRueckfahrtAbStation(
            Fahrplan fahrplan, int[] streckenDauern, int station, int minuten) {
        fahrplan.abfahrtRueck[station] += minuten;
        for (int seg = station - 1; seg >= 0; seg--) {
            fahrplan.ankunftRueck[seg] = fahrplan.abfahrtRueck[seg + 1] + streckenDauern[seg];
            if (seg > 0) {
                fahrplan.abfahrtRueck[seg] = fahrplan.ankunftRueck[seg] + HALTEZEIT;
            }
        }
    }

    private long berechneScoreAusWartezeiten(Fahrplan fahrplan, int n) {
        int wHin   = summeZusatzwartezeitHinfahrt(fahrplan, n);
        int wRueck = summeZusatzwartezeitRueckfahrt(fahrplan, n);
        return quadrat(wHin) + quadrat(wRueck);
    }

    private int summeZusatzwartezeitHinfahrt(Fahrplan fahrplan, int n) {
        int summe = 0;
        for (int station = 1; station < n; station++) {
            int extra = fahrplan.abfahrtHin[station] - fahrplan.ankunftHin[station] - HALTEZEIT;
            if (extra > 0) summe += extra;
        }
        return summe;
    }

    private int summeZusatzwartezeitRueckfahrt(Fahrplan fahrplan, int n) {
        int summe = 0;
        for (int station = 1; station < n; station++) {
            int extra = fahrplan.abfahrtRueck[station] - fahrplan.ankunftRueck[station] - HALTEZEIT;
            if (extra > 0) summe += extra;
        }
        return summe;
    }

    private long quadrat(long x) {
        return x * x;
    }

    private void fuegeHinfahrtIntervalleHinzu(Zug zug, Fahrplan fahrplan, int[] streckenDauern) {
        int n = streckenDauern.length;
        for (int seg = 0; seg < n; seg++) {
            int start = fahrplan.abfahrtHin[seg];
            int ende  = fahrplan.ankunftHin[seg + 1];
            zug.addIntervall(new Intervall(
                    new Zeit(start / 60, start % 60),
                    new Zeit(ende  / 60, ende  % 60)
            ));
        }
    }

    private void fuegeRueckfahrtIntervalleHinzu(Zug zug, Fahrplan fahrplan, int[] streckenDauern) {
        int n = streckenDauern.length;
        for (int seg = n - 1; seg >= 0; seg--) {
            int start = fahrplan.abfahrtRueck[seg + 1];
            int ende  = fahrplan.ankunftRueck[seg];
            zug.addIntervall(new Intervall(
                    new Zeit(start / 60, start % 60),
                    new Zeit(ende  / 60, ende  % 60)
            ));
        }
    }

    private static class Fahrplan {
        int[] abfahrtHin;
        int[] ankunftHin;
        int[] abfahrtRueck;
        int[] ankunftRueck;

        Fahrplan(int n) {
            abfahrtHin   = new int[n + 1];
            ankunftHin   = new int[n + 1];
            abfahrtRueck = new int[n + 1];
            ankunftRueck = new int[n + 1];
        }
    }

    private static class Konflikt {
        int segment;
        int taktFaktor;
        int zeitpunkt;

        Konflikt(int segment, int taktFaktor, int zeitpunkt) {
            this.segment    = segment;
            this.taktFaktor = taktFaktor;
            this.zeitpunkt  = zeitpunkt;
        }
    }
}

