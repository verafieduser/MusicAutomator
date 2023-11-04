package com.musicautomator;

import java.io.IOException;
import java.nio.file.Path;

import com.mpatric.mp3agic.ID3Wrapper;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class Mp3Metadata extends Metadata {

    public Mp3Metadata(Path songPath) throws UnsupportedTagException, InvalidDataException, IOException {
        Mp3File mp3 = new Mp3File(songPath);
        ID3v1 v1 = mp3.getId3v1Tag();
        ID3v2 v2 = mp3.getId3v2Tag();
        ID3Wrapper tag = new ID3Wrapper(v1, v2);
        artist = tag.getAlbumArtist();
        songname = tag.getTitle();
        album = tag.getAlbum();
        year = Short.valueOf(tag.getYear());
        comment = tag.getComment();
    }


}
