package com.verafied;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

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
        File path = new File(localMusicLibraryPath);
        for (File artist : path.listFiles()) {
            if (!artist.isDirectory()) {
                continue;
            }
            for (File album : artist.listFiles()) {

                if (!album.isDirectory()) {
                    continue;
                }
                for (File song : album.listFiles()) {
                    Metadata metadata;
                    try {
                        metadata = getMetadata(Paths.get(song.getPath()));
                    } catch (Exception e) {
                        continue;
                    }
                    addSong(library, metadata, artist, album, song);

                }
            }
        }
    }

    private void addSong(Library library, Metadata metadata, File artist, File album, File song) {
        String songName = metadata.getSongname();
        if (songName != null && metadata.getArtist().equalsIgnoreCase(artist.getName())
                && metadata.getAlbum().equalsIgnoreCase(album.getName())) {
            library.addArtist(
                    new Artist(artist.getName(),
                            album.getName(),
                            songName,
                            song.getAbsolutePath(),
                            false));
        }
    }


    private Metadata getMetadata(Path songPath) throws Exception {
        return new Metadata(songPath);
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

    private boolean songsMatch(Metadata candidate, Song song) {
        boolean result;
        if (song.getArtist().getName().equalsIgnoreCase(candidate.getArtist()) &&
                song.getAlbum().getName().equalsIgnoreCase(candidate.getAlbum()) &&
                song.getTitle().equalsIgnoreCase(candidate.getSongname())) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    /**
     * Connects songs that arent connected with a local file with a local file if
     * available
     * //TODO: refactor so that it takes a set of songs that is supposed to be
     * connected?
     * 
     * @param library the library containing all music in the database that the
     *                local files should be
     *                compared to.
     * @throws FileNotFoundException
     */
    public void connectMissing(Library library) throws FileNotFoundException {
        for (Song song : library.getSongs()) {
            Path albumPath = getAlbumPath(song);
            if (albumPath == null || song.getDeleted() || song.getPath() != null || !Files.exists(albumPath)) {
                continue;
            }

            for (File candidateMatch : albumPath.toFile().listFiles()) {
                Metadata metadata;
                try {
                    metadata = getMetadata(candidateMatch.toPath());
                } catch (Exception e) {
                    continue;
                }
                if (songsMatch(metadata, song)) {
                    song.setPath(candidateMatch.getAbsolutePath());
                }

            }

        }
        saver.writeToCSV(library);
    }

    private Path getAlbumPath(Song song) {
        Path albumPath = Paths.get(localMusicLibraryPath);
        String regex = "^[\\s.]+|[^.\\w\\s]";
        String artist = song.getArtist().getName().replaceAll(regex, "").trim();
        String album = song.getAlbum().getName().replaceAll(regex, "").trim();
        if(artist.isEmpty()){
            artist="Other";
        } 
        if(album.isEmpty()){
            album="Other";
        }
        try {
            albumPath = albumPath.resolve(artist);
            albumPath = albumPath.resolve(album);
        } catch (InvalidPathException e) {
            e.printStackTrace();
            System.err.println(albumPath);
        }
    
        return albumPath;
    }
}
