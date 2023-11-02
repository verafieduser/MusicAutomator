package com.musicautomator;


public abstract class Metadata {

    protected String tag;
    protected String songname;
    protected String artist;
    protected String album;
    protected short year;
    protected String comment;

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSongname() {
        return this.songname;
    }

    public void setSongName(String songName) {
        this.songname = songName;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public short getYear() {
        return this.year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
