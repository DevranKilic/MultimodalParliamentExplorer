package Class_MongoDB_Impl;

import Class_Impl.Abgeordneter_Impl;
import Helper.Hilfsmethoden;
import Class_Interfaces.Abgeordneter;
import database.MongoDBHandler;
import org.bson.Document;

import java.util.*;


/**
 * Diese Klasse besitzt eine Verbindung zu einer MongoDB und von dieser Datenbank werden alle Attribute und Funktionen/Objekte abgefragt.
 * Benötigt wird eine interne MongoID, durch welche eine eindeutige Zuordnung zum Dokument stattfinden kann.
 * Da die Klasse von der lokalen Hauptimplementation erbt, besitzt diese Klasse auch alle verfügbaren Attribute.
 */
public class Abgeordneter_MongoDB_Impl extends Abgeordneter_Impl implements Abgeordneter {

    private MongoDBHandler MongoDBHandler;
    private int MongoID_match;
    private String sCollection;
    private Document docu;

    public Abgeordneter_MongoDB_Impl(MongoDBHandler MongoDBHandler,int MongoID_match){
        this.MongoDBHandler = MongoDBHandler;
        this.MongoID_match = MongoID_match;
        this.sCollection = "Abgeordneter";
        this.docu = MongoDBHandler.getDocumentByMongoID(this.sCollection,this.MongoID_match);
    }

    public Abgeordneter_MongoDB_Impl(Document Abgeordneter_docu){
        this.MongoID_match = Abgeordneter_docu.getInteger("MongoID");
        this.sCollection = "Abgeordneter";
        this.docu = Abgeordneter_docu;
    }

    public String getName() {
        if (this.docu.getString("Nachname")==null){
            return "";
        } else {
            return this.docu.getString("Nachname");
        }
    }

    public String getVorname() {
        if (this.docu.getString("Vorname")==null){
            return "";
        } else {
            return this.docu.getString("Vorname");
        }
    }

    public String getOrtzusatz() {
        if (this.docu.getString("Ortszusatz")==null){
            return "";
        } else {
            return this.docu.getString("Ortszusatz");
        }
    }

    public String getAdel() {
        if (this.docu.getString("Adel")==null){
            return "";
        } else {
            return this.docu.getString("Adel");
        }
    }

    public String getAnrede() {
        if (this.docu.getString("Anrede_Titel")==null){
            return "";
        } else {
            return this.docu.getString("Anrede_Titel");
        }
    }


    public String getAkadTitel() {
        if (this.docu.getString("Akad_Titel")==null){
            return "";
        } else {
            return this.docu.getString("Akad_Titel");
        }
    }

    public String getGeburtsdatum_Mongo_Impl() {
        if (this.docu.getString("Geburtsdatum")==null){
            return "";
        } else {
            return this.docu.getString("Geburtsdatum");
        }
    }


    public String getGeburtsort() {
        if (this.docu.getString("Geburtsort")==null){
            return "";
        } else {
            return this.docu.getString("Geburtsort");
        }
    }


    public String getSterbedatum_Mongo_Impl() {
        if (this.docu.getString("Sterbedatum")==null){
            return "";
        } else {
            return this.docu.getString("Sterbedatum");
        }
    }


    public String getGeschlecht() {
        if (this.docu.getString("Geschlecht")==null){
            return "";
        } else {
            return this.docu.getString("Geschlecht");
        }
    }

    public String getReligion() {
        if (this.docu.getString("Religion")==null){
            return "";
        } else {
            return this.docu.getString("Religion");
        }
    }

    public String getBeruf() {
        if (this.docu.getString("Beruf")==null){
            return "";
        } else {
            return this.docu.getString("Beruf");
        }
    }

    public String getVita() {
        if (this.docu.getString("Vita_kurz")==null){
            return "";
        } else {
            return this.docu.getString("Vita_kurz");
        }
    }

    public int getID() {
        if (this.docu.getInteger("ID")==null){
            return 0;
        } else {
            return this.docu.getInteger("ID");
        }
    }

    public String getID_String() {
        if (this.docu.getInteger("ID")==null){
            return "";
        } else {
            return String.valueOf(this.docu.getInteger("ID"));
        }
    }

    public String getHistorie_von_Mongo_Impl() {
        if (this.docu.getString("Historie_von")==null){
            return "";
        } else {
            return this.docu.getString("Historie_von");
        }
    }

    public String getHistorie_bis_Mongo_Impl() {
        if (this.docu.getString("Historie_bis")==null){
            return "";
        } else {
            return this.docu.getString("Historie_bis");
        }
    }

    public String getGeburtsland() {
        if (this.docu.getString("Geburtsland")==null){
            return "";
        } else {
            return this.docu.getString("Geburtsland");
        }
    }

    public String getFamilienstand() {
        if (this.docu.getString("Familienstand")==null){
            return "";
        } else {
            return this.docu.getString("Familienstand");
        }
    }

    public String getPartei_kurz() {
        if (this.docu.getString("Partei_kurz")==null){
            return "";
        } else {
            return this.docu.getString("Partei_kurz");
        }
    }

    public String getVeröffentlichungspflichtiges() {
        if (this.docu.getString("Veröffentlichungspflichtiges")==null){
            return "";
        } else {
            return this.docu.getString("Veröffentlichungspflichtiges");
        }
    }

    public String BildBase64() {
        if (this.docu.getString("BildBase64")==null){
            return "";
        } else {
            return this.docu.getString("BildBase64");
        }
    }

    public String getHq_picture() {
        if (this.docu.getString("hq_picture")==null){
            return "";
        } else {
            return this.docu.getString("hq_picture");
        }
    }

    public String getPicture_description() {
        if (this.docu.getString("picture_description")==null){
            return "";
        } else {
            return this.docu.getString("picture_description");
        }
    }

    public String getPicture_BilddatenbankBeschreibung(){
        if (this.docu.getString("BilddatenbankBeschreibung")==null){
            return "";
        } else {
            return this.docu.getString("BilddatenbankBeschreibung");
        }
    }


    public List<Integer> getWahlperioden_Mongo_Impl() {
        if (this.docu.getList("Wahlperioden", Integer.class) ==null){
            return null;
        } else {
            return this.docu.getList("Wahlperioden", Integer.class);
        }
    }


    public List<Integer> getInstitutionen_Mongo_Impl() {
        if (this.docu.getList("Institutionen", Integer.class) ==null){
            return null;
        } else {
            return this.docu.getList("Institutionen", Integer.class);
        }
    }


    public List<Integer> getReden_Mongo_Impl() {
        if (this.docu.getList("Reden", Integer.class) ==null){
            return null;
        } else {
            return this.docu.getList("Reden", Integer.class);
        }
    }


    public List<Integer> getBildhistorieMongoIDs() {
        if (this.docu.getList("Bildhistorie_MongoIDs", Integer.class)==null){
            return null;
        } else {
            return this.docu.getList("Bildhistorie_MongoIDs", Integer.class);
        }
    }


    public int getMongoID() {
        if (this.docu.getInteger("MongoID")==null){
            return 0;
        } else {
            return this.docu.getInteger("MongoID");
        }
    }

    public String getMongoID_asString() {
        if (this.docu.getInteger("MongoID")==null){
            return "";
        } else {
            return String.valueOf(this.docu.getInteger("MongoID"));
        }
    }


    public String getMongoLabel() {
        if (this.docu.getString("MongoLabel")==null){
            return "";
        } else {
            return this.docu.getString("MongoLabel");
        }
    }

    public void putDocu(String docString, Object docObject){
        this.docu.put(docString,docObject);
    }

    public StringBuilder toHTML(){
        StringBuilder Abgeordneter_HTML =new StringBuilder();
        Abgeordneter_HTML.append("\n");
        Abgeordneter_HTML.append("<h1>"+this.getVorname()+" "+this.getName()+"</h1>\n");

        Abgeordneter_HTML.append("\n");
        /*
        if (!(this.BildBase64().equals("")) && !(this.BildBase64()==null)){
            Hilfsmethoden.BildBase64ToFileSave(this.BildBase64(),this.getID());
            Abgeordneter_HTML.append("<img class= Bild src=../BilderMongoDB/"+this.getID()+".jpg width=400>\n");
        } else if (!(this.hq_picture().equals("")) && !(this.hq_picture()==null)) {
            Abgeordneter_HTML.append("<img class= Bild src="+this.hq_picture()+" width=400>\n");
            if (!(this.picture_description().equals("")) && !(this.picture_description()==null)){
                Abgeordneter_HTML.append("<p><small> Beschreibung: "+this.picture_description()+"</small></p>");
            }
        }

         */

        if (!(this.getHq_picture().equals("")) && !(this.getHq_picture()==null)){
            Abgeordneter_HTML.append("<img class= Bild src="+this.getHq_picture()+" width=400>\n");
            if (!(this.getPicture_description().equals("")) && !(this.getPicture_description()==null)){
                Abgeordneter_HTML.append("<p><small> Beschreibung: "+this.getPicture_description()+"</small></p>");
            }
        } else if (!(this.BildBase64().equals("")) && !(this.BildBase64()==null)) {
            Hilfsmethoden.BildBase64ToFileSave(this.BildBase64(),this.getID());
            Abgeordneter_HTML.append("<img class= Bild src=../BilderMongoDB/"+this.getID()+".jpg width=400>\n");
        }


        Abgeordneter_HTML.append("\n");
        Abgeordneter_HTML.append(
                "<div class=\"Biografie\">\n" +
                "<h2 >Biografische Daten</h2>\n" +
                "<p>Abgeordneter ID: "+this.getID()+"</p>\n" +
                "<p>Partei: "+this.getPartei_kurz()+"</p>\n" +
                "<p>Vorname: "+this.getVorname()+"</p>\n" +
                "<p>Nachname: "+this.getName()+"</p>\n");
        if (!(this.getAnrede()==null) && !(this.getAnrede().trim().isEmpty())) {
            Abgeordneter_HTML.append("<p>Anrede: "+this.getAnrede()+"</p>\n");
        }

        if (!(this.getAdel()==null) && !(this.getAdel().trim().isEmpty())) {
            Abgeordneter_HTML.append("<p>Praefix: "+this.getAdel()+"</p>\n");
        }

        Abgeordneter_HTML.append(
                "<p>Geschlecht: "+this.getGeschlecht()+"</p>\n");

        if (!(this.getAkadTitel()==null) && !(this.getAkadTitel().trim().isEmpty())) {
            Abgeordneter_HTML.append("<p>Akadmischer Titel: "+this.getAkadTitel()+"</p>\n");
        }

        Abgeordneter_HTML.append(
                "<p>Geburtsdatum: "+this.getGeburtsdatum_Mongo_Impl()+"</p>\n" +
                "<p>Geburtsort: "+this.getGeburtsort()+"</p>\n" +
                "<p>Familienstand: "+this.getFamilienstand()+"</p>\n" +
                "<p>Religion: "+this.getReligion()+"</p>\n" +
                "<p>Beruf: "+this.getBeruf()+"</p>\n");

        if (!(this.getVita()==null) && !(this.getVita().trim().isEmpty())) {
            Abgeordneter_HTML.append("<p>Vita: "+this.getVita()+"</p>\n");
        }
        Abgeordneter_HTML.append("</div>\n");

        return Abgeordneter_HTML;

    }
}
