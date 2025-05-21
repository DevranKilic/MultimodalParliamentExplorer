package Class_MongoDB_Impl;

import Class_Impl.RedeAuszug_Impl;
import org.bson.Document;

import java.util.List;


/**
 * Diese Klasse besitzt eine Verbindung zu einer MongoDB und von dieser Datenbank werden alle Attribute und Funktionen/Objekte abgefragt.
 * Benötigt wird eine interne MongoID, durch welche eine eindeutige Zuordnung zum Dokument stattfinden kann.
 * Da die Klasse von der lokalen Hauptimplementation erbt, besitzt diese Klasse auch alle verfügbaren Attribute.
 */
public class RedeAuszug_MongoDB_Impl extends RedeAuszug_Impl {
    private database.MongoDBHandler MongoDBHandler;
    private int MongoID_match;
    private String sCollection;
    private Document docu;


    public RedeAuszug_MongoDB_Impl(database.MongoDBHandler MyMongoDBHandler, int MongoID_match){
        this.MongoDBHandler = MyMongoDBHandler;
        this.MongoID_match = MongoID_match;
        this.sCollection = "RedeAuszug";
        this.docu = MongoDBHandler.getDocumentByMongoID(this.sCollection,this.MongoID_match);

        if (docu.getInteger("Fraktion")!=-1){
            Fraktion_MongoDB_Impl Fraktion = new Fraktion_MongoDB_Impl(MyMongoDBHandler, docu.getInteger("Fraktion"));
            this.setFraktion(new Fraktion_MongoDB_Impl(MyMongoDBHandler, docu.getInteger("Fraktion")));
        }

        if (docu.getInteger("Abgeordneter")!=-1){
            this.setAbgeordneter(new Abgeordneter_MongoDB_Impl(MyMongoDBHandler, docu.getInteger("Abgeordneter")));
        }
    }

    public String getAuszug() {
        if (this.docu.getString("Auszug")==null){
            return "";
        } else {
            return this.docu.getString("Auszug");
        }
    }

    public Boolean getIsKommentarMongoDB() {
        if (this.docu.getBoolean("isKommentar")==null){
            return false;
        } else {
            return this.docu.getBoolean("isKommentar");
        }
    }


    public String getVideoAbsolutPath() {
        if (this.docu.getString("VideoAbsolutPath")==null){
            return null;
        } else {
            return this.docu.getString("VideoAbsolutPath");
        }
    }

    public String getMongoLabel() {
        if (this.docu.getString("MongoLabel")==null){
            return "";
        } else {
            return this.docu.getString("MongoLabel");
        }
    }

    public int getMongoIDMongoDB() {
        if (this.docu.getInteger("MongoID")==null){
            return 0;
        } else {
            return this.docu.getInteger("MongoID");
        }
    }

    public String getMongoIDMongoDB_String() {
        if (this.docu.getInteger("MongoID")==null){
            return "";
        } else {
            return String.valueOf(this.docu.getInteger("MongoID"));
        }
    }

    public List<Integer> getHistorieMongoIDs(){
        if (this.docu.getList("Historie_MongoIDs", Integer.class)==null){
            return null;
        } else{
            return this.docu.getList("Historie_MongoIDs", Integer.class);
        }
    }




    public void UpdateMongoDBAbgeordnter(int Abgeordneter_MongoID){
        this.MongoDBHandler.getCollection("RedeAuszug").updateOne(new Document("MongoID", this.MongoID_match),new Document("$set", new Document("Abgeordneter",Abgeordneter_MongoID)));
        this.setAbgeordneter(new Abgeordneter_MongoDB_Impl(MongoDBHandler, docu.getInteger("Abgeordneter")));
    }

    public void UpdateMongoDBFraktion(int Fraktion_MongoID){
        this.MongoDBHandler.getCollection("RedeAuszug").updateOne(new Document("MongoID", this.MongoID_match),new Document("$set", new Document("Fraktion",Fraktion_MongoID)));
        this.setFraktion(new Fraktion_MongoDB_Impl(MongoDBHandler, docu.getInteger("Fraktion")));
    }
}
