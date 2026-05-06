package org.example.model;

import java.util.List;
import java.util.Objects;

public class ZugOutput {



    private final String algoName;
    private final String[] strecke;
    private final int[] ankunftHin;
    private final int[] abfahrtHin;
    private final int[] abfahrtRueck;
    private final int[] ankunftRueck;
    private final List<String> kollisionspunkte;

    private ZugOutput(String algoName, String[] strecke,
                      int[] ankunftHin, int[] abfahrtHin,
                      int[] abfahrtRueck, int[] ankunftRueck,
                      List<String> kollisionspunkte) {
        this.algoName = algoName;
        this.strecke = strecke;
        this.ankunftHin = ankunftHin;
        this.abfahrtHin = abfahrtHin;
        this.abfahrtRueck = abfahrtRueck;
        this.ankunftRueck = ankunftRueck;
        this.kollisionspunkte = kollisionspunkte;
    }

    public static ZugOutput vonZug(String algoName, ZugInput inputData, Zug zug,
                                   List<String> kollisionspunkte) {
        int n = inputData.getAbstaende().length; // Anzahl Segmente in eine Richtung
        int nStationen = n + 1;
        List<Intervall> iv = zug.getIntervalle();

        int[] ankunftHin   = new int[nStationen];
        int[] abfahrtHin   = new int[nStationen];
        int[] abfahrtRueck = new int[nStationen];
        int[] ankunftRueck = new int[nStationen];

        // Hinfahrt: Intervall j → Station j nach j+1
        abfahrtHin[0] = iv.get(0).getStart().toMinuten();
        for (int i = 1; i < n; i++) {
            ankunftHin[i] = iv.get(i - 1).getEnde().toMinuten();
            abfahrtHin[i] = iv.get(i).getStart().toMinuten();
        }
        ankunftHin[n] = iv.get(n - 1).getEnde().toMinuten();

        // Rückfahrt: Rückweg-Intervall n+(n-1-j) → Station j+1 nach j
        abfahrtRueck[n] = iv.get(n).getStart().toMinuten();
        for (int i = n - 1; i > 0; i--) {
            ankunftRueck[i]  = iv.get(n + (n - 1 - i)).getEnde().toMinuten();
            abfahrtRueck[i]  = iv.get(n + (n - i)).getStart().toMinuten();
        }
        ankunftRueck[0] = iv.get(2 * n - 1).getEnde().toMinuten();

        return new ZugOutput(algoName, inputData.getStrecke(),
                ankunftHin, abfahrtHin, abfahrtRueck, ankunftRueck,
                kollisionspunkte);
    }

    // --- Hilfsmethoden ---

    private String fmtZeit(int minuten) {
        return String.format("%02d", minuten % 60);
    }

    private String buildRow(String label, int[] zeiten) {
        StringBuilder sb = new StringBuilder();
        sb.append(label).append("  ");
        for (int z : zeiten) {
            if (z == 0) {
                sb.append("        ");
            } else {
                sb.append(fmtZeit(z)).append("     ");
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    private int[] berechneExtraWartezeiten(int[] ankunft, int[] abfahrt) {
        int[] wa = new int[ankunft.length];
        for (int i = 0; i < ankunft.length; i++) {
            if (ankunft[i] != 0 && abfahrt[i] != 0) {
                wa[i] = Math.max(0, abfahrt[i] - ankunft[i] - 1);
            }
            // sonst 0 → keine Ausgabe
        }
        return wa;
    }

    private int summe(int[] arr) {
        int s = 0;
        for (int v : arr) s += v;
        return s;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(algoName).append(":\n");

        int[] waHin   = berechneExtraWartezeiten(ankunftHin, abfahrtHin);
        int[] waRueck = berechneExtraWartezeiten(ankunftRueck, abfahrtRueck);
        int score = summe(waHin) * summe(waHin) + summe(waRueck) * summe(waRueck);
        sb.append(buildRow("An", ankunftHin));
        sb.append(buildRow("Wa", waHin));
        sb.append(buildRow("Ab", abfahrtHin));

        sb.append("      ");
        for (String s : kollisionspunkte) {
            if (Objects.equals(s, "X")) {
                sb.append(" X   ");
            } else if (Objects.equals(s, "")) {
                sb.append("      ");
            } else {
                sb.append(s).append(" ");
            }
        }
        sb.append("\n");

        sb.append(buildRow("Ab", abfahrtRueck));
        sb.append(buildRow("Wa", waRueck));
        sb.append(buildRow("An", ankunftRueck));

        // --- Statistik ---
        int dauerHin   = ankunftHin[ankunftHin.length - 1] - abfahrtHin[0];
        int dauerRueck = ankunftRueck[0] - abfahrtRueck[abfahrtRueck.length - 1];
        int sumWaHin   = summe(waHin);
        int sumWaRueck = summe(waRueck);

        sb.append("Gesamtdauer Hinfahrt, Rückfahrt       : ")
          .append(dauerHin).append(", ").append(dauerRueck).append("\n");
        sb.append("Summe Wartezeiten Hinfahrt, Rückfahrt : ")
          .append(sumWaHin).append(", ").append(sumWaRueck).append("\n");
        sb.append("Summe Strafen                         : ")
          .append(score).append("\n");

        return sb.toString();
    }
}
