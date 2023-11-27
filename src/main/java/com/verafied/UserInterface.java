package com.verafied;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class UserInterface {

    Library library;
    MissingMusic missingMusic;
    LibraryCollector collector;
    SettingsHandler settings;

    public UserInterface(Initializer init) {
        library = new Library(init.getDatabase());
        missingMusic = init.getMissingMusic();
        collector = init.getCollector();
        settings = init.getSettings();
    }

    public void inputLoop(InputHandler ih) {
        Predicate<String> p = x -> (x.matches("\\d+") && Integer.valueOf(x) >= 0
                && Integer.valueOf(x) <= 19);
        int result = 0;
        String prompt = "0: Exit\n1: Connect database entries with local songs\n";
        prompt += "2: Import local songs into database\n";
        prompt += "3. Delete song\n";
        prompt += "4. Delete album\n";
        prompt += "5. Delete artist\n";
        prompt += "6. Get all by artist\n";
        prompt += "7. Get all by album\n";
        prompt += "8. Get song\n";
        prompt += "9. Change location of local music library\n";
        prompt += "10. Import database from file\n";
        prompt += "11. Import database from API (UNIMPLEMENTED)\n";
        prompt += "12. (UNIMPLEMENTED)\n";
        prompt += "13. Restore all deleted by song\n";
        prompt += "14. Restore all deleted by album\n";
        prompt += "15. Restore all deleted by artist\n"; 
        prompt += "16. Restore all deleted songs, albums and artists\n";
        prompt += "17. Download song (UNIMPLEMENTED)\n";
        prompt += "18. Download album (UNIMPLEMENTED)\n";
        prompt += "19. Download artist (UNIMPLEMENTED)\n";
        prompt += "20. Remove local files that were removed in library\n";
        prompt += "21. Reset/Empty database\n";
        prompt += "Enter option: ";

        try {
            result = Integer.valueOf(ih.loopingPromptUserInput(prompt, "Faulty inpout, please try again: ", p));
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            switchBoard(result, ih);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchBoard(int option, InputHandler ih) throws IOException {
        switch (option) {
            case 0:
                System.out.println("Exiting ...");
                break;
            case 1:
                missingMusic.connectMissing(library);
                break;
            case 2:
                missingMusic.findLocalSongs(library);
                break;
            case 3:
                // delete song
                getSong(ih).forEach(x -> library.deleteSong(x));
                break;
            case 4:
                // delete album
                getAlbum(ih).forEach(x -> library.deleteAlbum(x));
                break;
            case 5:
                // delete artist
                getArtist(ih).forEach(x -> library.deleteArtist(x));
                break;
            case 6:
                // get all by artist
                Set<Artist> artists = new HashSet<>();
                getArtist(ih).forEach(x -> artists.add(x)); 

                for(Artist artist : artists){ 
                   System.out.println(artist.toString());
                } 
                break;
            case 7:
                // get album
                Set<Album> albums = new HashSet<>();
                getAlbum(ih).forEach(x -> albums.add(x));

                for(Album album : albums){
                   System.out.println(album.toString());
                }
                
                break;
            case 8:
                // get song
                Set<Song> songs = new HashSet<>();
                getSong(ih).forEach(x -> songs.add(x));
                for (Song song : songs){
                    System.out.println(song.toString());
                }
            
                break;
            case 9:
                // change location of local music library
                settings.set("local.musiclibrary.path", getPath(ih));
                break;
            case 10:
                // import database from file
                collector.processCSV("verafiedmusic.csv", DataSource.BENBEN);
                break;
            case 11: 
                break; 
            case 13:
                // restore deleted objects by song
                getSong(ih).forEach(x -> library.restoreSong(x));
                break;
            case 14: 
                // restore deleted objects by album
                getAlbum(ih).forEach(x -> library.restoreAlbum(x));
                break;
            case 15:
                // restore deleted objects by artist
                getArtist(ih).forEach(x -> library.restoreArtist(x));
                break;
            case 16:
                // restore deleted objects in database
                library.restoreDeletedAll();
                break;
            case 17: 
                //download song
                break;
            case 18:
                // download album
                break;
            case 19: 
                // download artist
                break;
            case 20:
                library.deleteDeleted();
                break;
            case 21: 
                library.clearDatabase();
                break;
            default:
                throw new UnsupportedOperationException("\noption\n");
        }
        if(option!=0){
            inputLoop(ih);
        }
    }

    // TODO: connect this and below method to process file!
    private String getFileName(InputHandler ih) throws IOException {
        Predicate<String> p = x -> (new File(SettingsHandler.APPLICATION_PATH + "/Library/Unprocessed/" + x).exists());
        return ih.loopingPromptUserInput("Please enter name of file to process:", "Please try again: ", p);
    }

    private DataSource getDataType(InputHandler ih) throws IOException {
        Predicate<String> p = x -> (x.matches("\\d+") && Integer.valueOf(x) > 0
                && Integer.valueOf(x) < DataSource.values().length + 1);

        String typeStr = ih.loopingPromptUserInput(
                "Where did  you get the file from? Enter a number for below options\n1.BENBEN\nPlease enter: ",
                "Please try again: ", p);
        int result;
        try {
            result = Integer.valueOf(typeStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            result = 1;
        }
        DataSource type;
        switch (result) {
            case 1:
                type = DataSource.BENBEN;
                break;
            default:
                type = DataSource.LASTFM;
                System.out.println("No number was selected!");
                break;
        }
        return type;
    }

    private List<Song> getSong(InputHandler ih) {
        Function<String, Object> p = x -> library.getSong(x);
        return (List<Song>) getResponse(ih, p, "Enter song name: ");
    }

    private List<Album> getAlbum(InputHandler ih) {
        Function<String, Object> p = x -> library.getAlbum(x);
        return (List<Album>) getResponse(ih, p, "Enter album name: ");
    }

    private List<Artist> getArtist(InputHandler ih) {
        Function<String, Object> getArtist = x -> library.getArtist(x);
        return (List<Artist>) getResponse(ih, getArtist, "Enter artist name: ");
    }

    // TODO: make sure dir is empty?
    private String getPath(InputHandler ih) {
        Predicate<String> isDirectory = x -> new File(x).exists();
        return (String) getResponse(ih, isDirectory, "Enter new path: ");
    }

    private Object getResponse(InputHandler ih, Function<String, Object> f, String prompt) {
        Object object;
        try {
            object = ih.loopingPromptUserInput(prompt, "Please try again: ", f);
        } catch (IOException e) {
            object = null;
            e.printStackTrace();
        }
        return object;
    }

    private Object getResponse(InputHandler ih, Predicate<String> f, String prompt) {
        String object;
        try {
            object = ih.loopingPromptUserInput(prompt, "Faulty format, try again: ", f);
        } catch (IOException e) {
            object = null;
            e.printStackTrace();
        }
        return object;
    }
}
