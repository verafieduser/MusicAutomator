import java.util.HashSet;
import java.util.Set;

public class Artist {
    
    private String name; 
    private Set<Album> albums = new HashSet<>();

    public Artist(String name, String album, String song, boolean missing, boolean deleted){
        this.name = name;
        albums.add(new Album(album, this, song, missing, deleted));
    }

    public Artist(String name, String album, String song){
        this.name = name;
        albums.add(new Album(album, this, song));
    }

    public void addAlbum(String album, String song){
        if(!albums.add(new Album(album, this, song))){

        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Album> getAlbums() {
        return this.albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

    @Override 
    public boolean equals(Object other){
        if(other == null || this.getClass() != other.getClass()){
            return false;
        }
        Artist otherArtist = (Artist) other;
            
        return 
            name.equals(otherArtist.name);
        }

    @Override 
    public int hashCode(){
        return name.hashCode();
    }

}
