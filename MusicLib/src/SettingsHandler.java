import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class SettingsHandler {

    private Properties settings;
    private String settingsPath;

    public SettingsHandler() throws IOException {
        initialize();
    }

    public void initialize() throws IOException {
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
            setUp(settings);
        }
    }

    private void setUp(Properties settingsFile) {
        settingsFile.setProperty("localMusicLibraryPath", "");
    }

    public String get(String key){
        return settings.getProperty(key);
    }

    public String set(String key, String value){
        return (String) settings.setProperty(key, value);
    }

    public void save(){
        try (FileOutputStream fos = new FileOutputStream(settingsPath)) {
            settings.store(fos,null);            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
