package com.verafied;

import java.util.Objects;

import jakarta.persistence.*;

@Embeddable
public class SongId {
    
    @Column
    private String title;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "album_name",referencedColumnName = "name"),
        @JoinColumn(name = "album_artist", referencedColumnName = "artist")
    })
    private Album album;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }

    @Override
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        SongId otherSong = (SongId) other;

        return new SongTitle(getTitle()).equals(new SongTitle(otherSong.getTitle())) &&
                album.equals(otherSong.getAlbum());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash *= 29 * new SongTitle(getTitle()).hashCode();
        hash *= 31 * getAlbum().hashCode();
        return hash;
    }

    
}
