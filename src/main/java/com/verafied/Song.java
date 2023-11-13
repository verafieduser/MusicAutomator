package com.verafied;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class Song {
    private SongTitle title;
    private Album album;
    private Artist artist;
    private File path;
    private boolean deleted;

    // TODO: factory pattern

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
        this.title = new SongTitle(title);
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

    public String getTitle() {
        return this.title.getTitle();
    }

    public void setTitle(String title) {
        this.title = new SongTitle(title);
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

    public boolean getDeleted() {
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

    @Override
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        Song otherSong = (Song) other;

        return title.equals(otherSong.title) &&
                album.equals(otherSong.album);
    }

    @Override
    public int hashCode() {
        return title.hashCode() * album.hashCode();
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + title.getID() + "'" +
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
