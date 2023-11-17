package com.verafied;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.function.Predicate;

public class SettingsHandler {

    private Properties settings;
    private Path settingsPath;
    public static String APPLICATION_PATH = "";

    /**
     * Initializes the settings by either importing or creating the settings file.
     * @throws IOException if there are issues with creating or finding the settings file, this error is thrown
     */
    public SettingsHandler() throws IOException {
        initialize();
        APPLICATION_PATH = settings.getProperty("user.app.path");
    }

    private void initialize() throws IOException {
        String userHome = System.getProperty("user.home");
        if (userHome == null) {
            throw new IllegalStateException("user.home==null");
        }
        

        //File home = new File(userHome + "/MusicAutomator");

        Path homePath = Paths.get(userHome + "/MusicAutomator");
        try {
            Files.createDirectory(homePath);    
        } catch (FileAlreadyExistsException e) {
            
        }

        boolean noSettings;
        try {
            settingsPath = Files.createFile(homePath.resolve("settings.properties")); 
            noSettings = true;   
        } catch (FileAlreadyExistsException e) {
            settingsPath = homePath.resolve("settings.properties");
            noSettings = false;
        }
        
        settings = new Properties();

        try (FileReader fr = new FileReader(settingsPath.toAbsolutePath().toString())) {
            settings.load(fr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!noSettings && !settings.containsKey("user.app.path")){
            noSettings=true;
        }

        if(noSettings){
            setUp(settings, homePath);
            save();
        } 
    }

    private void setUp(Properties settingsFile, Path home) {
        settingsFile.setProperty("local.musiclibrary.path", getLocalMusicPath());
        settingsFile.setProperty("user.app.path", home.toAbsolutePath().toString());
        settingsFile.setProperty("database.initialized", "false");
    }

    private String getLocalMusicPath(){
        String musicLibraryPath;
        try 
        {

            InputHandler ih = new InputHandler();
            Predicate<String> p = x -> (Files.isDirectory(Paths.get(x).toAbsolutePath())); 
            musicLibraryPath = ih.loopingPromptUserInput("What is the directory of your music folder?: ", "Please retry: ", p);
        } catch (IOException e) {
            e.printStackTrace();
            musicLibraryPath = "";
        }
        return musicLibraryPath;
    }

    /**
     * local.musiclibrary.path is the path of the users local music folder
     * user.app.path is the path of the application in the user folder.
     * @param key containing the key of a property in the settings file
     * @return containing the value of the key. null if no property is found.
     */
    public String get(String key){
        return settings.getProperty(key);
    }

    /**
     * Sets or adds a property into the settings.
     * local.musiclibrary.path is the path of the users local music folder
     * user.app.path is the path of the application in the user folder.
     * @param key key to be placed into the list
     * @param value value corresponding to the key
     * @return the previous value the key held, null if none.
     */
    public String set(String key, String value){
        return (String) settings.setProperty(key, value);
    }

    /**
     * Saves the settings into the application folder in the user folder.
     */
    public void save(){
        try (FileOutputStream fos = new FileOutputStream(settingsPath.toString())) {
            settings.store(fos,null);            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
