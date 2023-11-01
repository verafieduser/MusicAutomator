import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Handles the saving of a library into a local .csv file
 */
public class LibrarySaver {

    private String defaultPath; 

    public LibrarySaver( boolean demo){
        defaultPath = "Library\\";
        if(demo){
            defaultPath += "Demo\\";
        }
        defaultPath += "db.csv";
    }

    /**
     * Saves a music library to a .csv file named db.csv. Each entry in the .csv 
     * follows Artist,Album,Song,LocalFilePath,isDeleted
     * @param pathName the path relative to the application folder in the user folder. 
     * @param library the music library that should be written to .csv 
     * @throws FileNotFoundException
     */
    public void writeToCSV(String pathName, Library library) throws FileNotFoundException {
        File csvOutputFile = new File(SettingsHandler.APPLICATION_PATH+pathName + "\\db.csv");
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

    /**
     * Saves a music library to a .csv file named db.csv. Each entry in the .csv 
     * follows Artist,Album,Song,LocalFilePath,isDeleted
     * @param library the music library that should be written to .csv 
     * @throws FileNotFoundException
     */
    public void writeToCSV(Library library) throws FileNotFoundException{
        writeToCSV(defaultPath, library);
    }
}
