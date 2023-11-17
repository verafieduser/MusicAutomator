package com.verafied;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Predicate;

import org.hibernate.Hibernate;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.jaudiotagger.tag.datatype.BooleanString;

public class Initializer {

    LibraryLoader loader;
    LibrarySaver saver;
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
    
        InputHandler ih = new InputHandler(demo);
        settings = new SettingsHandler();
        ormSetUp();
        createDirectoryStructure();
        //db = new SqlDatabaseHandler(settings);
        loader = new LibraryLoader(demo);
        saver = loader.getSaver();
        collector = new LibraryCollector(loader, loader.getSaver(), demo);
        library = openLibrary(loader);
        
        missingMusic = new MissingMusic(loader.getSaver(), settings.get("local.musiclibrary.path"));
    }

    private void ormSetUp(){
        
        // StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder();
        // BootstrapServiceRegistry bsr = new BootstrapServiceRegistryBuilder().build();
        // StandardServiceRegistryBuilder srb = new StandardServiceRegistryBuilder(bsr);
        HibernateUtil.getSessionJavaConfigFactory().openSession();
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

    private Library openLibrary(LibraryLoader loader) {
        try {
            return loader.loadLibrary();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
