package org.example;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger implements Closeable {

    private final String filename;
    private final FileWriter fileWriter;


    public FileLogger(String filename) {
        this.filename = filename;
        try {
            this.fileWriter = new FileWriter(filename);
        } catch (IOException e) {
            throw new RuntimeException("Failed to open file: " + filename);
        }
    }

    public void log(String string) {
        try {
            fileWriter.write(string + "\n");
        } catch (IOException e) {
            throw new RuntimeException("Failed to add to file: [" + string + "]");
        }
    }

    public void close() {
        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close file: " + filename);
        }
    }
}
