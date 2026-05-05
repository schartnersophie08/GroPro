package org.example.algorithmus;

import org.example.model.ZugInput;
import org.example.model.ZugOutput;

public class BeidseitigesWarten implements Algorithmus {
    @Override
    public ZugOutput loese(ZugInput inputData) {

        return new ZugOutput("Beidseitiges Warten",null, 0);
    }

}
