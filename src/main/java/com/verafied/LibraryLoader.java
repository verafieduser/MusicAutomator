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
public class LibraryLoader {

    private final LibrarySaver saver; 
    private String defaultPath; 
    private SqlDatabaseHandler db;
    private boolean demo;

    public LibraryLoader(boolean demo, SqlDatabaseHandler sqlDb) {
        db= sqlDb;
        saver = new LibrarySaver(demo, sqlDb);
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

    /**
     * Loads a .csv of a previously stored library, and creates a Library containing all the data
     * @return A library containing all the data that was contained in the .csv
     * @throws IOException if reading the .csv fails
     */
    public Library loadLibrary() throws IOException{
        return loadLibrary(defaultPath);
    }

    /**
     * Loads a .csv of a previously stored library, and creates a Library containing all the data
     * @param path relative to the application folder in the user folder.
     * @return A library containing all the data that was contained in the .csv
     * @throws IOException if reading the .csv fails
     */
    public Library loadLibrary(String path) throws IOException{
        List<List<String>> entries = openCSV(path);
        Library library = new Library(db);
        for (List<String> entry : entries){

            Artist artist = new Artist(entry.get(0), 
                entry.get(1),
                entry.get(2), 
                entry.get(3),
                Boolean.valueOf(entry.get(4))); // name, albums
            library.addArtist(artist);
        }
        library.initialize(); //not necessary but makes burden of computation more predictable?
        return library;
    }

    public LibrarySaver getSaver(){
        return saver;
    }
}
