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
    LibrarySaver saver;
    SettingsHandler settings;
    

    public UserInterface(Initializer init) {
        library = init.getLibrary();
        missingMusic = init.getMissingMusic();
        collector = init.getCollector();
        settings = init.getSettings();
        saver = init.getSaver();
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
        prompt += "12. Update library from file (UNIMPLEMENTED\n";
        prompt += "13. Update library from API (UNIMPLEMENTED)\n";
        prompt += "14. Empty song garbage bin (UNIMPLEMENTED)\n";
        prompt += "15. Get bandcamp link of album (UNIMPLEMENTED)\n";
        prompt += "16. Download album (UNIMPLEMENTED)\n";
        prompt += "17. Save Library\n";
        prompt += "18. Remove local files that were removed in library\n";
        prompt += "19. Reset/Empty database\n";
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
                getAlbum(ih).forEach(x-> library.deleteAlbum(x));
                break;
            case 5:
                // delete artist
                library.deleteArtist(getArtist(ih));
                break;
            case 6:
                // get all by artist
                Artist artist = getArtist(ih);
                System.out.println(artist.getName() + "\n" + artist.printAlbums());
                break;
            case 7:
                // get album
                Set<Album> albums = new HashSet<>();
                getAlbum(ih).forEach(x -> albums.add(x));
                for(Album album : albums){
                    System.out.println(album.getName() + "\n" + album.printSongs());
                }
                break;
            case 8:
                // get song
                getSong(ih).forEach(x -> System.out.println(x));
                break;
            case 9:
                // change location of local music library
                settings.set("local.musiclibrary.path", getPath(ih));
                break;
            case 10:
                // import database from file
                collector.processCSV("verafiedmusic.csv", DataSource.BENBEN);
                break;
            case 17:
                saver.writeToCSV(library);
                break;
            case 18:
                library.deleteDeleted();
                break;
            case 19: 

                throw new UnsupportedOperationException();
            default:
                throw new UnsupportedOperationException("\noption\n");
        }
        if(option!=0){
            inputLoop(ih);
        }
    }

    //TODO: connect this and below method to process file!
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

    private Artist getArtist(InputHandler ih) {
        Function<String, Object> getArtist = x -> library.getArtist(x);
        return (Artist) getResponse(ih, getArtist, "Enter artist name: ");
    }

    //TODO: make sure dir is empty?
    private String getPath(InputHandler ih){
        Predicate<String> isDirectory = x -> new File(x).exists();
        return (String) getResponse(ih, isDirectory, "Enter new path: ");
    }

    private Object getResponse(InputHandler ih, Function<String, Object> f, String prompt) {
        Object object;
        try {
            object = ih.loopingPromptUserInput(prompt, "Faulty format, try again: ", f);
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
