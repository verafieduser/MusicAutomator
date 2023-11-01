import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.function.Predicate;

public class SettingsHandler {

    private Properties settings;
    private String settingsPath;
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
        File home = new File(userHome + "/MusicAutomator");
        if (!home.exists() && !home.mkdir()) {
            throw new IllegalStateException(home.toString());
        }

        settingsPath = home.getAbsolutePath() + "/settings.properties";
        File settingsFile = new File(settingsPath);
        boolean noSettings = settingsFile.createNewFile();

        settings = new Properties();

        try (FileReader fr = new FileReader(settingsFile.getAbsolutePath())) {
            settings.load(fr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(noSettings){
            setUp(settings, home);
            save();
        } 
    }

    private void setUp(Properties settingsFile, File home) {
        settingsFile.setProperty("local.musiclibrary.path", getLocalMusicPath());
        settingsFile.setProperty("user.app.path", home.getAbsolutePath());
    }

    private String getLocalMusicPath(){
        String musicLibraryPath;
        try (
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(isr);) 
        {
            InputHandler ih = new InputHandler();
            Predicate<String> p = x -> (new File(x).exists());
            musicLibraryPath = ih.loopingPromptUserInput(reader, "What is the directory of your music folder?: ", "Please retry: ", p);
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
        try (FileOutputStream fos = new FileOutputStream(settingsPath)) {
            settings.store(fos,null);            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
