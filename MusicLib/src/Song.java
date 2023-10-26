public class Song {
    private String title;
    private String album;
    private String artist;
    private boolean missing;
    private boolean deleted;

    //TODO: factory pattern

    public Song(String artist, String album, String title){
        this(artist, album, title, true, false);
    }

    public Song(String artist, String album, String title, boolean missing, boolean deleted){
        this.artist = artist;
        this.album = album;
        this.title = title; 
        this.missing = missing;
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
            album.equals(otherSong.album) &&
            artist.equals(otherSong.artist);

    }

    @Override 
    public int hashCode(){
        return 0;
    }
}
