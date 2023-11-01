import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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
        createDirectoryStructure();
        loader = new LibraryLoader(demo);
        collector = new LibraryCollector(loader, loader.getSaver(), demo);
        library = openLibrary(loader);
        missingMusic = new MissingMusic(loader.getSaver(), settings.get("local.musiclibrary.path"));

        if (library == null) {
            // TODO: Ask user what file to import
            try (InputStreamReader isr = new InputStreamReader(System.in);
                BufferedReader reader = new BufferedReader(isr)) {
                library = processAndOpenLibrary(loader, collector, getFileName(reader), getDataType(reader));    
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }

    private String getFileName(BufferedReader reader) throws IOException {
        String fileName = promptUserInput(reader, "Please enter name of file to process: ");
        while(!new File(SettingsHandler.APPLICATION_PATH+"/Library/Unprocessed/"+fileName).exists()){
            fileName = promptUserInput(reader, "Please try again!: ");
        }
        return fileName;
    }

    private DataSource getDataType(BufferedReader reader) throws IOException {
        String typeStr = promptUserInput(reader, 
                "Where did you get the file from? Enter a number for below options\n1.BENBEN\nPlease enter:");
        int result;
        while (true) {
            try {
                result = Integer.valueOf(typeStr);
                break;
            } catch (NumberFormatException e) {
                typeStr = promptUserInput(reader, "Please try again: ");
            }
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
        String result = null;
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
