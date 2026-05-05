package org.example.algorithmus;

import org.example.model.ZugInput;
import org.example.model.ZugOutput;

public interface Algorithmus {
    final String algoName = "Algorithmus";
    ZugOutput loese(ZugInput inputData);
}
