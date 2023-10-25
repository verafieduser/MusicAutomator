import java.io.BufferedReader;
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
}
