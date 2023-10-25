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
                BENBEN, //Artist,Album,Song,Date
                LASTFM }
    private static final String MISSING = "missing=yes";
    private static final String DELETED = "deleted=no";

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
                String[] columns = {MISSING, DELETED};
                removeColumn(entries, 3);
                addColumns(entries, columns);        
                break;
            case LASTFM:
                break;
            default:
                break;
        }
        saver.writeToCSV(path, entries);
    }

    /**
     * 
     * @param entries
     * @param column 0-indexed
     * @return
     */
    private void removeColumn(List<List<String>> entries, int column){
        for(List<String> entry : entries){
            entry.remove(column);
        }
    }

    private void addColumns(List<List<String>> entries, String[] defaultValues){
        for(List<String> entry : entries){
            entry.addAll(Arrays.asList(defaultValues));
        }
    }
}
