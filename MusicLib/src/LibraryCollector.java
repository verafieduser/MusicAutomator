import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Collects data on what music user listens to and creates a .CSV with it using librarysaver.
 * Collects recommendations based on what the user has listened to recently
 */
public class LibraryCollector {
    
    private LibraryLoader loader;
    private LibrarySaver saver;
    private boolean demo;
    public enum DataSource { 
                BENBEN, //.csv consisting of Artist,Album,Song,Date
                LASTFM }

    public LibraryCollector(LibraryLoader loader, LibrarySaver saver, boolean demo){
        this.loader = loader;
        this.saver = saver;
        this.demo = demo;
    }

    public void processCSV(String name, DataSource source) throws IOException{
        String path = "Library\\";
        if(demo){
            path+="Demo\\";
        }
        String unprocessedPath="Unprocessed\\" + name;
        List<List<String>> entries = loader.openCSV(unprocessedPath);

        Library library = new Library(); //switch determines how it is written
        switch (source) {
            case BENBEN: //Artist, album, song, date
            for(List<String> entry : entries){
                Artist artist = new Artist(entry.get(0), entry.get(1), entry.get(2));
                library.addArtist(artist);
            }        
                break;
            case LASTFM:
                break;
            default:
                break;
        }
        saver.writeToCSV(path, library);
    }
}
