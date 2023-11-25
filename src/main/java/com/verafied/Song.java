package com.verafied;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import jakarta.persistence.*;


@Entity
@Table(name = "SONG")
public class Song {
    // @Id 
    // @Column(name = "id")
    // private String id;

    @Id 
    @Column
    private String title;

    @Id
    @ManyToOne(cascade=CascadeType.ALL)
    @PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name="album_name", referencedColumnName="name"),
        @PrimaryKeyJoinColumn(name = "album_artist", referencedColumnName ="artist")
    })
    private Album album;

    @Column
    private String path;

    @Column
    private boolean deleted;

    public Song(){
        super();
    }
    public Song(Album album, String title) {
        this(album, title, "", false); 
    }

    public Song(Album album, String title, String path, boolean deleted) {
        if (path.isEmpty()) {
            this.path = null;
        } else {
            this.path = path;
        }
        //this.artist = artist;
        this.album = album;
        //this.id = new SongTitle(title).getID();
        this.title = title;
        this.deleted = deleted;
    }

    public boolean delete() throws IOException {
        if (path == null) {
            return false;
        }
        deleted = true;
        Files.delete(new File(path).toPath());
        File album = new File(path).getParentFile();
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
            pathStr = new File(path).getAbsolutePath();
        }
        return pathStr;
    }

    // public String getId(){
    //     return this.id;
    // }

    // public void setId(String id){
    //     this.id = id;
    // }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title ;
    }

    public Album getAlbum() {
        return this.album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    // public Artist getArtist() {
    //     return this.artist;
    // }

    // public void setArtist(Artist artist) {
    //     this.artist = artist;
    // }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
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

        return new SongTitle(title).equals(new SongTitle(otherSong.title)) &&
                album.equals(otherSong.album);
    }

    @Override
    public int hashCode() {
        return title.hashCode() * album.hashCode();
    }


    @Override
    public String toString() {
        return "{" +
            // " id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", album='" + album + "'" +
            // ", artist='" + getAlbum().getArtist().getName() + "'" +
            ", path='" + getPath() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }

}
