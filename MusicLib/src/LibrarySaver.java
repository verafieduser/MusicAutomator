import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Updates the data in the library
 */
public class LibrarySaver {

    private String defaultPath; 
    private boolean demo;

    public LibrarySaver(boolean demo){
        this.demo = demo;
        defaultPath = "Library\\";
        if(demo){
            defaultPath += "Demo\\";
        }
        defaultPath += "db.csv";
    }


    public void writeToCSV(String pathName, List<List<String>> records, int columns) throws FileNotFoundException {
        File csvOutputFile = new File(pathName);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)){
            for (List<String> record : records){
                StringBuilder line = new StringBuilder(record.get(0));
                for (int i = 1; i < columns; i++){
                    line.append("," + record.get(i));
                }
                pw.println(line.toString());
            }
        } 
    }
    
    public void writeToCSV(String pathName, List<List<String>> records) throws FileNotFoundException{
        writeToCSV(pathName, records, records.get(0).size());
    }

    public void writeToCSV(List<List<String>> records) throws FileNotFoundException{
        writeToCSV(defaultPath, records, records.get(0).size());
    }
}
