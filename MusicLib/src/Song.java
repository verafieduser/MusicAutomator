import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Song {
    private String title;
    private Album album;
    private Artist artist;
    private File path;
    private boolean deleted;

    //TODO: factory pattern

    public Song(Artist artist, Album album, String title){
        this(artist, album, title, null, false);
    }

    public Song(Artist artist, Album album, String title, File path, boolean deleted){
        this.artist = artist;
        this.album = album;
        this.title = title; 
        this.path = path;
        this.deleted = deleted;
    }

    public boolean delete() throws IOException{
        if(path==null){
            return false;
        }
        Files.delete(path.toPath());
        File album = path.getParentFile();
        File[] remainingSongs = album.listFiles();
        if(remainingSongs.length==0){
            Files.delete(album.toPath());
            File artistFile = album.getParentFile();
            File[] remainingAlbums = artistFile.listFiles();
            if(remainingAlbums.length==0){
                Files.delete(artistFile.toPath());
            }
        }

        return true;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Album getAlbum() {
        return this.album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Artist getArtist() {
        return this.artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setPath(String path){
        this.path = new File(path);
    }

    @Override
    public boolean equals(Object other){
        if(other == null || this.getClass() != other.getClass()){
            return false;
        }
        Song otherSong = (Song) other;
        
        return 
            title.equals(otherSong.title) &&
            album.equals(otherSong.album);
    }

    @Override 
    public int hashCode(){
        return title.hashCode() * album.hashCode();
    }


    @Override
    public String toString() {
        String pathStr = "";
        if(path!=null){
            pathStr = path.getAbsolutePath();
        }
        return "{" +
            " title='" + getTitle() + "'" +
            ", path='" + pathStr + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }

    public String toCSV() {
        return getArtist().toCSV() + "," + getAlbum().toCSV() +
            "," + getTitle() + "," + path.getAbsolutePath() + "," + isDeleted();
    }

}
