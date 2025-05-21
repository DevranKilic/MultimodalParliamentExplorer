package Class_MongoDB_Impl;

import Class_Impl.Wahlperioden_Impl;
import Class_Interfaces.Wahlperioden;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse besitzt eine Verbindung zu einer MongoDB und von dieser Datenbank werden alle Attribute und Funktionen/Objekte abgefragt.
 * Benötigt wird eine interne MongoID, durch welche eine eindeutige Zuordnung zum Dokument stattfinden kann.
 * Da die Klasse von der lokalen Hauptimplementation erbt, besitzt diese Klasse auch alle verfügbaren Attribute.
 */
public class Wahlperioden_MongoDB_Impl extends Wahlperioden_Impl implements Wahlperioden {
    private database.MongoDBHandler MongoDBHandler;
    private int MongoID_match;
    private String sCollection;
    private Document docu;

    public Wahlperioden_MongoDB_Impl(database.MongoDBHandler MongoDBHandler, int MongoID_match){
        this.MongoDBHandler = MongoDBHandler;
        this.MongoID_match = MongoID_match;
        this.sCollection = "Wahlperioden";
        this.docu = MongoDBHandler.getDocumentByMongoID(this.sCollection,this.MongoID_match);
    }


    public int getNumber() {
        if (this.docu.getInteger("WP")==null){
            return 0;
        } else {
            return this.docu.getInteger("WP");
        }
    }

    public String getStartDate_Mongo_Impl() {
        if (this.docu.getString("WP_Von")==null){
            return "";
        } else {
            return this.docu.getString("WP_Von");
        }
    }


    public String getEndDate_Mongo_Impl() {
        if (this.docu.getString("WP_Bis")==null){
            return "";
        } else {
            return this.docu.getString("WP_Bis");
        }
    }

    public int getWahlkreis_Mongo_Impl() {
        if (this.docu.getInteger("Wahlkreis")==null){
            return 0;
        } else {
            return this.docu.getInteger("Wahlkreis");
        }
    }



    public List<Integer> getInstitutionen_Mongo_Impl() {
        if (this.docu.getList("Institutionen",Integer.class)==null){
            return new ArrayList<>();
        } else {
            return this.docu.getList("Institutionen",Integer.class);
        }
    }

    public String getListe() {
        if (this.docu.getString("Liste")==null){
            return "";
        } else {
            return this.docu.getString("Liste");
        }
    }

    public String getMandatsart() {
        if (this.docu.getString("Mandatsart")==null){
            return "";
        } else {
            return this.docu.getString("Mandatsart");
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
        StringBuilder Wahlperioden = new StringBuilder();
        Wahlperioden.append("<div class=\"Wahlperiode\">\n" +
                "<h2>Wahlperiode: "+this.getNumber()+"</h2>\n" +
                "<p>Von: "+this.getStartDate_Mongo_Impl()+"</p>\n");

        if (!(this.getEndDate_Mongo_Impl()==null) && !(this.getEndDate_Mongo_Impl().trim().isEmpty())){
            Wahlperioden.append("<p>Bis: "+this.getEndDate_Mongo_Impl()+"</p>\n");
        }

        Wahlperioden.append("<p>Mandatsart: "+this.getMandatsart()+"</p>\n");


        if (!this.getListe().trim().isEmpty()){
            Wahlperioden.append("<p>Liste: "+this.getListe()+"</p>\n");
        }

        Wahlperioden.append("</div>\n");


        return Wahlperioden;
    }
}
