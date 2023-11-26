package com.verafied;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 * Class that helps perform changes to the library
 */
public class Library {

    private Set<Song> songs = new HashSet<>();
    private Set<Album> albums = new HashSet<>();
    private TreeMap<String, Artist> artists = new TreeMap<>();
    private String libraryDatabasePath;
    private SessionFactory db;
    private boolean initialized = false;

    public Library() {
        libraryDatabasePath = "Library\\db.csv";
    }

    public Library(String libraryDatabasePath) {
        this.libraryDatabasePath = libraryDatabasePath;
    }

    public Library(SessionFactory db){
        this.db = db;
    }

    /**
     * 
     * @param s a session with a transaction opened.
     * @param artist 
     * @param album
     * @param song
     */
    public void addEntryToDatabase(Session s, Artist artist, Album album, Song song){
        s.persist(artist);
        s.persist(album);
        s.persist(song);
    }

    public void addArtistToDatabase(Session s, Artist artist){
        s.persist("ARTIST", artist);
    }


    /**
     * Add artist to the library. If already present - merge contents of incoming
     * and previous artists.
     * 
     * @param artist
     */
    public void addArtist(Artist artist) {
        Set<Album> artistAlbums = artist.getAlbums();
        if (artistAlbums.isEmpty()) {
            throw new IllegalArgumentException("Artists much have atleast one instance of albums with (a) song(s)");
        }
        Artist oldArtist = artists.putIfAbsent(artist.getName().toLowerCase(), artist);
        if (oldArtist != null) { // If artist is already present, add new instance albums to old instance
            oldArtist.merge(artist);
        }
        Set<Album> newArtistAlbums = artists.get(artist.getName().toLowerCase()).getAlbums();
        albums.addAll(newArtistAlbums);
        newArtistAlbums.forEach(x -> songs.addAll(x.getSongs()));
        initialized = false;
    }

    public void addArtists(Set<Artist> artists) {
        for (Artist artist : artists) {
            addArtist(artist);
        }
        initialized = false;
    }

    /**
     * Adds all albums and songs of all artists into the library.
     * is done automatically each time you access the library.
     * However, the data you access gets out of date as soon as
     * you add new information into the library, and will need
     * to be re-accessed to ensure it is up-to-date.
     */
    public void initialize() {
        if (initialized) {
            return;
        }
        for (Map.Entry<String, Artist> artist : artists.entrySet()) {
            for (Album album : artist.getValue().getAlbums()) {
                albums.add(album);
                for (Song song : album.getSongs()) {
                    songs.add(song);
                }
            }
        }
        initialized = true;
    }

    /**
     * Returns all songs in the library. Data returned is out of date as soon as new
     * information is added into the library. Can be made up-to-date by re-accessing
     * it
     * through this method again.
     * 
     * @return a set of all songs in the library
     */
    public Set<Song> getSongs() {
        if (!initialized) {
            initialize();
        }
        return songs;
    }


    public Artist getArtist(String name){
        if(!initialized){
            initialize();
        }
        try (Session session = db.openSession()) {
            Query<Artist> query = session.createQuery("FROM Artist a JOIN FETCH a.albums as JOIN FETCH a.albums.songs s WHERE a.name = " + "\'" + name + "\'", Artist.class); 
            return query.list().get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Album> getAlbum(String name){
        try (Session session = db.openSession()) {
            Query<Album> query = session.createQuery("FROM Album a JOIN FETCH a.id.artist ar JOIN FETCH a.songs s " +  "WHERE a.id.name = " + "\'" + name + "\'", Album.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    } 

    public List<Song> getSong(String name){
        try (Session session = db.openSession()) {
            Query<Song> query = session.createQuery("FROM Song s " + "JOIN FETCH s.id.album a" + " WHERE s.id.title = " + "\'" + name + "\'" , Song.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    /**
     * Deletes all songs with deleted=true from local disk.
     * Removes all album and artist directories that end up empty.
     * 
     * @return
     * @throws IOException
     */
    public boolean deleteDeleted() throws IOException {
        if (!initialized) {
            initialize();
        }
        for (Song song : songs) {
            if (song.isDeleted()) {
                song.delete();
            }
        }
        return false;
    }

    /**
     * Marks all songs by artist with deleted=true. Use deleteDeleted() to process
     * deletions.
     * 
     * @param artist
     */
    public void deleteArtist(Artist artist) {
        if (!initialized) {
            initialize();
        }
        if(artist==null){
            return;
        }
        for (Album album : artist.getAlbums()) {
            deleteAlbum(album);
        }
    }

    /**
     * Marks all songs in album with deleted=true. Use deleteDeleted() to process
     * deletions.
     * 
     * @param album
     */
    public void deleteAlbum(Album album) {
        if (!initialized) {
            initialize();
        }
        if(album==null){
            return;
        }
        for (Song song : album.getSongs()) {
            deleteSong(song);
        }
    }

    /**
     * Marks a song with deleted=true. Use deleteDeleted() to process deletions.
     * 
     * @param song
     */
    public void deleteSong(Song song) {
        if (!initialized) {
            initialize();
        }
        if(song==null){
            return;
        }
        song.setDeleted(true);
    }

    /**
     * Returns all albums in the library. Data returned is out of date as soon as
     * new
     * information is added into the library. Can be made up-to-date by re-accessing
     * it
     * through this method again.
     * 
     * @return a set of all albums in the library
     */
    public Set<Album> getAlbums() {
        if (!initialized) {
            initialize();
        }
        return this.albums;
    }

    /**
     * Returns all artists in the library. Data returned is out of date as soon as
     * new
     * information is added into the library. Can be made up-to-date by re-accessing
     * it
     * through this method again.
     * 
     * @return a set of all artists in the library
     */
    public Map<String, Artist> getArtists() {
        if (!initialized) {
            initialize();
        }
        return this.artists;
    }

    public String getLocalMusicLibraryPath() {
        return this.libraryDatabasePath;
    }

    public void setLocalMusicLibraryPath(String libraryDatabasePath) {
        this.libraryDatabasePath = libraryDatabasePath;
    }
}
