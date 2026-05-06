package org.example.model;

import java.util.ArrayList;
import java.util.List;
import org.example.Konstanten;

public class Zug {
    List<Intervall> intervalle;
    public Zug() {
        this.intervalle = new ArrayList<>();
    }

    public List<Intervall> getIntervalle() {
        return intervalle;
    }
    public void setIntervalle(List<Intervall> intervalle) {
        this.intervalle = intervalle;
    }
    public void addIntervall(Intervall intervall) {
        this.intervalle.add(intervall);
    }


    public void platziereOhneRuecksicht(int startzeit, List<Integer> abstaende) {
        Zeit start = new Zeit(startzeit / 60, startzeit % 60);
        for (int abstand : abstaende) {
            Zeit end = start.add(abstand);
            this.intervalle.add(new Intervall(start, end));
            start = end.add(Konstanten.HALTEZEIT);
        }
    }
}
