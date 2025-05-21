package Class_MongoDB_Impl;

import Class_Impl.Historie_Impl;
import Class_Interfaces.Historie;
import database.MongoDBHandler;
import org.bson.Document;

/**
 * Diese Klasse besitzt eine Verbindung zu einer MongoDB und von dieser Datenbank werden alle Attribute und Funktionen/Objekte abgefragt.
 * Benötigt wird eine interne MongoID, durch welche eine eindeutige Zuordnung zum Dokument stattfinden kann.
 * Da die Klasse von der lokalen Hauptimplementation erbt, besitzt diese Klasse auch alle verfügbaren Attribute.
 */
public class Historie_MongoDB_Impl extends Historie_Impl implements Historie {
    private MongoDBHandler MongoDBHandler;
    private int MongoID_match;
    private String sCollection;
    private Document docu;

    public Historie_MongoDB_Impl(database.MongoDBHandler MongoDBHandler, int MongoID_match) {
        this.MongoDBHandler = MongoDBHandler;
        this.MongoID_match = MongoID_match;
        this.sCollection = "Historie";
        this.docu = MongoDBHandler.getDocumentByMongoID(this.sCollection,this.MongoID_match);
    }

    @Override
    public String getOldValue1() {
        if (this.docu.getString("OldValue1")==null){
            return "";
        } else {
            return this.docu.getString("OldValue1");
        }
    }

    @Override
    public String getNewValue1() {
        if (this.docu.getString("NewValue1")==null){
            return "";
        } else {
            return this.docu.getString("NewValue1");
        }
    }

    @Override
    public String getOldValue2() {
        if (this.docu.getString("OldValue2")==null){
            return "";
        } else {
            return this.docu.getString("OldValue2");
        }
    }

    @Override
    public String getNewValue2() {
        if (this.docu.getString("NewValue2")==null){
            return "";
        } else {
            return this.docu.getString("NewValue2");
        }
    }

    @Override
    public String getDate() {
        if (this.docu.getString("Date")==null){
            return "";
        } else {
            return this.docu.getString("Date");
        }
    }

    @Override
    public String getTime() {
        if (this.docu.getString("Time")==null){
            return "";
        } else {
            return this.docu.getString("Time");
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

}
