package org.example.io.input;

import java.io.IOException;
import org.example.model.ZugInput;

public interface InputReader {

    ZugInput readInput(String filename) throws IOException;
}
