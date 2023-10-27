import java.util.HashSet;
import java.util.Set;

public class Album {
    
    private String name;
    private Artist artist;
    private Set<Song> songs = new HashSet<>();

    public Album(String name, Artist artist, String song, boolean missing, boolean deleted){
        this.name = name;
        this.artist = artist;
        songs.add(new Song(artist, this, song, missing, deleted));
    }

    public Album(String name, Artist artist, String song){
        this.name = name;
        this.artist = artist;
        songs.add(new Song(artist, this, song));
    }

    public String getName(){
        return name;
    }

    public Artist getArtist() {
        return this.artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Set<Song> getSongs() {
        return this.songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }

    @Override 
    public boolean equals(Object other){
        if(other == null || this.getClass() != other.getClass()){
            return false;
        }
        Album otherAlbum = (Album) other;
        
        return 
            name.equals(otherAlbum.name) &&
            artist.equals(otherAlbum.artist);
    }

    @Override 
    public int hashCode(){
        return name.hashCode() * artist.hashCode();
    }

    
}
