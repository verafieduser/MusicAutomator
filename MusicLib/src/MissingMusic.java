import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Compares local music with library .csv. For each song that is local but not in library, add to library
 * Each song that is in library but not local, add "missing = 1" to .csv for that track (together with librarysaver?)
 */
public class MissingMusic {
    private String localMusicLibraryPath;
    private LibrarySaver saver;

    public MissingMusic(LibrarySaver saver, String localMusicLibraryPath){
        this.saver = saver;
        this.localMusicLibraryPath = localMusicLibraryPath;
    }

    /**
     * Compares songs in database with local music library, adds into the database whether they are missing
     * or not. 
     * @param entries
     * @throws FileNotFoundException
     */
    public void findMissing(List<List<String>> entries) throws FileNotFoundException{
        for(List<String> entry : entries){
            if(entry.get(4).equals("DELETED=true")){
                continue;
            }
            StringBuilder sb = new StringBuilder(localMusicLibraryPath);
            sb.append("\\"+entry.get(0)); //artist folder
            sb.append("\\"+entry.get(1)); //album folder
            File dir = new File(sb.toString().toLowerCase());
            String[] songs = dir.list();
            boolean fileFound = false;
            for(String song : songs){
                if(song.equalsIgnoreCase(entry.get(2))){
                    fileFound = true;
                }
            }
            entry.set(3, "MISSING="+fileFound);
        }
        saver.writeToCSV(entries);
    }
}
