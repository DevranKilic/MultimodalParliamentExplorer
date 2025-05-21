package Class_MongoDB_Impl;

import Class_Impl.Institution_Impl;
import Class_Interfaces.Institution;
import org.bson.Document;

/**
 * Diese Klasse besitzt eine Verbindung zu einer MongoDB und von dieser Datenbank werden alle Attribute und Funktionen/Objekte abgefragt.
 * Benötigt wird eine interne MongoID, durch welche eine eindeutige Zuordnung zum Dokument stattfinden kann.
 * Da die Klasse von der lokalen Hauptimplementation erbt, besitzt diese Klasse auch alle verfügbaren Attribute.
 */
public class Institution_MongoDB_Impl extends Institution_Impl implements Institution {
    private database.MongoDBHandler MongoDBHandler;
    private int MongoID_match;
    private String sCollection;
    private Document docu;

    public Institution_MongoDB_Impl(database.MongoDBHandler MongoDBHandler, int MongoID_match) {
        this.MongoDBHandler = MongoDBHandler;
        this.MongoID_match = MongoID_match;
        this.sCollection = "Institution";
        this.docu = MongoDBHandler.getDocumentByMongoID(this.sCollection,this.MongoID_match);
    }


    public String getInsart_Lang() {
        if (this.docu.getString("Insart_Lang")==null){
            return "";
        } else {
            return this.docu.getString("Insart_Lang");
        }
    }

    public String getIns_Lang() {
        if (this.docu.getString("Ins_Lang")==null){
            return "";
        } else {
            return this.docu.getString("Ins_Lang");
        }
    }


    public String getMDBins_Von_Mongo_Impl() {
        if (this.docu.getString("MDBins_Von")==null){
            return "";
        } else {
            return this.docu.getString("MDBins_Von");
        }
    }

    public String getMDBins_Bis_Mongo_Impl() {
        if (this.docu.getString("MDBins_Bis")==null){
            return "";
        } else {
            return this.docu.getString("MDBins_Bis");
        }
    }

    public String getFkt_lang() {
        if (this.docu.getString("Fkt_lang")==null){
            return "";
        } else {
            return this.docu.getString("Fkt_lang");
        }
    }


    public String getFktins_Von_Mongo_Impl() {
        if (this.docu.getString("Fktins_Von")==null){
            return "";
        } else {
            return this.docu.getString("Fktins_Von");
        }
    }


    public String getFktins_Bis_Mongo_Impl() {
        if (this.docu.getString("Fktins_Bis")==null){
            return "";
        } else {
            return this.docu.getString("Fktins_Bis");
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


    public StringBuilder Fraktion_GruppeToHTML() {
        StringBuilder InstitutionHTML = new StringBuilder();
        InstitutionHTML.append(
                "<p>Fraktion: " + this.getIns_Lang() + "</p>\n");

        if (!(getMDBins_Von_Mongo_Impl()==null && !(getMDBins_Von_Mongo_Impl().trim().isEmpty()))){
            InstitutionHTML.append("<p>Mitglied von: " + this.getMDBins_Von_Mongo_Impl() + "</p>\n");
        }

        if (!(getMDBins_Bis_Mongo_Impl()==null) && !(getMDBins_Bis_Mongo_Impl().trim().isEmpty())){
            InstitutionHTML.append("<p>Mitglied bis: " + this.getMDBins_Bis_Mongo_Impl() + "</p>\n");
        }

        return InstitutionHTML;
    }

    public StringBuilder AusschussToHTML() {
        StringBuilder InstitutionHTML = new StringBuilder();
        InstitutionHTML.append(
                "<p>Art: " + this.getIns_Lang() + "</p>\n" +
                "<p>Beschreibung: " + this.getIns_Lang() + "</p>\n");

        if (!(getMDBins_Von_Mongo_Impl()==null)){
            InstitutionHTML.append("<p>Mitglied von: " + this.getMDBins_Von_Mongo_Impl() + "</p>\n");
        }
        if (!(getMDBins_Bis_Mongo_Impl()==null) && !(getMDBins_Bis_Mongo_Impl().trim().isEmpty())){
            InstitutionHTML.append("<p>Mitglied bis: " + this.getMDBins_Bis_Mongo_Impl() + "</p>\n");
        }

        InstitutionHTML.append("<p>Funktion: " + this.getFkt_lang() + "</p>\n");

        if (!(getFktins_Von_Mongo_Impl()==null)){
            InstitutionHTML.append("<p>Funktion von: " + this.getFktins_Von_Mongo_Impl() + "</p>\n");
        }

        if (!(getFktins_Bis_Mongo_Impl()==null) && !(getFktins_Bis_Mongo_Impl().trim().isEmpty())){
            InstitutionHTML.append("<p>Funktion bis: " + this.getMDBins_Bis_Mongo_Impl() + "</p>\n");
        }

        return InstitutionHTML;
    }

    public StringBuilder SonstigesToHTML() {
        StringBuilder InstitutionHTML = new StringBuilder();
        InstitutionHTML.append(
                "<p>Art: " + this.getIns_Lang() + "</p>\n" +
                "<p>Beschreibung: " + this.getIns_Lang() + "</p>\n");
        if (!(getMDBins_Von_Mongo_Impl()==null)){
            InstitutionHTML.append("<p>Mitglied von: " + this.getMDBins_Von_Mongo_Impl() + "</p>\n");
        }
        if (!(getMDBins_Bis_Mongo_Impl()==null) && !(getMDBins_Bis_Mongo_Impl().trim().isEmpty())){
            InstitutionHTML.append("<p>Mitglied bis: " + this.getMDBins_Bis_Mongo_Impl() + "</p>\n");
        }

        InstitutionHTML.append("<p>Funktion: " + this.getFkt_lang() + "</p>\n");

        if (!(getFktins_Von_Mongo_Impl()==null)){
            InstitutionHTML.append("<p>Funktion von: " + this.getFktins_Von_Mongo_Impl() + "</p>\n");
        }

        if (!(getFktins_Bis_Mongo_Impl()==null) && !(getFktins_Bis_Mongo_Impl().trim().isEmpty())){
            InstitutionHTML.append("<p>Funktion bis: " + this.getFktins_Bis_Mongo_Impl() + "</p>\n");
        }

        return InstitutionHTML;
    }
}
