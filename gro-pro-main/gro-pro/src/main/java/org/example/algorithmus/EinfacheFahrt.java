package org.example.algorithmus;

import org.example.model.ZugInput;
import org.example.model.ZugOutput;

public class EinfacheFahrt implements Algorithmus {

    @Override
    public ZugOutput loese(ZugInput inputData) {



        return new ZugOutput("Einfache Fahrt", null, 0);
    }
}
