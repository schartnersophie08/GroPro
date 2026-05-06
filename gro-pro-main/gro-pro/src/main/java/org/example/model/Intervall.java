package org.example.model;

import org.example.Konstanten;

public class Intervall {
 Zeit start;
 Zeit ende;

 public Intervall(Zeit start, Zeit ende) {
     this.start = start;
     this.ende = ende;
 }

 public Zeit getStart() { return start; }
 public Zeit getEnde()  { return ende; }
 public boolean kollidiert(Intervall anderes) {
     // Kein Konflikt: this endet mind. 1 Min. vor Start von anderes
     if (!anderes.start.vor(this.ende.add(Konstanten.SICHERHEITSZEIT))) return false;
     // Kein Konflikt: anderes endet mind. 1 Min. vor Start von this
     if (!this.start.vor(anderes.ende.add(Konstanten.SICHERHEITSZEIT))) return false;
     return true;
 }

}
