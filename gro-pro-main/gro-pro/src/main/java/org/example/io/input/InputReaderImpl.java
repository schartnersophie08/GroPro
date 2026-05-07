package org.example.io.input;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import org.example.Konstanten;
import org.example.model.ZugInput;

public class InputReaderImpl implements InputReader {

    private static final String HEADER_STRECKE   = "Strecke:";
    private static final String HEADER_ABSTAENDE = "Abstaende:";
    private static final String HEADER_START     = "Start Hinfahrt:";
    private static final int    MAX_TEILSTRECKE  = (30-Integer.max(Konstanten.HALTEZEIT, Konstanten.SICHERHEITSZEIT));

    @Override
    public ZugInput readInput(String filename) throws IOException {

        List<String> alleZeilen = Files.readAllLines(new File(filename).toPath(), StandardCharsets.UTF_8);

        if (alleZeilen.size() < 8) {
            throw new IllegalArgumentException(
                "Ungültiges Dateiformat: Die Eingabedatei muss mindestens 8 Zeilen enthalten.");
        }

        // --- Überschriften prüfen ---
        pruefeUeberschrift(alleZeilen.get(0), HEADER_STRECKE,   "Zeile 1");
        pruefeUeberschrift(alleZeilen.get(3), HEADER_ABSTAENDE, "Zeile 4");
        pruefeUeberschrift(alleZeilen.get(6), HEADER_START,     "Zeile 7");

        // --- Strecke ---
        String streckeZeile = alleZeilen.get(1).trim();
        if (streckeZeile.isEmpty()) {
            throw new IllegalArgumentException(
                "Ungültige Eingabe in Zeile 2: Die Strecke darf nicht leer sein.");
        }
        String[] strecke = streckeZeile.split("\\s+");
        for (int i = 0; i < strecke.length; i++) {
            if (strecke[i].length() != 1) {
                throw new IllegalArgumentException(
                    "Ungültige Eingabe in Zeile 2: Jeder Stationsname muss genau ein Zeichen lang sein, " +
                    "aber \"" + strecke[i] + "\" hat " + strecke[i].length() + " Zeichen.");
            }
        }

        // --- Abstände ---
        String abstandZeile = alleZeilen.get(4).trim();
        if (abstandZeile.isEmpty()) {
            throw new IllegalArgumentException(
                "Ungültige Eingabe in Zeile 5: Die Abstände dürfen nicht leer sein.");
        }
        String[] abstandTokens = abstandZeile.split("\\s+");

        // Anzahl Abstände muss genau strecke.length - 1 sein
        int erwartet = strecke.length - 1;
        if (abstandTokens.length != erwartet) {
            throw new IllegalArgumentException(
                "Ungültige Eingabe in Zeile 5: Bei " + strecke.length + " Stationen werden " +
                erwartet + " Abstände erwartet, aber es wurden " + abstandTokens.length + " angegeben.");
        }

        int[] abstaende = new int[abstandTokens.length];
        for (int i = 0; i < abstandTokens.length; i++) {
            try {
                abstaende[i] = Integer.parseInt(abstandTokens[i]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                    "Ungültige Eingabe in Zeile 5: \"" + abstandTokens[i] +
                    "\" ist keine gültige ganze Zahl.");
            }
            if (abstaende[i] <= 0) {
                throw new IllegalArgumentException(
                    "Ungültige Eingabe in Zeile 5: Alle Abstände müssen positiv sein, " +
                    "aber Abstand " + (i + 1) + " ist " + abstaende[i] + ".");
            }
            if (abstaende[i] >= MAX_TEILSTRECKE) {
                throw new IllegalArgumentException(
                    "Ungültige Eingabe in Zeile 5: Abstand " + (i + 1) + " beträgt " + abstaende[i] +
                    " Minuten. Teilstreckenlängen müssen kleiner als " + MAX_TEILSTRECKE + " sein.");
            }
        }

        // --- Startzeit ---
        String startZeile = alleZeilen.get(7).trim();
        int start;
        try {
            start = Integer.parseInt(startZeile);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Ungültige Eingabe in Zeile 8: \"" + startZeile +
                "\" ist keine gültige ganze Zahl für die Startzeit.");
        }
        if (start < 0 || start > 59) {
            throw new IllegalArgumentException(
                "Ungültige Eingabe in Zeile 8: Die Startzeit muss zwischen 0 und 59 liegen, " +
                "aber angegeben wurde " + start + ".");
        }

        return new ZugInput(filename, strecke, abstaende, start);
    }

    private void pruefeUeberschrift(String zeile, String erwartet, String position) {
        if (!zeile.trim().equals(erwartet)) {
            throw new IllegalArgumentException(
                "Ungültiges Dateiformat in " + position + ": Überschrift \"" + erwartet +
                "\" erwartet, aber \"" + zeile.trim() + "\" gefunden.");
        }
    }
}
