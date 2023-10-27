import java.io.File;
import java.io.FileNotFoundException;

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
    public void findMissing(Library library) throws FileNotFoundException{
        for(Song song : library.getSongs()){
            if(song.getDeleted()){
                continue;
            }


            StringBuilder sb = new StringBuilder(localMusicLibraryPath);
            sb.append("\\"+song.getArtist()); //artist folder
            sb.append("\\"+song.getAlbum()); //album folder
            File dir = new File(sb.toString().toLowerCase());
            String[] localSongs = dir.list();
            boolean fileFound = false;
            for(String localName : localSongs){
                if(localName.equalsIgnoreCase(song.getTitle())){
                    fileFound = true;
                }
            }
            song.setMissing(!fileFound);
        }
        saver.writeToCSV(library);
    }
}
