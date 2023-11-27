package com.verafied;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
@Entity
@Table(name = "ARTIST")
public class Artist {

    @Id 
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy="id.artist", cascade=CascadeType.ALL)
    private Set<Album> albums;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    public Artist(){
        super();
    }

    public Artist(String name, String album, String song, String path, boolean deleted) {
        this.name = name;
        albums = new HashSet<>();
        albums.add(new Album(album, this, song, path, deleted));
    }

    public Artist(String name, String album, String song) {
        this.name = name;
        albums = new HashSet<>();
        albums.add(new Album(album, this, song));
    }

    public Artist(String name, boolean deleted){
        this.name = name;
        this.deleted = deleted;
    }

    public void merge(Artist other) {
        if (!name.equalsIgnoreCase(other.getName())) {
            throw new IllegalArgumentException();
        }
        for (Album otherAlbum : other.getAlbums()) {
            otherAlbum.setArtist(this);
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

    public void addAlbum(Album album){
        albums.add(album);
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


    public boolean isDeleted() {
        return this.deleted;
    }

    public boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
        this.albums.forEach(x -> x.setDeleted(deleted));
    }

    


    @Override
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        Artist otherArtist = (Artist) other;

        return name.equalsIgnoreCase(otherArtist.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String toCSV() {
        return name;
    }

    public String printAlbums() {
        StringBuilder sb = new StringBuilder("\n");
        albums.forEach(x -> sb.append("\t" + x.toString() + "\n"));
        return sb.toString();
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", deleted='" + isDeleted() + "'" +
            ", albums='" + printAlbums() + "'" +
            "}";
    }


}
