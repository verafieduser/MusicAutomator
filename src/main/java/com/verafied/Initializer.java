package com.verafied;

import java.io.File;
import java.io.IOException;
import org.hibernate.SessionFactory;


public class Initializer {

    LibraryCollector collector;
    Library library;
    SessionFactory database;
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
        database = ormSetUp();
        collector = new LibraryCollector(new CsvHandler(demo), database, demo);
        missingMusic = new MissingMusic(settings.get("local.musiclibrary.path"));
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

    public LibraryCollector getCollector() { return this.collector; }

    public MissingMusic getMissingMusic(){ return this.missingMusic; }

    public SettingsHandler getSettings(){ return this.settings; }

    public SessionFactory getDatabase(){ return this.database; }
}
