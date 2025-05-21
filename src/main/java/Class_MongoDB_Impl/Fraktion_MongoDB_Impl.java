package Class_MongoDB_Impl;

import Class_Impl.Fraktion_Impl;
import Class_Interfaces.Fraktion;
import org.bson.Document;

import java.util.*;

/**
 * Diese Klasse besitzt eine Verbindung zu einer MongoDB und von dieser Datenbank werden alle Attribute und Funktionen/Objekte abgefragt.
 * Benötigt wird eine interne MongoID, durch welche eine eindeutige Zuordnung zum Dokument stattfinden kann.
 * Da die Klasse von der lokalen Hauptimplementation erbt, besitzt diese Klasse auch alle verfügbaren Attribute.
 */
public class Fraktion_MongoDB_Impl extends Fraktion_Impl implements Fraktion {
    private database.MongoDBHandler MongoDBHandler;
    private int MongoID_match;
    private String sCollection;
    private Document docu;
    private List<Abgeordneter_MongoDB_Impl> Mitglieder_DB;

    public Fraktion_MongoDB_Impl(database.MongoDBHandler MongoDBHandler, int MongoID_match){
        this.Mitglieder_DB = new ArrayList<>();
        this.MongoDBHandler = MongoDBHandler;
        this.MongoID_match = MongoID_match;
        this.sCollection = "Fraktion";
        this.docu = MongoDBHandler.getDocumentByMongoID(this.sCollection,this.MongoID_match);
    }


    public String getName() {
        if (this.docu.getString("Name")==null){
            return "";
        } else {
            return this.docu.getString("Name");
        }
    }


    public Map<String, List<String>> getAbgeordneter_ID_WP_Nr_Mongo_Impl() {
        if (this.docu.get("Abgeordneter_ID_WP_Nr", Map.class)==null){
            return new HashMap<>();
        } else {
            return this.docu.get("Abgeordneter_ID_WP_Nr", Map.class);
        }
    }

    public int getMongoID() {
        if (this.docu.getInteger("MongoID")==null){
            return 0;
        } else {
            return this.docu.getInteger("MongoID");
        }
    }

    public String getMongoLabel() {
        if (this.docu.getString("MongoLabel")==null){
            return "";
        } else {
            return this.docu.getString("MongoLabel");
        }
    }

    public StringBuilder toHTML(){
        StringBuilder String_HTML = new StringBuilder();
        String_HTML.append("<div class=\"Fraktion\">\n");
        String_HTML.append("<h4><bold>Fraktion: "+ this.getName() +"</bold></h4>\n");
        String_HTML.append("</div>\n");

        return String_HTML;
    }

    public List<Abgeordneter_MongoDB_Impl> getMitglieder_DB() {
        return Mitglieder_DB;
    }

    public void AddMitglieder_DB(Abgeordneter_MongoDB_Impl mitglied_DB) {
        this.Mitglieder_DB.add(mitglied_DB);
    }
}
