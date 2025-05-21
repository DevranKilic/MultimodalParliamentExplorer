package Class_Impl;

import Class_MongoDB_Impl.Abgeordneter_MongoDB_Impl;
import Class_MongoDB_Impl.Fraktion_MongoDB_Impl;

import java.util.ArrayList;
import java.util.List;


/**
 * Diese Klasse beschreibt die Attribute und Methoden die für alle anderen Arten von Implementationen zur verfügung stehen sollten.
 * Mit dieser Klasse kann man lokal am effektivsten Arbeiten.
 */
public class RedeAuszug_Impl implements Class_Interfaces.RedeAuszug {
    private String Auszug;
    private Boolean isKommentar;
    private int AbgeordneterMongoID = -1;
    private int FraktionMongoID = -1;
    private Abgeordneter_MongoDB_Impl Abgeordneter = null;
    private Fraktion_MongoDB_Impl Fraktion = null;
    private List<Historie_Impl> Historie_Impl = new ArrayList<>();
    private List<Integer> Historie_MongoIDs = new ArrayList<>();

    private String MongoLabel = "REDEAUSZUG";
    private int MongoID;


    public RedeAuszug_Impl(){
        this.MongoID = hashCode();
    }

    @Override
    public String getMongoLabel() {
        return MongoLabel;
    }

    public void setMongoLabel(String mongoLabel) {
        MongoLabel = mongoLabel;
    }

    @Override
    public int getMongoID() {
        return MongoID;
    }

    public void setMongoID(int mongoID) {
        MongoID = mongoID;
    }

    public String getRedeAuszug() {
        return Auszug;
    }

    public void setRedeAuszug(String redeAuszug) {
        Auszug = redeAuszug;
    }

    public Boolean getIsKommentar() {
        return isKommentar;
    }

    public void setIsKommentar(Boolean Iskommentar) {
        isKommentar = Iskommentar;
    }

    public int getAbgeordneterMongoID() {
        return AbgeordneterMongoID;
    }

    public int getFraktionMongoID() {
        return FraktionMongoID;
    }



    public Abgeordneter_MongoDB_Impl getAbgeordneter() {
        return Abgeordneter;
    }

    public void setAbgeordneter(Abgeordneter_MongoDB_Impl abgeordneter) {
        Abgeordneter = abgeordneter;
    }

    public Fraktion_MongoDB_Impl getFraktion() {
        return Fraktion;
    }

    public void setFraktion(Fraktion_MongoDB_Impl fraktion) {
        Fraktion = fraktion;
    }

    public List<Class_Impl.Historie_Impl> getHistorie_Impl() {
        return Historie_Impl;
    }

    public void AddHistorie_Impl(Class_Impl.Historie_Impl historie_Impl) {
        this.Historie_Impl.add(historie_Impl);
    }

    public List<Integer> getHistorie_MongoIDs() {
        return Historie_MongoIDs;
    }

    public void AddHistorie_MongoIDs(Integer historie_MongoID) {
        this.Historie_MongoIDs.add(historie_MongoID);
    }
}
