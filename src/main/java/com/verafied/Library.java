package com.verafied;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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
    /**
     * Artists containing albums containing songs to be added to the database.
     */
    private TreeMap<String, Artist> toBeAdded = new TreeMap<>();
    private String libraryDatabasePath;
    private SessionFactory db;
    private boolean initialized = false;
    private boolean toBePopulated = false;

    public Library(SessionFactory db){
        this.db = db;
    }

    // /**
    //  * 
    //  * @param s a session with a transaction opened.
    //  * @param artist 
    //  * @param album
    //  * @param song
    //  */
    // public void addEntryToDatabase(Session s, Artist artist, Album album, Song song){
    //     s.persist(artist);
    //     s.persist(album);
    //     s.persist(song);
    //     toBePopulated = true; 
    // } 

    // public void addArtistToDatabase(Session s, Artist artist){
    //     s.persist("ARTIST", artist);
    //     toBePopulated = true;
    // }


    /**
     * Add artist to the library. If already present - merge contents of incoming
     * and previous toBeAdded.
     * 
     * @param artist
     */
    public void addArtistToBeAdded(Artist artist) {
        Set<Album> artistAlbums = artist.getAlbums();
        if (artistAlbums.isEmpty()) {
            throw new IllegalArgumentException("Artists much have atleast one instance of albums with (a) song(s)");
        }
        Artist oldArtist = toBeAdded.putIfAbsent(artist.getName().toLowerCase(), artist);
        if (oldArtist != null) { // If artist is already present, add new instance albums to old instance
            oldArtist.merge(artist);
        }
        Set<Album> newArtistAlbums = toBeAdded.get(artist.getName().toLowerCase()).getAlbums();
        albums.addAll(newArtistAlbums);
        newArtistAlbums.forEach(x -> songs.addAll(x.getSongs()));
        toBePopulated = true;
    }

    public void addArtistsToBeAdded(Set<Artist> toBeAdded) {
        for (Artist artist : toBeAdded) {
            addArtistToBeAdded(artist);
        }
        toBePopulated = false;
    }


    public void populateDatabase() {
        Transaction t = null;
        Collection<Artist> artists = getToBeAdded().values(); 
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
        toBeAdded.clear();
        toBePopulated=false;
    }

    /**
     * Adds all albums and songs of all artists IN MEMORY into the library.
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

    public List<Song> getSong(String name){
        if (toBePopulated){
            populateDatabase();
        }
        try (Session session = db.openSession()) {
            Query<Song> query = session.createQuery("FROM Song s " + "JOIN FETCH s.id.album a " + "WHERE s.id.title = " + "\'" + name + "\'" , Song.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Returns all songs in the IN MEMORY library. Data returned is out of date as soon as new
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

    /**
     * Returns all songs in the IN DATABASE library.
     * @param whereClause
     * @return
     */
    public List<Song> getSongs(String whereClause){
        if (toBePopulated){
            populateDatabase();
        }
        try (Session session = db.openSession()) {
            Query<Song> query = session.createQuery("FROM Song s " + "JOIN FETCH s.id.album a " + whereClause, Song.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }


    public List<Artist> getArtist(String name){
        if (toBePopulated){
            populateDatabase();
        }
        try (Session session = db.openSession()) {
            Query<Artist> query = session.createQuery("FROM Artist a JOIN FETCH a.albums as JOIN FETCH a.albums.songs s WHERE a.name = " + "\'" + name + "\'", Artist.class); 
            List<Artist> result = query.list();
            if(result.isEmpty()){
                return List.of();
            } else {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Returns all toBeAdded in the library. Data returned is out of date as soon as
     * new
     * information is added into the library. Can be made up-to-date by re-accessing
     * it
     * through this method again.
     * 
     * @return a set of all toBeAdded in the library
     */
    public Map<String, Artist> getArtists() {
        if (!initialized) {
            initialize();
        }
        return this.artists;
    }

    /**
     * Returns all artists in the IN DATABASE library.
     * @param whereClause
     * @return
     */
    public List<Artist> getArtists(String whereClause){
        if (toBePopulated){
            populateDatabase();
        }
        try (Session session = db.openSession()) {
            Query<Artist> query = session.createQuery("FROM Artist a " + "JOIN FETCH a.albums as JOIN FETCH a.albums.songs s " + whereClause, Artist.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Album> getAlbum(String name){
        if (toBePopulated){
            populateDatabase();
        }
        try (Session session = db.openSession()) {
            Query<Album> query = session.createQuery("FROM Album a JOIN FETCH a.id.artist ar JOIN FETCH a.songs s " +  "WHERE a.id.name = " + "\'" + name + "\'", Album.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
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
     * Returns all album in the IN DATABASE library.
     * @param whereClause
     * @return
     */
    public List<Album> getAlbums(String whereClause){
        if (toBePopulated){
            populateDatabase();
        }
        try (Session session = db.openSession()) {
            Query<Album> query = session.createQuery("FROM Album a JOIN FETCH a.id.artist ar JOIN FETCH a.songs s " + whereClause, Album.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public Map<String, Artist> getToBeAdded(){ return this.toBeAdded; };


    /**
     * Deletes all songs with deleted=true from local disk.
     * Removes all album and artist directories that end up empty.
     * 
     * @return
     * @throws IOException
     */
    public void deleteDeleted() throws IOException {
        if (toBePopulated){
            populateDatabase();
        }
        if (!initialized) {
            initialize();
        }
        List<Song> result;
        try (Session session = db.openSession()) {
            Query<Song> query = session.createQuery("FROM Song s WHERE s.deleted = " + "\'" + "true" + "\'" , Song.class);
            result = query.list();
        } catch (Exception e) {
            e.printStackTrace();
            result = List.of();
        }
        for (Song song : result) {
            if (song.isDeleted()) {
                song.delete();
            }
        }
    }

    /**
     * Marks all songs by artist with deleted=true. Use deleteDeleted() to process
     * deletions.
     * 
     * @param artist
     */
    public void deleteArtist(Artist artist) {
        if (toBePopulated){
            populateDatabase();
        }
        if (!initialized) {
            initialize();
        }
        if(artist==null){
            return;
        }
        for (Album album : artist.getAlbums()) {
            deleteAlbum(album);
        }
        artist.setDeleted(true);
        update(artist);
    }

    /**
     * Marks all songs in album with deleted=true. Use deleteDeleted() to process
     * deletions.
     * 
     * @param album
     */
    public void deleteAlbum(Album album) {
        if (toBePopulated){
            populateDatabase();
        }
        if (!initialized) {
            initialize();
        }
        if(album==null){
            return;
        }
        for (Song song : album.getSongs()) {
            deleteSong(song);
        }
        album.setDeleted(true);
        update(album);
    }

    /**
     * Marks a song with deleted=true. Use deleteDeleted() to process deletions.
     * 
     * @param song
     */
    public void deleteSong(Song song) {
        if (toBePopulated){
            populateDatabase();
        }
        if (!initialized) {
            initialize();
        }
        if(song==null){
            return;
        }
        song.setDeleted(true);
        update(song);
    }

    public void restoreDeletedAll() {
        List<Artist> artists = getArtists("WHERE deleted = true");
        for(Artist artist : artists){
            artist.setDeleted(false);
            update(artist);
        }
    }

    public void restoreArtist(Artist artist){
        artist.setDeleted(false);
        update(artist);
    }

    public void restoreAlbum(Album album) {
        album.setDeleted(false);
        update(album);
    }

    public void restoreSong(Song song){
        song.setDeleted(false);
        update(song);
    }

    public void clearDatabase(){
        try (Session session = db.openSession()) {
            Transaction transaction = session.beginTransaction();
            List<Object> all = session.createQuery("from java.lang.Object", Object.class).list();
            Iterator<Object> it = all.iterator();
            while (it.hasNext()) {
               session.remove(it.next());
            }
            
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Object obj){
        if (toBePopulated){
            populateDatabase();
        }
        try (Session session = db.openSession()) {
            Transaction t = session.beginTransaction();
            session.merge(obj);
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public SessionFactory getDatabase(){ return this.db; }

    public String getLocalMusicLibraryPath() {
        return this.libraryDatabasePath;
    }

    public void setLocalMusicLibraryPath(String libraryDatabasePath) {
        this.libraryDatabasePath = libraryDatabasePath;
    }
}
