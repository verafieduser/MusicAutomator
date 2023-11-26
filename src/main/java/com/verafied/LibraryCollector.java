package com.verafied;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Collects data on what music user listens to and creates a .csv with it using
 * librarysaver.
 */
public class LibraryCollector {

    private CsvHandler loader;
    private SessionFactory db;
    private boolean demo;

    public LibraryCollector(CsvHandler loader, SessionFactory db, boolean demo) {
        this.loader = loader;
        this.db = db;
        this.demo = demo;
    }

    /**
     * 
     * @param name   the filename of the .csv file that should be datamined for a
     *               library.
     * @param source how is the data structured in the .csv, which determines how to
     *               process it.
     * @throws IOException if the .csv cannot be opened or saved.
     */
    public void processCSV(String name, DataSource source) throws IOException {
        String path = "\\Library\\";
        if (demo) {
            path += "Demo\\";
        }
        String unprocessedPath = path + "Unprocessed\\" + name;
        List<List<String>> entries = loader.openCSV(unprocessedPath);

        Library library = new Library(db); // switch determines how it is written
        switch (source) {
            case BENBEN: // Artist, album, song, date (ONLY ONE ARTIST PER ALBUM)
                benbenCollection(library, entries);
                break;
            case LASTFM:
                break;
            case SPOTIFY:
                break;
            case TIDAL:
                break;
            default:
                break;
        }
        populateDatabase(library);
    }

    private void benbenCollection(Library library, List<List<String>> entries) {
        for (List<String> entry : entries) {
            String artist = entry.get(0);
            String album = entry.get(1);
            String song = entry.get(2);
            if (!songIsValid(artist, album, song)) {
                continue;
            }

            Artist newArtist = new Artist(artist, album, song, "", false);
            library.addArtist(newArtist);

        }
    }

    private void populateDatabase(Library library) {
        Transaction t = null;
        Collection<Artist> artists = library.getArtists().values();
        try (Session session = db.openSession()) {
            t = session.beginTransaction();
            for (Artist artist : artists) {
                session.merge(artist);
            }

            t.commit();
        } catch (Exception e) {
            if (t != null) {
                t.rollback();
            }
            e.printStackTrace();
        }
    }

    private boolean songIsValid(String artist, String album, String song) {
        boolean result = true;
        if (artist.isBlank() || album.isBlank() || song.isBlank()) {
            result = false;
        }
        return result;
    }
}
