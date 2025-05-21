package Class_MongoDB_Impl;

import Class_Impl.Wahlkreis_Impl;
import Class_Interfaces.Wahlkreis;
import org.bson.Document;

/**
 * Diese Klasse besitzt eine Verbindung zu einer MongoDB und von dieser Datenbank werden alle Attribute und Funktionen/Objekte abgefragt.
 * Benötigt wird eine interne MongoID, durch welche eine eindeutige Zuordnung zum Dokument stattfinden kann.
 * Da die Klasse von der lokalen Hauptimplementation erbt, besitzt diese Klasse auch alle verfügbaren Attribute.
 */
public class Wahlkreis_MongoDB_Impl extends Wahlkreis_Impl implements Wahlkreis {
    private database.MongoDBHandler MongoDBHandler;
    private int MongoID_match;
    private String sCollection;
    private Document docu;

    public Wahlkreis_MongoDB_Impl(database.MongoDBHandler MongoDBHandler, int MongoID_match){
        this.MongoDBHandler = MongoDBHandler;
        this.MongoID_match = MongoID_match;
        this.sCollection = "Wahlkreis";
        this.docu = MongoDBHandler.getDocumentByMongoID(this.sCollection,this.MongoID_match);
    }

    public int getNumber() {
        if (this.docu.getInteger("WKR_Nummer")==null){
            return 0;
        } else {
            return this.docu.getInteger("WKR_Nummer");
        }
    }

    public String getWKR_Name() {
        if (this.docu.getString("WKR_Name")==null){
            return "";
        } else {
            return this.docu.getString("WKR_Name");
        }
    }


    public String getWKR_Land() {
        if (this.docu.getString("WKR_Land")==null){
            return "";
        } else {
            return this.docu.getString("WKR_Land");
        }
    }

    public String getWKR_Liste() {
        if (this.docu.getString("WKR_Liste")==null){
            return "";
        } else {
            return this.docu.getString("WKR_Liste");
        }
    }


    public int getWahlperiode_Mongo_Impl() {
        if (this.docu.getInteger("Wahlperiode")==null){
            return 0;
        } else {
            return this.docu.getInteger("Wahlperiode");
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
        StringBuilder Wahlkreis = new StringBuilder();
        Wahlkreis.append("\n" +
                "<div class=\"Wahlkreis\">\n" +
                "<h2>Wahlkreis</h2>\n");
        if (!(this.getNumber()==0)){
            Wahlkreis.append("<p>Wahlkreis Nummer: "+this.getNumber()+"</p>\n");
        }

        if (!(this.getWKR_Name()==null) && !(this.getWKR_Name().isEmpty())){
            Wahlkreis.append("<p>Wahlkreis Name: "+this.getWKR_Name()+"</p>\n");
        }

        if (!(this.getWKR_Land()==null)&& !(this.getWKR_Land().isEmpty())){
            Wahlkreis.append("<p>Wahlkreis Land: "+this.getWKR_Land()+"</p>\n");
        }

        if (!(this.getWKR_Liste()==null) && !(this.getWKR_Liste().isEmpty())){
            Wahlkreis.append("<p>Wahlkreis Liste: "+this.getWKR_Liste()+"</p>\n");
        }
        Wahlkreis.append("</div>\n");

        return Wahlkreis;
    }
}
