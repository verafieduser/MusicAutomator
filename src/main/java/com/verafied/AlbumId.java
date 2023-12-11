package com.verafied;

import jakarta.persistence.*;

@Embeddable
public class AlbumId {
    
    @Column(name = "name")
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "artist", referencedColumnName = "artist_name")
    private Artist artist;

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public Artist getArtist() {return artist;}
    public void setArtist(Artist artist) {this.artist = artist;}

    @Override 
    public boolean equals(Object other){
        if(other == null || this.getClass() != other.getClass()){
            return false;
        }
        AlbumId otherAlbumId = (AlbumId) other;
        
        return 
            getName().equalsIgnoreCase(otherAlbumId.getName()) && 
            getArtist().equals(otherAlbumId.getArtist());
    }

    @Override 
    public int hashCode(){
        int hash = 7;
        hash *= 29 * getName().toLowerCase().hashCode();
        hash *= 31 * getArtist().hashCode();
        return hash;
    }
}
