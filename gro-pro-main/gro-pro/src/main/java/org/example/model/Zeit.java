package org.example.model;

public class Zeit {
    int stunden;
    int minuten;

    public Zeit(int stunden, int minuten) {
        this.stunden = stunden;
        this.minuten = minuten;
    }
    public Zeit add(int minuten) {
        int m = this.minuten;
        int s = this.stunden;
        m += minuten;
        while (m >= 60) {
            s++;
            m -= 60;
        }
        return new Zeit(s, m);
    }

    public boolean vor(Zeit andere) {
        if (this.stunden < andere.stunden) {
            return true;
        } else if (this.stunden == andere.stunden) {
            return this.minuten < andere.minuten;
        } else {
            return false;
        }
    }
    public int toMinuten() {
        return stunden * 60 + minuten;
    }

    public boolean nach(Zeit andere) {
        if (this.stunden > andere.stunden) {
            return true;
        } else if (this.stunden == andere.stunden) {
            return this.minuten > andere.minuten;
        } else {
            return false;
        }
    }


}
