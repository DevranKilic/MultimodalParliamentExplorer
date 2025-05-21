package Class_MongoDB_Impl;

import Class_Impl.Ausschuss_Impl;
import Class_Interfaces.Ausschuss;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import database.MongoDBHandler;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Diese Klasse besitzt eine Verbindung zu einer MongoDB und von dieser Datenbank werden alle Attribute und Funktionen/Objekte abgefragt.
 * Benötigt wird eine interne MongoID, durch welche eine eindeutige Zuordnung zum Dokument stattfinden kann.
 * Da die Klasse von der lokalen Hauptimplementation erbt, besitzt diese Klasse auch alle verfügbaren Attribute.
 */
public class Ausschuss_MongoDB_Impl extends Ausschuss_Impl implements Ausschuss {

    private database.MongoDBHandler MongoDBHandler;
    private int MongoID_match;
    private String sCollection;

    public Ausschuss_MongoDB_Impl(MongoDBHandler MongoDBHandler,int MongoID_match){
        this.MongoDBHandler = MongoDBHandler;
        this.MongoID_match = MongoID_match;
        this.sCollection = "Ausschuss";
    }


    public String getAusschuss_Art() {

        String Inhalt = "";
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = o.getString("Ausschuss_Art");
        }

        return Inhalt;
    }


    public String getBeschreibung() {

        String Inhalt = "";
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = o.getString("Beschreibung");
        }

        return Inhalt;
    }

    public Map<String, List<String>> getWP_Nr_Abgeorndeten_IDs_Mongo_Impl() {
        Map<String, List<String>> Inhalt = new HashMap<>();
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = (Map<String, List<String>>) o.get("WP_Nr_Abgeorndeten_IDs");
        }

        return Inhalt;
    }


    public int getMongoID() {

        int Inhalt = 0;
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = o.getInteger("MongoID");
        }

        return Inhalt;
    }


    public String getMongoLabel() {

        String Inhalt = "";
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = o.getString("MongoLabel");
        }

        return Inhalt;
    }
}
