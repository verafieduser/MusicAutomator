package com.verafied;

import jakarta.persistence.*;

@Embeddable
public class AlbumId {
    
    @Column
    private String name;
    
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "artist", referencedColumnName = "name")
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
        AlbumId otherAlbum = (AlbumId) other;
        
        return 
            getName().equalsIgnoreCase(otherAlbum.getName()) &&
            getArtist().equals(otherAlbum.getArtist());
    }

    @Override 
    public int hashCode(){
        return getName().hashCode() * getArtist().hashCode(); 
    }
}
