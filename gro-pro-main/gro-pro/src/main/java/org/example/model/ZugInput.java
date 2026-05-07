package org.example.model;

public class ZugInput {

    private final String[] strecke;
    private final int[] abstaende;
    private final int start;
    private final String filename;
    private int sicherheitsWartezeit = 1;

    public ZugInput(String filename, String[] strecke, int[] abstaende, int start) {
        this.strecke = strecke;
        this.abstaende = abstaende;
        this.start = start;
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public String[] getStrecke() {
        return strecke;
    }

    public int[] getAbstaende() {
        return abstaende;
    }

    public int getStart() {
        return start;
    }
    public int getAnzahlStationen() {
        return strecke.length;
    }
    public int getMindestdauer(){
        int summe = 0;
        for (int abstand : abstaende) {
            summe += abstand;
        }
        return summe + (getAnzahlStationen() - 2)*sicherheitsWartezeit;
    }

    @Override
    public String toString() {
        String streckeStr  = String.join(" ", strecke);
        StringBuilder abstaendeStr = new StringBuilder();
        for (int i = 0; i < abstaende.length; i++) {
            if (i > 0) abstaendeStr.append(" ");
            abstaendeStr.append(abstaende[i]);
        }
        return "Strecke:\n" + streckeStr + "\n\n"
             + "Abstaende:\n" + abstaendeStr + "\n\n"
             + "Start Hinfahrt:\n" + start + "\n\n"
             + "Anzahl Bahnhöfe : " + getAnzahlStationen() + "\n"
             + "Mindestdauer    : " + getMindestdauer() + "\n";
    }
}
