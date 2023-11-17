package com.verafied;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import jakarta.persistence.*;


@Entity
@Table(name = "SONG")
public class Song {
    @Id @GeneratedValue 
    @Column(name = "id")
    private String id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="song_album", referencedColumnName="name"),
        @JoinColumn(name="song_artist", referencedColumnName="album_artist")
    })
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable=false, updatable=false)
    private Artist artist;

    @Column(name = "PATH")
    private File path;

    @Column(name = "DELETED")
    private boolean deleted;

    public Song(Artist artist, Album album, String title) {
        this(artist, album, title, "", false);
    }

    public Song(Artist artist, Album album, String title, String path, boolean deleted) {
        if (path.isEmpty()) {
            this.path = null;
        } else {
            this.path = new File(path);
        }
        this.artist = artist;
        this.album = album;
        this.id = new SongTitle(title).getID();
        this.title = title;
        this.deleted = deleted;
    }

    public boolean delete() throws IOException {
        if (path == null) {
            return false;
        }
        deleted = true;
        Files.delete(path.toPath());
        File album = path.getParentFile();
        File[] contents = album.listFiles(x -> Metadata.isSupportedSong(x.toPath()));
        if (contents.length == 0) {
            Arrays.asList(contents).forEach(x -> {
                try {
                    Files.delete(x.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Files.delete(album.toPath());
            File artistFile = album.getParentFile();
            File[] remainingAlbums = artistFile.listFiles();
            if (remainingAlbums.length == 0) {
                Files.delete(artistFile.toPath());
            }
        }

        return true;
    }

    private String pathGetter() {
        String pathStr = "";
        if (path != null) {
            pathStr = path.getAbsolutePath();
        }
        return pathStr;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = new SongTitle(title).toString();
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

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setPath(String path) {
        this.path = new File(path);
    }

    public File getPath() {
        return path;
    }


    public boolean getDeleted() {
        return this.deleted;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        Song otherSong = (Song) other;

        return id.equals(otherSong.id) &&
                album.equals(otherSong.album);
    }

    @Override
    public int hashCode() {
        return title.hashCode() * album.hashCode();
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + id + "'" +
                " title='" + getTitle() + "'" +
                ", artist='" + artist.getName() + "'" +
                ", album='" + album.getName() + "'" +
                ", path='" + pathGetter() + "'" +
                ", deleted='" + isDeleted() + "'" +
                "}";
    }

    public String toCSV() {
        return getArtist().toCSV() + "," + getAlbum().toCSV() +
                "," + getTitle() + "," + pathGetter() + "," + isDeleted();
    }

}
