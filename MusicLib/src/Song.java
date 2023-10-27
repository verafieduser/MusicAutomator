public class Song {
    private String title;
    private Album album;
    private Artist artist;
    private boolean missing;
    private boolean deleted;

    //TODO: factory pattern

    public Song(Artist artist, Album album, String title){
        this(artist, album, title, true, false);
    }

    public Song(Artist artist, Album album, String title, boolean missing, boolean deleted){
        this.artist = artist;
        this.album = album;
        this.title = title; 
        this.missing = missing;
        this.deleted = deleted;
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

    public boolean isMissing() {
        return this.missing;
    }

    public boolean getMissing() {
        return this.missing;
    }

    public void setMissing(boolean missing) {
        this.missing = missing;
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
        return "{" +
            " title='" + getTitle() + "'" +
            ", album='" + getAlbum() + "'" +
            ", artist='" + getArtist() + "'" +
            ", missing='" + isMissing() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }

    public String toCSV() {
        return getTitle() + "," + getAlbum() +
            "," + getArtist() + "," + isMissing() + "," + isDeleted();
    }

}
