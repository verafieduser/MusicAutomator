import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Class that helps perform changes to the library
 */
public class Library {
    
    private Set<Song> songs = new HashSet<>();
    private Set<Album> albums = new HashSet<>();
    private TreeMap<String, Artist> artists = new TreeMap<>();
    private String localMusicLibraryPath;
    private boolean initialized = false;

    public Library(){
        localMusicLibraryPath = "Library\\db.csv";
    }

    public Library(String localMusicLibraryPath){
        this.localMusicLibraryPath = localMusicLibraryPath;
    }

    /**
     * Add artist to the library. If already present - merge contents of incoming and previous artists.
     * @param artist
     */
    public void addArtist(Artist artist){
        Set<Album> artistAlbums = artist.getAlbums();
        if(artistAlbums.isEmpty()){
            throw new IllegalArgumentException("Artists much have atleast one instance of albums with (a) song(s)");
        }
        Artist value = artists.putIfAbsent(artist.getName(), artist); 
        if (value != null){ // If artist is already present, add new instance albums to old instance
            value.merge(artist);
        }
        Set<Album> newArtistAlbums = artists.get(artist.getName()).getAlbums();
        albums.addAll(newArtistAlbums);
        newArtistAlbums.forEach(x -> songs.addAll(x.getSongs()));
    }

    public void addArtists(Set<Artist> artists){
        for(Artist artist : artists){
            addArtist(artist);
        }
    }

    /**
     * Adds all albums and songs of all artists into the library
     */
    public void initialize(){
        for(Map.Entry<String, Artist> artist : artists.entrySet()){
            for(Album album : artist.getValue().getAlbums()){
                albums.add(album);
                for(Song song : album.getSongs()){
                    songs.add(song);
                }
            }
        }
        initialized = true;
    }

    public Set<Song> getSongs(){
        if(!initialized){
            throw new IllegalStateException("Library is uninitialized");
        }
        return songs;
    }
    
    /**
     * Deletes all songs with deleted=true from local disk.
     * Removes all album and artist directories that end up empty.
     * @return
     * @throws IOException
     */
    public boolean deleteDeleted() throws IOException{
        if(!initialized){
            throw new IllegalStateException("Library is uninitialized");
        }
        for(Song song : songs){
            if(song.isDeleted()){
                song.delete();
            }
        }
        return false;
    }

    /**
     * Marks all songs by artist with deleted=true. Use deleteDeleted() to process deletions.
     * @param artist
     */
    public void deleteArtist(Artist artist){
        for(Album album : artist.getAlbums()){
            deleteAlbum(album);
        }
    }

    /**
     * Marks all songs in album with deleted=true. Use deleteDeleted() to process deletions.
     * @param album
     */
    public void deleteAlbum(Album album){
        for(Song song : album.getSongs()){
            deleteSong(song);
        }
    }

    /**
     * Marks a song with deleted=true. Use deleteDeleted() to process deletions.
     * @param song
     */
    public void deleteSong(Song song){
        song.setDeleted(true);
    } 

    public Set<Album> getAlbums() {
        if(!initialized){
            throw new IllegalStateException("Library is uninitialized");
        }
        return this.albums;
    }


    public Map<String, Artist> getArtists() {
        return this.artists;
    }

    public String getLocalMusicLibraryPath() {
        return this.localMusicLibraryPath;
    }

    public void setLocalMusicLibraryPath(String localMusicLibraryPath) {
        this.localMusicLibraryPath = localMusicLibraryPath;
    }

    /**
     * If library is not initialized, albums or songs lists wont work as they should.
     * @return
     */
    public boolean isInitialized(){
        return initialized;
    }

}
