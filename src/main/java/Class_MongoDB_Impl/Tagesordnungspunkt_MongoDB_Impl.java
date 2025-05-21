package Class_MongoDB_Impl;

import Class_Impl.Tagesordnungspunkt_Impl;
import Class_Interfaces.Tagesordnungspunkt;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse besitzt eine Verbindung zu einer MongoDB und von dieser Datenbank werden alle Attribute und Funktionen/Objekte abgefragt.
 * Benötigt wird eine interne MongoID, durch welche eine eindeutige Zuordnung zum Dokument stattfinden kann.
 * Da die Klasse von der lokalen Hauptimplementation erbt, besitzt diese Klasse auch alle verfügbaren Attribute.
 */
public class Tagesordnungspunkt_MongoDB_Impl extends Tagesordnungspunkt_Impl implements Tagesordnungspunkt {
    private database.MongoDBHandler MongoDBHandler;
    private int MongoID_match;
    private String sCollection;

    public Tagesordnungspunkt_MongoDB_Impl(database.MongoDBHandler MongoDBHandler, int MongoID_match){
        this.MongoDBHandler = MongoDBHandler;
        this.MongoID_match = MongoID_match;
        this.sCollection = "Tagesordnungspunkt";
    }


    public String getTagesordnungsname() {
        String Inhalt = "";
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = o.getString("Tagesordnungsname");
        }

        return Inhalt;
    }

    public String getTOP_Beschreibung() {
        String Inhalt = "";
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = o.getString("TOP_Beschreibung");
        }

        return Inhalt;
    }

    public List<String> getRede_IDs_Mongo_Impl() {
        List<String> Inhalt = new ArrayList<>();
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = (List<String>) o.get("Rede_IDs");
        }

        return Inhalt;
    }


    public int getSitzung_Mongo_Impl() {
        int Inhalt = 0;
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = o.getInteger("Sitzung");
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
