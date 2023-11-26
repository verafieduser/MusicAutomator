package com.verafied;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Hibernate;

import jakarta.persistence.*;

@Entity
@Table(name = "ALBUM")
public class Album {
    
    @EmbeddedId 
    private AlbumId id; 

    @OneToMany(mappedBy = "id.album", cascade=CascadeType.ALL)
    private Set<Song> songs = new HashSet<>();

    @Column
    private String bandcampLink;

    @Column(name = "deleted")
    private boolean deleted;

    public Album(){
        super();
    }

    public Album(String name, Artist artist, boolean deleted){
        id = new AlbumId();
        id.setName(name);
        id.setArtist(artist);
        this.deleted = deleted;
    }
    public Album(String name, Artist artist, String song, String path, boolean deleted){
        id = new AlbumId();
        id.setArtist(artist);
        id.setName(name);
        bandcampLink = "https://" + toUrl(artist.getName().replaceAll("[ö]", "o")) + ".bandcamp.com/album/" + toUrl(name.replaceAll("[\\s]", "-"));
        songs.add(new Song(/*artist,*/ this, song, path, deleted));
    }

    private String toUrl(String comp){
        return comp.replaceAll("[^\\w-]", "").toLowerCase();
    }

    public Album(String name, Artist artist, String song){
        id = new AlbumId();
        id.setName(name);
        id.setArtist(artist);
        songs.add(new Song(this, song));
    }

    public void merge(Album other){
        if(!id.getName().equalsIgnoreCase(other.getName())){
            throw new IllegalArgumentException();
        }

        //Ensuring that path is transferred over if other has a path
        for(Song otherSong : other.getSongs()){
            if(otherSong.getPath()==null){
                continue;
            }
            for (Song currentSong : songs){
                if (otherSong.equals(currentSong)){
                    currentSong.setPath(new File(otherSong.getPath()).getAbsolutePath());
                } 
            }
        }        
        songs.addAll(other.getSongs());
    }

    public void addSong(String song){
        songs.add(new Song(this, song));

    }

    public void addSong(Song song){
        this.songs.add(song);
        song.setAlbum(this);
    }

    public AlbumId getId() {return id; }
    public void setId(AlbumId id) {this.id = id;}

    public String getName() { return this.id.getName(); }
    public void setName(String name) { this.id.setName(name);}

    public Artist getArtist() { return this.id.getArtist(); }
    public void setArtist(Artist artist) {this.id.setArtist(artist); }

    public Set<Song> getSongs() { return this.songs; }
    public void setSongs(Set<Song> songs) {this.songs = songs;}

    public String getBandcampLink() { return this.bandcampLink; }
    public void setBandcampLink(String bandcampLink) { this.bandcampLink = bandcampLink; }

    public boolean isDeleted() { return this.deleted; }
    public boolean getDeleted() { return this.deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    
    @Override 
    public boolean equals(Object other){
        if(other == null || this.getClass() != other.getClass()){
            return false;
        }
        Album otherAlbum = (Album) other;
        return id.equals(otherAlbum.getId());
    }

    @Override 
    public int hashCode(){
        return id.hashCode(); 
    }

    public String printSongs() {
        StringBuilder sb = new StringBuilder("\n");
        for(Song song : getSongs()){
            sb.append("\t\t" + song.getTitle() + "\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", artist='" + getArtist().getName() + "'" +
            ", bandcampLink='" + getBandcampLink() + "'" +
            ", songs='" + printSongs() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
    
}
