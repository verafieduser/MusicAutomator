import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class that helps perform changes to the library
 */
public class Library {
    
    private Set<Song> songs; 
    private Set<Album> albums;
    private Set<Artist> artists;
    private String localMusicLibraryPath;

    public Library(){
        localMusicLibraryPath = "Library\\db.csv";
    }

    public Library(String localMusicLibraryPath){
        this.localMusicLibraryPath = localMusicLibraryPath;
    }

    public void addArtist(Artist artist){
        Set<Album> artistAlbums = artist.getAlbums();
        if(artistAlbums.isEmpty()){
            throw new IllegalArgumentException("Artists much have atleast one instance of albums with (a) song(s)");
        }
        artists.add(artist);
        artistAlbums.addAll(artist.getAlbums());
        for(Album album : artistAlbums){
            songs.addAll(album.getSongs());
        }
    }

    public void addArtists(Set<Artist> artists){
        for(Artist artist : artists){
            addArtist(artist);
        }
    }

    public void initialize(){
        for(Artist artist : artists){
            for(Album album : artist.getAlbums()){
                albums.add(album);
                for(Song song : album.getSongs()){
                    songs.add(song);
                }
            }
        }
    }

    public Set<Song> getSongs(){
        return songs;
    }
    
    public boolean deleteDeleted(){
        for(Song song : songs){
            if(song.isDeleted()){
                String dir = localMusicLibraryPath+"\\"+song.getArtist()+"\\"+song.getAlbum();
                deleteSong(dir, song);
            }
        }
        return false;
    }

    public boolean deleteArtist(String dir, Artist artist){
        for(Album album : artist.getAlbums()){
            deleteAlbum(dir+"\\"+artist.getName(), album);
        }
        return false;
    }

    public boolean deleteAlbum(String dir, Album album){
        for(Song song : album.getSongs()){
            deleteSong(dir+"\\"+album.getName(), song);
        }
        return false;
    }

    //TODO: figure out what to do with file extensions?
    public boolean deleteSong(String dir, Song song){

        //delete Dir
        //if album ends up empty, delete, if artist ends up empty, delete
        return false;
    } 

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }

    public Set<Album> getAlbums() {
        return this.albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

    public Set<Artist> getArtists() {
        return this.artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }

    public String getLocalMusicLibraryPath() {
        return this.localMusicLibraryPath;
    }

    public void setLocalMusicLibraryPath(String localMusicLibraryPath) {
        this.localMusicLibraryPath = localMusicLibraryPath;
    }


}
