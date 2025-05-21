package Class_MongoDB_Impl;

import Class_Impl.Partei_Impl;
import Class_Interfaces.Partei;
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
public class Partei_MongoDB_Impl extends Partei_Impl implements Partei {
    private database.MongoDBHandler MongoDBHandler;
    private int MongoID_match;
    private String sCollection;

    public Partei_MongoDB_Impl(database.MongoDBHandler MongoDBHandler, int MongoID_match){
        this.MongoDBHandler = MongoDBHandler;
        this.MongoID_match = MongoID_match;
        this.sCollection = "Partei";
    }

    public String getName() {
        String Inhalt = "";
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = o.getString("Name");
        }

        return Inhalt;
    }


    public List<Integer> getMitglieder_Mongo_Impl() {
        List<Integer> Inhalt = new ArrayList<>();
        MongoCollection<Document> pCollection = MongoDBHandler.getCollection(this.sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID",this.MongoID_match);

        FindIterable<Document> Cursor = pCollection.find(searchQuery);
        for (Document o : Cursor) {
            Inhalt = (List<Integer>) o.get("Mitglieder");
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
