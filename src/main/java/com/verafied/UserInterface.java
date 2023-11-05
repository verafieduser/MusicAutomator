package com.verafied;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Predicate;

public class UserInterface {

    Library library;
    MissingMusic missingMusic;
    LibraryCollector collector;


    public UserInterface(Initializer init){
        library = init.getLibrary();
        missingMusic = init.getMissingMusic();
        collector = init.getCollector();
    }
    public void inputLoop() {
        InputHandler ih = new InputHandler();
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

        try (InputStreamReader isr = new InputStreamReader(System.in);
                BufferedReader reader = new BufferedReader(isr)) {
            result = Integer.valueOf(ih.loopingPromptUserInput(reader, prompt, "Faulty inpout, please try again: ", p));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            switchBoard(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void switchBoard(int option) throws IOException{
        switch (option) {
            case 1:
                // connect
                missingMusic.connectMissing(library);
                break;
            case 2:
                // import local
                missingMusic.findLocalSongs(library);
                break;
            case 3:
                // delete song
                library.deleteSong(null);
                break;
            case 4:
                // delete album
                library.deleteAlbum(null);
                break;
            case 5:
                // delete artist 
                library.deleteArtist(null);
                break;
            case 6:
                // get all by artist
                library.getArtist(null);
                break;
            case 7:
                // get album
                library.getAlbum(null);
                break;
            case 8:
                // get song 
                library.getSong(null);
                break;
            case 9:
                // change location of local music library
                
                break;
            case 10:
                // import database from file 
                collector.processCSV(null, null);
                break;

            default:
                throw new UnsupportedOperationException("\noption\n");
        }
    }
}
