package org.example.io.output;

import java.io.IOException;
import java.util.List;
import org.example.model.ZugInput;
import org.example.model.ZugOutput;

public interface OutputWriter {

    void write(ZugInput zugInput, List<ZugOutput> data) throws IOException;
}
