import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Collects data on what music user listens to and creates a .csv with it using
 * librarysaver.
 */
public class LibraryCollector {

    private LibraryLoader loader;
    private LibrarySaver saver;
    private boolean demo;

    public LibraryCollector(LibraryLoader loader, LibrarySaver saver, boolean demo) {
        this.loader = loader;
        this.saver = saver;
        this.demo = demo;
    }

    /**
     * 
     * @param name   the filename of the .csv file that should be datamined for a
     *               library.
     * @param source how is the data structured in the .csv, which determines how to
     *               process it.
     * @throws IOException if the .csv cannot be opened or saved.
     */
    public void processCSV(String name, DataSource source) throws IOException {
        String path = "\\Library\\";
        if (demo) {
            path += "Demo\\";
        }
        String unprocessedPath = path + "Unprocessed\\" + name;
        List<List<String>> entries = loader.openCSV(unprocessedPath);

        Library library = new Library(); // switch determines how it is written
        switch (source) {
            case BENBEN: // Artist, album, song, date (ONLY ONE ARTIST PER ALBUM)
                for (List<String> entry : entries) {
                    Artist artist = new Artist(entry.get(0), entry.get(1), entry.get(2));
                    library.addArtist(artist);
                }
                break;
            case LASTFM:
                break;
            case SPOTIFY:
                break;
            case TIDAL:
                break;
            default:
                break;
        }
        saver.writeToCSV(path, library);
    }
}
