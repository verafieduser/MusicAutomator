package com.verafied;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "ALBUM")
public class Album {
    
    @Id
    @Column
    private String name;

    @Id
    @ManyToOne(cascade=CascadeType.ALL)
    private Artist artist;

    @OneToMany(mappedBy = "album_name", cascade=CascadeType.ALL)
    private Set<Song> songs = new HashSet<>();

    @Column
    private String bandcampLink;

    @Column(name = "deleted")
    private boolean deleted;

    public Album(){
        super();
    }

    public Album(String name, Artist artist, boolean deleted){
        this.name = name;
        this.artist = artist;
        this.deleted = deleted;
    }
    public Album(String name, Artist artist, String song, String path, boolean deleted){
        this.name = name;
        this.artist = artist;
        bandcampLink = "https://" + toUrl(artist.getName().replaceAll("[ö]", "o")) + ".bandcamp.com/album/" + toUrl(name.replaceAll("[\\s]", "-"));
        songs.add(new Song(/*artist,*/ this, song, path, deleted));
    }

    private String toUrl(String comp){
        return comp.replaceAll("[^\\w-]", "").toLowerCase();
    }

    public Album(String name, Artist artist, String song){
        this.name = name;
        this.artist = artist;
        songs.add(new Song(this, song));
    }

    public void merge(Album other){
        if(!name.equalsIgnoreCase(other.getName())){
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

    public String getName(){
        return name;
    }

    public Artist getArtist() {
        return this.artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
        //songs.forEach(x -> x.setArtist(artist));
    }

    public Set<Song> getSongs() {
        return this.songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getBandcampLink() {
        return this.bandcampLink;
    }

    public void setBandcampLink(String bandcampLink) {
        this.bandcampLink = bandcampLink;
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
        Album otherAlbum = (Album) other;
        
        return 
            name.equalsIgnoreCase(otherAlbum.name) &&
            artist.equals(otherAlbum.artist);
    }

    @Override 
    public int hashCode(){
        return name.hashCode() * artist.hashCode();
    }

    public String toCSV() {
        return name;
    }

    public String printSongs() {
        StringBuilder sb = new StringBuilder();
        songs.forEach(song -> sb.append(song.getTitle()+ "\n\t\t"));
        return sb.toString();
    }

    @Override
    public String toString() {
        return "{" +
            ", name='" + getName() + "'" +
            ", artist='" + artist.getName() + "'" +
            ", bandcampLink='" + getBandcampLink() + "'" +
            ", songs='" + getSongs() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
    
}
