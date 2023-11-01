import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

/**
 * Compares local music with library .csv. For each song that is local but not in library, add to library.
 * For each song in the library not available locally, collect for download. 
 */
public class MissingMusic {
    private String localMusicLibraryPath;
    private LibrarySaver saver;

    public MissingMusic(LibrarySaver saver, String localMusicLibraryPath){
        this.saver = saver;
        this.localMusicLibraryPath = localMusicLibraryPath;
    }
    
    /**
     * Search through local music folder and add all songs that are present that aren't in the library 
     */
    public void findLocalSongs(){
        //TODO: figure out how to deal with faulty data in user library
    }

    /**
     * Finds all songs that are not represented in the local music library and returns a list of them
     * @param entries
     */
    public Set<Song> findMissing(Library library){
        Set<Song> missingSongs = new HashSet<>();
        for(Song song : library.getSongs()){
            if(song.getDeleted() || song.getPath()!=null){
                continue;
            }
            missingSongs.add(song);
        }
        return missingSongs;
    }

    /**
     * Connects songs that arent connected with a local file with a local file if available
     * @param library the library containing all music in the database that the local files should be
     * compared to.
     * @throws FileNotFoundException
     */
    public void connectMissing(Library library) throws FileNotFoundException{
        for(Song song : library.getSongs()){
            if(song.getDeleted() || song.getPath()!=null){
                continue;
            }

            StringBuilder sb = new StringBuilder(localMusicLibraryPath);
            sb.append("\\"+song.getArtist()); //artist folder
            sb.append("\\"+song.getAlbum()); //album folder
            File dir = new File(sb.toString().toLowerCase()); 
            String[] localSongs = dir.list();
            for(String localName : localSongs){
                String localNameNoExtension = localName.substring(0, localName.lastIndexOf("."));
                if(localNameNoExtension.equalsIgnoreCase(song.getTitle())){
                    song.setPath(dir.getAbsolutePath()+"\\"+localName); 
                }
            }
        }
        saver.writeToCSV(library);
    }
}
