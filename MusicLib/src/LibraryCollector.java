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

        switch (source) {
            case BENBEN:
            //TODO: remove duplicate songs 
                removeDuplicates(entries);
                break;
            case LASTFM:
                break;
            default:
                break;
        }
        saver.writeToCSV(path, entries);
    }


    private void removeDuplicates(List<List<String>> entries){

    }
}
