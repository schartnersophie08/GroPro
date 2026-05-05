package org.example.algorithmus;

import org.example.model.ZugInput;
import org.example.model.ZugOutput;

public class EinseitigesWarten implements Algorithmus {

    @Override
    public ZugOutput loese(ZugInput inputData) {

        return new ZugOutput("Einseitiges Warten", null, 0);
    }
}
