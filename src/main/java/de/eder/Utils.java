package de.eder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {

    private Utils() {

    }

    public static String readFile(String filepath) {
        String str;
        try {
            str = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException e) {
            throw new RuntimeException("Error reading file [" + filepath + "]", e);
        }
        return str;
    }
}
