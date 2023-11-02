package com.musicautomator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

/**
 * Compares local music with library .csv. For each song that is local but not
 * in library, add to library.
 * For each song in the library not available locally, collect for download.
 */
public class MissingMusic {
    private String localMusicLibraryPath;
    private LibrarySaver saver;

    public MissingMusic(LibrarySaver saver, String localMusicLibraryPath) {
        this.saver = saver;
        this.localMusicLibraryPath = localMusicLibraryPath;
    }

    /**
     * Search through local music folder and add all songs that are present that
     * aren't in the library. If song metadata does not match directory structure
     * it is not added and will count as faulty data. This will reduce the risk of
     * faulty data being entered into the database.
     * 
     * @param library library that missing songs are to be added into.
     */
    public void findLocalSongs(Library library) {
        File path = new File(localMusicLibraryPath);;
        for (File artist : path.listFiles()) {
            for (File album : path.listFiles()) {
                for (File song : album.listFiles()) {
                    Metadata metadata;
                    try {
                        metadata = getMetadata(song.getAbsolutePath());
                    } catch (IOException | UnsupportedTagException | InvalidDataException e) {
                        // if unsupported file, skip!
                        e.printStackTrace();
                        continue;
                    }

                    String songName = metadata.getSongname();
                    if (songName == null || metadata.getArtist().equals(artist.getName())
                            || metadata.getAlbum().equals(album.getName())) {
                        // If song does not have metadata that follows file structure it cannot be
                        // trusted to work and is therefore skipped.
                        continue;
                    }
                    library.addArtist(
                            new Artist(artist.getName(), album.getName(), songName, song.getAbsolutePath(), false));
                }
            }
        }
        // TODO: figure out how to deal with faulty data in user library
    }

    private Metadata getMetadata(String songPath) throws IOException, UnsupportedTagException, InvalidDataException {
        String songExtension = songPath; // make sure you only get file extension!
        FileExtension type = Enum.valueOf(FileExtension.class, songExtension);
        Metadata data;
        switch (type) {
            case MP3:
                data = new Mp3Metadata(songPath);
                break;

            default:
                throw new IOException("File did not have supported file extension");
        }
        return data;
    }

    /**
     * Finds all songs that are not represented in the local music library and
     * returns a list of them
     * 
     * @param entries
     */
    public Set<Song> findMissing(Library library) {
        Set<Song> missingSongs = new HashSet<>();
        for (Song song : library.getSongs()) {
            if (song.getDeleted() || song.getPath() != null) {
                continue;
            }
            missingSongs.add(song);
        }
        return missingSongs;
    }

    /**
     * Connects songs that arent connected with a local file with a local file if
     * available
     * 
     * @param library the library containing all music in the database that the
     *                local files should be
     *                compared to.
     * @throws FileNotFoundException
     */
    public void connectMissing(Library library) throws FileNotFoundException {
        for (Song song : library.getSongs()) {
            if (song.getDeleted() || song.getPath() != null) {
                continue;
            }

            StringBuilder sb = new StringBuilder(localMusicLibraryPath);
            sb.append("\\" + song.getArtist()); // artist folder
            sb.append("\\" + song.getAlbum()); // album folder
            File dir = new File(sb.toString().toLowerCase());
            String[] localSongs = dir.list();
            for (String localName : localSongs) {
                String localNameNoExtension = localName.substring(0, localName.lastIndexOf("."));
                if (localNameNoExtension.equalsIgnoreCase(song.getTitle())) {
                    song.setPath(dir.getAbsolutePath() + "\\" + localName);
                }
            }
        }
        saver.writeToCSV(library);
    }
}
