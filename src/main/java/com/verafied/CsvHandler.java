package com.verafied;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Loads a CSV of what the users music library "should" be
 */
public class CsvHandler {

    private String defaultPath; 
    private boolean demo;

    public CsvHandler(boolean demo) {
        this.demo = demo;
        defaultPath = getCSV("db.csv");
    }

    public String getCSV(String name) {
        String path = "\\Library\\";
        if(demo){
            path+="Demo\\";
        } 
        return path+name;
    }

    /**
     * Opens a .csv and creates a list(vertical) of lists(horizontal) of the data contained within
     * @param path relative to the application folder in the user folder.
     * @return The first list is all the rows, the lists within are the columns.
     * @throws IOException if reading the .csv fails
     */
    public List<List<String>> openCSV(String path) throws IOException {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(SettingsHandler.APPLICATION_PATH+path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
        } 
        return records;
    }

    /**
     * Opens a .csv and creates a list(vertical) of lists(horizontal) of the data contained within
     * @return The first list is all the rows, the lists within are the columns.
     * @throws IOException if reading the .csv fails
     */
    public List<List<String>> openCSV() throws IOException {
        return openCSV(defaultPath);
    }
}
