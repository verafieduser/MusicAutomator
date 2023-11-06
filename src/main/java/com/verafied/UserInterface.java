package com.verafied;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Function;
import java.util.function.Predicate;

public class UserInterface {

    Library library;
    MissingMusic missingMusic;
    LibraryCollector collector;
    SettingsHandler settings;

    public UserInterface(Initializer init) {
        library = init.getLibrary();
        missingMusic = init.getMissingMusic();
        collector = init.getCollector();
        settings = init.getSettings();
    }

    public void inputLoop(InputHandler ih) {
        Predicate<String> p = x -> (x.matches("\\d+") && Integer.valueOf(x) >= 0
                && Integer.valueOf(x) < 17);
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
                library.deleteSong(getSong(ih));
                break;
            case 4:
                // delete album
                library.deleteAlbum(getAlbum(ih));
                break;
            case 5:
                // delete artist
                library.deleteArtist(getArtist(ih));
                break;
            case 6:
                // get all by artist
                System.out.println(getArtist(ih));
                break;
            case 7:
                // get album
                System.out.println(getAlbum(ih));
                break;
            case 8:
                // get song
                System.out.println(getSong(ih));
                break;
            case 9:
                // change location of local music library
                settings.set("local.musiclibrary.path", getPath(ih));
                break;
            case 10:
                // import database from file
                collector.processCSV(null, null);
                break;

            default:
                throw new UnsupportedOperationException("\noption\n");
        }
        if(option!=0){
            inputLoop(ih);
        }
    }

    private Song getSong(InputHandler ih) {
        Function<String, Object> p = x -> library.getSong(x);
        Song song;
        try {
            song = (Song) ih.loopingPromptUserInput("Enter song name: ", "Faulty format, try again: ", p);
        } catch (IOException e) {
            song = null;
            e.printStackTrace();
        }

        return song;
    }

    private Album getAlbum(InputHandler ih) {
        Function<String, Object> p = x -> library.getAlbum(x);
        Album album;
        try  {
            album = (Album) ih.loopingPromptUserInput("Enter album name: ", "Faulty format, try again: ", p);
        } catch (IOException e ){
            album = null;
            e.printStackTrace();
        }
        return album;
    }

    private Artist getArtist(InputHandler ih) {
        Function<String, Object> getArtist = x -> library.getArtist(x);
        Artist artist;
        try  {
            artist = (Artist) ih.loopingPromptUserInput("Enter artist name: ",
                    "Faulty format, try again: ", getArtist);
        } catch (IOException e) {
            e.printStackTrace();
            artist=null;
        }
        return artist;
    }

    private String getPath(InputHandler ih){
        Predicate<String> isDirectory = x -> new File(x).exists();
        String result;
        try {
            result = ih.loopingPromptUserInput("Enter path name: ",
                    "Faulty format, try again: ", isDirectory);
        } catch (IOException e) {
            e.printStackTrace();
            result=null;
        }
        return result;
    }
}
