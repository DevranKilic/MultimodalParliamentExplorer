package database;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Diese Klasse ermöglicht es Daten aus der MongoDBConfig.properties zu entnehmen und zu verwenden.
 */
public class MongoDBConfig extends Properties {

    private String remote_host;
    private String remote_database;
    private String remote_user;
    private String remote_password;
    private int remote_port;
    private String remote_collection;
    private String remote_database_credential;

    public MongoDBConfig(){
        try{
            String ConfigPath = "src/main/java/Properties/MongoDBConfig.properties";
            Properties ConfigProperties = new Properties();
            FileInputStream InputStream = new FileInputStream(ConfigPath);
            ConfigProperties.load(InputStream);

            this.remote_host = ConfigProperties.getProperty("remote_host");
            this.remote_database = ConfigProperties.getProperty("remote_database");
            this.remote_user = ConfigProperties.getProperty("remote_user");
            this.remote_password = ConfigProperties.getProperty("remote_password");
            this.remote_port = Integer.valueOf(ConfigProperties.getProperty("remote_port"));
            this.remote_collection = ConfigProperties.getProperty("remote_collection");
            this.remote_database_credential = ConfigProperties.getProperty("remote_database_credential");

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    protected String getRemote_database_credential(){ return remote_database_credential; }

    protected String getRemote_host() {
        return remote_host;
    }

    protected String getRemote_database() {
        return remote_database;
    }

    protected String getRemote_user() {
        return remote_user;
    }

    protected String getRemote_password() {
        return remote_password;
    }

    protected int getRemote_port() {
        return remote_port;
    }

    protected String getRemote_collection() {
        return remote_collection;
    }
}

