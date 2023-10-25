import java.util.List;

/**
 * If emptied, go through all songs with DELETED=true and deletes local files if they exist. 
 * If directory ends up empty, remove it.
 */
public class GarbageBin {

    private String localMusicLibraryPath;

    public GarbageBin(String localMusicLibraryPath){
        this.localMusicLibraryPath=localMusicLibraryPath;
    }
    
    public boolean deleteDeleted(List<List<String>> entries){
        for(List<String> entry : entries){
            if(entry.get(4).equals("DELETED=true")){
                String dir = localMusicLibraryPath+"\\"+entry.get(0)+"\\"+entry.get(1);
                deleteSong(dir, entry.get(2));
            }
        }
        return false;
    }

    public boolean deleteArtist(String dir, String name){
        String[] albums = {};
        for(String album : albums){
            deleteAlbum(dir+"\\"+name, album);
        }
        return false;
    }

    public boolean deleteAlbum(String dir, String name){
        String[] songs = {};
        for(String song : songs){
            deleteSong(dir+"\\"+name, song);
        }
        return false;
    }

    //TODO: figure out what to do with file extensions?
    public boolean deleteSong(String dir, String name){

        return false;
    }
}
