package com.royalkid.util;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Gets help from file and prints it on screen.
 *
 * @author Vladimir Pantasenko
 * @since JDK1.8
 */
public class HelpMenu {
    /**
     * Prints content of README.txt file stored in ./readme directory.
     */
    public static void printReadme() {
        Path pathToReadmeFile = Paths.get("./readme/README.txt");
        try (BufferedReader inputStream = new BufferedReader(
                new FileReader(pathToReadmeFile.toString()))) {
            String line;
            while ((line = inputStream.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
