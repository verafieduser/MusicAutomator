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

    @EmbeddedId 
    private SongId id;

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
        id = new SongId(); 
        id.setTitle(title);
        id.setAlbum(album);
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

    public SongId getId() { return id; } 
    public void setId( SongId id ) { this.id = id; }

    public String getTitle() { return this.id.getTitle(); }
    public void setTitle(String title) { this.id.setTitle(title); }

    public Album getAlbum() { return this.id.getAlbum(); }
    public void setAlbum(Album album) { this.id.setAlbum(album); }

    public boolean getDeleted() { return this.deleted; }
    public boolean isDeleted() { return this.deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public void setPath(String path) { this.path = path; }
    public String getPath() { return path; }

    @Override
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        Song otherSong = (Song) other;
        return id.equals(otherSong.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


    @Override
    public String toString() {
        return "{" +
            // " id='" + getId() + "'" +
            " title='" + getTitle() + "'" +
            ", album='" + getAlbum().getName() + "'" +
            ", artist='" + getAlbum().getArtist().getName() + "'" +
            ", path='" + getPath() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }

}
