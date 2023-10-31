import java.io.IOException;

public class Initializer {

    LibraryLoader loader;
    LibraryCollector collector;
    Library library;
    SettingsHandler settings;
    MissingMusic missingMusic;

    public Initializer() throws IOException {
        this(false);
    }

    public Initializer(boolean demo) throws IOException {
        settings = new SettingsHandler();
        loader = new LibraryLoader(demo);
        collector = new LibraryCollector(loader, loader.getSaver(), demo);
        library = openLibrary(loader);
        if (library == null) {
            library = processAndOpenLibrary(loader, collector);
        }

        missingMusic = new MissingMusic(loader.getSaver(), settings.get("localMusicLibraryPath"));
    }

    private Library openLibrary(LibraryLoader loader) {
        try {
            return loader.loadLibrary();
        } catch (IOException e) {
            System.err.println("No library found!");
        }
        return null;
    }

    private Library processAndOpenLibrary(LibraryLoader loader, LibraryCollector collector) {
        try {
            collector.processCSV("verafiedmusic.csv", DataSource.BENBEN);
            return loader.loadLibrary();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void test() {
        Artist artist = library.getArtists().get("Oklou");
        System.out.println(artist);
    }

    public LibraryLoader getLoader() {
        return this.loader;
    }

    public void setLoader(LibraryLoader loader) {
        this.loader = loader;
    }

    public LibraryCollector getCollector() {
        return this.collector;
    }

    public void setCollector(LibraryCollector collector) {
        this.collector = collector;
    }

    public Library getLibrary() {
        return this.library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

}
