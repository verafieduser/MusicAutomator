package com.verafied;

import java.io.File;
import java.io.IOException;
import org.hibernate.SessionFactory;


public class Initializer {

    CsvHandler loader;
    LibrarySaver saver;
    LibraryCollector collector;
    Library library;
    SessionFactory db;
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
    
        InputHandler ih = new InputHandler(demo);
        settings = new SettingsHandler();
        createDirectoryStructure();
        db = ormSetUp();
        loader = new CsvHandler(demo);
        saver = loader.getSaver();
        collector = new LibraryCollector(loader, loader.getSaver(), db, demo);
        library = new Library(db);
        //library = openLibrary(loader);
        missingMusic = new MissingMusic(loader.getSaver(), settings.get("local.musiclibrary.path"));
    }

    private SessionFactory ormSetUp(){
        return HibernateUtil.getSessionJavaConfigFactory();
    }

    private void createDirectoryStructure() {
        String[] newDirs = { "Library", "Library/Demo", "Library/Demo/Unprocessed", "Library/Unprocessed" };
        createDirs(SettingsHandler.APPLICATION_PATH, newDirs);
    }

    private void createDirs(String dir, String[] subDirs) {
        for (String subDir : subDirs) {
            File newFile = new File(dir, subDir);
            if (!newFile.exists() && !newFile.mkdir()) {
                throw new IllegalStateException(newFile.getAbsolutePath());
            }
        }
    }

    private Library openLibrary(CsvHandler loader) {
        try {
            return loader.loadLibrary();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CsvHandler getLoader() {
        return this.loader;
    }

    public LibraryCollector getCollector() {
        return this.collector;
    }

    public Library getLibrary() {
        return this.library;
    }

    public MissingMusic getMissingMusic(){
        return this.missingMusic;
    }

    public SettingsHandler getSettings(){
        return this.settings;
    }

    public LibrarySaver getSaver(){
        return this.saver;
    }
}
