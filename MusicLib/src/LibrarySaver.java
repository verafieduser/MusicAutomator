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


    public void writeToCSV(String pathName, Library library) throws FileNotFoundException {
        File csvOutputFile = new File(pathName + "\\db.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)){
            for(Artist artist : library.getArtists().values()){
                for(Album album : artist.getAlbums()){
                    for(Song song : album.getSongs()){
                        pw.println(song.toCSV());
                    }
                }
            }
        } 
    }

    public void writeToCSV(Library library) throws FileNotFoundException{
        writeToCSV(defaultPath, library);
    }
}
