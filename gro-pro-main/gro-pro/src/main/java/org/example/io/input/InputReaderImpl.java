package org.example.io.input;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import org.example.model.ZugInput;

public class InputReaderImpl implements InputReader {

    @Override
    public ZugInput readInput(String filename) throws IOException {

        List<String> alleZeilen = Files.readAllLines(new File(filename).toPath(), StandardCharsets.UTF_8);

        // Strecke: Bahnhofsnamen aus Zeile nach "Strecke:"
        String[] strecke = alleZeilen.get(1).trim().split("\\s+");

        // Abstände: Zahlen aus Zeile nach "Abstaende:"
        String[] abstandTokens = alleZeilen.get(4).trim().split("\\s+");
        int[] abstaende = new int[abstandTokens.length];
        for (int i = 0; i < abstandTokens.length; i++) {
            abstaende[i] = Integer.parseInt(abstandTokens[i]);
        }

        // Start Hinfahrt: Zahl aus letzter Zeile
        int start = Integer.parseInt(alleZeilen.get(7).trim());

        return new ZugInput(filename,strecke, abstaende, start);
    }
}
