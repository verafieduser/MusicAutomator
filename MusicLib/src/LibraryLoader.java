import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Loads a CSV of what the users music library "should" be
 */
public class LibraryLoader {

    private final LibraryCollector collector;
    private final LibrarySaver saver; 
    private String defaultPath; 
    boolean demo;

    public LibraryLoader(boolean demo) {
        saver = new LibrarySaver(demo);
        collector = new LibraryCollector(this, saver, demo);
        this.demo = demo;
        defaultPath = getCSV("db.csv");
    }

    public String getCSV(String name) {
        String path = "Library\\";
        if(demo){
            path+="Demo\\";
        } 
        return path+name;
    }

    //TODO: should return a set of Songs, but this method can remain for unprocessed data!
    public List<List<String>> openCSV(String path) throws IOException {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
        } 
        return records;
    }

    public List<List<String>> openCSV() throws IOException {
        return openCSV(defaultPath);
    }

    public Library loadLibrary() throws IOException{
        return loadLibrary(defaultPath);
    }

    public Library loadLibrary(String path) throws IOException{
        List<List<String>> entries = openCSV(path);
        Library library = new Library();
        for (List<String> entry : entries){

            Artist artist = new Artist(entry.get(0), 
                entry.get(1),
                entry.get(2), 
                entry.get(3),
                Boolean.valueOf(entry.get(4))); // name, albums
            library.addArtist(artist);
        }
        library.initialize();
        return library;
    }


    public LibrarySaver getSaver(){
        return saver;
    }
}
