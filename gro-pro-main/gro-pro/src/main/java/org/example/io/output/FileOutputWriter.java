package org.example.io.output;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.example.model.ZugInput;
import org.example.model.ZugOutput;

public class FileOutputWriter implements OutputWriter {

    private static final String OUTPUT_DIR = "gro-pro-main/output";

    @Override
    public void write(ZugInput zugInput, List<ZugOutput> data) throws IOException {
        File outputDir = new File(OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        String nurDateiName = java.nio.file.Path.of(zugInput.getFilename()).getFileName().toString();
        String ausgabeDateiName = nurDateiName.replaceAll("\\.[^.]+$", "") + ".out";
        String filePath = OUTPUT_DIR + File.separator + ausgabeDateiName;

        try (PrintWriter writer = new PrintWriter(new java.io.FileWriter(filePath))) {
            writer.println(zugInput);
            for (ZugOutput zugOutput : data) {
                writer.println(zugOutput);
            }
        }
    }
}
