package com.ec.svg.generator.app.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

    public static String readFile(String filePath) {
        String result = "";
        try {
            List<String> allLines = Files.readAllLines(Path.of(filePath));
            result =  String.join(" ", allLines);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return result;
    }
}
