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

    public void findMissing(List<List<String>> entries){
        for(List<String> entry : entries){
            String artist = entry.get(0);
            String album = entry.get(1);
            String song = entry.get(2);
            //TODO: compare?
        }
    }
}
