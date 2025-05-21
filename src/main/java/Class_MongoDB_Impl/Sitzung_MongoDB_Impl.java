package Class_MongoDB_Impl;

import Class_Impl.Sitzung_Impl;
import Class_Interfaces.Sitzung;
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
public class Sitzung_MongoDB_Impl extends Sitzung_Impl implements Sitzung {
    private database.MongoDBHandler MongoDBHandler;
    private int MongoID_match;
    private String sCollection;

    public Sitzung_MongoDB_Impl(database.MongoDBHandler MongoDBHandler, int MongoID_match){
        this.MongoDBHandler = MongoDBHandler;
        this.MongoID_match = MongoID_match;
        this.sCollection = "Sitzung";
    }


    public int getWP_Nummer() {
        int Inhalt = 0;
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = o.getInteger("WP_Nummer");
        }

        return Inhalt;
    }

    public int getSitzungsnr() {
        int Inhalt = 0;
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = o.getInteger("Sitzungsnr");
        }

        return Inhalt;
    }


    public String getSitzungs_Ort() {
        String Inhalt = "";
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = o.getString("Sitzungs_Ort");
        }

        return Inhalt;
    }


    public String getSitzungsdatum_Mongo_Impl() {
        String Inhalt = "";
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = o.getString("Sitzungsdatum");
        }

        return Inhalt;
    }

    public List<Integer> getTagesordnungspunkte_Mongo_Impl() {
        List<Integer> Inhalt = new ArrayList<>();
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = (List<Integer>) o.get("Tagesordnungspunkt");
        }

        return Inhalt;
    }


    public String getSitzungsbeginn_Mongo_Impl() {
        String Inhalt = "";
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = o.getString("Sitzungsbeginn");
        }

        return Inhalt;
    }

    public String getSitzungsende_Mongo_Impl() {
        String Inhalt = "";
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = o.getString("Sitzungsende");
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
