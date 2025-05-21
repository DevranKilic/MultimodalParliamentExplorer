package Class_Freemarker_Impl;

import Class_MongoDB_Impl.*;
import database.MongoDBHandler;
import org.bson.Document;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Diese Klasse ermöglicht es den Freemarker Template leichter zu gestalten. Sie ermöglicht ein einfaches iterieren über mehrere Klassen im gleichen Kontext.
 * Die Klasse nimmt eine Wahlperiode (interne MongoID) entgegen und speichert die Referenzen zu den Objekten wie Wahlperiode, Wahlkreis, und die jeweiligen
 * Institutionen des Abgeordneten.
 */
public class Wahlperiode_Freemarker_Impl {
    private Wahlperioden_MongoDB_Impl Wahlperiode;
    private Wahlkreis_MongoDB_Impl Wahlkreis;
    private List<Institution_MongoDB_Impl> Institution;

    private Integer Wahlperiode_MongoID;


    public Wahlperiode_Freemarker_Impl(MongoDBHandler MyMongoDBHandler, Integer Wahlperiode_MongoID) {
        this.Institution = new ArrayList<>();
        this.Wahlperiode_MongoID = Wahlperiode_MongoID;
        init(MyMongoDBHandler,this.Wahlperiode_MongoID);
    }

    public void init(MongoDBHandler MyMongoDBHandler, Integer Wahlperiode_MongoID){
        this.Wahlperiode = new Wahlperioden_MongoDB_Impl(MyMongoDBHandler,Wahlperiode_MongoID);
        this.Wahlkreis = new Wahlkreis_MongoDB_Impl(MyMongoDBHandler, this.Wahlperiode.getWahlkreis_Mongo_Impl());

        List<Integer> Instituionen_MongoID = this.Wahlperiode.getInstitutionen_Mongo_Impl();
        Instituionen_MongoID.forEach(MongoID -> {
            AddInstitution(new Institution_MongoDB_Impl(MyMongoDBHandler,MongoID));
        });
    }

    public Wahlperioden_MongoDB_Impl getWahlperiode() {
        return Wahlperiode;
    }

    public void setWahlperiode(Wahlperioden_MongoDB_Impl wahlperiode) {
        Wahlperiode = wahlperiode;
    }

    public Wahlkreis_MongoDB_Impl getWahlkreis() {
        return Wahlkreis;
    }

    public void setWahlkreis(Wahlkreis_MongoDB_Impl wahlkreis) {
        Wahlkreis = wahlkreis;
    }

    public List<Institution_MongoDB_Impl> getInstitution() {
        return Institution;
    }

    public void AddInstitution(Institution_MongoDB_Impl Institution) {
        this.Institution.add(Institution);
    }
}
