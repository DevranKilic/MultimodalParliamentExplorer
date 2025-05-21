package Rest;

import io.javalin.http.staticfiles.Location;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Diese Klasse ermöglicht es Daten aus der MongoDBConfig.properties zu entnehmen und zu verwenden.
 */
public class JavalinConfig extends Properties {

    private String StaticFilesDirectory;
    private Location StaticFilesLocation;
    private String TemplateDirectory;
    private int Port;

    public JavalinConfig(){
        try{
            String Javalin_ConfigPath = "src/main/java/Properties/JavalinConfig.properties";
            FileInputStream InputStream = new FileInputStream(Javalin_ConfigPath);
            this.load(InputStream);
            this.StaticFilesDirectory = this.getProperty("StaticFilesDirectory");
            this.StaticFilesLocation = Location.valueOf(this.getProperty("StaticFilesLocation"));
            this.TemplateDirectory = this.getProperty("TemplateDirectory");
            this.Port = Integer.parseInt(this.getProperty("Port"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    protected String getStaticFilesDirectory() {
        return StaticFilesDirectory;
    }

    protected String getTemplateDirectory() {
        return TemplateDirectory;
    }

    protected int getPort() {
        return Port;
    }

    protected Location getStaticFilesLocation() {
        return StaticFilesLocation;
    }
}