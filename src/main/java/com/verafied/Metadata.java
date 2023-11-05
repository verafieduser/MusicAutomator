package com.verafied;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class Metadata {

    protected String songname;
    protected String artist;
    protected String album;
    protected short year;
    protected String comment;

    public Metadata(Path path) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException{
        if(!isSupportedSong(path)){
            throw new IllegalArgumentException();
        }
        AudioFile song = AudioFileIO.read(path.toFile());
        Tag tag = song.getTag();
        songname = tag.getFirst(FieldKey.TITLE);
        album = tag.getFirst(FieldKey.ALBUM);
        year = Short.valueOf(tag.getFirst(FieldKey.YEAR));
        comment = tag.getFirst(FieldKey.COMMENT);
        artist = tag.getFirst(FieldKey.ALBUM_ARTIST);
        if(artist == null || artist.isBlank()){
            artist = tag.getFirst(FieldKey.ARTIST);
        }

    }

    public static String getFileExtension(Path path) {
        String fileName = path.toString();
        int extensionIndex = fileName.lastIndexOf(".");
        return fileName.substring(extensionIndex + 1);
    }

    public static boolean isSupportedSong(Path path) {
        String fileExtension = getFileExtension(path);
        boolean result;
        try {
            Enum.valueOf(FileExtension.class, fileExtension.toUpperCase());
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public String getSongname() {
        return this.songname;
    }

    public void setSongName(String songName) {
        this.songname = songName;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public short getYear() {
        return this.year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
