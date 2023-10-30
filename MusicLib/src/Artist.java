import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Artist {

    private String name;
    private Set<Album> albums = new HashSet<>();

    public Artist(String name, String album, String song, String path, boolean deleted) {
        this.name = name;
        albums.add(new Album(album, this, song, path, deleted));
    }

    public Artist(String name, String album, String song) {
        this.name = name;
        albums.add(new Album(album, this, song));
    }

    public void merge(Artist other) {
        if (!name.equals(other.getName())) {
            throw new IllegalArgumentException();
        }
        for (Album otherAlbum : other.getAlbums()) {
            if(!albums.add(otherAlbum)){ //if album was already present, merge:
                for(Album thisAlbum : albums){
                    if(thisAlbum.equals(otherAlbum)){
                        thisAlbum.merge(otherAlbum);
                        break;
                    }
                }
            }
        }

    }

    public void addAlbums(Set<Album> albums) {
        this.albums.addAll(albums);
    }

    public void addAlbum(String album, String song) {
        if (!albums.add(new Album(album, this, song))) {
            // if the album is already there, add song to it.
            for (Album oldAlbum : albums) {
                if (oldAlbum.getName().equals(album)) {
                    oldAlbum.addSong(song);
                }
            }
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
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        Artist otherArtist = (Artist) other;

        return name.equals(otherArtist.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String toCSV() {
        return name;
    }

    public String printAlbums() {
        StringBuilder sb = new StringBuilder();
        albums.forEach(x -> sb.append(x + "\n\t"));
        return sb.toString();
    }

    @Override
    public String toString() {
        return "{" +
                " name='" + getName() + "'" +
                ", albums='\n\t" + printAlbums() + "'" +
                "}";
    }

}
