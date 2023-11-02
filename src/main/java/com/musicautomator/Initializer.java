package com.musicautomator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Predicate;

public class Initializer {

    LibraryLoader loader;
    LibraryCollector collector;
    Library library;
    SettingsHandler settings;
    MissingMusic missingMusic;

    /**
     * Initializes the program and links instances together.
     * 
     * @throws IOException if file structure or settings cannot be accessed nor
     *                     created, this exception is thrown
     */
    public Initializer() throws IOException {
        this(false);
    }

    /**
     * Initializes the program and links instances together.
     * 
     * @param demo Determines whether demo-folder will be used for library or not.
     * @throws IOException if file structure or settings cannot be accessed nor
     *                     created, this exception is thrown
     */
    public Initializer(boolean demo) throws IOException {
        settings = new SettingsHandler();
        createDirectoryStructure();
        loader = new LibraryLoader(demo);
        collector = new LibraryCollector(loader, loader.getSaver(), demo);
        library = openLibrary(loader);
        missingMusic = new MissingMusic(loader.getSaver(), settings.get("local.musiclibrary.path"));

        if (library == null) {
            try (InputStreamReader isr = new InputStreamReader(System.in);
                    BufferedReader reader = new BufferedReader(isr)) {
                library = processAndOpenLibrary(loader, collector, getFileName(reader), getDataType(reader));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        missingMusic.findLocalSongs(library);
        loader.getSaver().writeToCSV(library);
    }

    private String getFileName(BufferedReader reader) throws IOException {
        Predicate<String> p = x -> (new File(SettingsHandler.APPLICATION_PATH + "/Library/Unprocessed/" + x).exists());
        InputHandler ih = new InputHandler();
        return ih.loopingPromptUserInput(reader, "Please enter name of file to process:", "Please try again: ", p);
    }

    private DataSource getDataType(BufferedReader reader) throws IOException {
        Predicate<String> p = x -> (x.matches("\\d+") && Integer.valueOf(x) > 0
                && Integer.valueOf(x) < DataSource.values().length + 1);
        InputHandler ih = new InputHandler();

        String typeStr = ih.loopingPromptUserInput(reader,
                "Where did  you get the file from? Enter a number for below options\n1.BENBEN\nPlease enter: ",
                "Please try again: ", p);
        int result;
        try {
            result = Integer.valueOf(typeStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            result = 1;
        }
        DataSource type;
        switch (result) {
            case 1:
                type = DataSource.BENBEN;
                break;
            default:
                type = DataSource.LASTFM;
                System.out.println("No number was selected!");
                break;
        }
        return type;
    }

    private String promptUserInput(BufferedReader reader, String prompt) throws IOException {
        System.out.print(prompt);
        return reader.readLine();
    }

    private void createDirectoryStructure() {
        String[] newDirs = { "Library", "Library/Demo", "Library/Demo/Unprocessed", "Library/Unprocessed" };
        createDirs(SettingsHandler.APPLICATION_PATH, newDirs);
    }

    private void createDirs(String dir, String[] subDirs) {
        for (String subDir : subDirs) {
            File newFile = new File(dir + "/" + subDir);
            if (!newFile.exists() && !newFile.mkdir()) {
                throw new IllegalStateException(newFile.getAbsolutePath());
            }
        }
    }

    private Library openLibrary(LibraryLoader loader) {
        try {
            return loader.loadLibrary();
        } catch (IOException e) {
            System.err.println("No library found!");
        }
        return null;
    }

    private Library processAndOpenLibrary(LibraryLoader loader, LibraryCollector collector, String csvName,
            DataSource type) {
        try {
            collector.processCSV("verafiedmusic.csv", type);
            return loader.loadLibrary();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void test() {
        // Artist artist = library.getArtists().get("Oklou");
        // System.out.println(artist);
    }

    public LibraryLoader getLoader() {
        return this.loader;
    }

    public LibraryCollector getCollector() {
        return this.collector;
    }

    public Library getLibrary() {
        return this.library;
    }
}
